package ru.spbstu.bug_tracker.issue.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.spbstu.bug_tracker.common.ErrorCode
import ru.spbstu.bug_tracker.common.ErrorData
import ru.spbstu.bug_tracker.common.RequestResult
import ru.spbstu.bug_tracker.common.RequestResult.Error
import ru.spbstu.bug_tracker.common.RequestResult.Success
import ru.spbstu.bug_tracker.extensions.isEnumValue
import ru.spbstu.bug_tracker.issue.model.Issue
import ru.spbstu.bug_tracker.issue.model.IssueStatus
import ru.spbstu.bug_tracker.user.model.UserType
import ru.spbstu.bug_tracker.user.service.UserService
import ru.spbstu.bug_tracker.user.service.UserService.Companion.getUserNotFoundError

@Service
class IssueService(
        private val issueRepository: IssueRepository,
        private val userService: UserService,
) {

    fun getIssues(userId: Long): RequestResult<List<Issue>> {
        val author = userService.getUser(userId) ?: return getUserNotFoundError(userId)
        val issues = when (author.userType) {
            UserType.dev -> issueRepository.findByAssigneeId(userId)
            UserType.test -> issueRepository.findByAuthorId(userId)
        }
        return Success(HttpStatus.FOUND, issues)
    }

    fun getIssue(id: Long) = issueRepository.findByIdOrNull(id)

    fun createIssue(authorId: Long, assigneeId: Long, title: String): RequestResult<Issue> {
        val author = userService.getUser(authorId) ?: return getUserNotFoundError(authorId)
        val assignee = userService.getUser(assigneeId) ?: return getUserNotFoundError(assigneeId)
        when {
            author.userType != UserType.test -> return Error(
                    ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.ISSUE_CREATION_NOT_ALLOWED, author)
            )
            assignee.userType != UserType.dev -> return Error(
                    ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.USER_ASSIGNMENT_NOT_ALLOWED, assignee)
            )
        }
        val issue = issueRepository.save(Issue(title = title, authorId = authorId, assigneeId = assigneeId))
        return Success(HttpStatus.CREATED, issue)
    }

    fun changeStatus(userId: Long, issueId: Long, status: String): RequestResult<Issue> {
        if (!status.isEnumValue<IssueStatus>()) {
            return Error(ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.WRONG_ISSUE_STATUS))
        }

        val user = userService.getUser(userId) ?: return getUserNotFoundError(userId)
        val issue = getIssue(issueId) ?: return Error(
                ErrorData(HttpStatus.NOT_FOUND, ErrorCode.ISSUE_NOT_FOUND, issueId)
        )

        if (user.userType == UserType.dev && issue.assigneeId != userId ||
                user.userType == UserType.test && issue.authorId != userId) {
            return Error(ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.ISSUE_STATUS_CHANGE_NOT_ALLOWED, user, issue))
        }

        val issueStatus = IssueStatus.valueOf(status)
        if (issueStatus !in issue.status.nextStatuses()) {
            return Error(ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.WRONG_ISSUE_NEXT_STATUS, status, issue.status.nextStatuses().joinToString()))
        }
        if (issueStatus !in UserType.getAllowedStatuses(user.userType)) {
            return Error(ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.ISSUE_STATUS_NOT_ALLOWED_FOR_USER, status, user))
        }

        return Success(HttpStatus.OK, issueRepository.save(issue.copy(status = issueStatus)))
    }
}
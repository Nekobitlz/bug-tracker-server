package ru.spbstu.bug_tracker.issue.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.spbstu.bug_tracker.issue.service.IssueService

@RestController
class IssueController(private val issueService: IssueService) {

    @Operation(
            summary = "Get all issues for user",
            description = "For the tester, returns all the issues that he created. " +
                    "For the developer, returns all issues that were assigned to him.",
            parameters = [
                Parameter(name = "user_id", description = "Id of the user whose issues wants to return")
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "Returns issues list"),
                ApiResponse(responseCode = "404", description = "User not found"),
            ]
    )
    @RequestMapping(
            path = ["/issues"],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getIssues(@RequestParam("user_id") userId: Long) = issueService.getIssues(userId).toResponseEntity()

    @Operation(
            summary = "Create issue for developer",
            description = "You must specify the title of the issue, the author of the issue and the person who will perform it",
            parameters = [
                Parameter(name = "author_id", description = "Id of user that will be author of issue"),
                Parameter(name = "assignee_id", description = "Id of user to assign the issue to"),
                Parameter(name = "title", description = "Issue title"),
            ],
            responses = [
                ApiResponse(responseCode = "201", description = "Returns created issue body"),
                ApiResponse(responseCode = "400", description = "Author or assignee not allowed to create issue"),
                ApiResponse(responseCode = "404", description = "Author or assignee not found"),
            ]
    )
    @RequestMapping(
            path = ["/create_issue"],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createIssue(
            @RequestParam("author_id") authorId: Long,
            @RequestParam("assignee_id") assigneeId: Long,
            @RequestParam("title") title: String,
    ) = issueService.createIssue(authorId, assigneeId, title).toResponseEntity()

    @Operation(
            summary = "Change issue status",
            description = "Only the developer can transfer the issue from the \"open\" status to the \"ready\" status, " +
                    "only the tester can transfer the issue from the \"ready\" status to the \"approved\" or \"declined\", " +
                    "and everyone can return the issue to \"open\".",
            parameters = [
                Parameter(name = "user_id", description = "Id of user that wants to change status"),
                Parameter(name = "issue_id", description = "Id of issue where needs to change status"),
                Parameter(name = "status", description = "New issue status, can be: [open, ready, approved, declined]"),
            ],
            responses = [
                ApiResponse(responseCode = "200", description = "Returns issue with new status"),
                ApiResponse(responseCode = "400", description = "Author or assignee not allowed to change status or issue status is incorrect"),
                ApiResponse(responseCode = "404", description = "User or issue not found"),
            ]
    )
    @RequestMapping(
            path = ["/change_status"],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    fun changeStatus(
            @RequestParam("user_id") userId: Long,
            @RequestParam("issue_id") issueId: Long,
            @RequestParam("status") status: String,
    ) = issueService.changeStatus(userId, issueId, status.lowercase()).toResponseEntity()
}

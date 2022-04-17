package ru.spbstu.bug_tracker.common

import org.springframework.http.HttpStatus
import ru.spbstu.bug_tracker.issue.model.IssueStatus
import ru.spbstu.bug_tracker.user.model.UserType

enum class ErrorCode(val message: String) {
    WRONG_USER_TYPE("User type is incorrect. Allowed: ${UserType.values().joinToString()}"),
    WRONG_ISSUE_STATUS("Issue status is incorrect. Allowed: ${IssueStatus.values().joinToString()}}"),
    WRONG_ISSUE_NEXT_STATUS("Can't move issue status to %s. Allowed: %s"),
    USER_NOT_FOUND("User with id: [%d] is not found"),
    ISSUE_NOT_FOUND("Issue with id: [%d] is not found"),
    ISSUE_CREATION_NOT_ALLOWED("%s is not allowed to create issues"),
    ISSUE_STATUS_CHANGE_NOT_ALLOWED("%s not allowed to change status of %s"),
    ISSUE_STATUS_NOT_ALLOWED_FOR_USER("Status '%s' not allowed for %s"),
    USER_ASSIGNMENT_NOT_ALLOWED("%s can't be assigned a issue"),
}

data class ErrorData(val httpStatus: HttpStatus,
                     val errorName: String,
                     val errorMessage: String) {
    constructor(httpStatus: HttpStatus, errorCode: ErrorCode) : this(httpStatus, errorCode.name, errorCode.message)

    constructor(
            httpStatus: HttpStatus, errorCode: ErrorCode, vararg args: Any
    ) : this(httpStatus, errorCode.name, errorCode.message.format(*args))
}
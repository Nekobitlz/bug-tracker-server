package ru.spbstu.bug_tracker.issue.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.spbstu.bug_tracker.issue.service.IssueService

@RestController
class IssueController(private val issueService: IssueService) {

    /*@Operation(
            summary = "Create a new order",
            description = "Use this endpoint to create a new order in the backend",
            responses = [
                ApiResponse(responseCode = "201", description = "Success"),
                ApiResponse(responseCode = "403", description = "Forbidden"),
                ApiResponse(responseCode = "500", description = "Server Error")
            ]
    )*/
    @RequestMapping(
            path = ["/issues"],
            method = [RequestMethod.GET],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.FOUND)
    fun getIssues(@RequestParam("user_id") userId: Long) = issueService.getIssues(userId).toResponseEntity()

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

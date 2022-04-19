package ru.spbstu.bug_tracker.user.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.spbstu.bug_tracker.user.service.UserService

@RestController
class UserController(private val userService: UserService) {

    @Operation(
            summary = "Register new user",
            description = "To register a user, you need to pass the type of user being registered (test or dev), " +
                    "the response contains a new user ID.",
            parameters = [
                Parameter(name = "type", description = "Type of user, can be [dev, test]")
            ],
            responses = [
                ApiResponse(responseCode = "201", description = "Returns created user"),
                ApiResponse(responseCode = "400", description = "Author or assignee not allowed to create issue"),
                ApiResponse(responseCode = "404", description = "Author or assignee not found"),
            ]
    )
    @RequestMapping(
            path = ["/register"],
            method = [RequestMethod.POST],
            produces = [MediaType.APPLICATION_JSON_VALUE])
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun register(
            @RequestParam("type") userType: String,
    ) = userService.register(userType.lowercase()).toResponseEntity()
}
package ru.spbstu.bug_tracker.user.controller

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ru.spbstu.bug_tracker.user.service.UserService

@RestController
class UserController(private val userService: UserService) {

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
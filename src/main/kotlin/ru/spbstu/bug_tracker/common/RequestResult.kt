package ru.spbstu.bug_tracker.common

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

sealed class RequestResult<T> {

    abstract fun toResponseEntity(): ResponseEntity<Any>

    data class Success<T>(val httpStatus: HttpStatus, val data: T) : RequestResult<T>() {
        override fun toResponseEntity(): ResponseEntity<Any> = ResponseEntity
                .status(httpStatus)
                .body(data)
    }

    data class Error<T>(val errorData: ErrorData) : RequestResult<T>() {
        override fun toResponseEntity(): ResponseEntity<Any> = ResponseEntity
                .status(errorData.httpStatus)
                .body(errorData)
    }
}
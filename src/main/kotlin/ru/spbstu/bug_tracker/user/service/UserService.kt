package ru.spbstu.bug_tracker.user.service

import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import ru.spbstu.bug_tracker.common.ErrorCode
import ru.spbstu.bug_tracker.common.ErrorData
import ru.spbstu.bug_tracker.common.RequestResult
import ru.spbstu.bug_tracker.extensions.isEnumValue
import ru.spbstu.bug_tracker.issue.model.Issue
import ru.spbstu.bug_tracker.user.model.User
import ru.spbstu.bug_tracker.user.model.UserType

@Service
class UserService(private val userRepository: UserRepository) {

    fun register(userType: String): RequestResult<User> {
        if (!userType.isEnumValue<UserType>()) {
            return RequestResult.Error(ErrorData(HttpStatus.BAD_REQUEST, ErrorCode.WRONG_USER_TYPE))
        }
        val user = userRepository.save(User(userType = UserType.valueOf(userType)))
        return RequestResult.Success(HttpStatus.CREATED, user)
    }

    fun getUser(userId: Long) = userRepository.findByIdOrNull(userId)

    companion object {
        fun <T> getUserNotFoundError(userId: Long): RequestResult.Error<T> = RequestResult.Error(
                ErrorData(HttpStatus.NOT_FOUND, ErrorCode.USER_NOT_FOUND, userId)
        )
    }
}
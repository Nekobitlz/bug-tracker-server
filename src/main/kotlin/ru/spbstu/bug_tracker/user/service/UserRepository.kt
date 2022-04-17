package ru.spbstu.bug_tracker.user.service

import org.springframework.data.repository.CrudRepository
import ru.spbstu.bug_tracker.user.model.User

interface UserRepository : CrudRepository<User, Long>
package ru.spbstu.bug_tracker.issue.service

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import ru.spbstu.bug_tracker.issue.model.Issue

interface IssueRepository : CrudRepository<Issue, Long> {
    @Query(value = "SELECT * FROM ${Issue.TABLE_NAME} WHERE author_id = ?1", nativeQuery = true)
    fun findByAuthorId(authorId: Long): List<Issue>

    @Query(value = "SELECT * FROM ${Issue.TABLE_NAME} WHERE assignee_id = ?1", nativeQuery = true)
    fun findByAssigneeId(authorId: Long): List<Issue>
}
package ru.spbstu.bug_tracker.issue.model

enum class IssueStatus {
    open,
    ready,
    approved,
    declined;

    fun nextStatuses(): List<IssueStatus> = when (this) {
        open -> listOf(ready)
        ready -> listOf(approved, declined)
        approved, declined -> listOf(open)
    }
}
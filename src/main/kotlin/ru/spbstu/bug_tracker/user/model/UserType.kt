package ru.spbstu.bug_tracker.user.model

import ru.spbstu.bug_tracker.issue.model.IssueStatus

enum class UserType {
    dev, test;

    companion object {
        fun getAllowedStatuses(userType: UserType): List<IssueStatus> = when (userType) {
            dev -> listOf(IssueStatus.open, IssueStatus.ready)
            test -> listOf(IssueStatus.open, IssueStatus.approved, IssueStatus.declined)
        }
    }
}
package ru.spbstu.bug_tracker.issue.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = Issue.TABLE_NAME)
data class Issue(
        @Id
        @JsonProperty("id")
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "issues_seq")
        val id: Long = 0L,

        @JsonProperty("title")
        @Column(name = "title")
        val title: String = "",

        @JsonProperty("status")
        @Column(name = "status")
        var status: IssueStatus = IssueStatus.open,

        @JsonProperty("author_id")
        @Column(name = "author_id")
        val authorId: Long = 0L,

        @JsonProperty("assignee_id")
        @Column(name = "assignee_id")
        val assigneeId: Long = 0L,
) {
    companion object {
        const val TABLE_NAME = "issues"
    }
}
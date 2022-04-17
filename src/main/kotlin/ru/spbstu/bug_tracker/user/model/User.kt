package ru.spbstu.bug_tracker.user.model

import com.fasterxml.jackson.annotation.JsonProperty
import javax.persistence.*

@Entity
@Table(name = User.TABLE_NAME)
data class User(
        @Id
        @JsonProperty("id")
        @Column(name = "id")
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "users_seq")
        val id: Long = 0L,

        @JsonProperty("type")
        @Column(name = "type")
        var userType: UserType = UserType.dev,
) {
    companion object {
        const val TABLE_NAME = "users"
    }
}
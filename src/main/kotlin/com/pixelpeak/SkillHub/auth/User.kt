package com.pixelpeak.SkillHub.features.auth

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document("users")
data class User(
    @Id val id: UUID = UUID.randomUUID(),
    val username: String,
    val password: String?
)
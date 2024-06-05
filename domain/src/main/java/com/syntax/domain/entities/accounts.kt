package com.syntax.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val currency: String = "USD",
    val balance: Double = 0.0
)
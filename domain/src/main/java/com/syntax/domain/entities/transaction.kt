package com.syntax.domain.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val amount: Double,
    val category: String,
    val type: String // Income or Expense
)
package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val dateCreated: Long = System.currentTimeMillis()
)

@Entity(tableName = "materis")
data class Materi(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val folderId: Int?,
    val dateCreated: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "scripts")
data class Script(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val language: String,
    val code: String,
    val folderId: Int?,
    val dateCreated: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

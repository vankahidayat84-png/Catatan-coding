package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Folder::class, Materi::class, Script::class], version = 1, exportSchema = false)
abstract class StudyVaultDatabase : RoomDatabase() {
    abstract fun dao(): StudyVaultDao

    companion object {
        @Volatile
        private var Instance: StudyVaultDatabase? = null

        fun getDatabase(context: Context): StudyVaultDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(context, StudyVaultDatabase::class.java, "studyvault_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
    }
}

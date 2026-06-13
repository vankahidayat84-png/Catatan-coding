package com.example

import android.app.Application
import com.example.data.StudyVaultDatabase
import com.example.data.StudyVaultRepository

class StudyVaultApplication : Application() {
    val database by lazy { StudyVaultDatabase.getDatabase(this) }
    val repository by lazy { StudyVaultRepository(database.dao()) }
}

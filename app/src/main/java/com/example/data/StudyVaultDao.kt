package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface StudyVaultDao {
    // Folders
    @Query("SELECT * FROM folders ORDER BY dateCreated DESC")
    fun getAllFolders(): Flow<List<Folder>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder)

    @Update
    suspend fun updateFolder(folder: Folder)

    @Delete
    suspend fun deleteFolder(folder: Folder)

    @Query("SELECT * FROM folders WHERE name LIKE '%' || :query || '%'")
    fun searchFolders(query: String): Flow<List<Folder>>

    // Materis
    @Query("SELECT * FROM materis ORDER BY dateCreated DESC")
    fun getAllMateris(): Flow<List<Materi>>

    @Query("SELECT * FROM materis WHERE folderId = :folderId ORDER BY dateCreated DESC")
    fun getMaterisByFolder(folderId: Int): Flow<List<Materi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMateri(materi: Materi)

    @Update
    suspend fun updateMateri(materi: Materi)

    @Delete
    suspend fun deleteMateri(materi: Materi)

    @Query("SELECT * FROM materis WHERE title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%' ORDER BY dateCreated DESC")
    fun searchMateris(query: String): Flow<List<Materi>>

    @Query("SELECT * FROM materis WHERE isFavorite = 1 ORDER BY dateCreated DESC")
    fun getFavoriteMateris(): Flow<List<Materi>>
    
    @Query("DELETE FROM materis")
    suspend fun deleteAllMateris()

    // Scripts
    @Query("SELECT * FROM scripts ORDER BY dateCreated DESC")
    fun getAllScripts(): Flow<List<Script>>

    @Query("SELECT * FROM scripts WHERE folderId = :folderId ORDER BY dateCreated DESC")
    fun getScriptsByFolder(folderId: Int): Flow<List<Script>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScript(script: Script)

    @Update
    suspend fun updateScript(script: Script)

    @Delete
    suspend fun deleteScript(script: Script)

    @Query("SELECT * FROM scripts WHERE title LIKE '%' || :query || '%' OR code LIKE '%' || :query || '%' ORDER BY dateCreated DESC")
    fun searchScripts(query: String): Flow<List<Script>>

    @Query("SELECT * FROM scripts WHERE isFavorite = 1 ORDER BY dateCreated DESC")
    fun getFavoriteScripts(): Flow<List<Script>>

    @Query("DELETE FROM scripts")
    suspend fun deleteAllScripts()

    // Reset all
    @Query("DELETE FROM folders")
    suspend fun deleteAllFolders()
}

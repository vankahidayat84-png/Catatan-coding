package com.example.data

class StudyVaultRepository(private val dao: StudyVaultDao) {

    // Folders
    fun getAllFolders() = dao.getAllFolders()
    fun searchFolders(query: String) = dao.searchFolders(query)
    suspend fun insertFolder(folder: Folder) = dao.insertFolder(folder)
    suspend fun updateFolder(folder: Folder) = dao.updateFolder(folder)
    suspend fun deleteFolder(folder: Folder) = dao.deleteFolder(folder)

    // Materis
    fun getAllMateris() = dao.getAllMateris()
    fun getMaterisByFolder(folderId: Int) = dao.getMaterisByFolder(folderId)
    fun searchMateris(query: String) = dao.searchMateris(query)
    fun getFavoriteMateris() = dao.getFavoriteMateris()
    suspend fun insertMateri(materi: Materi) = dao.insertMateri(materi)
    suspend fun updateMateri(materi: Materi) = dao.updateMateri(materi)
    suspend fun deleteMateri(materi: Materi) = dao.deleteMateri(materi)
    suspend fun deleteAllMateris() = dao.deleteAllMateris()

    // Scripts
    fun getAllScripts() = dao.getAllScripts()
    fun getScriptsByFolder(folderId: Int) = dao.getScriptsByFolder(folderId)
    fun searchScripts(query: String) = dao.searchScripts(query)
    fun getFavoriteScripts() = dao.getFavoriteScripts()
    suspend fun insertScript(script: Script) = dao.insertScript(script)
    suspend fun updateScript(script: Script) = dao.updateScript(script)
    suspend fun deleteScript(script: Script) = dao.deleteScript(script)
    suspend fun deleteAllScripts() = dao.deleteAllScripts()
    
    // Reset All
    suspend fun resetAllData() {
        dao.deleteAllMateris()
        dao.deleteAllScripts()
        dao.deleteAllFolders()
    }
}

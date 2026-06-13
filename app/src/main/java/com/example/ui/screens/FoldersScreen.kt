package com.example.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Folder
import com.example.viewmodel.StudyVaultViewModel
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoldersScreen(
    viewModel: StudyVaultViewModel,
    onBack: () -> Unit
) {
    val query by viewModel.searchFolderQuery.collectAsStateWithLifecycle()
    val folders by if (query.isEmpty()) {
        viewModel.allFolders.collectAsStateWithLifecycle()
    } else {
        viewModel.searchedFolders.collectAsStateWithLifecycle()
    }

    var showDialog by remember { mutableStateOf(false) }
    var editingFolder by remember { mutableStateOf<Folder?>(null) }
    var folderName by remember { mutableStateOf("") }

    Scaffold(
        containerColor = BgMain,
        topBar = {
            TopAppBar(
                title = { Text("Menu Folder", color = TextMain) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextMain)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgMain)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { 
                    editingFolder = null
                    folderName = ""
                    showDialog = true 
                },
                containerColor = ColorAccent,
                contentColor = TextMain
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Folder")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = { viewModel.updateSearchFolderQuery(it) },
                placeholder = { Text("Cari folder...", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAccent,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(folders) { folder ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = BgCard),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = folder.name, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(folder.dateCreated)),
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                            IconButton(onClick = {
                                editingFolder = folder
                                folderName = folder.name
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Edit", tint = ColorAccent)
                            }
                            IconButton(onClick = { viewModel.deleteFolder(folder) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ColorAccent)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(if (editingFolder != null) "Edit Folder" else "Tambah Folder", color = TextMain) },
            text = {
                OutlinedTextField(
                    value = folderName,
                    onValueChange = { folderName = it },
                    label = { Text("Nama Folder", color = TextSecondary) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorAccent,
                        unfocusedBorderColor = TextSecondary,
                        focusedTextColor = TextMain,
                        unfocusedTextColor = TextMain
                    ),
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    if (folderName.isNotBlank()) {
                        if (editingFolder != null) {
                            viewModel.updateFolder(editingFolder!!.copy(name = folderName))
                        } else {
                            viewModel.insertFolder(Folder(name = folderName))
                        }
                        showDialog = false
                    }
                }) {
                    Text("Simpan", color = ColorAccent)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Batal", color = TextSecondary)
                }
            },
            containerColor = BgCard
        )
    }
}

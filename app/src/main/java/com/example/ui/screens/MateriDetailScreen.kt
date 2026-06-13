package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Materi
import com.example.viewmodel.StudyVaultViewModel
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MateriDetailScreen(
    materiId: Int,
    viewModel: StudyVaultViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var folderId by remember { mutableStateOf<Int?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    var materiToEdit by remember { mutableStateOf<Materi?>(null) }
    
    val folders by viewModel.allFolders.collectAsStateWithLifecycle()
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(materiId) {
        if (materiId != -1) {
            val materi = viewModel.allMateris.firstOrNull()?.find { it.id == materiId }
            materi?.let {
                title = it.title
                content = it.content
                folderId = it.folderId
                isFavorite = it.isFavorite
                materiToEdit = it
            }
        }
    }

    Scaffold(
        containerColor = BgMain,
        topBar = {
            TopAppBar(
                title = { Text(if (materiId == -1) "Tambah Materi" else "Edit Materi", color = TextMain) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextMain)
                    }
                },
                actions = {
                    IconButton(onClick = { isFavorite = !isFavorite }) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Star else Icons.Outlined.StarBorder,
                            contentDescription = "Favorite",
                            tint = ColorAccent
                        )
                    }
                    if (materiId != -1) {
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Materi Content", content)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Materi disalin", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Content", tint = TextMain)
                        }
                        IconButton(onClick = {
                            materiToEdit?.let {
                                scope.launch {
                                    viewModel.deleteMateri(it)
                                    onBack()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMain)
                        }
                    }
                    IconButton(onClick = {
                        if (title.isNotBlank() && content.isNotBlank()) {
                            val newMateri = Materi(
                                id = if (materiId == -1) 0 else materiId,
                                title = title,
                                content = content,
                                folderId = folderId,
                                dateCreated = materiToEdit?.dateCreated ?: System.currentTimeMillis(),
                                isFavorite = isFavorite
                            )
                            scope.launch {
                                if (materiId == -1) viewModel.insertMateri(newMateri)
                                else viewModel.updateMateri(newMateri)
                                onBack()
                            }
                        } else {
                            Toast.makeText(context, "Judul dan isi tidak boleh kosong", Toast.LENGTH_SHORT).show()
                        }
                    }) {
                        Icon(Icons.Default.Save, contentDescription = "Save", tint = ColorAccent)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = BgMain)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Judul Materi", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAccent,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = folders.find { it.id == folderId }?.name ?: "Pilih Folder (Opsional)",
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorAccent,
                        unfocusedBorderColor = TextSecondary,
                        focusedTextColor = TextMain,
                        unfocusedTextColor = TextSecondary
                    )
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Tanpa Folder") },
                        onClick = {
                            folderId = null
                            expanded = false
                        }
                    )
                    folders.forEach { folder ->
                        DropdownMenuItem(
                            text = { Text(folder.name) },
                            onClick = {
                                folderId = folder.id
                                expanded = false
                            }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = content,
                onValueChange = { content = it },
                label = { Text("Isi Materi", color = TextSecondary) },
                modifier = Modifier.fillMaxSize(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAccent,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                )
            )
        }
    }
}

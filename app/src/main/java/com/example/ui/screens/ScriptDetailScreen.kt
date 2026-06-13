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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.Script
import com.example.viewmodel.StudyVaultViewModel
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.EditorLineNumber
import com.example.ui.theme.EditorText
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScriptDetailScreen(
    scriptId: Int,
    viewModel: StudyVaultViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    
    var title by remember { mutableStateOf("") }
    var code by remember { mutableStateOf("") }
    var language by remember { mutableStateOf("Kotlin") }
    var folderId by remember { mutableStateOf<Int?>(null) }
    var isFavorite by remember { mutableStateOf(false) }
    var scriptToEdit by remember { mutableStateOf<Script?>(null) }
    
    val folders by viewModel.allFolders.collectAsStateWithLifecycle()
    var expandedFolder by remember { mutableStateOf(false) }
    var expandedLanguage by remember { mutableStateOf(false) }
    
    val availableLanguages = listOf("Kotlin", "Java", "Python", "JavaScript", "HTML", "CSS", "SQL", "Lainnya")

    LaunchedEffect(scriptId) {
        if (scriptId != -1) {
            val script = viewModel.allScripts.firstOrNull()?.find { it.id == scriptId }
            script?.let {
                title = it.title
                code = it.code
                language = it.language
                folderId = it.folderId
                isFavorite = it.isFavorite
                scriptToEdit = it
            }
        }
    }

    Scaffold(
        containerColor = BgMain,
        topBar = {
            TopAppBar(
                title = { Text(if (scriptId == -1) "Tambah Script" else "Edit Script", color = TextMain) },
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
                    if (scriptId != -1) {
                        IconButton(onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("Script Code", code)
                            clipboard.setPrimaryClip(clip)
                            Toast.makeText(context, "Kode disalin", Toast.LENGTH_SHORT).show()
                        }) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy Content", tint = TextMain)
                        }
                        IconButton(onClick = {
                            scriptToEdit?.let {
                                scope.launch {
                                    viewModel.deleteScript(it)
                                    onBack()
                                }
                            }
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = TextMain)
                        }
                    }
                    IconButton(onClick = {
                        if (title.isNotBlank() && code.isNotBlank()) {
                            val newScript = Script(
                                id = if (scriptId == -1) 0 else scriptId,
                                title = title,
                                language = language,
                                code = code,
                                folderId = folderId,
                                dateCreated = scriptToEdit?.dateCreated ?: System.currentTimeMillis(),
                                isFavorite = isFavorite
                            )
                            scope.launch {
                                if (scriptId == -1) viewModel.insertScript(newScript)
                                else viewModel.updateScript(newScript)
                                onBack()
                            }
                        } else {
                            Toast.makeText(context, "Judul dan kode tidak boleh kosong", Toast.LENGTH_SHORT).show()
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
                label = { Text("Judul Script", color = TextSecondary) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ColorAccent,
                    unfocusedBorderColor = TextSecondary,
                    focusedTextColor = TextMain,
                    unfocusedTextColor = TextMain
                )
            )
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ExposedDropdownMenuBox(
                    expanded = expandedLanguage,
                    onExpandedChange = { expandedLanguage = !expandedLanguage },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = language,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Bahasa", color = TextSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLanguage) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorAccent,
                            unfocusedBorderColor = TextSecondary,
                            focusedTextColor = TextMain,
                            unfocusedTextColor = TextSecondary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedLanguage,
                        onDismissRequest = { expandedLanguage = false }
                    ) {
                        availableLanguages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang) },
                                onClick = {
                                    language = lang
                                    expandedLanguage = false
                                }
                            )
                        }
                    }
                }

                ExposedDropdownMenuBox(
                    expanded = expandedFolder,
                    onExpandedChange = { expandedFolder = !expandedFolder },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = folders.find { it.id == folderId }?.name ?: "Pilih Folder",
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Folder", color = TextSecondary) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedFolder) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ColorAccent,
                            unfocusedBorderColor = TextSecondary,
                            focusedTextColor = TextMain,
                            unfocusedTextColor = TextSecondary
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedFolder,
                        onDismissRequest = { expandedFolder = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Tanpa Folder") },
                            onClick = {
                                folderId = null
                                expandedFolder = false
                            }
                        )
                        folders.forEach { folder ->
                            DropdownMenuItem(
                                text = { Text(folder.name) },
                                onClick = {
                                    folderId = folder.id
                                    expandedFolder = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Editor like textfield with line numbers on the left
            Row(modifier = Modifier.fillMaxSize()) {
                val linesCount = maxOf(1, code.count { it == '\n' } + 1)
                
                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(36.dp)
                        .padding(top = 16.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    for (i in 1..linesCount) {
                        Text(
                            text = "$i",
                            color = EditorLineNumber,
                            fontSize = 14.sp,
                            fontFamily = FontFamily.Monospace,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(8.dp))
                
                OutlinedTextField(
                    value = code,
                    onValueChange = { code = it },
                    placeholder = { Text("Isi kode...", color = TextSecondary) },
                    modifier = Modifier.fillMaxSize(),
                    textStyle = LocalTextStyle.current.copy(
                        fontFamily = FontFamily.Monospace,
                        fontSize = 14.sp
                    ),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ColorAccent,
                        unfocusedBorderColor = TextSecondary,
                        focusedTextColor = EditorText,
                        unfocusedTextColor = EditorText
                    )
                )
            }
        }
    }
}

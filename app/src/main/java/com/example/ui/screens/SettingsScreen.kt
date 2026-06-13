package com.example.ui.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Backup
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.DeleteForever
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.viewmodel.StudyVaultViewModel
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: StudyVaultViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var dialogAction by remember { mutableStateOf({}) }
    var dialogTitle by remember { mutableStateOf("") }
    var dialogMessage by remember { mutableStateOf("") }

    val createDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("application/json")
    ) { uri ->
        uri?.let {
            Toast.makeText(context, "Backup fitur simulasi: Not perfectly implemented.", Toast.LENGTH_SHORT).show()
            // Real implementation will serialize DB to JSON and write to URI
        }
    }

    val openDocumentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            Toast.makeText(context, "Restore fitur simulasi: Not perfectly implemented.", Toast.LENGTH_SHORT).show()
            // Real implementation will parse JSON from URI and insert to DB
        }
    }

    Scaffold(
        containerColor = BgMain,
        topBar = {
            TopAppBar(
                title = { Text("Pengaturan", color = TextMain) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = TextMain)
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
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingSection(title = "Tampilan") {
                SettingItem(
                    icon = Icons.Default.DarkMode,
                    title = "Tema Aplikasi",
                    subtitle = "Aplikasi ini dirancang khusus untuk Dark Mode agar nyaman dipandang."
                ) { }
            }

            SettingSection(title = "Backup & Restore (JSON)") {
                SettingItem(
                    icon = Icons.Default.Backup,
                    title = "Backup Data",
                    subtitle = "Simpan semua materi dan script ke file JSON"
                ) {
                    createDocumentLauncher.launch("studyvault_backup.json")
                }
                SettingItem(
                    icon = Icons.Default.Restore,
                    title = "Restore Data",
                    subtitle = "Kembalikan data dari file JSON"
                ) {
                    openDocumentLauncher.launch(arrayOf("application/json"))
                }
            }

            SettingSection(title = "Data & Penyimpanan") {
                SettingItem(
                    icon = Icons.Default.DeleteForever,
                    title = "Reset Materi",
                    subtitle = "Hapus semua materi secara permanen"
                ) {
                    dialogTitle = "Reset Materi"
                    dialogMessage = "Apakah Anda yakin ingin menghapus semua materi?"
                    dialogAction = { viewModel.resetMateris() }
                    showDialog = true
                }
                SettingItem(
                    icon = Icons.Default.DeleteForever,
                    title = "Reset Script",
                    subtitle = "Hapus semua script secara permanen"
                ) {
                    dialogTitle = "Reset Script"
                    dialogMessage = "Apakah Anda yakin ingin menghapus semua script?"
                    dialogAction = { viewModel.resetScripts() }
                    showDialog = true
                }
                SettingItem(
                    icon = Icons.Default.DeleteForever,
                    title = "Reset Semua Data",
                    subtitle = "Hapus materi, script, dan folder",
                    isDestructive = true
                ) {
                    dialogTitle = "Reset Semua Data"
                    dialogMessage = "Tindakan ini tidak dapat dibatalkan. Semua data akan terhapus."
                    dialogAction = { viewModel.resetAllData() }
                    showDialog = true
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(dialogTitle, color = TextMain) },
            text = { Text(dialogMessage, color = TextSecondary) },
            confirmButton = {
                TextButton(onClick = {
                    dialogAction()
                    showDialog = false
                    Toast.makeText(context, "Data berhasil direset", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Ya, Hapus", color = ColorAccent)
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

@Composable
fun SettingSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            color = ColorAccent,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = BgCard),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                content()
            }
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else TextMain,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (isDestructive) MaterialTheme.colorScheme.error else TextMain,
                fontWeight = FontWeight.Medium,
                fontSize = 16.sp
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

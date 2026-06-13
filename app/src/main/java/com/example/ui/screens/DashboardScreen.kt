package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NoteAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.StudyVaultViewModel
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: StudyVaultViewModel,
    onNavigateToFolders: () -> Unit,
    onNavigateToMateris: () -> Unit,
    onNavigateToScripts: () -> Unit,
    onNavigateToSearch: () -> Unit,
    onNavigateToFavorite: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToMateriDetail: (Int) -> Unit,
    onNavigateToScriptDetail: (Int) -> Unit
) {
    val folders by viewModel.allFolders.collectAsStateWithLifecycle()
    val materis by viewModel.allMateris.collectAsStateWithLifecycle()
    val scripts by viewModel.allScripts.collectAsStateWithLifecycle()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = BgCard
            ) {
                Spacer(Modifier.height(12.dp))
                Text(
                    "StudyVault",
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextMain
                )
                Spacer(Modifier.height(12.dp))
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Folder, contentDescription = null, tint = TextSecondary) },
                    label = { Text("Folder", color = TextMain) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onNavigateToFolders() },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = BgCard)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.NoteAlt, contentDescription = null, tint = TextSecondary) },
                    label = { Text("Materi", color = TextMain) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onNavigateToMateris() },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = BgCard)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Code, contentDescription = null, tint = TextSecondary) },
                    label = { Text("Script", color = TextMain) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onNavigateToScripts() },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = BgCard)
                )
                NavigationDrawerItem(
                    icon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
                    label = { Text("Cari Materi", color = TextMain) },
                    selected = false,
                    onClick = { scope.launch { drawerState.close() }; onNavigateToSearch() },
                    colors = NavigationDrawerItemDefaults.colors(unselectedContainerColor = BgCard)
                )
            }
        }
    ) {
        Scaffold(
            containerColor = BgMain,
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(ColorAccent, RoundedCornerShape(12.dp))
                                    .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "S", color = TextMain, fontSize = 24.sp, fontWeight = FontWeight.Bold, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("StudyVault", color = TextMain, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, letterSpacing = (-0.5).sp)
                                Text("DEV: MASKAAV", color = TextSecondary, fontSize = 10.sp, fontWeight = FontWeight.Medium, letterSpacing = 2.sp)
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { scope.launch { drawerState.open() } }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextMain)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = BgMain)
                )
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { onNavigateToMateriDetail(-1) },
                    containerColor = ColorAccent,
                    contentColor = TextMain,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .testTag("fab_add")
                        .size(56.dp)
                        .border(width = 1.dp, color = Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(16.dp))
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(32.dp))
                }
            }
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                item {
                    // Statistics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(title = "Folder", count = folders.size, modifier = Modifier.weight(1f))
                        StatCard(title = "Materi", count = materis.size, modifier = Modifier.weight(1f))
                        StatCard(title = "Script", count = scripts.size, modifier = Modifier.weight(1f))
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "TOMBOL CEPAT",
                        color = TextSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp, // tracking-wider
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickButton("Materi Baru", Icons.Default.NoteAlt, modifier = Modifier.weight(1f)) { onNavigateToMateriDetail(-1) }
                            QuickButton("Script Baru", Icons.Default.Code, modifier = Modifier.weight(1f)) { onNavigateToScriptDetail(-1) }
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            QuickButton("Folder", Icons.Default.Folder, modifier = Modifier.weight(1f), onClick = onNavigateToFolders)
                            QuickButton("Cari Data", Icons.Default.Search, modifier = Modifier.weight(1f), onClick = onNavigateToSearch)
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Terakhir Dibuka (Materi)",
                        color = TextMain,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }

                items(materis.take(5)) { materi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToMateriDetail(materi.id) },
                        colors = CardDefaults.cardColors(containerColor = BgCard),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = materi.title, color = TextMain, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = materi.content,
                                color = TextSecondary,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 14.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(materi.dateCreated)),
                                color = ColorAccent,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
                
                if (materis.isEmpty() && folders.isEmpty() && scripts.isEmpty()) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(96.dp)
                                    .background(BgCard, CircleShape)
                                    .border(
                                        width = 1.dp,
                                        color = ColorAccent.copy(alpha = 0.3f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NoteAlt,
                                    contentDescription = null,
                                    tint = ColorAccent.copy(alpha = 0.4f),
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(
                                text = "Belum ada data",
                                color = TextMain,
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Gunakan tombol + untuk membuat materi atau script pertamamu.",
                                color = TextSecondary,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(200.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(title: String, count: Int, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = BgCard),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = count.toString(), color = ColorAccent, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = title, color = TextSecondary, fontSize = 12.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun QuickButton(title: String, icon: ImageVector, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Card(
        modifier = modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = BgCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(ColorAccent.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = ColorAccent, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = title, color = TextMain, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}

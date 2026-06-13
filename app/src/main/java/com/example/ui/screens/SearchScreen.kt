package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.viewmodel.StudyVaultViewModel
import com.example.ui.theme.BgCard
import com.example.ui.theme.BgMain
import com.example.ui.theme.ColorAccent
import com.example.ui.theme.EditorText
import com.example.ui.theme.TextMain
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: StudyVaultViewModel,
    onBack: () -> Unit,
    onNavigateToMateriDetail: (Int) -> Unit,
    onNavigateToScriptDetail: (Int) -> Unit
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    
    val queryMateri by viewModel.searchMateriQuery.collectAsStateWithLifecycle()
    val materis by viewModel.searchedMateris.collectAsStateWithLifecycle()
    
    val queryScript by viewModel.searchScriptQuery.collectAsStateWithLifecycle()
    val scripts by viewModel.searchedScripts.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = BgMain,
        topBar = {
            TopAppBar(
                title = { Text("Pencarian", color = TextMain) },
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
        ) {
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = BgMain,
                contentColor = TextMain,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = ColorAccent
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { Text("Materi") },
                    selectedContentColor = ColorAccent,
                    unselectedContentColor = TextSecondary
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { Text("Script") },
                    selectedContentColor = ColorAccent,
                    unselectedContentColor = TextSecondary
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.padding(horizontal = 16.dp).fillMaxSize()) {
                if (selectedTabIndex == 0) {
                    OutlinedTextField(
                        value = queryMateri,
                        onValueChange = { viewModel.updateSearchMateriQuery(it) },
                        placeholder = { Text("Cari materi...", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
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
                        if (queryMateri.isNotBlank()) {
                            items(materis) { materi ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onNavigateToMateriDetail(materi.id) },
                                    colors = CardDefaults.cardColors(containerColor = BgCard),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = materi.title, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = materi.content,
                                            color = TextSecondary,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                } else {
                    OutlinedTextField(
                        value = queryScript,
                        onValueChange = { viewModel.updateSearchScriptQuery(it) },
                        placeholder = { Text("Cari script...", color = TextSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
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
                        if (queryScript.isNotBlank()) {
                            items(scripts) { script ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { onNavigateToScriptDetail(script.id) },
                                    colors = CardDefaults.cardColors(containerColor = BgCard),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = script.title, color = TextMain, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = script.language, color = ColorAccent, fontWeight = FontWeight.Medium, fontSize = 14.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = script.code,
                                            color = EditorText,
                                            maxLines = 3,
                                            overflow = TextOverflow.Ellipsis,
                                            fontSize = 14.sp,
                                            fontFamily = FontFamily.Monospace
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

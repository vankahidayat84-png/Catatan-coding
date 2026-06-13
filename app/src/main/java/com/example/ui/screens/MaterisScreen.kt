package com.example.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MaterisScreen(
    viewModel: StudyVaultViewModel,
    onBack: () -> Unit,
    onNavigateToDetail: (Int) -> Unit
) {
    val query by viewModel.searchMateriQuery.collectAsStateWithLifecycle()
    val materis by if (query.isEmpty()) {
        viewModel.allMateris.collectAsStateWithLifecycle()
    } else {
        viewModel.searchedMateris.collectAsStateWithLifecycle()
    }

    Scaffold(
        containerColor = BgMain,
        topBar = {
            TopAppBar(
                title = { Text("Menu Materi", color = TextMain) },
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
                onClick = { onNavigateToDetail(-1) },
                containerColor = ColorAccent,
                contentColor = TextMain
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Materi")
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
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(materis) { materi ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToDetail(materi.id) },
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
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(materi.dateCreated)),
                                color = ColorAccent,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

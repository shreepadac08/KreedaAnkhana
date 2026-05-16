package com.example.kreedaankhana.ui.challenge

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.kreedaankhana.R
import com.example.kreedaankhana.data.database.entities.Challenge
import com.example.kreedaankhana.ui.navigation.Screen
import com.example.kreedaankhana.ui.theme.TextPrimary
import com.example.kreedaankhana.utils.SessionManager
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeBoardScreen(navController: NavController, viewModel: ChallengeViewModel = viewModel()) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("OPEN", "MATCHED")
    
    val openChallenges by viewModel.openChallenges.observeAsState(initial = emptyList())
    val matchedChallenges by viewModel.matchedChallenges.observeAsState(initial = emptyList())
    
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    var showAddDialog by remember { mutableStateOf(false) }

    // State for new challenge
    var teamName by remember { mutableStateOf("") }
    var sportType by remember { mutableStateOf("") }
    var groundName by remember { mutableStateOf("") }
    var expandedGround by remember { mutableStateOf(false) }
    val grounds = listOf("R R ground", "MEI ground", "DVG ground")

    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    
    val calendar = Calendar.getInstance()

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Post New Challenge", fontWeight = FontWeight.Bold) },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    item {
                        OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Team Name") }, modifier = Modifier.fillMaxWidth())
                        OutlinedTextField(value = sportType, onValueChange = { sportType = it }, label = { Text("Sport Type") }, modifier = Modifier.fillMaxWidth())
                        
                        ExposedDropdownMenuBox(
                            expanded = expandedGround,
                            onExpandedChange = { expandedGround = !expandedGround },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = groundName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Select Ground") },
                                leadingIcon = { Icon(Icons.Default.LocationOn, null) },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGround) },
                                modifier = Modifier.menuAnchor().fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = expandedGround,
                                onDismissRequest = { expandedGround = false }
                            ) {
                                grounds.forEach { ground ->
                                    DropdownMenuItem(
                                        text = { Text(ground) },
                                        onClick = {
                                            groundName = ground
                                            expandedGround = false
                                        }
                                    )
                                }
                            }
                        }
                        
                        OutlinedTextField(
                            value = date,
                            onValueChange = {},
                            label = { Text("Date") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    DatePickerDialog(context, { _, y, m, d ->
                                        date = String.format("%04d-%02d-%02d", y, m + 1, d)
                                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                                }) { Icon(Icons.Default.CalendarToday, null) }
                            }
                        )
                        
                        OutlinedTextField(
                            value = startTime,
                            onValueChange = {},
                            label = { Text("Start Time") },
                            modifier = Modifier.fillMaxWidth(),
                            readOnly = true,
                            trailingIcon = {
                                IconButton(onClick = {
                                    TimePickerDialog(context, { _, h, min ->
                                        startTime = String.format("%02d:%02d", h, min)
                                    }, 16, 0, true).show()
                                }) { Icon(Icons.Default.Schedule, null) }
                            }
                        )
                        
                        OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Description") }, modifier = Modifier.fillMaxWidth().height(80.dp))
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (teamName.isNotBlank() && date.isNotBlank() && startTime.isNotBlank()) {
                        viewModel.postChallenge(teamName, sessionManager.getUserName() ?: "Unknown", sportType, groundName, date, startTime, description, sessionManager.getUserId())
                        showAddDialog = false
                    }
                }) { Text("POST") }
            },
            dismissButton = { TextButton(onClick = { showAddDialog = false }) { Text("CANCEL") } }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.challenge_bg), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = 0.4f)

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                Column {
                    TopAppBar(
                        title = { Text("Challenge Board", fontWeight = FontWeight.Bold) },
                        navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f), titleContentColor = MaterialTheme.colorScheme.onPrimary, navigationIconContentColor = MaterialTheme.colorScheme.onPrimary)
                    )
                    TabRow(selectedTabIndex = selectedTab, containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.9f), contentColor = MaterialTheme.colorScheme.onPrimary) {
                        tabs.forEachIndexed { index, title ->
                            Tab(selected = selectedTab == index, onClick = { selectedTab = index }, text = { Text(title, fontWeight = FontWeight.Bold) })
                        }
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(onClick = { showAddDialog = true }, containerColor = MaterialTheme.colorScheme.secondary) { Icon(Icons.Default.Add, "Add") }
            }
        ) { padding ->
            val currentChallenges = if (selectedTab == 0) openChallenges else matchedChallenges
            
            if (currentChallenges.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Text("No challenges found.", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextPrimary)
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(currentChallenges) { challenge ->
                        ChallengeCard(challenge, isMatched = selectedTab == 1) {
                            if (selectedTab == 0) {
                                navController.navigate("challenge_response/${challenge.id}")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ChallengeCard(challenge: Challenge, isMatched: Boolean, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = if (isMatched) "${challenge.teamName} VS ${challenge.matchedWithTeam}" else challenge.teamName,
                    fontWeight = FontWeight.Bold, fontSize = 20.sp, color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                if (isMatched) {
                    Badge(containerColor = MaterialTheme.colorScheme.tertiary) { Text("MATCHED", modifier = Modifier.padding(4.dp), color = MaterialTheme.colorScheme.onTertiary) }
                }
            }
            
            Text(text = challenge.sportType, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.secondary)
            
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.LocationOn, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                Text(" ${challenge.ground}", style = MaterialTheme.typography.bodyMedium)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CalendarToday, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                Text(" ${challenge.date} | ", style = MaterialTheme.typography.bodyMedium)
                Icon(Icons.Default.Schedule, null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                Text(" ${challenge.startTime}", style = MaterialTheme.typography.bodyMedium)
            }
            

            
            if (!isMatched) {
                Spacer(modifier = Modifier.height(12.dp))
                Text("TAP TO ACCEPT CHALLENGE", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary, fontSize = 12.sp, modifier = Modifier.align(Alignment.End))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChallengeResponseScreen(navController: NavController, challengeId: Int, viewModel: ChallengeViewModel = viewModel()) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val openChallenges by viewModel.openChallenges.observeAsState(initial = emptyList())
    val challenge = openChallenges.find { it.id == challengeId }

    var teamName by remember { mutableStateOf("") }
    var userName by remember { mutableStateOf(sessionManager.getUserName() ?: "") }
    var description by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(painter = painterResource(id = R.drawable.challenge_bg), contentDescription = null, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Crop, alpha = 0.2f)

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Accept Challenge", fontWeight = FontWeight.Bold) },
                    navigationIcon = { IconButton(onClick = { navController.popBackStack() }) { Icon(Icons.Default.ArrowBack, "Back") } }
                )
            }
        ) { padding ->
            if (challenge == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Challenge not found or already matched.") }
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Challenge From: ${challenge.teamName}", fontWeight = FontWeight.Bold)
                            Text("Sport: ${challenge.sportType}")
                            Text("At: ${challenge.ground} on ${challenge.date}")
                        }
                    }

                    OutlinedTextField(value = userName, onValueChange = { userName = it }, label = { Text("Your Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = teamName, onValueChange = { teamName = it }, label = { Text("Your Team Name") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Short Description / Message") }, modifier = Modifier.fillMaxWidth().height(120.dp))

                    Spacer(modifier = Modifier.weight(1f))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        OutlinedButton(onClick = { navController.popBackStack() }, modifier = Modifier.weight(1f)) { Text("CANCEL") }
                        Button(
                            onClick = {
                                if (teamName.isNotBlank() && userName.isNotBlank()) {
                                    viewModel.acceptChallenge(challenge, teamName, userName, sessionManager.getUserId(), description)
                                    navController.popBackStack()
                                    Toast.makeText(context, "Challenge Accepted! Check Matched Tab.", Toast.LENGTH_LONG).show()
                                } else {
                                    Toast.makeText(context, "Please enter your team name.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.weight(1f)
                        ) { Text("ACCEPT") }
                    }
                }
            }
        }
    }
}

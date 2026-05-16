package com.example.kreedaankhana.ui.booking

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
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
import com.example.kreedaankhana.utils.SessionManager
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSlotScreen(navController: NavController, viewModel: BookingViewModel = viewModel()) {
    val context = LocalContext.current
    val sessionManager = SessionManager(context)
    val bookingStatus by viewModel.bookingStatus.observeAsState()

    var teamName by remember { mutableStateOf("") }
    var groundName by remember { mutableStateOf("") }
    var expandedGround by remember { mutableStateOf(false) }
    val grounds = listOf("R R ground", "MEI ground", "DVG ground")
    
    var date by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    val calendar = Calendar.getInstance()

    LaunchedEffect(bookingStatus) {
        bookingStatus?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            if (it.contains("successfully")) {
                navController.popBackStack()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.book_slot_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.4f // More visible
        )

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Book Arena Slot", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surface.copy(alpha = 0.7f), // Reduced whitening
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Text(
                        text = "Enter Booking Details",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(8.dp)
                    )
                }
                
                OutlinedTextField(
                    value = teamName,
                    onValueChange = { teamName = it },
                    label = { Text("Team Name") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                    )
                )

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
                        leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedGround) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        )
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
                    shape = MaterialTheme.shapes.medium,
                    leadingIcon = { Icon(Icons.Default.CalendarToday, contentDescription = null) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                    ),
                    trailingIcon = {
                        IconButton(onClick = {
                            DatePickerDialog(context, { _, y, m, d ->
                                date = String.format("%04d-%02d-%02d", y, m + 1, d)
                            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
                        }) {
                            Text("📅", fontSize = 20.sp)
                        }
                    }
                )

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = startTime,
                        onValueChange = {},
                        label = { Text("Start Time") },
                        modifier = Modifier.weight(1f),
                        readOnly = true,
                        shape = MaterialTheme.shapes.medium,
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                TimePickerDialog(context, { _, h, min ->
                                    startTime = String.format("%02d:%02d", h, min)
                                }, 10, 0, true).show()
                            }) {
                                Text("🕐", fontSize = 20.sp)
                            }
                        }
                    )
                    OutlinedTextField(
                        value = endTime,
                        onValueChange = {},
                        label = { Text("End Time") },
                        modifier = Modifier.weight(1f),
                        readOnly = true,
                        shape = MaterialTheme.shapes.medium,
                        leadingIcon = { Icon(Icons.Default.Schedule, contentDescription = null) },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f),
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.6f)
                        ),
                        trailingIcon = {
                            IconButton(onClick = {
                                TimePickerDialog(context, { _, h, min ->
                                    endTime = String.format("%02d:%02d", h, min)
                                }, 12, 0, true).show()
                            }) {
                                Text("🕐", fontSize = 20.sp)
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                Button(
                    onClick = { viewModel.bookSlot(teamName, groundName, date, startTime, endTime, sessionManager.getUserId()) },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text("CONFIRM BOOKING", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(navController: NavController, viewModel: BookingViewModel = viewModel()) {
    val bookings by viewModel.allBookings.observeAsState(initial = emptyList())

    Box(modifier = Modifier.fillMaxSize()) {
        // Background Image
        Image(
            painter = painterResource(id = R.drawable.calendar_bg),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Fit,
            alpha = 0.4f // Increased visibility
        )

        Scaffold(
            containerColor = androidx.compose.ui.graphics.Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { Text("Booking Calendar", fontWeight = FontWeight.Bold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        ) { padding ->
            if (bookings.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.TopCenter) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(" ", fontSize = 64.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("No bookings yet.", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Book your first slot!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(bookings) { booking ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f) // Less whitening
                            ),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.8f),
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("🏆")
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(
                                        text = booking.teamName,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), thickness = 0.5.dp)
                                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                    Column {
                                        Text("Ground", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                        Text(booking.groundName, fontWeight = FontWeight.Medium)
                                    }
                                    Column {
                                        Text("Date", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                        Text(booking.date, fontWeight = FontWeight.Medium)
                                    }
                                    Column {
                                        Text("Time Slot", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                        Text("${booking.startTime} - ${booking.endTime}", fontWeight = FontWeight.Medium)
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

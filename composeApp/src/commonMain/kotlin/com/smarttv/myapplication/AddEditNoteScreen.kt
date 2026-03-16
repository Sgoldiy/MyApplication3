package com.smarttv.myapplication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.datetime.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditNoteScreen(navController: NavController, viewModel: NotesViewModel) {
    val currentNote by viewModel.currentNote.collectAsState()
    var title by remember { mutableStateOf(currentNote?.title ?: "") }
    var content by remember { mutableStateOf(currentNote?.content ?: "") }
    var selectedColor by remember { mutableStateOf(currentNote?.color ?: 0xFFFFEB3B) }
    var hasReminder by remember { mutableStateOf(currentNote?.reminderTime != null) }
    var reminderTime by remember { mutableStateOf(currentNote?.reminderTime) }

    val colors = listOf(
        0xFFFFEB3B, // Yellow
        0xFF81C784, // Green
        0xFF64B5F6, // Blue
        0xFFFF8A65, // Orange
        0xFFBA68C8, // Purple
        0xFFF06292, // Pink
        0xFF4CAF50, // Dark Green
        0xFF2196F3, // Dark Blue
        0xFFFF5722, // Deep Orange
        0xFF9C27B0, // Deep Purple
        0xFFE91E63, // Deep Pink
        0xFF00BCD4, // Cyan
        0xFF8BC34A, // Light Green
        0xFFCDDC39, // Lime
        0xFFFFC107, // Amber
        0xFF795548, // Brown
        0xFF9E9E9E  // Grey
    )

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (currentNote == null) "Add Note" else "Edit Note") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Text("←")
                    }
                },
                actions = {
                    TextButton(onClick = {
                        val finalReminderTime = if (hasReminder) reminderTime else null
                        if (currentNote == null) {
                            viewModel.addNote(title, content, selectedColor, finalReminderTime)
                        } else {
                            currentNote?.id?.let { id ->
                                viewModel.updateNote(id, title, content, selectedColor, finalReminderTime)
                            }
                        }
                        viewModel.setCurrentNote(null)
                        navController.popBackStack()
                    }) {
                        Text("Save")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 800.dp) // Constrain width for better readability on web
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Content") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 200.dp),
                    maxLines = Int.MAX_VALUE
                )
                
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = hasReminder, onCheckedChange = { hasReminder = it })
                            Text("Set Reminder")
                        }
                        if (hasReminder) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(onClick = { showDatePicker = true }) {
                                    Text("Date")
                                }
                                Button(onClick = { showTimePicker = true }) {
                                    Text("Time")
                                }
                            }
                            reminderTime?.let {
                                val instant = Instant.fromEpochMilliseconds(it)
                                val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
                                Text("Set for: ${localDateTime.date} ${localDateTime.time}", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }

                Text("Background Color:", style = MaterialTheme.typography.titleMedium)
                FlowRow( // Better for many small items
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { color ->
                        val isSelected = selectedColor == color
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color(color), RoundedCornerShape(8.dp))
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { selectedColor = color }
                        )
                    }
                }
            }
        }
    }

    // Dialogs remain the same...
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    showDatePicker = false
                    showTimePicker = true
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    val selectedDateMillis = datePickerState.selectedDateMillis
                    if (selectedDateMillis != null) {
                        val localDate = Instant.fromEpochMilliseconds(selectedDateMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
                        val localTime = LocalTime(timePickerState.hour, timePickerState.minute)
                        val localDateTime = LocalDateTime(localDate, localTime)
                        reminderTime = localDateTime.toInstant(TimeZone.currentSystemDefault()).toEpochMilliseconds()
                    }
                    showTimePicker = false
                }) {
                    Text("OK")
                }
            },
            text = {
                TimePicker(state = timePickerState)
            }
        )
    }
}

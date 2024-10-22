package coded.toolbox.roomdatabase.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coded.toolbox.roomdatabase.roomDb.Note
import coded.toolbox.roomdatabase.ui.components.EditNoteDialog
import coded.toolbox.roomdatabase.ui.components.NoteCard
import coded.toolbox.roomdatabase.viewModel.NoteViewModel
import java.util.Calendar

@Composable
fun Notescreen(viewModel: NoteViewModel)
{
    val context = LocalContext.current

    var name by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") } // New for date
    var time by remember { mutableStateOf("") } // New for time

    var noteList by remember { mutableStateOf(emptyList<Note>()) }
    viewModel.getNotes().observe(context as ComponentActivity) {
        noteList = it
    }

    var isEditDialogOpen by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }

    // DatePicker dialog state
    val datePickerState = remember { mutableStateOf(false) }
    // TimePicker dialog state
    val timePickerState = remember { mutableStateOf(false) }

    // Create a DatePicker dialog
    if (datePickerState.value) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth" // format the date
            datePickerState.value = false
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    // Create a TimePicker dialog
    if (timePickerState.value) {
        val calendar = Calendar.getInstance()
        TimePickerDialog(context, { _, hourOfDay, minute ->
            time = String.format("%02d:%02d", hourOfDay, minute) // format the time
            timePickerState.value = false
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = "Name") }
        )
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = body,
            onValueChange = { body = it },
            placeholder = { Text(text = "Body") }
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Button to open DatePicker
        Button(onClick = { datePickerState.value = true }) {
            Text(text = if (date.isNotBlank()) "Date: $date" else "Select Date")
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Button to open TimePicker
        Button(onClick = { timePickerState.value = true }) {
            Text(text = if (time.isNotBlank()) "Time: $time" else "Select Time")
        }

        Spacer(modifier = Modifier.height(45.dp))

        Button(onClick = {
            // Check if all fields are filled
            if (name.isNotBlank() || body.isNotBlank() || date.isNotBlank() || time.isNotBlank()) {
                val note = Note(noteName = name, noteBody = body, noteDate = date, noteTime = time)
                viewModel.upsertNote(note)
                // Clear fields
                name = ""
                body = ""
                date = ""
                time = ""
            } else {
                Toast.makeText(context, "Please fill in at least one field.", Toast.LENGTH_SHORT).show()
            }
        }) {
            Text(text = "Set Data")
        }

        Spacer(modifier = Modifier.height(25.dp))
        LazyColumn {
            items(noteList) { note ->
                NoteCard(
                    note = note,
                    onDeleteClick = { viewModel.deleteNote(note) },
                    onEditClick = {
                        noteToEdit = note
                        isEditDialogOpen = true
                    }
                )
            }
        }
    }

    if (isEditDialogOpen && noteToEdit != null) {
        EditNoteDialog(
            currentNote = noteToEdit!!,
            onDismiss = {
                isEditDialogOpen = false
                noteToEdit = null
            },
            onConfirm = { newName, newBody, newDate, newTime ->
                val updatedNote = noteToEdit!!.copy(
                    noteName = newName,
                    noteBody = newBody,
                    noteDate = newDate,
                    noteTime = newTime
                )
                viewModel.upsertNote(updatedNote)  // Pass the updated note
            }
        )
    }
}

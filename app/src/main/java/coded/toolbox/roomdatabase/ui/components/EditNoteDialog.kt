package coded.toolbox.roomdatabase.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coded.toolbox.roomdatabase.roomDb.Note
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.ui.platform.LocalContext
import java.util.Calendar


@Composable
fun EditNoteDialog(
    currentNote: Note,
    onDismiss: () -> Unit,
    onConfirm: (String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(currentNote.noteName) }
    var body by remember { mutableStateOf(currentNote.noteBody) }
    var date by remember { mutableStateOf(currentNote.noteDate) }
    var time by remember { mutableStateOf(currentNote.noteTime) }

    // DatePicker dialog state
    val datePickerState = remember { mutableStateOf(false) }
    // TimePicker dialog state
    val timePickerState = remember { mutableStateOf(false) }

    // Create a DatePicker dialog
    if (datePickerState.value) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        DatePickerDialog(context, { _, year, month, dayOfMonth ->
            date = "$year-${month + 1}-$dayOfMonth" // format the date
            datePickerState.value = false
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
    }

    // Create a TimePicker dialog
    if (timePickerState.value) {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()
        TimePickerDialog(context, { _, hourOfDay, minute ->
            time = String.format("%02d:%02d", hourOfDay, minute) // format the time
            timePickerState.value = false
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Edit Note") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = body,
                    onValueChange = { body = it },
                    label = { Text("Body") }
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Button to open DatePicker
                Button(onClick = { datePickerState.value = true }) {
                    Text(text = if (date.isNotBlank()) "Date: $date" else "Select Date")
                }
                Spacer(modifier = Modifier.height(8.dp))

                // Button to open TimePicker
                Button(onClick = { timePickerState.value = true }) {
                    Text(text = if (time.isNotBlank()) "Time: $time" else "Select Time")
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(name, body, date, time)
                    onDismiss() // Close the dialog
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

package coded.toolbox.roomdatabase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import coded.toolbox.roomdatabase.roomDb.Note
import coded.toolbox.roomdatabase.roomDb.NoteDatabase
import coded.toolbox.roomdatabase.viewModel.NoteViewModel
import coded.toolbox.roomdatabase.viewModel.Repository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Myscreen()
        }
    }
}

@Composable
private fun Myscreen()
{
    val context = LocalContext.current
    val db by lazy {
        Room.databaseBuilder(
            context,
            NoteDatabase::class.java,
            "note.db"
        ).build()
    }

    val viewModel: NoteViewModel = viewModel(
        factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return NoteViewModel(Repository(db)) as T

            }
        }
    )
    var name by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
    val note = Note(name, body)
    var noteList by remember { mutableStateOf(emptyList<Note>()) }
    viewModel.getNotes().observe(context as ComponentActivity) {
        noteList = it
    }
    var isEditDialogOpen by remember { mutableStateOf(false) }
    var noteToEdit by remember { mutableStateOf<Note?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TextField(
            value = name,
            onValueChange = { name = it },
            placeholder = { Text(text = "Name") })
        Spacer(modifier = Modifier.height(20.dp))
        TextField(
            value = body,
            onValueChange = { body = it },
            placeholder = { Text(text = "Body") })

        Spacer(modifier = Modifier.height(45.dp))

        Button(onClick = {
            // Check if both fields are empty
            if (name.isNotBlank() || body.isNotBlank()) {
                viewModel.upsertNote(note)
                // Optionally clear the fields after submission
                name = ""
                body = ""
            } else {
                // Optionally, show a message or feedback
                // For example, you can use Toast (import android.widget.Toast)
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
                    onEditClick = {  noteToEdit = note
                        isEditDialogOpen = true }
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
            onConfirm = { newName, newBody ->
                // Update the existing note instead of creating a new one
                val updatedNote = noteToEdit!!.copy(noteName = newName, noteBody = newBody)
                viewModel.upsertNote(updatedNote)  // Pass the updated note
            }
        )
    }

}


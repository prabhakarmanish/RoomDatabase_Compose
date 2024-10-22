package coded.toolbox.roomdatabase.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coded.toolbox.roomdatabase.roomDb.Note

@Composable
fun NoteCard(
    note: Note,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(text = note.noteName, style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = note.noteBody, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Date: ${note.noteDate}", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Time: ${note.noteTime}", style = MaterialTheme.typography.bodyMedium)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Edit")
                }

                Button(
                    onClick = onDeleteClick,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(text = "Delete")
                }
            }
        }
    }
}

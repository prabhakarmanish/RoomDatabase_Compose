package coded.toolbox.roomdatabase.roomDb

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val noteName: String,
    val noteBody: String,
    val noteDate: String, // New column for date
    val noteTime: String, // New column for time
    @PrimaryKey(autoGenerate = true)
    val noteId: Int = 0
)

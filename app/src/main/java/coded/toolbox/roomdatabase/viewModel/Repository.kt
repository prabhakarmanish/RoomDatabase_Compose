package coded.toolbox.roomdatabase.viewModel

import coded.toolbox.roomdatabase.roomDb.Note
import coded.toolbox.roomdatabase.roomDb.NoteDatabase

class Repository(private val db: NoteDatabase) {
    suspend fun upsertNote(note: Note) {
        db.dao.upsertNote(note)
    }

    suspend fun deleteNote(note: Note) {
        db.dao.deleteNote(note)
    }

    fun getAllNotes() = db.dao.getAllNotes()
}
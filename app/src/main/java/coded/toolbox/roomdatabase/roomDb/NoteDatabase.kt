package coded.toolbox.roomdatabase.roomDb

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [Note::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract val dao : RoomDao
}
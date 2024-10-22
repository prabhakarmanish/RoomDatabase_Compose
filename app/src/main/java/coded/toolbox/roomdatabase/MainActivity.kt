package coded.toolbox.roomdatabase

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import coded.toolbox.roomdatabase.roomDb.NoteDatabase
import coded.toolbox.roomdatabase.ui.screens.Notescreen
import coded.toolbox.roomdatabase.viewModel.NoteViewModel
import coded.toolbox.roomdatabase.viewModel.Repository

class MainActivity : ComponentActivity() {


    val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // Add the 'noteDate' and 'noteTime' columns to the 'Note' table with default values
            database.execSQL("ALTER TABLE `Note` ADD COLUMN `noteDate` TEXT DEFAULT ''")
            database.execSQL("ALTER TABLE `Note` ADD COLUMN `noteTime` TEXT DEFAULT ''")
        }
    }


    // Initialize the Room database
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            NoteDatabase::class.java,
            "note.db"
        )
            .addMigrations(MIGRATION_1_2)
            .build()
    }

    // Use viewModels() to initialize ViewModel in an idiomatic way
    private val viewModel: NoteViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return NoteViewModel(Repository(db)) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Notescreen(viewModel = viewModel)
        }
    }
}

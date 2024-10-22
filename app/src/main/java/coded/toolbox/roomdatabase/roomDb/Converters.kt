package coded.toolbox.roomdatabase.roomDb


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Converters {
    @RequiresApi(Build.VERSION_CODES.O)
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromDate(date: LocalDate?): String? {
        return date?.format(dateFormatter)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun toDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it, dateFormatter) }
    }
}

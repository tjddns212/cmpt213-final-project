package group6.learnlock.room

import android.content.Context
import android.graphics.Color
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import group6.learnlock.model.Assignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.Random

@Database(entities = [Assignment::class], version = 5 )
abstract class AssignmentDatabase : RoomDatabase(){

    abstract fun getAssignmentDao() : AssignmentDAO

    companion object
    {

        @Volatile
        private var INSTANCE:AssignmentDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope):AssignmentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AssignmentDatabase::class.java, "assignment_database"
                ).addCallback(AssignmentDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

    private class AssignmentDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, 2023)
            calendar.set(Calendar.MONTH, Calendar.DECEMBER)
            calendar.set(Calendar.DAY_OF_MONTH, 5)
            val timestamp = calendar.timeInMillis

            INSTANCE?.let { database ->
                scope.launch {
                    val assignmentDao = database.getAssignmentDao()
                    /*assignmentDao.insert(Assignment("Title 3", "Description 1", timestamp,getRandomColor(),false))
                    assignmentDao.insert(Assignment("Title 4", "Description 2", timestamp,getRandomColor(),false))
                    assignmentDao.insert(Assignment("Title 1", "Description 3", timestamp, getRandomColor(),false))
                    assignmentDao.insert(Assignment("Title 2", "Description 3", timestamp, getRandomColor(),false))
                    assignmentDao.insert(Assignment("Title 3", "Description 3", timestamp, getRandomColor(),false))
                    assignmentDao.insert(Assignment("Title 4", "Description 3", timestamp, getRandomColor(),false))
                    assignmentDao.insert(Assignment("Title 5", "Description 3", timestamp, getRandomColor(),false))*/

                }
            }
        }
        private fun getRandomColor(): Int {
            val rnd = Random()
            return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
        }
    }


}

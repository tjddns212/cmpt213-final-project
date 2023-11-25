package group6.learnlock.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import group6.learnlock.model.Assignment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Assignment::class], version = 1 )
abstract class AssignmentDatabase : RoomDatabase(){

    abstract fun getAssignmentDao() : AssignmentDAO

    companion object{
        @Volatile
        private var INSTANCE:AssignmentDatabase? = null

        fun getDatabase(context: Context,scope: CoroutineScope):AssignmentDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AssignmentDatabase::class.java, "assignment_database"
                ).addCallback(AssignmentDatabaseCallback(scope))
                    .build()

                INSTANCE = instance

                instance
            }
        }
    }

    private class AssignmentDatabaseCallback(private val scope : CoroutineScope):RoomDatabase.Callback(){
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)

            INSTANCE?.let{database->
                scope.launch {
                    val assignmentDao = database.getAssignmentDao()
                    assignmentDao.insert(Assignment("Title 1","Description 1", "dueDateTime 1"))
                    assignmentDao.insert(Assignment("Title 2","Description 2", "dueDateTime 2"))
                    assignmentDao.insert(Assignment("Title 3","Description 3", "dueDateTime 3"))
                }

            }
        }
    }

}
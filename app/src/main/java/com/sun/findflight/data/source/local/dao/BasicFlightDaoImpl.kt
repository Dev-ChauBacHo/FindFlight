package com.sun.findflight.data.source.local.dao

import android.util.Log
import com.sun.findflight.data.model.BasicFlight
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_ID
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_TABLE_NAME
import com.sun.findflight.data.source.local.db.AppDatabase

class BasicFlightDaoImpl private constructor(database: AppDatabase) : BasicFlightDao {

    private val writeableDB = database.writableDatabase
    private val readableDB = database.readableDatabase

    override fun getBasicFlight(): BasicFlight? {
        Log.d("BasicFlightDaoImpl", "getBasicFlight: Called")
        var basicFlight: BasicFlight? = null
        val cursor = readableDB.query(
            BASIC_FLIGHT_TABLE_NAME,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        )
        cursor.use {
            if (it.moveToFirst()) basicFlight = BasicFlight(it)
        }
        return basicFlight
    }

    override fun updateBasicFlight(basicFlight: BasicFlight): Boolean {
        val whereClause = "$BASIC_FLIGHT_ID = ?"
        val whereArgs = arrayOf("1")
        return if (writeableDB.update(
                BASIC_FLIGHT_TABLE_NAME,
                basicFlight.getContentValue(),
                whereClause,
                whereArgs
            ) == 0
        ) {
            insertBasicFlight(basicFlight) != -1L
        } else {
            true
        }
    }

    private fun insertBasicFlight(basicFlight: BasicFlight) =
        writeableDB.insert(BASIC_FLIGHT_TABLE_NAME, null, basicFlight.getContentValue())

    companion object {
        private var instance: BasicFlightDaoImpl? = null

        fun getInstance(database: AppDatabase) =
            instance ?: BasicFlightDaoImpl(database).also { instance = it }
    }
}

package com.sun.findflight.data.source.local.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_ADULT
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_CHILD
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_CURRENCY_CODE
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_DEPARTURE_DATE
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_DESTINATION_CODE
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_DESTINATION_NAME
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_ID
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_INFANT
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_ONEWAY
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_ORIGIN_CODE
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_ORIGIN_NAME
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_RETURN_DATE
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_TABLE_NAME
import com.sun.findflight.data.model.BasicFlight.Companion.BASIC_FLIGHT_TRAVEL_CLASS
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_DEPARTURE_DATE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_DESTINATION_CODE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_DESTINATION_NAME
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_FLIGHT_LINK
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_ID
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_IS_FAVOURITE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_ORIGIN_CODE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_ORIGIN_NAME
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_RETURN_DATE
import com.sun.findflight.data.model.Flight.Companion.FLIGHT_TABLE_NAME

class AppDatabase private constructor(
    context: Context?,
    dbName: String,
    version: Int
) : SQLiteOpenHelper(context, dbName, null, version) {

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            Log.d("AppDatabase", "onCreate: called")
            db?.apply {
                execSQL(CREATE_BASIC_FLIGHT_TABLE)
                execSQL(CREATE_FLIGHT_TABLE)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.apply {
            execSQL(DROP_BASIC_FLIGHT_TABLE)
            execSQL(DROP_FLIGHT_TABLE)
        }
        onCreate(db)
    }

    companion object {
        private const val DB_NAME = "findflight.db"
        private const val DB_VERSION = 1

        private const val CREATE_BASIC_FLIGHT_TABLE =
            "CREATE TABLE $BASIC_FLIGHT_TABLE_NAME (" +
                    "$BASIC_FLIGHT_ID INTEGER PRIMARY KEY, " +
                    "$BASIC_FLIGHT_ORIGIN_CODE TEXT, " +
                    "$BASIC_FLIGHT_DESTINATION_CODE TEXT, " +
                    "$BASIC_FLIGHT_ORIGIN_NAME TEXT, " +
                    "$BASIC_FLIGHT_DESTINATION_NAME TEXT, " +
                    "$BASIC_FLIGHT_DEPARTURE_DATE TEXT, " +
                    "$BASIC_FLIGHT_ONEWAY INTEGER, " +
                    "$BASIC_FLIGHT_RETURN_DATE TEXT, " +
                    "$BASIC_FLIGHT_ADULT TEXT, " +
                    "$BASIC_FLIGHT_CHILD TEXT, " +
                    "$BASIC_FLIGHT_INFANT TEXT, " +
                    "$BASIC_FLIGHT_TRAVEL_CLASS TEXT, " +
                    "$BASIC_FLIGHT_CURRENCY_CODE TEXT)"

        private val DROP_BASIC_FLIGHT_TABLE =
            String.format("DROP TABLE IF EXISTS %s", BASIC_FLIGHT_TABLE_NAME)

        private const val CREATE_FLIGHT_TABLE =
            "CREATE TABLE $FLIGHT_TABLE_NAME (" +
                    "$FLIGHT_ID INTEGER PRIMARY KEY, " +
                    "$FLIGHT_ORIGIN_CODE TEXT, " +
                    "$FLIGHT_DESTINATION_CODE TEXT, " +
                    "$FLIGHT_ORIGIN_NAME TEXT, " +
                    "$FLIGHT_DESTINATION_NAME TEXT, " +
                    "$FLIGHT_DEPARTURE_DATE TEXT, " +
                    "$FLIGHT_RETURN_DATE TEXT, " +
                    "$FLIGHT_FLIGHT_LINK TEXT," +
                    "$FLIGHT_IS_FAVOURITE INTEGER)"

        private val DROP_FLIGHT_TABLE =
            String.format("DROP TABLE IF EXISTS %s", FLIGHT_TABLE_NAME)

        private var instance: AppDatabase? = null

        fun getInstance(context: Context) =
            instance ?: AppDatabase(context, DB_NAME, DB_VERSION).also { instance = it }
    }
}

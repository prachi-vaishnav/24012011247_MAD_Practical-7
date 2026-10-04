package com.example.a24012011247_mad_practical_7

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    companion object {

        private const val DATABASE_NAME = "PersonDatabase.db"
        private const val DATABASE_VERSION = 1

        private const val TABLE_NAME = "persons"

        private const val COLUMN_ID = "id"
        private const val COLUMN_NAME = "name"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PHONE = "phone"
        private const val COLUMN_ADDRESS = "address"
        private const val COLUMN_LATITUDE = "latitude"
        private const val COLUMN_LONGITUDE = "longitude"
    }

    override fun onCreate(db: SQLiteDatabase) {

        val createTable = """
            CREATE TABLE $TABLE_NAME (
                $COLUMN_ID TEXT PRIMARY KEY,
                $COLUMN_NAME TEXT,
                $COLUMN_EMAIL TEXT,
                $COLUMN_PHONE TEXT,
                $COLUMN_ADDRESS TEXT,
                $COLUMN_LATITUDE REAL,
                $COLUMN_LONGITUDE REAL
            )
        """.trimIndent()

        db.execSQL(createTable)
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    fun insertPerson(person: Person): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put(COLUMN_ID, person.id)
        values.put(COLUMN_NAME, person.name)
        values.put(COLUMN_EMAIL, person.email)
        values.put(COLUMN_PHONE, person.phone)
        values.put(COLUMN_ADDRESS, person.address)
        values.put(COLUMN_LATITUDE, person.latitude)
        values.put(COLUMN_LONGITUDE, person.longitude)

        val result =
            db.insert(TABLE_NAME, null, values)

        db.close()

        return result != -1L
    }

    fun getAllPersons(): ArrayList<Person> {

        val personList = ArrayList<Person>()

        val db = readableDatabase

        val cursor =
            db.rawQuery(
                "SELECT * FROM $TABLE_NAME",
                null
            )

        if (cursor.moveToFirst()) {

            do {

                val person = Person(
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_ID
                        )
                    ),
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_NAME
                        )
                    ),
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_EMAIL
                        )
                    ),
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_PHONE
                        )
                    ),
                    cursor.getString(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_ADDRESS
                        )
                    ),
                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_LATITUDE
                        )
                    ),
                    cursor.getDouble(
                        cursor.getColumnIndexOrThrow(
                            COLUMN_LONGITUDE
                        )
                    )
                )

                personList.add(person)

            } while (cursor.moveToNext())
        }

        cursor.close()
        db.close()

        return personList
    }

    fun deletePerson(id: String): Boolean {

        val db = writableDatabase

        val result =
            db.delete(
                TABLE_NAME,
                "$COLUMN_ID = ?",
                arrayOf(id)
            )

        db.close()

        return result > 0
    }
}
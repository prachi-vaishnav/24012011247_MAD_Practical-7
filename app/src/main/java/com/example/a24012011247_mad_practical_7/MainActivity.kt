package com.example.a24012011247_mad_practical_7

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.a24012011247_mad_practical_7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var databaseHelper: DatabaseHelper

    private lateinit var adapter: PersonAdapter

    private var personList = ArrayList<Person>()

    companion object {

        private const val TAG = "MainActivity"

        private const val API_URL =
            "https://api.json-generator.com/templates/9Rul6Z6MrDF-/data"

        private const val TOKEN = "PASTE_YOUR_FULL_TOKEN_HERE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding =
            ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        databaseHelper =
            DatabaseHelper(this)

        setupRecyclerView()

        loadPersonsFromDatabase()

        getPersonDataFromApi()
    }

    private fun setupRecyclerView() {

        adapter = PersonAdapter(
            personList,
            databaseHelper
        )

        binding.recyclerViewPersons.layoutManager =
            LinearLayoutManager(this)

        binding.recyclerViewPersons.adapter =
            adapter
    }

    private fun getPersonDataFromApi() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val data =
                    HttpRequest().makeServiceCall(
                        reqUrl = API_URL,
                        token = TOKEN
                    )
                Log.d(TAG, "API RESPONSE: $data")
                withContext(Dispatchers.Main) {

                    if (data != null) {

                        getPersonDetailsFromJson(data)

                    } else {

                        Log.e(
                            TAG,
                            "No Data Received"
                        )
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    TAG,
                    "API Error",
                    e
                )
            }
        }
    }

    private fun getPersonDetailsFromJson(
        json: String
    ) {

        try {

            val db =
                databaseHelper.writableDatabase

            db.delete(
                "persons",
                null,
                null
            )

            db.close()

            val jsonArray =
                JSONArray(json)

            for (i in 0 until jsonArray.length()) {

                val obj =
                    jsonArray.getJSONObject(i)

                val id =
                    obj.getString("id")

                val email =
                    obj.getString("email")

                val phone =
                    obj.getString("phone")

                val profile =
                    obj.getJSONObject("profile")

                val name =
                    profile.getString("name")

                val address =
                    profile.getString("address")

                val location =
                    profile.getJSONObject("location")

                val latitude =
                    location.getDouble("lat")

                val longitude =
                    location.getDouble("long")

                val person =
                    Person(
                        id,
                        name,
                        email,
                        phone,
                        address,
                        latitude,
                        longitude
                    )

                databaseHelper.insertPerson(person)
            }

            loadPersonsFromDatabase()

        } catch (e: Exception) {

            Log.e(
                TAG,
                "JSON Parsing Error",
                e
            )
        }
    }

    private fun loadPersonsFromDatabase() {

        personList.clear()

        personList.addAll(
            databaseHelper.getAllPersons()
        )

        adapter.notifyDataSetChanged()
    }
}
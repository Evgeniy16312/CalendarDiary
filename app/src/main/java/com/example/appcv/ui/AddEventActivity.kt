package com.example.appcv.ui

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.appcv.MainActivity
import com.example.appcv.databinding.AddEventActivityBinding
import com.example.appcv.model.Event
import com.example.appcv.utils.Formats
import com.google.gson.Gson
import io.realm.Realm
import java.util.*

class AddEventActivity : AppCompatActivity() {

    private var dayId: Long? = null
    var eventTimeFrom: Long? = null
    var eventTimeTo: Long? = null
    var eventDescription: String? = null
    var eventTitle: String? = null

    private lateinit var realm: Realm
    private lateinit var gson: Gson
    private lateinit var binding: AddEventActivityBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = AddEventActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        realm = Realm.getDefaultInstance()
        gson = Gson().newBuilder().create()

        binding.dateET.setOnClickListener {
            chooseDate()
        }

        binding.timeFromET.setOnClickListener {
            chooseTimeFrom()
        }

        binding.timeToET.setOnClickListener {
            chooseTimeTo()
        }

        binding.commitBtn.setOnClickListener {
            eventTitle = binding.titleED.text.toString()
            if (checkTransaction()) {
                addEventToDB()
            } else {
                Toast.makeText(this, "Incorrect input, check the fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun addEventToDB() {
        try {
            realm.executeTransaction {
                eventDescription = binding.descriptionED.text.toString()

                val currentIdNumber: Number? = realm.where(Event::class.java).max("id")
                val nextId = if (currentIdNumber == null) {
                    1
                } else {
                    currentIdNumber.toInt() + 1
                }

                val event = Event(
                    nextId,
                    dayId,
                    eventTitle,
                    eventTimeFrom,
                    eventTimeTo,
                    eventDescription
                )
                val jsonEventString = gson.toJson(event)
                realm.createOrUpdateObjectFromJson(Event::class.java, jsonEventString)

            }
            Toast.makeText(this, "Event added successfully!", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed $e", Toast.LENGTH_LONG).show()
        }
    }

    private fun chooseTimeTo() {
        val now = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this, { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                binding.timeToET.setText(Formats().timeFormat.format(selectedTime.time))
                eventTimeTo = selectedTime.time.time
            },
            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
        )
        timePicker.show()
    }

    private fun chooseTimeFrom() {
        val now = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            this, { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                binding.timeFromET.setText(Formats().timeFormat.format(selectedTime.time))
                eventTimeFrom = selectedTime.time.time
            },
            now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false
        )
        timePicker.show()
    }

    private fun chooseDate() {
        val now = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->

                val selectedDate = Calendar.getInstance()
                selectedDate.set(Calendar.YEAR, year)
                selectedDate.set(Calendar.MONTH, month)
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                selectedDate.set(Calendar.HOUR_OF_DAY, 0)
                selectedDate.set(Calendar.MINUTE, 0)
                selectedDate.set(Calendar.SECOND, 0)
                selectedDate.set(Calendar.MILLISECOND, 0)

                val date = Formats().format.format(selectedDate.time)

                dayId = selectedDate.time.time
                binding.dateET.setText(date)
            },
            now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

    private fun checkTransaction(): Boolean {
        return !(dayId == null ||
                eventTitle!!.isEmpty() ||
                eventTimeFrom == null ||
                eventTimeTo == null || eventTimeFrom!! > eventTimeTo!!)
    }
}
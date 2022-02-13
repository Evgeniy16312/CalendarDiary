package com.example.appcv

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.CalendarView
import androidx.recyclerview.widget.RecyclerView
import com.example.appcv.ui.AddEventActivity
import com.example.appcv.ui.adapter.EventsAdapter
import com.example.appcv.model.Event
import com.google.android.material.floatingactionbutton.FloatingActionButton
import io.realm.Realm
import io.realm.RealmResults
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var realm: Realm
    lateinit var eventsRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        realm = Realm.getDefaultInstance()
        eventsRV = findViewById(R.id.eventsRV)

        val calendar: CalendarView = findViewById(R.id.calendarView)
        calendar.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDay = Calendar.getInstance()
            selectedDay.set(Calendar.YEAR, year)
            selectedDay.set(Calendar.MONTH, month)
            selectedDay.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            selectedDay.set(Calendar.HOUR_OF_DAY, 0)
            selectedDay.set(Calendar.MINUTE, 0)
            selectedDay.set(Calendar.SECOND, 0)
            selectedDay.set(Calendar.MILLISECOND, 0)
            loadEvents(selectedDay.timeInMillis)
        }

        val addEventBtn = findViewById<FloatingActionButton>(R.id.addEvent_btn)
        addEventBtn.setOnClickListener {
            startActivity(Intent(this, AddEventActivity::class.java))
            finish()
        }

        val selectedDay = Calendar.getInstance()
        selectedDay.set(Calendar.HOUR_OF_DAY, 0)
        selectedDay.set(Calendar.MINUTE, 0)
        selectedDay.set(Calendar.SECOND, 0)
        selectedDay.set(Calendar.MILLISECOND, 0)
        loadEvents(selectedDay.timeInMillis)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun loadEvents(time: Long) {
        val results: RealmResults<Event> = realm.where(Event::class.java)
            .equalTo("dayId", time).findAll()
        eventsRV.adapter = EventsAdapter(this, results)
        eventsRV.adapter!!.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }
}
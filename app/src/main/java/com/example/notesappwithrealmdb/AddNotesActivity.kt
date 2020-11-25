package com.example.notesappwithrealmdb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import io.realm.Realm
import java.lang.Exception

class AddNotesActivity : AppCompatActivity() {

    private lateinit var titleED: EditText
    private lateinit var descriptionED: EditText
    private lateinit var saveNotesBtn: EditText
    private lateinit var realm: Realm

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        // init views

        realm = Realm.getDefaultInstance()
        titleED = findViewById(R.id.title_EditText)
        descriptionED = findViewById(R.id.description_EditText)
        saveNotesBtn = findViewById(R.id.saveNotesBtn)

        // onclick Listener

        saveNotesBtn.setOnClickListener {

            addNotesToDB()
        }

    }

    private fun addNotesToDB() {

        try {
            // Auto increment ID of the note
            realm.beginTransaction()

            val currentIdNumber:Number? = realm.where(Notes::class.java).max("id")
            val nextID:Int

            nextID = if (currentIdNumber == null) {
                1
            } else {
                currentIdNumber.toInt() + 1
            }

            val notes = Notes()
            notes.title = titleED.text.toString()
            notes.description = descriptionED.text.toString()
            notes.id = nextID

            // copy this transaction & commit

            realm.copyToRealmOrUpdate(notes)
            realm.commitTransaction()

            Toast.makeText(this, "Notes Added Successfully", Toast.LENGTH_SHORT).show()

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        } catch (e: Exception) {

            Toast.makeText(this, "Something failed: $e", Toast.LENGTH_SHORT).show()
        }

    }
}
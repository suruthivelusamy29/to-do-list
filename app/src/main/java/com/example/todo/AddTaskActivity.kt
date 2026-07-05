/*package com.example.todo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTaskTitle: EditText
    private lateinit var etTaskDate: EditText
    private lateinit var etTaskTime: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnAddTask: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        // Initialize views
        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDate = findViewById(R.id.etTaskDate)
        etTaskTime = findViewById(R.id.etTaskTime)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnAddTask = findViewById(R.id.btnAddTask)

        // Populate categories from intent
        val categories = intent.getStringArrayListExtra("categories") ?: arrayListOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Date picker
        etTaskDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this,
                { _, year, month, dayOfMonth ->
                    etTaskDate.setText("${year}-${month + 1}-${dayOfMonth}")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time picker
        etTaskTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(this,
                { _, hourOfDay, minute ->
                    etTaskTime.setText(String.format("%02d:%02d", hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        // Add Task button
        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val date = etTaskDate.text.toString().trim()
            val time = etTaskTime.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()
            val category = spinnerCategory.selectedItem?.toString() ?: ""

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val intent = intent
            intent.putExtra("title", title)
            intent.putExtra("date", date)
            intent.putExtra("time", time)
            intent.putExtra("description", description)
            intent.putExtra("category", category)
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
package com.example.todo

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTaskTitle: EditText
    private lateinit var etTaskDate: EditText
    private lateinit var etTaskTime: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnAddTask: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var categories: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDate = findViewById(R.id.etTaskDate)
        etTaskTime = findViewById(R.id.etTaskTime)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnAddTask = findViewById(R.id.btnAddTask)

        categories = intent.getStringArrayListExtra("categories") ?: listOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Date picker
        etTaskDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth ->
                    etTaskDate.setText("${year}-${month + 1}-${dayOfMonth}")
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time picker
        etTaskTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute ->
                    etTaskTime.setText(String.format("%02d:%02d", hourOfDay, minute))
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        // Add Task button click
        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val date = etTaskDate.text.toString().trim()
            val time = etTaskTime.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()
            val category = spinnerCategory.selectedItem?.toString() ?: ""

            // Validation
            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = firebaseAuth.currentUser?.uid
            if (userId == null) {
                Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val color = when (category) {
                "Work" -> Color.parseColor("#FF6B6B")
                "Personal" -> Color.parseColor("#4ECDC4")
                "Shop" -> Color.parseColor("#FFD93D")
                "Health" -> Color.parseColor("#6BCB77")
                else -> Color.GRAY
            }

            val newTaskRef = db.collection("tasks").document()
            val newTask = Task(
                userId = userId,
                title = title,
                description = description,
                date = date,
                time = time,
                category = category,
                color = color,
                completed = false,
                id = newTaskRef.id
            )

            newTaskRef.set(newTask)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()
                    finish() // Close AddTaskActivity
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add task: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}*/
/*package com.example.todo
import android.content.Intent
import android.app.Activity


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTaskTitle: EditText
    private lateinit var etTaskDate: EditText
    private lateinit var etTaskTime: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnAddTask: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var categories: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDate = findViewById(R.id.etTaskDate)
        etTaskTime = findViewById(R.id.etTaskTime)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnAddTask = findViewById(R.id.btnAddTask)

        categories = intent.getStringArrayListExtra("categories") ?: listOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        // Date picker
        etTaskDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth -> etTaskDate.setText("${year}-${month + 1}-${dayOfMonth}") },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        // Time picker
        etTaskTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute -> etTaskTime.setText(String.format("%02d:%02d", hourOfDay, minute)) },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val date = etTaskDate.text.toString().trim()
            val time = etTaskTime.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()
            val category = spinnerCategory.selectedItem?.toString() ?: ""

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = firebaseAuth.currentUser?.uid ?: run {
                Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val color = when (category) {
                "Work" -> Color.parseColor("#FF6B6B")
                "Personal" -> Color.parseColor("#4ECDC4")
                "Shop" -> Color.parseColor("#FFD93D")
                "Health" -> Color.parseColor("#6BCB77")
                else -> Color.GRAY
            }

            val newTaskRef = db.collection("tasks").document()
            val newTask = Task(userId, title, description, date, time, category, color, false, newTaskRef.id)

            newTaskRef.set(newTask)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()

                    val resultIntent = Intent()
                    resultIntent.putExtra("title", title)
                    resultIntent.putExtra("date", date)
                    resultIntent.putExtra("time", time)
                    resultIntent.putExtra("description", description)
                    resultIntent.putExtra("category", category)
                    resultIntent.putExtra("id", newTaskRef.id)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add task: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}*/package com.example.todo

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.content.Intent

import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AddTaskActivity : AppCompatActivity() {

    private lateinit var etTaskTitle: EditText
    private lateinit var etTaskDate: EditText
    private lateinit var etTaskTime: EditText
    private lateinit var etTaskDescription: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var btnAddTask: Button

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var categories: List<String> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        etTaskTitle = findViewById(R.id.etTaskTitle)
        etTaskDate = findViewById(R.id.etTaskDate)
        etTaskTime = findViewById(R.id.etTaskTime)
        etTaskDescription = findViewById(R.id.etTaskDescription)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        btnAddTask = findViewById(R.id.btnAddTask)

        categories = intent.getStringArrayListExtra("categories") ?: listOf()
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        etTaskDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, year, month, dayOfMonth -> etTaskDate.setText("${year}-${month + 1}-${dayOfMonth}") },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        etTaskTime.setOnClickListener {
            val calendar = Calendar.getInstance()
            TimePickerDialog(
                this,
                { _, hourOfDay, minute -> etTaskTime.setText(String.format("%02d:%02d", hourOfDay, minute)) },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        btnAddTask.setOnClickListener {
            val title = etTaskTitle.text.toString().trim()
            val date = etTaskDate.text.toString().trim()
            val time = etTaskTime.text.toString().trim()
            val description = etTaskDescription.text.toString().trim()
            val category = spinnerCategory.selectedItem?.toString() ?: ""

            if (title.isEmpty() || date.isEmpty() || time.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val userId = firebaseAuth.currentUser?.uid ?: run {
                Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val color = when (category) {
                "💼" -> Color.parseColor("#FF6B6B")
                "🏠" -> Color.parseColor("#4ECDC4")
                "🛒" -> Color.parseColor("#FFD93D")
                "💊" -> Color.parseColor("#6BCB77")
                else -> Color.GRAY
            }

            val newTaskRef = db.collection("users")
                .document(userId)
                .collection("tasks")
                .document()
            val newTask = Task(userId, title, description, date, time, category, color, false, newTaskRef.id)

            newTaskRef.set(newTask)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task added!", Toast.LENGTH_SHORT).show()

                    val resultIntent = Intent()
                    resultIntent.putExtra("title", title)
                    resultIntent.putExtra("date", date)
                    resultIntent.putExtra("time", time)
                    resultIntent.putExtra("description", description)
                    resultIntent.putExtra("category", category)
                    resultIntent.putExtra("id", newTaskRef.id)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to add task: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

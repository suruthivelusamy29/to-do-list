/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val tvTitle: TextView = findViewById(R.id.tvTaskTitle)
        val tvDescription: TextView = findViewById(R.id.tvTaskDescription)
        val tvCategory: TextView = findViewById(R.id.tvTaskCategory)
        val tvDate: TextView = findViewById(R.id.tvTaskDate)
        val tvTime: TextView = findViewById(R.id.tvTaskTime)
        val btnDone: Button = findViewById(R.id.btnDone)

        val index = intent.getIntExtra("taskIndex", -1)
        tvTitle.text = intent.getStringExtra("taskTitle")
        tvDescription.text = intent.getStringExtra("taskDescription")
        tvCategory.text = "Category: ${intent.getStringExtra("taskCategory")}"
        tvDate.text = "Date: ${intent.getStringExtra("taskDate")}"
        tvTime.text = "Time: ${intent.getStringExtra("taskTime")}"

        btnDone.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("taskIndex", index)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}

package com.example.todo

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class TaskDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val tvTitle: TextView = findViewById(R.id.tvTaskTitle)
        val tvDescription: TextView = findViewById(R.id.tvTaskDescription)
        val tvCategory: TextView = findViewById(R.id.tvTaskCategory)
        val tvDate: TextView = findViewById(R.id.tvTaskDate)
        val tvTime: TextView = findViewById(R.id.tvTaskTime)
        val btnDone: Button = findViewById(R.id.btnDone)

        val taskId = intent.getStringExtra("taskId") ?: return

        tvTitle.text = intent.getStringExtra("taskTitle")
        tvDescription.text = intent.getStringExtra("taskDescription")
        tvCategory.text = "Category: ${intent.getStringExtra("taskCategory")}"
        tvDate.text = "Date: ${intent.getStringExtra("taskDate")}"
        tvTime.text = "Time: ${intent.getStringExtra("taskTime")}"

        btnDone.setOnClickListener {
            FirebaseFirestore.getInstance().collection("tasks").document(taskId)
                .update("completed", true)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task marked done!", Toast.LENGTH_SHORT).show()
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to mark done: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }
}*/
package com.example.todo

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.firestore.FirebaseFirestore



class TaskDetailActivity : AppCompatActivity() {

    private lateinit var taskId: String
    private lateinit var btnDone: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val tvTitle: TextView = findViewById(R.id.tvTaskTitle)
        val tvDescription: TextView = findViewById(R.id.tvTaskDescription)
        val tvCategory: TextView = findViewById(R.id.tvTaskCategory)
        val tvDate: TextView = findViewById(R.id.tvTaskDate)
        val tvTime: TextView = findViewById(R.id.tvTaskTime)
        btnDone = findViewById(R.id.btnDone)

        taskId = intent.getStringExtra("taskId").orEmpty()
        if (taskId.isEmpty()) {
            Toast.makeText(this, "Task not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        tvTitle.text = intent.getStringExtra("taskTitle")
        tvDescription.text = intent.getStringExtra("taskDescription")
        tvCategory.text = "Category: ${intent.getStringExtra("taskCategory")}"
        tvDate.text = "Date: ${intent.getStringExtra("taskDate")}"
        tvTime.text = "Time: ${intent.getStringExtra("taskTime")}"

        btnDone.setOnClickListener { markTaskDone() }
    }

    /*private fun markTaskDone() {
        FirebaseFirestore.getInstance().collection("tasks").document(taskId)
            .update("completed", true)
            .addOnSuccessListener {
                Toast.makeText(this, "Task marked done!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)  // ✅ Return OK to HomeActivity
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to mark done: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}*/

    private fun markTaskDone() {

        val user = FirebaseAuth.getInstance().currentUser
        if (user == null) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show()
            return
        }

        FirebaseFirestore.getInstance()
            .collection("users")
            .document(user.uid)
            .collection("tasks")
            .document(taskId)
            .update("completed", true)
            .addOnSuccessListener {
                Toast.makeText(this, "Task marked done!", Toast.LENGTH_SHORT).show()
                setResult(Activity.RESULT_OK)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to mark done: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }
}


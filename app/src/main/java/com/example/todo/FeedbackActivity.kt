/*package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)
    }
}*/
/*package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val etMessage = findViewById<EditText>(R.id.etFeedback)
        val btnSend = findViewById<Button>(R.id.btnSendFeedback)

        btnSend.setOnClickListener {
            val message = etMessage.text.toString()

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "message/rfc822"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("support@todoapp.com"))
            intent.putExtra(Intent.EXTRA_SUBJECT, "App Feedback")
            intent.putExtra(Intent.EXTRA_TEXT, message)

            startActivity(Intent.createChooser(intent, "Send Feedback"))
        }
    }
}*/
package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class FeedbackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        val etName = findViewById<EditText>(R.id.etFeedbackName)
        val etEmail = findViewById<EditText>(R.id.etFeedbackEmail)
        val etMessage = findViewById<EditText>(R.id.etFeedbackMessage)
        val btnSend = findViewById<Button>(R.id.btnSendFeedback)

        val db = FirebaseFirestore.getInstance()

        btnSend.setOnClickListener {
            val name = etName.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val message = etMessage.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || message.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val feedback = hashMapOf(
                "name" to name,
                "email" to email,
                "message" to message,
                "timestamp" to System.currentTimeMillis()
            )

            db.collection("feedback")
                .add(feedback)
                .addOnSuccessListener {
                    Toast.makeText(this, "Feedback sent successfully!", Toast.LENGTH_SHORT).show()
                    etName.text.clear()
                    etEmail.text.clear()
                    etMessage.text.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to send feedback", Toast.LENGTH_SHORT).show()
                }
        }
    }
}



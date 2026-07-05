/*package com.example.todo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class EditProfileActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile) // create this XML
    }
}*/
/*package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSave: Button

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.etName)
        btnSave = findViewById(R.id.btnSaveProfile)

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun saveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val name = etName.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
            return
        }

        firestore.collection("users")
            .document(uid)
            .update("name", name)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show()
            }
    }
}*/
/*package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEditEmail)
        btnSave = findViewById(R.id.btnSaveProfile)

        loadProfile()

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return

        firestore.collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener { doc ->
                if (doc.exists()) {
                    etName.setText(doc.getString("name") ?: "")
                    etEmail.setText(doc.getString("email") ?: auth.currentUser?.email)
                } else {
                    etEmail.setText(auth.currentUser?.email)
                }
            }
    }

    private fun saveProfile() {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        val userData = hashMapOf(
            "name" to name,
            "email" to email
        )

        firestore.collection("users")
            .document(uid)
            .set(userData, com.google.firebase.firestore.SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}*/
/*package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEditEmail)
        btnSave = findViewById(R.id.btnSaveProfile)

        loadProfile()

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val user = auth.currentUser ?: return

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                etName.setText(doc.getString("name") ?: "")
                etEmail.setText(doc.getString("email") ?: user.email)
            }
    }

    private fun saveProfile() {
        val user = auth.currentUser
        if (user == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val name = etName.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()

        if (name.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // 🔐 Step 1: Update AUTH email if changed
        if (newEmail != user.email) {
            user.updateEmail(newEmail)
                .addOnSuccessListener {
                    saveToFirestore(user.uid, name, newEmail)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Email update failed: Re-login required",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            saveToFirestore(user.uid, name, newEmail)
        }
    }

    // 🔥 Step 2: Save to Firestore safely
    private fun saveToFirestore(uid: String, name: String, email: String) {
        val userData = hashMapOf(
            "name" to name,
            "email" to email
        )

        firestore.collection("users")
            .document(uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Firestore error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}*/
/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var imgAvatar: ImageView

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEditEmail)
        btnSave = findViewById(R.id.btnSaveProfile)
        imgAvatar = findViewById(R.id.imgEditAvatar)

        loadProfile()

        imgAvatar.setOnClickListener {
            pickImageFromGallery()
        }

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    // 🔹 LOAD EXISTING PROFILE
    private fun loadProfile() {
        val user = auth.currentUser ?: return

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                etName.setText(doc.getString("name") ?: "")
                etEmail.setText(doc.getString("email") ?: user.email)

                val avatarUrl = doc.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(imgAvatar)
                }
            }
    }

    // 🔹 IMAGE PICKER
    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imgAvatar.setImageURI(imageUri)
        }
    }

    // 🔹 SAVE PROFILE
    private fun saveProfile() {
        val user = auth.currentUser ?: return

        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        if (imageUri != null) {
            uploadAvatar(user.uid, name, email)
        } else {
            saveToFirestore(user.uid, name, email, null)
        }
    }

    // 🔹 UPLOAD AVATAR TO STORAGE
    private fun uploadAvatar(uid: String, name: String, email: String) {
        val ref = storage.reference.child("avatars/$uid.jpg")

        imageUri?.let {
            ref.putFile(it)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    saveToFirestore(uid, name, email, uri.toString())
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Avatar upload failed", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // 🔹 SAVE TO FIRESTORE
    private fun saveToFirestore(
        uid: String,
        name: String,
        email: String,
        avatarUrl: String?
    ) {
        val data = hashMapOf(
            "name" to name,
            "email" to email
        )

        if (avatarUrl != null) {
            data["avatarUrl"] = avatarUrl
        }

        firestore.collection("users")
            .document(uid)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Save failed", Toast.LENGTH_SHORT).show()
            }
    }
}*/
 */
/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class EditProfileActivity : AppCompatActivity() {

    private lateinit var imgAvatar: ImageView
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private var imageUri: Uri? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        imgAvatar = findViewById(R.id.imgEditAvatar)
        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEditEmail)
        btnSave = findViewById(R.id.btnSaveProfile)

        loadProfile()

        imgAvatar.setOnClickListener {
            pickImage()
        }

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val user = auth.currentUser ?: return

        firestore.collection("users")
            .document(user.uid)
            .get()
            .addOnSuccessListener { doc ->
                etName.setText(doc.getString("name") ?: "")
                etEmail.setText(doc.getString("email") ?: user.email)

                val avatarUrl = doc.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .into(imgAvatar)
                }
            }
    }

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            imgAvatar.setImageURI(imageUri)
        }
    }

    private fun saveProfile() {
        val user = auth.currentUser ?: return

        val name = etName.text.toString().trim()
        val email = etEmail.text.toString().trim()

        if (name.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "All fields required", Toast.LENGTH_SHORT).show()
            return
        }

        if (email != user.email) {
            user.updateEmail(email)
                .addOnSuccessListener {
                    uploadAvatarAndSave(user.uid, name, email)
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Email update failed. Re-login required",
                        Toast.LENGTH_LONG
                    ).show()
                }
        } else {
            uploadAvatarAndSave(user.uid, name, email)
        }
    }

    private fun uploadAvatarAndSave(uid: String, name: String, email: String) {
        if (imageUri == null) {
            saveToFirestore(uid, name, email, null)
            return
        }

        val ref = storage.reference.child("avatars/$uid.jpg")
        ref.putFile(imageUri!!)
            .continueWithTask { ref.downloadUrl }
            .addOnSuccessListener { uri ->
                saveToFirestore(uid, name, email, uri.toString())
            }
    }

    private fun saveToFirestore(uid: String, name: String, email: String, avatarUrl: String?) {
        val data = hashMapOf(
            "name" to name,
            "email" to email
        )

        if (avatarUrl != null) {
            data["avatarUrl"] = avatarUrl
        }

        firestore.collection("users")
            .document(uid)
            .set(data, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show()
                finish()
            }
    }
}*/

 */
package com.example.todo

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class EditProfileActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnSave: Button
    private lateinit var imgAvatar: ImageView

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    private var avatarUri: Uri? = null

    // Image picker launcher
    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                avatarUri = uri
                imgAvatar.setImageURI(uri)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEditEmail)
        btnSave = findViewById(R.id.btnSaveProfile)
        imgAvatar = findViewById(R.id.imgEditAvatar)

        loadProfile()

        imgAvatar.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        btnSave.setOnClickListener {
            saveProfile()
        }
    }

    private fun loadProfile() {
        val user = auth.currentUser ?: return

        firestore.collection("users").document(user.uid).get()
            .addOnSuccessListener { doc: DocumentSnapshot ->
                etName.setText(doc.getString("name") ?: "")
                etEmail.setText(doc.getString("email") ?: user.email)
                val avatarUrl: String? = doc.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this).load(avatarUrl).into(imgAvatar)
                }
            }
            .addOnFailureListener { exception: Exception ->
                Toast.makeText(this, "Failed to load profile: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfile() {
        val user = auth.currentUser ?: run {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show()
            return
        }

        val name: String = etName.text.toString().trim()
        val newEmail: String = etEmail.text.toString().trim()

        if (name.isEmpty() || newEmail.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return
        }

        // Step 1: Update Auth email if changed
        if (newEmail != user.email) {
            user.updateEmail(newEmail)
                .addOnSuccessListener {
                    // Continue to save Firestore after email updated
                    uploadAvatarAndSave(user.uid, name, newEmail)
                }
                .addOnFailureListener { exception: Exception ->
                    Toast.makeText(this, "Email update failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            uploadAvatarAndSave(user.uid, name, newEmail)
        }
    }

    private fun uploadAvatarAndSave(uid: String, name: String, email: String) {
        if (avatarUri != null) {
            val avatarRef: StorageReference = storage.reference.child("avatars/$uid/avatar.jpg")
            avatarRef.putFile(avatarUri!!)
                .addOnSuccessListener {
                    avatarRef.downloadUrl.addOnSuccessListener { uri: Uri ->
                        saveToFirestore(uid, name, email, uri.toString())
                    }
                        .addOnFailureListener { exception: Exception ->
                            Toast.makeText(this, "Failed to get avatar URL: ${exception.message}", Toast.LENGTH_LONG).show()
                        }
                }
                .addOnFailureListener { exception: Exception ->
                    Toast.makeText(this, "Avatar upload failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        } else {
            saveToFirestore(uid, name, email, null)
        }
    }

    private fun saveToFirestore(uid: String, name: String, email: String, avatarUrl: String?) {
        val userData: MutableMap<String, Any> = hashMapOf(
            "name" to name,
            "email" to email
        )
        if (avatarUrl != null) userData["avatarUrl"] = avatarUrl

        firestore.collection("users").document(uid)
            .set(userData, SetOptions.merge())
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { exception: Exception ->
                Toast.makeText(this, "Firestore error: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}

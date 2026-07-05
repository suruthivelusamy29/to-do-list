/*package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var tvProfileEmail: TextView
    private lateinit var btnLogout: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        firebaseAuth = FirebaseAuth.getInstance()

        tvProfileEmail = findViewById(R.id.tvProfileEmail)
        btnLogout = findViewById(R.id.btnLogout)
        btnBack = findViewById(R.id.btnBack)

        // Show user email
        tvProfileEmail.text = firebaseAuth.currentUser?.email ?: "Unknown User"

        // Logout
        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
            goToLogin()
        }

        // Back button
        btnBack.setOnClickListener { finish() }
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}*/

/*package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SettingsActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: ImageView
    private lateinit var btnLogout: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        firebaseAuth = FirebaseAuth.getInstance()

        tvName = findViewById(R.id.tvProfileName)
        tvEmail = findViewById(R.id.tvProfileEmail)
        imgAvatar = findViewById(R.id.imgAvatar)
        btnLogout = findViewById(R.id.btnLogout)

        // Load user info
        val user = firebaseAuth.currentUser
        if (user != null) {
            tvEmail.text = user.email
            tvName.text = user.displayName ?: "User"
        }

        btnLogout.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        imgAvatar.setOnClickListener {
            // Optional: implement avatar picker
        }
    }
}*/

/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: ImageView
    private lateinit var btnLogout: Button

    private var imageUri: Uri? = null
    private var profileListener: ListenerRegistration? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        tvName = findViewById(R.id.tvProfileName)
        tvEmail = findViewById(R.id.tvProfileEmail)
        imgAvatar = findViewById(R.id.imgAvatar)
        btnLogout = findViewById(R.id.btnLogout)

        observeUserProfile()

        imgAvatar.setOnClickListener { pickImageFromGallery() }
        tvName.setOnClickListener { showEditNameDialog() }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    // 🔹 REALTIME FIRESTORE LISTENER
    private fun observeUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        profileListener = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener

                tvName.text = snapshot.getString("name") ?: "User"
                tvEmail.text = snapshot.getString("email") ?: ""

                val avatarUrl = snapshot.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(imgAvatar)
                } else {
                    imgAvatar.setImageResource(R.drawable.ic_default_avatar)
                }
            }
    }

    // 🔹 PICK IMAGE
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
            uploadAvatar()
        }
    }

    // 🔹 UPLOAD AVATAR
    private fun uploadAvatar() {
        val uid = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("avatars/$uid.jpg")

        imageUri?.let {
            ref.putFile(it)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    firestore.collection("users")
                        .document(uid)
                        .update("avatarUrl", uri.toString())
                }
        }
    }

    // 🔹 EDIT NAME
    private fun showEditNameDialog() {
        val editText = EditText(this)
        editText.setText(tvName.text.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                updateName(editText.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateName(name: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .update("name", name)
    }

    override fun onDestroy() {
        super.onDestroy()
        profileListener?.remove()
    }
}*/


 package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: ImageView
    private lateinit var btnLogout: Button

    private var imageUri: Uri? = null
    private var profileListener: ListenerRegistration? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        tvName = findViewById(R.id.tvProfileName)
        tvEmail = findViewById(R.id.tvProfileEmail)
        imgAvatar = findViewById(R.id.imgAvatar)
        btnLogout = findViewById(R.id.btnLogout)

        // Observe profile updates
        observeUserProfile()

        // Avatar click
        imgAvatar.setOnClickListener { pickImageFromGallery() }

        // Name click
        tvName.setOnClickListener { showEditNameDialog() }

        // Logout click
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Settings options clicks
        findViewById<LinearLayout>(R.id.llEditProfile).setOnClickListener {
            Toast.makeText(this, "Edit Profile clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Edit Profile screen
        }

        findViewById<LinearLayout>(R.id.llAppPreferences).setOnClickListener {
            Toast.makeText(this, "App Preferences clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to App Preferences screen
        }

        findViewById<LinearLayout>(R.id.llAbout).setOnClickListener {
            Toast.makeText(this, "About clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to About screen
        }

        findViewById<LinearLayout>(R.id.llFeedback).setOnClickListener {
            Toast.makeText(this, "Feedback / Contact clicked", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to Feedback / Contact screen
        }
    }

    // 🔹 REALTIME FIRESTORE LISTENER
    private fun observeUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        profileListener = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener

                tvName.text = snapshot.getString("name") ?: "User"
                tvEmail.text = snapshot.getString("email") ?: ""

                val avatarUrl = snapshot.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(imgAvatar)
                } else {
                    imgAvatar.setImageResource(R.drawable.ic_default_avatar)
                }
            }
    }

    // 🔹 PICK IMAGE
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
            uploadAvatar()
        }
    }

    // 🔹 UPLOAD AVATAR
    private fun uploadAvatar() {
        val uid = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("avatars/$uid.jpg")

        imageUri?.let {
            ref.putFile(it)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    firestore.collection("users")
                        .document(uid)
                        .update("avatarUrl", uri.toString())
                }
        }
    }

    // 🔹 EDIT NAME
    private fun showEditNameDialog() {
        val editText = EditText(this)
        editText.setText(tvName.text.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                updateName(editText.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


    private fun updateName(name: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .update("name", name)
    }



    override fun onDestroy() {
        super.onDestroy()
        profileListener?.remove()
    }
    // Edit Profile
    findViewById<LinearLayout>(R.id.llEditProfile).setOnClickListener {
        val intent = Intent(this, EditProfileActivity::class.java)
        startActivity(intent)
    }

// App Preferences
    findViewById<LinearLayout>(R.id.llAppPreferences).setOnClickListener {
        val intent = Intent(this, AppPreferencesActivity::class.java)
        startActivity(intent)
    }

// About
    findViewById<LinearLayout>(R.id.llAbout).setOnClickListener {
        val intent = Intent(this, AboutActivity::class.java)
        startActivity(intent)
    }

// Feedback / Contact
    findViewById<LinearLayout>(R.id.llFeedback).setOnClickListener {
        val intent = Intent(this, FeedbackActivity::class.java)
        startActivity(intent)
    }
}
*/

 */
package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: ImageView
    private lateinit var btnLogout: Button

    private var imageUri: Uri? = null
    private var profileListener: ListenerRegistration? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        tvName = findViewById(R.id.tvProfileName)
        tvEmail = findViewById(R.id.tvProfileEmail)
        imgAvatar = findViewById(R.id.imgAvatar)
        btnLogout = findViewById(R.id.btnLogout)

        // Observe profile updates
        observeUserProfile()

        // Avatar click
        imgAvatar.setOnClickListener { pickImageFromGallery() }

        // Name click
        tvName.setOnClickListener { showEditNameDialog() }

        // Logout click
        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        // Settings options clicks - navigate to new Activities
        findViewById<LinearLayout>(R.id.llEditProfile).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.llAppPreferences).setOnClickListener {
            startActivity(Intent(this, AppPreferencesActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.llAbout).setOnClickListener {
            startActivity(Intent(this, AboutActivity::class.java))
        }

        findViewById<LinearLayout>(R.id.llFeedback).setOnClickListener {
            startActivity(Intent(this, FeedbackActivity::class.java))
        }
    }

    private fun observeUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        profileListener = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener

                tvName.text = snapshot.getString("name") ?: "User"
                tvEmail.text = snapshot.getString("email") ?: ""

                val avatarUrl = snapshot.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(imgAvatar)
                } else {
                    imgAvatar.setImageResource(R.drawable.ic_default_avatar)
                }
            }
    }

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
            uploadAvatar()
        }
    }

    private fun uploadAvatar() {
        val uid = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("avatars/$uid.jpg")

        imageUri?.let {
            ref.putFile(it)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    firestore.collection("users")
                        .document(uid)
                        .update("avatarUrl", uri.toString())
                }
        }
    }

    private fun showEditNameDialog() {
        val editText = EditText(this)
        editText.setText(tvName.text.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                updateName(editText.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateName(name: String) {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users")
            .document(uid)
            .update("name", name)
    }

    override fun onDestroy() {
        super.onDestroy()
        profileListener?.remove()
    }
}

/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var tvName: TextView
    private lateinit var tvEmail: TextView
    private lateinit var imgAvatar: ImageView
    private lateinit var btnLogout: Button

    private var imageUri: Uri? = null
    private var profileListener: ListenerRegistration? = null

    companion object {
        private const val IMAGE_PICK_CODE = 101
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        tvName = findViewById(R.id.tvProfileName)
        tvEmail = findViewById(R.id.tvProfileEmail)
        imgAvatar = findViewById(R.id.imgAvatar)
        btnLogout = findViewById(R.id.btnLogout)

        ensureUserDocument()
        observeUserProfile()

        imgAvatar.setOnClickListener { pickImageFromGallery() }
        tvName.setOnClickListener { showEditNameDialog() }

        btnLogout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        findViewById<LinearLayout>(R.id.llEditProfile)
            .setOnClickListener { startActivity(Intent(this, EditProfileActivity::class.java)) }

        findViewById<LinearLayout>(R.id.llAppPreferences)
            .setOnClickListener { startActivity(Intent(this, AppPreferencesActivity::class.java)) }

        findViewById<LinearLayout>(R.id.llAbout)
            .setOnClickListener { startActivity(Intent(this, AboutActivity::class.java)) }

        findViewById<LinearLayout>(R.id.llFeedback)
            .setOnClickListener { startActivity(Intent(this, FeedbackActivity::class.java)) }
    }

    // ✅ Create user document if missing
    private fun ensureUserDocument() {
        val user = auth.currentUser ?: return
        val data = hashMapOf(
            "email" to user.email,
            "name" to "User"
        )

        firestore.collection("users")
            .document(user.uid)
            .set(data, SetOptions.merge())
    }

    // ✅ Real-time profile listener
    private fun observeUserProfile() {
        val uid = auth.currentUser?.uid ?: return

        profileListener = firestore.collection("users")
            .document(uid)
            .addSnapshotListener { snapshot, _ ->
                if (snapshot == null || !snapshot.exists()) return@addSnapshotListener

                tvName.text = snapshot.getString("name") ?: "User"
                tvEmail.text = snapshot.getString("email") ?: ""

                val avatarUrl = snapshot.getString("avatarUrl")
                if (!avatarUrl.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(avatarUrl)
                        .placeholder(R.drawable.ic_default_avatar)
                        .into(imgAvatar)
                } else {
                    imgAvatar.setImageResource(R.drawable.ic_default_avatar)
                }
            }
    }

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
            uploadAvatar()
        }
    }

    // ✅ PERMANENT avatar save
    private fun uploadAvatar() {
        val uid = auth.currentUser?.uid ?: return
        val ref = storage.reference.child("avatars/$uid.jpg")

        imageUri?.let {
            ref.putFile(it)
                .continueWithTask { task ->
                    if (!task.isSuccessful) throw task.exception!!
                    ref.downloadUrl
                }
                .addOnSuccessListener { uri ->
                    val data = hashMapOf("avatarUrl" to uri.toString())

                    firestore.collection("users")
                        .document(uid)
                        .set(data, SetOptions.merge())
                }
        }
    }

    private fun showEditNameDialog() {
        val editText = EditText(this)
        editText.setText(tvName.text.toString())

        AlertDialog.Builder(this)
            .setTitle("Edit Name")
            .setView(editText)
            .setPositiveButton("Save") { _, _ ->
                updateName(editText.text.toString())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun updateName(name: String) {
        val uid = auth.currentUser?.uid ?: return
        val data = hashMapOf("name" to name)

        firestore.collection("users")
            .document(uid)
            .set(data, SetOptions.merge())
    }

    override fun onDestroy() {
        super.onDestroy()
        profileListener?.remove()
    }
}*/*/

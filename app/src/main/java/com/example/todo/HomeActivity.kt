


/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Task(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "",
    val color: Int = Color.GRAY,
    var completed: Boolean = false,
    val id: String = ""
)
data class Category(
    val name: String,
    val color: Int
)
class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var tasks: MutableList<Task> = mutableListOf()
    private lateinit var categories: List<Category>
    private lateinit var taskContainer: LinearLayout
    private lateinit var categoryContainer: LinearLayout
    private lateinit var btnSettings: ImageView

    private lateinit var btnAddTask: FloatingActionButton
    private lateinit var etSearch: EditText

    // Modern ActivityResultLaunchers
    private val addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadTasks() // reload tasks after adding
        }
    }

    private val taskDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadTasks() // reload tasks after marking done
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        taskContainer = findViewById(R.id.taskContainer)
        categoryContainer = findViewById(R.id.categoryContainer)
        btnAddTask = findViewById(R.id.fabAddTask)
        etSearch = findViewById(R.id.etSearch)

        // Define categories
        categories = listOf(
            Category("💼", Color.parseColor("#FF6B6B")),   // Work
            Category("🏠", Color.parseColor("#4ECDC4")),   // Personal
            Category("🛒", Color.parseColor("#FFD93D")),   // Shop
            Category("💊", Color.parseColor("#6BCB77"))    // Health
        )


        loadTasks()
        loadCategoryButtons()
        setupSearch()

        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putStringArrayListExtra("categories", ArrayList(categories.map { it.name }))
            addTaskLauncher.launch(intent)
        }
    }

    private fun loadTasks() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        db.collection("tasks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                tasks = snapshot.toObjects(Task::class.java).toMutableList()
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load tasks: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayTasks(list: List<Task>) {
        taskContainer.removeAllViews()
        val sortedList = list.sortedWith(compareBy({ it.completed }, { it.date }, { it.time }))

        sortedList.forEach { task ->
            val card = CardView(this).apply {
                radius = 28f
                setCardBackgroundColor(task.color)
                cardElevation = 12f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 20) }
                setContentPadding(30, 30, 30, 30)
            }

            val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

            val tvTitle = TextView(this).apply {
                text = task.title
                textSize = 20f
                setTextColor(Color.WHITE)
                if (task.completed) paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            layout.addView(tvTitle)

            layout.addView(TextView(this).apply {
                text = "${task.date} | ${task.time}"
                textSize = 16f
                setTextColor(Color.WHITE)
            })

            layout.addView(TextView(this).apply {
                text = "${task.category} - ${task.description}"
                textSize = 14f
                setTextColor(Color.WHITE)
            })

            val btnDone = Button(this).apply {
                text = if (task.completed) "Done" else "Mark as Done"
                setBackgroundColor(Color.parseColor("#6AB8F7"))
                setTextColor(Color.WHITE)
                setOnClickListener { if (!task.completed) markTaskDone(task) }
            }
            layout.addView(btnDone)

            card.addView(layout)

            // Open TaskDetailActivity
            card.setOnClickListener {
                val intent = Intent(this, TaskDetailActivity::class.java)
                intent.putExtra("taskId", task.id)
                intent.putExtra("taskTitle", task.title)
                intent.putExtra("taskDescription", task.description)
                intent.putExtra("taskDate", task.date)
                intent.putExtra("taskTime", task.time)
                intent.putExtra("taskCategory", task.category)
                taskDetailLauncher.launch(intent)
            }

            taskContainer.addView(card)
        }
    }

    private fun markTaskDone(task: Task) {
        db.collection("tasks").document(task.id)
            .update("completed", true)
            .addOnSuccessListener {
                task.completed = true
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update task: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }



    /*private fun loadCategoryButtons() {
        categoryContainer.removeAllViews()
        categories.forEach { category ->
            val btn = Button(this).apply {
                text = category.name
                setTextColor(Color.WHITE)
                background = GradientDrawable().apply {
                    setColor(category.color)
                    cornerRadius = 50f
                }
            }

            btn.setOnClickListener {
                val filtered = tasks.filter { it.category == category.name }
                displayTasks(filtered)
            }

            categoryContainer.addView(btn)
        }
    }*/
    private fun loadCategoryButtons() {
        categoryContainer.removeAllViews()
        categories.forEach { category ->

            val btn = Button(this).apply {
                text = category.name
                setTextColor(Color.WHITE)

                // Make the button bigger
                textSize = 18f
                setPadding(50, 25, 50, 25)

                background = GradientDrawable().apply {
                    setColor(category.color)
                    cornerRadius = 60f
                }

                // Add space between category buttons
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 30   // <-- Space between category buttons
                }
            }

            btn.setOnClickListener {
                val filtered = tasks.filter { it.category == category.name }
                displayTasks(filtered)
            }

            categoryContainer.addView(btn)
        }
    }







    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filtered = tasks.filter {
                    it.title.lowercase().contains(query) || it.category.lowercase().contains(query)
                }
                displayTasks(filtered)
            }
        })
    }
}*/


/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Task(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "",
    val color: Int = Color.GRAY,
    var completed: Boolean = false,
    val id: String = ""
)
data class Category(
    val name: String,
    val color: Int
)
class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var tasks: MutableList<Task> = mutableListOf()
    private lateinit var categories: List<Category>
    private lateinit var taskContainer: LinearLayout
    private lateinit var categoryContainer: LinearLayout
    private lateinit var btnAddTask: FloatingActionButton
    private lateinit var etSearch: EditText

    // Modern ActivityResultLaunchers
    private val addTaskLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadTasks() // reload tasks after adding
        }
    }

    private val taskDetailLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            loadTasks() // reload tasks after marking done
        }
    }
    // Add this as a function in your HomeActivity class (outside of setupNavBar)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        taskContainer = findViewById(R.id.taskContainer)
        categoryContainer = findViewById(R.id.categoryContainer)
        btnAddTask = findViewById(R.id.fabAddTask)
        etSearch = findViewById(R.id.etSearch)




        // Define categories
        categories = listOf(
            Category("💼", Color.parseColor("#FF6B6B")),   // Work
            Category("🏠", Color.parseColor("#4ECDC4")),   // Personal
            Category("🛒", Color.parseColor("#FFD93D")),   // Shop
            Category("💊", Color.parseColor("#6BCB77"))    // Health
        )


        loadTasks()
        loadCategoryButtons()
        setupSearch()

        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putStringArrayListExtra("categories", ArrayList(categories.map { it.name }))
            addTaskLauncher.launch(intent)
        }
    }

    private fun loadTasks() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        db.collection("tasks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                tasks = snapshot.toObjects(Task::class.java).toMutableList()
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load tasks: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun displayTasks(list: List<Task>) {
        taskContainer.removeAllViews()
        val sortedList = list.sortedWith(compareBy({ it.completed }, { it.date }, { it.time }))

        sortedList.forEach { task ->
            val card = CardView(this).apply {
                radius = 28f
                setCardBackgroundColor(task.color)
                cardElevation = 12f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 20) }
                setContentPadding(30, 30, 30, 30)
            }

            val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

            val tvTitle = TextView(this).apply {
                text = task.title
                textSize = 20f
                setTextColor(Color.WHITE)
                if (task.completed) paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            layout.addView(tvTitle)

            layout.addView(TextView(this).apply {
                text = "${task.date} | ${task.time}"
                textSize = 16f
                setTextColor(Color.WHITE)
            })

            layout.addView(TextView(this).apply {
                text = "${task.category} - ${task.description}"
                textSize = 14f
                setTextColor(Color.WHITE)
            })

            val btnDone = Button(this).apply {
                text = if (task.completed) "Done" else "Mark as Done"
                setBackgroundColor(Color.parseColor("#6AB8F7"))
                setTextColor(Color.WHITE)
                setOnClickListener { if (!task.completed) markTaskDone(task) }
            }
            layout.addView(btnDone)

            card.addView(layout)

            // Open TaskDetailActivity
            card.setOnClickListener {
                val intent = Intent(this, TaskDetailActivity::class.java)
                intent.putExtra("taskId", task.id)
                intent.putExtra("taskTitle", task.title)
                intent.putExtra("taskDescription", task.description)
                intent.putExtra("taskDate", task.date)
                intent.putExtra("taskTime", task.time)
                intent.putExtra("taskCategory", task.category)
                taskDetailLauncher.launch(intent)
            }

            taskContainer.addView(card)
        }
    }

    private fun markTaskDone(task: Task) {
        db.collection("tasks").document(task.id)
            .update("completed", true)
            .addOnSuccessListener {
                task.completed = true
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update task: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }



    /*private fun loadCategoryButtons() {
        categoryContainer.removeAllViews()
        categories.forEach { category ->
            val btn = Button(this).apply {
                text = category.name
                setTextColor(Color.WHITE)
                background = GradientDrawable().apply {
                    setColor(category.color)
                    cornerRadius = 50f
                }
            }

            btn.setOnClickListener {
                val filtered = tasks.filter { it.category == category.name }
                displayTasks(filtered)
            }

            categoryContainer.addView(btn)
        }
    }*/
    private fun loadCategoryButtons() {
        categoryContainer.removeAllViews()
        categories.forEach { category ->

            val btn = Button(this).apply {
                text = category.name
                setTextColor(Color.WHITE)

                // Make the button bigger
                textSize = 18f
                setPadding(50, 25, 50, 25)

                background = GradientDrawable().apply {
                    setColor(category.color)
                    cornerRadius = 60f
                }

                // Add space between category buttons
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    marginEnd = 30   // <-- Space between category buttons
                }
            }

            btn.setOnClickListener {
                val filtered = tasks.filter { it.category == category.name }
                displayTasks(filtered)
            }

            categoryContainer.addView(btn)
        }
    }







    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filtered = tasks.filter {
                    it.title.lowercase().contains(query) || it.category.lowercase().contains(query)
                }
                displayTasks(filtered)
            }
        })
    }
}*/


/*package com.example.todo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Task(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "",
    val color: Int = Color.GRAY,
    var completed: Boolean = false,
    val id: String = ""
)

data class Category(
    val name: String,
    val color: Int
)

class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var tasks: MutableList<Task> = mutableListOf()
    private lateinit var categories: List<Category>
    private lateinit var taskContainer: LinearLayout
    private lateinit var categoryContainer: LinearLayout
    private lateinit var btnAddTask: FloatingActionButton
    private lateinit var etSearch: EditText

    // Navbar items
    private lateinit var navHome: ImageView
    private lateinit var navTasks: ImageView
    private lateinit var navProfile: ImageView

    private val addTaskLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) loadTasks()
        }

    private val taskDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) loadTasks()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Views
        taskContainer = findViewById(R.id.taskContainer)
        categoryContainer = findViewById(R.id.categoryContainer)
        btnAddTask = findViewById(R.id.fabAddTask)
        etSearch = findViewById(R.id.etSearch)

        navHome = findViewById(R.id.nav_home)
        navTasks = findViewById(R.id.nav_tasks)
        navProfile = findViewById(R.id.nav_profile)

        // Categories
        categories = listOf(
            Category("💼", Color.parseColor("#FF6B6B")),
            Category("🏠", Color.parseColor("#4ECDC4")),
            Category("🛒", Color.parseColor("#FFD93D")),
            Category("💊", Color.parseColor("#6BCB77"))
        )

        loadTasks()
        loadCategoryButtons()
        setupSearch()
        setupNavBar()

        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putStringArrayListExtra("categories", ArrayList(categories.map { it.name }))
            addTaskLauncher.launch(intent)
        }

        findViewById<ImageView>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        //findViewById<Button>(R.id.btnLogout).setOnClickListener {
           // firebaseAuth.signOut()
            //finish()
        //}
    }

    private fun setupNavBar() {
        fun setActiveNav(selected: ImageView) {
            val defaultColor = Color.GRAY
            val activeColor = Color.BLACK
            navHome.setColorFilter(defaultColor)
            navTasks.setColorFilter(defaultColor)
            navProfile.setColorFilter(defaultColor)
            selected.setColorFilter(activeColor)
        }

        navHome.setOnClickListener {
            loadTasks()
            setActiveNav(navHome)
        }

        navTasks.setOnClickListener {
            Toast.makeText(this, "Tasks clicked", Toast.LENGTH_SHORT).show()
            setActiveNav(navTasks)
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            setActiveNav(navProfile)
        }

        // Default active
        setActiveNav(navHome)
    }

    private fun loadTasks() {
        val userId = firebaseAuth.currentUser?.uid ?: return
        db.collection("tasks")
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { snapshot ->
                tasks = snapshot.toObjects(Task::class.java).toMutableList()
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load tasks: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun displayTasks(list: List<Task>) {
        taskContainer.removeAllViews()
        val sortedList = list.sortedWith(compareBy({ it.completed }, { it.date }, { it.time }))
        sortedList.forEach { task ->
            val card = CardView(this).apply {
                radius = 28f
                setCardBackgroundColor(task.color)
                cardElevation = 12f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 20) }
                setContentPadding(30, 30, 30, 30)
            }

            val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

            val tvTitle = TextView(this).apply {
                text = task.title
                textSize = 20f
                setTextColor(Color.WHITE)
                if (task.completed) paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            layout.addView(tvTitle)

            layout.addView(TextView(this).apply {
                text = "${task.date} | ${task.time}"
                textSize = 16f
                setTextColor(Color.WHITE)
            })

            layout.addView(TextView(this).apply {
                text = "${task.category} - ${task.description}"
                textSize = 14f
                setTextColor(Color.WHITE)
            })

            val btnDone = Button(this).apply {
                text = if (task.completed) "Done" else "Mark as Done"
                setBackgroundColor(Color.parseColor("#6AB8F7"))
                setTextColor(Color.WHITE)
                setOnClickListener { if (!task.completed) markTaskDone(task) }
            }
            layout.addView(btnDone)

            card.addView(layout)

            card.setOnClickListener {
                val intent = Intent(this, TaskDetailActivity::class.java)
                intent.putExtra("taskId", task.id)
                intent.putExtra("taskTitle", task.title)
                intent.putExtra("taskDescription", task.description)
                intent.putExtra("taskDate", task.date)
                intent.putExtra("taskTime", task.time)
                intent.putExtra("taskCategory", task.category)
                taskDetailLauncher.launch(intent)
            }

            taskContainer.addView(card)
        }
    }

    private fun markTaskDone(task: Task) {
        db.collection("tasks").document(task.id)
            .update("completed", true)
            .addOnSuccessListener {
                task.completed = true
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update task: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun loadCategoryButtons() {
        categoryContainer.removeAllViews()
        categories.forEach { category ->
            val btn = Button(this).apply {
                text = category.name
                setTextColor(Color.WHITE)
                textSize = 18f
                setPadding(50, 25, 50, 25)
                background = GradientDrawable().apply {
                    setColor(category.color)
                    cornerRadius = 60f
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = 30 }
            }
            btn.setOnClickListener {
                val filtered = tasks.filter { it.category == category.name }
                displayTasks(filtered)
            }
            categoryContainer.addView(btn)
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filtered = tasks.filter {
                    it.title.lowercase().contains(query) || it.category.lowercase().contains(query)
                }
                displayTasks(filtered)
            }
        })
    }
}*/
package com.example.todo

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

data class Task(
    val userId: String = "",
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val category: String = "",
    val color: Int = Color.GRAY,
    var completed: Boolean = false,
    val id: String = ""
)

data class Category(
    val name: String,
    val color: Int
)

class HomeActivity : AppCompatActivity() {

    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var tasks: MutableList<Task> = mutableListOf()
    private lateinit var categories: List<Category>
    private lateinit var taskContainer: LinearLayout
    private lateinit var categoryContainer: LinearLayout
    private lateinit var btnAddTask: FloatingActionButton
    private lateinit var etSearch: EditText

    // Navbar items
    private lateinit var navHome: ImageView
    private lateinit var navTasks: ImageView
    private lateinit var navProfile: ImageView

    private val addTaskLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) loadTasks()
        }

    private val taskDetailLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) loadTasks()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Firebase
        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Views
        taskContainer = findViewById(R.id.taskContainer)
        categoryContainer = findViewById(R.id.categoryContainer)
        btnAddTask = findViewById(R.id.fabAddTask)
        etSearch = findViewById(R.id.etSearch)

        navHome = findViewById(R.id.nav_home)
        navTasks = findViewById(R.id.nav_tasks)
        navProfile = findViewById(R.id.nav_profile)

        // Categories
        categories = listOf(
            Category("💼", Color.parseColor("#FF6B6B")),
            Category("🏠", Color.parseColor("#4ECDC4")),
            Category("🛒", Color.parseColor("#FFD93D")),
            Category("💊", Color.parseColor("#6BCB77"))
        )

        loadTasks()
        loadCategoryButtons()
        setupSearch()
        setupNavBar()

        btnAddTask.setOnClickListener {
            val intent = Intent(this, AddTaskActivity::class.java)
            intent.putStringArrayListExtra("categories", ArrayList(categories.map { it.name }))
            addTaskLauncher.launch(intent)
        }

        findViewById<ImageView>(R.id.btnSettings).setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
    }

    private fun setupNavBar() {
        fun setActiveNav(selected: ImageView) {
            val defaultColor = Color.GRAY
            val activeColor = Color.BLACK
            navHome.setColorFilter(defaultColor)
            navTasks.setColorFilter(defaultColor)
            navProfile.setColorFilter(defaultColor)
            selected.setColorFilter(activeColor)
        }

        navHome.setOnClickListener {
            loadTasks()
            setActiveNav(navHome)
        }

        navTasks.setOnClickListener {
            Toast.makeText(this, "Tasks clicked", Toast.LENGTH_SHORT).show()
            setActiveNav(navTasks)
        }

        navProfile.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
            setActiveNav(navProfile)
        }

        setActiveNav(navHome)
    }

    private fun loadTasks() {
        val userId = firebaseAuth.currentUser?.uid ?: return

        // 🔹 Correct Firestore path based on rules
        db.collection("users")
            .document(userId)
            .collection("tasks")
            .get()
            .addOnSuccessListener { snapshot ->
                tasks = snapshot.toObjects(Task::class.java).toMutableList()
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load tasks: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun displayTasks(list: List<Task>) {
        taskContainer.removeAllViews()
        val sortedList = list.sortedWith(compareBy({ it.completed }, { it.date }, { it.time }))
        sortedList.forEach { task ->
            val card = CardView(this).apply {
                radius = 28f
                setCardBackgroundColor(task.color)
                cardElevation = 12f
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { setMargins(0, 0, 0, 20) }
                setContentPadding(30, 30, 30, 30)
            }

            val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }

            val tvTitle = TextView(this).apply {
                text = task.title
                textSize = 20f
                setTextColor(Color.WHITE)
                if (task.completed) paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            }
            layout.addView(tvTitle)

            layout.addView(TextView(this).apply {
                text = "${task.date} | ${task.time}"
                textSize = 16f
                setTextColor(Color.WHITE)
            })

            layout.addView(TextView(this).apply {
                text = "${task.category} - ${task.description}"
                textSize = 14f
                setTextColor(Color.WHITE)
            })

            val btnDone = Button(this).apply {
                text = if (task.completed) "Done" else "Mark as Done"
                setBackgroundColor(Color.parseColor("#6AB8F7"))
                setTextColor(Color.WHITE)
                setOnClickListener { if (!task.completed) markTaskDone(task) }
            }
            layout.addView(btnDone)

            card.addView(layout)

            card.setOnClickListener {
                val intent = Intent(this, TaskDetailActivity::class.java)
                intent.putExtra("taskId", task.id)
                intent.putExtra("taskTitle", task.title)
                intent.putExtra("taskDescription", task.description)
                intent.putExtra("taskDate", task.date)
                intent.putExtra("taskTime", task.time)
                intent.putExtra("taskCategory", task.category)
                taskDetailLauncher.launch(intent)
            }

            taskContainer.addView(card)
        }
    }

    private fun markTaskDone(task: Task) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        db.collection("users")
            .document(userId)
            .collection("tasks")
            .document(task.id)
            .update("completed", true)
            .addOnSuccessListener {
                task.completed = true
                displayTasks(tasks)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to update task: ${it.message}", Toast.LENGTH_SHORT)
                    .show()
            }
    }

    private fun loadCategoryButtons() {
        categoryContainer.removeAllViews()
        categories.forEach { category ->
            val btn = Button(this).apply {
                text = category.name
                setTextColor(Color.WHITE)
                textSize = 18f
                setPadding(50, 25, 50, 25)
                background = GradientDrawable().apply {
                    setColor(category.color)
                    cornerRadius = 60f
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginEnd = 30 }
            }
            btn.setOnClickListener {
                val filtered = tasks.filter { it.category == category.name }
                displayTasks(filtered)
            }
            categoryContainer.addView(btn)
        }
    }

    private fun setupSearch() {
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim().lowercase()
                val filtered = tasks.filter {
                    it.title.lowercase().contains(query) || it.category.lowercase().contains(query)
                }
                displayTasks(filtered)
            }
        })
    }
}

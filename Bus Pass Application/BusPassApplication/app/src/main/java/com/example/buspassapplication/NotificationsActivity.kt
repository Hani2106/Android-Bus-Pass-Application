package com.example.buspassapplication

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotificationsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var notificationsContainer: LinearLayout
    private lateinit var noNotificationsTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        notificationsContainer = findViewById(R.id.notifications_container)
        noNotificationsTextView = findViewById(R.id.no_notifications_text)

        fetchNotifications()
    }

    private fun fetchNotifications() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            noNotificationsTextView.visibility = View.VISIBLE
            noNotificationsTextView.text = "You must be logged in to see notifications."
            return
        }

        // We will store notifications in a subcollection under the user's document
        db.collection("users").document(currentUser.uid).collection("notifications")
            .orderBy("timestamp", Query.Direction.DESCENDING) // Show newest first
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    noNotificationsTextView.visibility = View.VISIBLE
                } else {
                    noNotificationsTextView.visibility = View.GONE
                    // Clear any old views before adding new ones
                    notificationsContainer.removeAllViews()
                    for (document in documents) {
                        val message = document.getString("message")
                        if (message != null) {
                            addNotificationView(message)
                        }
                    }
                }
            }
            .addOnFailureListener { e ->
                noNotificationsTextView.visibility = View.VISIBLE
                noNotificationsTextView.text = "Failed to load notifications."
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    /**
     * Dynamically creates a TextView for a notification message and adds it to the container.
     */
    private fun addNotificationView(message: String) {
        val notificationTextView = TextView(this).apply {
            text = message
            textSize = 16f
            setPadding(8, 16, 8, 16)
        }
        notificationsContainer.addView(notificationTextView)
    }
}

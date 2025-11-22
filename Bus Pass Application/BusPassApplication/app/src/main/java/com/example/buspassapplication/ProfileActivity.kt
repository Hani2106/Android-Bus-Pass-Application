package com.example.buspassapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Declare UI elements
    private lateinit var nameEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var contactNumberEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var updateProfileButton: Button
    private lateinit var deleteAccountButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize UI elements from the layout
        nameEditText = findViewById(R.id.profile_name_edit_text)
        ageEditText = findViewById(R.id.profile_age_edit_text)
        contactNumberEditText = findViewById(R.id.profile_contact_number_edit_text)
        addressEditText = findViewById(R.id.profile_address_edit_text)
        updateProfileButton = findViewById(R.id.update_profile_button)
        deleteAccountButton = findViewById(R.id.delete_account_button)

        loadUserProfile()

        updateProfileButton.setOnClickListener {
            updateUserProfile()
        }

        deleteAccountButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    /**
     * Fetches the user's current data from Firestore and populates the EditText fields.
     */
    private fun loadUserProfile() {
        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show()
            return
        }

        db.collection("pass_applications").document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    // Populate the fields with existing data
                    nameEditText.setText(document.getString("name"))
                    ageEditText.setText(document.getString("age"))
                    contactNumberEditText.setText(document.getString("contactNumber"))
                    addressEditText.setText(document.getString("address"))
                } else {
                    Toast.makeText(this, "No profile data found. Please register first.", Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to load profile: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Updates the user's data in Firestore with the new values from the EditText fields.
     */
    private fun updateUserProfile() {
        val currentUser = auth.currentUser ?: return

        val updatedData = mapOf(
            "name" to nameEditText.text.toString(),
            "age" to ageEditText.text.toString(),
            "contactNumber" to contactNumberEditText.text.toString(),
            "address" to addressEditText.text.toString()
        )

        db.collection("pass_applications").document(currentUser.uid)
            .update(updatedData)
            .addOnSuccessListener {
                Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                finish() // Close the profile activity and go back
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Update failed: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    /**
     * Shows a confirmation dialog before deleting the user account.
     */
    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Are you sure you want to permanently delete your account? This action cannot be undone.")
            .setPositiveButton("Delete") { _, _ ->
                deleteUserAccount()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    /**
     * Deletes all user data from Firestore and then deletes the user from Firebase Authentication.
     */
    private fun deleteUserAccount() {
        val user = auth.currentUser ?: return

        // Step 1: Delete Firestore documents
        val passAppDoc = db.collection("pass_applications").document(user.uid)
        val userNotificationsDoc = db.collection("users").document(user.uid)

        passAppDoc.delete()
            .addOnSuccessListener {
                // After deleting pass application, delete notifications
                userNotificationsDoc.collection("notifications").get().addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        document.reference.delete()
                    }
                    userNotificationsDoc.delete() // Delete the user document itself
                }

                // Step 2: Delete the user from Firebase Authentication
                user.delete()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Account deleted successfully.", Toast.LENGTH_LONG).show()
                            // Navigate back to the Login screen
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        } else {
                            Toast.makeText(this, "Failed to delete account: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                        }
                    }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to delete profile data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}

package com.example.buspassapplication

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize all the UI elements from the layout
        val nameEditText: EditText = findViewById(R.id.name_edit_text)
        val ageEditText: EditText = findViewById(R.id.age_edit_text)
        val dobEditText: EditText = findViewById(R.id.dob_edit_text)
        val genderRadioGroup: RadioGroup = findViewById(R.id.gender_radio_group)
        val bloodGroupEditText: EditText = findViewById(R.id.blood_group_edit_text)
        val contactNumberEditText: EditText = findViewById(R.id.contact_number_edit_text)
        val emergencyContactEditText: EditText = findViewById(R.id.emergency_contact_edit_text)
        val addressEditText: EditText = findViewById(R.id.address_edit_text)
        val validitySpinner: Spinner = findViewById(R.id.validity_spinner)
        val submitButton: Button = findViewById(R.id.submit_button)

        // Setup the spinner with options from arrays.xml
        ArrayAdapter.createFromResource(
            this,
            R.array.validity_options,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            validitySpinner.adapter = adapter
        }

        submitButton.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                // This should not happen if the user is logged in, but it's a good safety check
                Toast.makeText(this, "You must be logged in to register.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Get selected gender from the RadioGroup
            val selectedGenderId = genderRadioGroup.checkedRadioButtonId
            if (selectedGenderId == -1) {
                Toast.makeText(this, "Please select a gender.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedRadioButton: RadioButton = findViewById(selectedGenderId)
            val gender = selectedRadioButton.text.toString()

            // Get all the data from the form
            val name = nameEditText.text.toString()
            val age = ageEditText.text.toString()
            val dob = dobEditText.text.toString()
            val bloodGroup = bloodGroupEditText.text.toString()
            val contactNumber = contactNumberEditText.text.toString()
            val emergencyContact = emergencyContactEditText.text.toString()
            val address = addressEditText.text.toString()
            val validity = validitySpinner.selectedItem.toString()

            // Simple validation
            if (name.isEmpty() || age.isEmpty() || dob.isEmpty() || contactNumber.isEmpty() || emergencyContact.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all the fields.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Create a data map to upload to Firestore, including all new fields
            val passApplication = hashMapOf(
                "userId" to currentUser.uid,
                "name" to name,
                "age" to age,
                "dateOfBirth" to dob,
                "gender" to gender,
                "bloodGroup" to bloodGroup,
                "contactNumber" to contactNumber,
                "emergencyContact" to emergencyContact,
                "address" to address,
                "passValidity" to validity,
                "status" to "submitted", // Initial status is always "submitted"
                "passId" to "pending",
                "timestamp" to System.currentTimeMillis(),
                "notification_sent" to false // Always initialize the flag to false
            )

            // Save the data to Firestore.
            db.collection("pass_applications").document(currentUser.uid)
                .set(passApplication)
                .addOnSuccessListener {
                    Toast.makeText(this, "Application submitted successfully!", Toast.LENGTH_LONG).show()
                    finish() // Close the registration activity and go back to the main screen
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}


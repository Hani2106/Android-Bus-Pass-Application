package com.example.buspassapplication

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeEncoder

class StatusActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    // Declare all UI elements
    private lateinit var statusTextView: TextView
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var pendingLayout: LinearLayout
    private lateinit var approvedLayout: ScrollView

    // E-Pass UI elements
    private lateinit var passUserPhotoImageView: ImageView
    private lateinit var passNameTextView: TextView
    private lateinit var passIdTextView: TextView
    private lateinit var passValidityTextView: TextView
    private lateinit var passAgeTextView: TextView
    private lateinit var passDobTextView: TextView
    private lateinit var passGenderTextView: TextView
    private lateinit var passBloodGroupTextView: TextView
    private lateinit var passContactTextView: TextView
    private lateinit var passEmergencyContactTextView: TextView
    private lateinit var passAddressTextView: TextView
    private lateinit var passQrCodeImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        // Initialize UI elements
        statusTextView = findViewById(R.id.status_text_view)
        loadingIndicator = findViewById(R.id.loading_indicator)
        pendingLayout = findViewById(R.id.pending_layout)
        approvedLayout = findViewById(R.id.approved_layout)

        // Initialize E-Pass UI elements, including all new fields
        passUserPhotoImageView = findViewById(R.id.pass_user_photo)
        passNameTextView = findViewById(R.id.pass_name)
        passIdTextView = findViewById(R.id.pass_id)
        passValidityTextView = findViewById(R.id.pass_validity)
        passAgeTextView = findViewById(R.id.pass_age)
        passDobTextView = findViewById(R.id.pass_dob)
        passGenderTextView = findViewById(R.id.pass_gender)
        passBloodGroupTextView = findViewById(R.id.pass_blood_group)
        passContactTextView = findViewById(R.id.pass_contact)
        passEmergencyContactTextView = findViewById(R.id.pass_emergency_contact)
        passAddressTextView = findViewById(R.id.pass_address)
        passQrCodeImageView = findViewById(R.id.pass_qr_code)

        fetchApplicationStatus()
    }

    private fun fetchApplicationStatus() {
        loadingIndicator.visibility = View.VISIBLE
        pendingLayout.visibility = View.GONE
        approvedLayout.visibility = View.GONE

        val currentUser = auth.currentUser
        if (currentUser == null) {
            Toast.makeText(this, "You need to be logged in.", Toast.LENGTH_SHORT).show()
            loadingIndicator.visibility = View.GONE
            pendingLayout.visibility = View.VISIBLE
            statusTextView.text = "Error: Not logged in."
            return
        }

        // Fetch the application document using the current user's ID
        db.collection("pass_applications").document(currentUser.uid)
            .get()
            .addOnSuccessListener { document ->
                loadingIndicator.visibility = View.GONE
                if (document != null && document.exists()) {
                    val status = document.getString("status") ?: "Unknown"

                    if (status == "approved") {
                        approvedLayout.visibility = View.VISIBLE
                        pendingLayout.visibility = View.GONE
                        displayEpass(document.data)
                    } else {
                        approvedLayout.visibility = View.GONE
                        pendingLayout.visibility = View.VISIBLE
                        statusTextView.text = "Your application status is: ${status.replaceFirstChar { it.uppercase() }}"
                    }
                } else {
                    approvedLayout.visibility = View.GONE
                    pendingLayout.visibility = View.VISIBLE
                    statusTextView.text = "No application found. Please submit one first."
                }
            }
            .addOnFailureListener { e ->
                loadingIndicator.visibility = View.GONE
                pendingLayout.visibility = View.VISIBLE
                statusTextView.text = "Error fetching status."
                Toast.makeText(this, "Failed to load data: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun displayEpass(data: Map<String, Any>?) {
        if (data == null) return

        // To use the user photo, you'll need to add photo upload to the registration form
        // and save the URL to Firestore, just like we did in the very first version.
        // val photoUrl = data["photoUrl"].toString()
        // Glide.with(this).load(photoUrl).into(passUserPhotoImageView)

        // Set text for all fields, including the new ones
        passNameTextView.text = "${data["name"]}"
        passIdTextView.text = "Pass ID: ${data["passId"]}"
        passValidityTextView.text = "Validity: ${data["passValidity"]}"
        passAgeTextView.text = "Age: ${data["age"]}"
        passDobTextView.text = "DOB: ${data["dateOfBirth"]}"
        passGenderTextView.text = "Gender: ${data["gender"]}"
        passBloodGroupTextView.text = "Blood Group: ${data["bloodGroup"]}"
        passContactTextView.text = "Contact: ${data["contactNumber"]}"
        passEmergencyContactTextView.text = "Emergency: ${data["emergencyContact"]}"
        passAddressTextView.text = "Address: ${data["address"]}"

        // Generate and display QR code from the passId
        val passId = data["passId"].toString()
        if (passId.isNotEmpty() && passId != "pending") {
            try {
                val barcodeEncoder = BarcodeEncoder()
                val bitmap: Bitmap = barcodeEncoder.encodeBitmap(passId, BarcodeFormat.QR_CODE, 400, 400)
                passQrCodeImageView.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


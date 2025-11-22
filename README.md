<img width="975" height="518" alt="image" src="https://github.com/user-attachments/assets/29454614-080f-4581-9578-b0ef10b85d2f" /># 🚌 Bus Pass Application – Android (Kotlin + Firebase)

The **Bus Pass Application** is an Android-based mobile solution designed to digitize and simplify the traditional bus-pass issuing process. Instead of standing in queues and completing paperwork manually, users can easily **register, apply for a bus pass, track status, view bus routes, receive notifications, and access their digital pass with QR code** — all from their smartphone.

---

## 🚀 Key Features

| Feature | Description |
|--------|-------------|
| 🔐 Login & Authentication | Firebase secure login and signup |
| 📝 Online Bus Pass Registration | Submit user details and request a new pass |
| ⏳ Real-Time Status Tracking | View application status anytime |
| 🔔 Push Notifications | Receive alerts when pass is approved/rejected |
| 🗺 View Bus Routes | Google Maps integration with multiple routes |
| 🧾 Digital E-Pass | QR-coded digital pass with user information |
| 👤 Profile Management | Edit profile or permanently delete account |

---

## 🧰 Tech Stack

| Component | Technology |
|----------|------------|
| Programming Language | **Kotlin** |
| XML UI | **Android Layout XML** |
| Database | **Firebase Firestore** |
| Authentication | **Firebase Auth** |
| Push Notifications | **Notification Channel (Android)** |
| Maps | **Google Maps SDK** |
| QR Generator | **ZXing Barcode / QR Library** |
| IDE | **Android Studio Ladybug 2024.2.1 Patch 1** |
| Min SDK | **24** |

---

## 📱 Application Architecture

The project follows a **modular activity-based architecture**:

| Activity | Responsibility |
|---------|----------------|
| LoginActivity | Authenticates users via Firebase |
| MainActivity | Central dashboard to navigate app features |
| RegistrationActivity | Collects user info and uploads to Firestore |
| StatusActivity | Displays live status & digital pass with QR |
| NotificationsActivity | Lists past notifications stored in Firestore |
| MapActivity | Displays multiple bus routes using Google Maps |
| ProfileActivity | Edit profile / delete account |

Lifecycle methods like onCreate(), onStart(), onResume(), onPause() etc. are used to manage resources efficiently.  
📄 For detailed implementation, refer to MAD_Report_cipat.docx


## 🔧 Installation & Setup

### 🟡 Step 1 – Clone this repository

git clone https://github.com/<your-username>/<repository-name>.git


### 🟡 Step 2 – Open in Android Studio  
File → Open → Select the project folder

### 🟡 Step 3 – Configure Firebase
1. Create Firebase project → Add Android App
2. Add **google-services.json** to /app folder
3. Enable:
   - Firebase Authentication (Email/Password)
   - Firestore Database
4. Add required dependencies (already included)

### 🟡 Step 4 – Enable Google Maps SDK
1. Go to Google Cloud Console
2. Create Maps API key → Add inside AndroidManifest.xml

<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY"/>

---

## ▶ Run APK Directly

An **APK build** is included for quick installation:

📦 app-release.apk

(Download → Install on Android → Allow unknown sources)

---

## 📸 Screenshots

Figure - App interface
<img width="295" height="587" alt="image" src="https://github.com/user-attachments/assets/86e873f3-3d87-49dd-bad3-f2b9b6c3fd20" />
<img width="295" height="582" alt="image" src="https://github.com/user-attachments/assets/1dcdb09b-bcaa-4ab9-9ba1-7ed5be53dee1" />

Figure – Login screen
<img width="278" height="523" alt="image" src="https://github.com/user-attachments/assets/64a0c7a4-c6a8-48ae-8d49-92b24742bdda" />

Figure – Home screen
<img width="266" height="515" alt="image" src="https://github.com/user-attachments/assets/745edad0-3f46-4206-82d7-2c22d4dd4279" />

Figure – Registration form
<img width="266" height="561" alt="image" src="https://github.com/user-attachments/assets/a7afc1e1-2180-4ceb-988f-fe415c5e5abe" />
<img width="266" height="561" alt="image" src="https://github.com/user-attachments/assets/0c939795-82f1-49e9-b125-5bd79d152672" />

Figure – Notification screen
<img width="273" height="578" alt="image" src="https://github.com/user-attachments/assets/6f23ac7f-0a50-46d0-8257-d261f4be3cd3" />
<img width="274" height="579" alt="image" src="https://github.com/user-attachments/assets/20424766-a5ce-43bc-b3e7-ba5ffc507cd5" />

Figure – E-Pass with QR
<img width="274" height="578" alt="image" src="https://github.com/user-attachments/assets/e7955fd3-be72-4d3e-8d22-0855c9e549be" />

Figure – Google Maps route viewer
<img width="274" height="579" alt="image" src="https://github.com/user-attachments/assets/3b6c646a-9fb8-4f88-9895-81452368b4c2" />

Figure – Profile edit / delete
<img width="274" height="579" alt="image" src="https://github.com/user-attachments/assets/fb9d392b-6289-41c0-b349-566cc99098f6" />

Figure - Firebase 
<img width="975" height="518" alt="image" src="https://github.com/user-attachments/assets/995edeaa-e5dc-4ecd-a57e-935ae8fb3804" />
<img width="975" height="518" alt="image" src="https://github.com/user-attachments/assets/2ea8412c-5a0d-439a-9a5c-c9554d02b5ae" />

---

## 🛠 Future Enhancements
🔹 Admin panel for pass verification  
🔹 Online payment gateway for pass renewal  
🔹 Bus tracking using live GPS  
🔹 Student ID verification using face recognition  
🔹 Offline QR verification for conductors  

---

## 👨‍💻 Author: Hani Patel❤️
⭐Linkedin : http://linkedin.com/in/hani-patel-6a9370265
🔗Email: hanipatel0621@gmail.com
---

## 📄 License
This project is for academic & learning purposes.  
Feel free to fork, modify & contribute ⭐

---

### 🌟 If you like this project, don’t forget to star the repository!

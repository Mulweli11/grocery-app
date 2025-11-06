# grocery-app
🛍️Roots Grocery App


📖 Overview

Roots Grocery App is a modern Android application that allows users to browse, search, and purchase grocery items online.
The app focuses on clean UI, smooth shopping experience, and fast performance — making it easier for users to get fresh groceries delivered right to their doorstep.

🎯 Purpose of the App

The main purpose of the app is to:

Help users find and order groceries quickly from local stores.

Provide a convenient and contactless shopping experience.

Allow users to add items to their cart, view exclusive offers, and explore top-selling products.

Serve as a foundation for future e-commerce systems, including payment integration and order tracking.

🧩 Core Features

✅ Modern & Responsive UI
✅ User authentication (Firebase-ready)
✅ Browse groceries by categories
✅ Add / Remove items from cart
✅ Track orders
✅ Exclusive offers and best-selling sections
✅ Persistent cart management with Kotlin object classes
✅ Bottom navigation for easy navigation

🎨 Design Considerations

The app’s design emphasizes simplicity, speed, and accessibility.

Design Element	Description
Color Palette	Green (#2E7D32) for trust & freshness, White for clarity
Typography	Poppins font family (Bold & Medium) for modern readability
Layout	Built using ConstraintLayout and RecyclerView for flexible, scalable UI
Icons	Vector assets from Material Icons
UX Flow	Smooth transitions between home, explore, cart, favourite, and account screens

📱 Home Screen	Cart Screen	Explore Screen

	
<img width="155" height="337" alt="image" src="https://github.com/user-attachments/assets/bef947c9-be39-43ba-ba9b-083f095085e1" />
<img width="155" height="332" alt="image" src="https://github.com/user-attachments/assets/f4136a1c-aa5e-49c6-a90d-ed91a95a8100" />
<img width="151" height="329" alt="image" src="https://github.com/user-attachments/assets/93f98cbf-a843-40dc-8778-1e6baf2ff9a3" />
<img width="148" height="332" alt="image" src="https://github.com/user-attachments/assets/6aa77124-f8e4-4828-92e2-a23dd873c93f" />
<img width="112" height="248" alt="image" src="https://github.com/user-attachments/assets/ac066445-0fb6-40ac-ba91-c437702e8d71" />
<img width="151" height="331" alt="image" src="https://github.com/user-attachments/assets/b0e9e9e5-f8c0-49b2-a3b7-0d34ebd98382" />
<img width="149" height="332" alt="image" src="https://github.com/user-attachments/assets/1de1ccd5-f57d-4d88-8553-6add956aefd3" />

	
🧠 App Architecture

The project follows MVVM (Model-View-ViewModel) and clean architecture principles:

com.example.groceryapp
│
├── data/               # CartManager, Repository
├── model/              # Data classes (Product, User)
├── ui/                 # Activities and Fragments
├── adapter/            # RecyclerView adapters
└── utils/              # Helper classes and constants

🔍 Key Components

RecyclerView for displaying product lists

Kotlin data classes for product management

Singleton CartManager for centralized cart handling

BottomNavigationView for intuitive navigation

🧰 Tech Stack
Category	Technology
Language	Kotlin
Architecture	MVVM
UI Framework	AndroidX, Material Design
Database (future)	Firebase Firestore / Room
Version Control	Git + GitHub
CI/CD	GitHub Actions
Design Tools	Figma / Android Studio Preview

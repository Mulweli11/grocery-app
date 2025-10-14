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

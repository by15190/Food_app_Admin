# [EAT-A-WAY](https://github.com/by15190/Food_app) Admin - Restaurant Management App

A powerful restaurant management application built with Kotlin and Firebase, enabling restaurant owners to manage their menu, orders, and business operations efficiently.

## Features

- Authentication & Authorization
  - Secure email/password login
  - Role-based access control
  - Password reset functionality
  - Session management
- Restaurant Profile Management
  - Basic information management
  - Operating hours
  - Delivery radius settings
  - Restaurant photos
  - Contact information
- Menu Management
  - Category creation and organization
  - Item creation and editing
  - Price management
  - Customization options
  - Special offers and discounts
  - Bulk menu updates
  - Item availability toggle
- Order Management
  - Real-time order notifications
  - Order status updates
  - Order history and analytics
  - Customer details view
  - Special instructions handling
  - Order acceptance/rejection
- Kitchen Management
  - Order queue management
  - Preparation time updates
  - Order prioritization
  - Kitchen notes
- Delivery Management
  - Delivery zone settings
  - Delivery fee configuration
  - Estimated delivery times
  - Delivery tracking
- Business Analytics
  - Daily/weekly/monthly sales reports
  - Popular items analysis
  - Revenue analytics
- Settings
  - Notification preferences
  - Auto-accept order settings
  - Printer configuration

## Tech Stack

### Android
- Kotlin
- Firebase SDK
- Material Design 3
- XML
  - Navigation
  - View Binding
  - Coroutines for async operations
 

### Backend Services (Firebase)
- Authentication
  - Email/Password
  - Admin role management
- Cloud Firestore
  - Menu management
  - Order processing
  - Restaurant profiles
- Cloud Storage
  - Menu item images




## Getting Started

### Prerequisites
- Android Studio Arctic Fox or later
- Kotlin 1.6+
- Android SDK 21+
- Firebase account with Blaze plan (for Cloud Functions)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/by15190/Food_app_Admin.git
cd tasty-bites-admin
```

2. Open the project in Android Studio

3. Configure Firebase:
   - Create a new Firebase project
   - Enable Authentication, Firestore, and Storage
   - Download `google-services.json`
   - Place it in the app/ directory
   - Set up Cloud Functions for notifications

4. Build and run the project


## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request



## Acknowledgments

- [Firebase](https://firebase.google.com) for backend services
- [Material Design](https://m3.material.io) for UI components

  # ⚠️ Important Notice: Firebase Cloud Storage Issue

This app was originally designed to store and retrieve images using Firebase Cloud Storage. However, due to Firebase's recent policy change, Cloud Storage now requires a paid **Blaze Plan**, which is not available on the free **Spark Plan**.

## 🚀 Workarounds & Next Steps

### 🔄 Alternative Storage Solutions
- The app can be modified to use **Cloudinary, Imgur, Supabase, or local storage** for images.
- Future updates may integrate a free cloud storage service.

### 🛠 How to Run the App Without Firebase Storage
- If you want to test the app, you can manually replace Firebase image URLs with **local assets** or a **free hosting service**.

### 🧐 Code Review
- The **image retrieval logic** is already implemented.
- If Firebase Cloud Storage is enabled (**Blaze Plan**), it will work as expected.


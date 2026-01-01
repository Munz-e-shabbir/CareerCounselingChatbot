# Career Counselling Chatbot

An AI-powered Android application that provides personalized career counselling and guidance to users using Google's Gemini AI.

## Features

- 💬 Interactive chat interface for career guidance
- 🎯 Personalized career recommendations powered by Gemini AI
- 📊 Career assessment and analysis
- 🤖 Real-time AI-powered responses
- 📱 User-friendly Android interface with Material Design
- 🔐 Firebase Authentication (Email & Google Sign-In)
- ☁️ Cloud storage with Firebase Firestore

## Technologies Used

- **Language**: Kotlin
- **Platform**: Android
- **IDE**: Android Studio
- **Minimum SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Compile SDK**: 34

### Key Libraries & Services

- **AI Engine**: Google Gemini AI (generativeai:0.1.2)
- **Backend**: Firebase (Authentication, Firestore)
- **Networking**: Retrofit 2.9.0 + OkHttp
- **UI Components**: Material Design 1.11.0, RecyclerView
- **Architecture**: MVVM with ViewModel & LiveData
- **Async Operations**: Kotlin Coroutines
- **Authentication**: Firebase Auth with Google Sign-In

## Prerequisites

Before running this project, you need:

1. Android Studio (latest version)
2. Google Gemini API Key
3. Firebase project setup

## Installation

1. Clone this repository:
```bash
   git clone https://github.com/Munz-e-shabbir/careercounsellingchatbot.git
```

2. Open the project in Android Studio

3. **Set up Firebase**:
    - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
    - Add your Android app to the Firebase project
    - Download `google-services.json` and place it in the `app/` directory
    - Enable Authentication (Email/Password & Google Sign-In)
    - Enable Firestore Database

4. **Add Gemini API Key**:
    - Get your API key from [Google AI Studio](https://makersuite.google.com/app/apikey)
    - Replace the API key in `app/build.gradle.kts`:
```kotlin
     buildConfigField("String", "GEMINI_API_KEY", "\"YOUR_API_KEY_HERE\"")
```

5. Sync Gradle files

6. Run the app on an emulator or physical device (Android 7.0+)

## How to Use

1. Launch the app
2. Sign up or log in (Email or Google)
3. Start chatting with the AI career counsellor
4. Ask questions about career paths, skills, education
5. Receive personalized AI-powered career recommendations
6. View chat history stored in Firestore

## Project Structure
```
app/
├── src/main/
│   ├── java/com/example/careercounselingchatbot/
│   │   ├── ui/           # Activities & Fragments
│   │   ├── viewmodel/    # ViewModels
│   │   ├── model/        # Data models
│   │   ├── repository/   # Data layer
│   │   └── utils/        # Utility classes
│   └── res/              # Resources (layouts, drawables, etc.)
└── build.gradle.kts      # App-level dependencies
```

## Screenshots

Coming soon!

## Features in Detail

### AI-Powered Conversations
- Uses Google's Gemini AI for intelligent responses
- Context-aware career guidance
- Natural language understanding

### User Authentication
- Secure Firebase Authentication
- Multiple sign-in options (Email & Google)
- Session management

### Data Persistence
- Chat history stored in Firestore
- Real-time data synchronization
- User profile management

## Future Enhancements

- 📚 Add career resources library
- 🎓 Integrate with educational platforms
- 💼 Connect with job portals
- 📝 Personality assessment tests
- 🌍 Multi-language support
- 📊 Career progress tracking
- 🔔 Push notifications for updates

## Known Issues

None at the moment

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## License

This project is open source and available under the [MIT License](LICENSE).

## Security Note

⚠️ **Important**: Never commit your API keys or `google-services.json` to public repositories. Add them to `.gitignore`.

## Contact
~~~~
- **Developer**: Muntaha
- **Email**: muntahashabbir85@gmail.com
- **GitHub**: [@Munz-e-shabbir](https://github.com/Munz-e-shabbir)

## Acknowledgments

- Google Gemini AI for powering the chatbot
- Firebase for backend services
- Material Design for UI components

---

Made with ❤️ for career guidance
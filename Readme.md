===========================================\\
Environment Setup for Running the Application:

- **IDE**: Android Studio Iguana | 2023.2.1
- **Android Device**:
  - **MinSDK**: 29
  - **TargetSDK**: 34
  - **Emulator Tested on API 34**: Release name UpsideDownCake
- **Access Account**:
  - Because the project uses Firebase Authentication and Google Sign-In for login functionality, users must use their Google account to sign in.

===========================================\\
Notes:
- **Firestore**: The application utilizes Firestore, a cloud storage solution on Firebase.
- **Keystore Specification**: For some devices, to run the application in debug mode and access Firebase, it's crucial to specify the device's keystore.
  - **Steps to Specify Keystore**:
    1. Open the project in Android Studio.
    2. Navigate to **File -> Project Structure -> Modules -> Signing Configs**.
    3. Under **Store File**, select the debug keystore file located in the "source" directory, then click **Apply**.

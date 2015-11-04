# README

## How to Build

1. Download and install Android Studio and the Android SDK from https://developer.android.com/sdk/index.html
2. Launch Android Studio.
    - Click *Configure* then *SDK Manager* then click the blue hyperlink labelled *Launch Standalone SDK Manager*
    - Ensure the following packages are installed with their respective latest revision:
        - Tools:
            - Android SDK Tools
            - Android SDK Platform-Tools
            - Android SDK Build-Tools
        - Android 6.0 (API 23):
            - SDK Platform
            - Google APIs
            - Google APIs Intel x86 Atom_64 System Image
        - Extras:
            - Android Support Repository
            - Android Support Library
            - Google Play Services
            - Google Repository
            - Intel x86 Emulation Accelerator (HAXM Installer) (Linux Users: Use kernel-based KVM instead)
3. Clone this repository.
4. Return to Android Studio's start window and click *Import Project*.
    - Within *androidcru*, select *HelloWorld* as the project directory.

## Running the application
1. If you don't have an Android test device, setup an Emulator by clicking *Tools --> Android --> AVD Manager*.
    - Click *New Virtual Device* then pick any device such as a *Nexus 4* or *Nexus 5*. 
    - Choose *Marshmallow, x86_64 (With Google APIs)*
    - Click *Advance Settings*
        - Change *RAM* to *1 GB*
    - Click *Finish*
2. To the left of the green "run" arrow on toolbar, ensure the runner is "app" then click the arrow. After the Gradle build completes, choose your emulator or test device to host the application.

## Running local UI tests

1. Open the project in Android Studio. 
2. Ensure the Android tree is open by clicking *Project* on the left-most window border and choose *Android* on the resultant drop-down. 
3. In the tree, highlight *app --> java -> com.androidcru.helloworld (androidTest)* then click *Run --> Run Tests in...* from the top-level menu or right-click to find the same option.
4. After the Gradle build completes, choose your emulator or test device to host the tests.
5. View results in the *Run* tab found on the bottom-most window border.

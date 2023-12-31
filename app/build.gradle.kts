plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.rtjhapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.rtjhapp"
        minSdk = 30
        targetSdk = 33
        ndk {
            abiFilters.add("armeabi-v7a")
            abiFilters.add("arm64-v8a")
        }
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        //noinspection DataBindingWithoutKapt
        dataBinding = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("org.greenrobot:eventbus:3.2.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.github.licheedev:Modbus4Android:2.0.2")
    implementation("com.afollestad.material-dialogs:core:3.3.0")
    implementation("io.github.muddz:styleabletoast:2.4.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.databinding:databinding-runtime:8.1.3")
    implementation("com.github.zcweng:switch-button:0.0.3@aar")
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")
    implementation("io.github.xmaihh:serialport:2.1.1")
    implementation("com.felipecsl.asymmetricgridview:library:2.0.1")
    implementation("com.google.code.gson:gson:2.8.9")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
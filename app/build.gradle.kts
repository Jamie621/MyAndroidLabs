plugins {
    id("com.android.application")
}

android {
    namespace = "algonquin.cst2335.ju000013"
    compileSdk = 34

    defaultConfig {
        applicationId = "algonquin.cst2335.ju000013"
        minSdk = 22
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
//    implementation(fileTree(mapOf("dir" to "C:\\Users\\jujun\\AppData\\Local\\Android\\Sdk\\platforms\\android-34", "include" to listOf("*.aar", "*.jar"), "exclude" to listOf())))
//    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar", "*.aar"))))
//    implementation(fileTree(mapOf("dir" to "C:\\Users\\jujun\\AppData\\Local\\Android\\Sdk\\platforms\\android-34", "include" to listOf("*.aar", "*.jar"), "exclude" to listOf())))
//    implementation(fileTree(mapOf("dir" to "C:\\Users\\jujun\\AppData\\Local\\Android\\Sdk\\platforms\\android-32", "include" to listOf("*.aar", "*.jar"), "exclude" to listOf())))
    implementation(fileTree("dir" to "libs", "include" to listOf("*.jar")))

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}
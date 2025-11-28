plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")}

android {
    namespace = "com.example.net"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.net"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        externalNativeBuild {
            cmake {
                cppFlags += ""
            }
        }
    }

    // Source set removed as the file is in src/main/java

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    val retrofitVersion = "2.9.0"
    val koinVersion = "3.5.3"

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.22")
    implementation("org.reflections:reflections:0.10.2")
    implementation("com.squareup.retrofit2:retrofit:$retrofitVersion")
    implementation("com.squareup.retrofit2:converter-gson:$retrofitVersion")
    implementation("io.coil-kt:coil:2.5.0")

    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-android-compat:$koinVersion")
    implementation("io.insert-koin:koin-androidx-workmanager:$koinVersion")
    implementation("io.insert-koin:koin-androidx-navigation:$koinVersion")

    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.3.1")
    implementation("androidx.media3:media3-ui:1.3.1")
}

tasks.register<JavaExec>("runCiCdTest") {
    group = "Verification"
    description = "Runs the CiCdTest main function"
    mainClass.set("com.example.net.CiCdTest")
    dependsOn("compileDebugKotlin", "compileDebugJavaWithJavac")

    doFirst {
        val android = project.extensions.getByName("android") as com.android.build.gradle.AppExtension
        val debugVariant = android.applicationVariants.find { it.name == "debug" }
        
        if (debugVariant != null) {
            classpath = files(
                project.layout.buildDirectory.dir("tmp/kotlin-classes/debug"),
                debugVariant.javaCompileProvider.get().destinationDirectory,
                debugVariant.runtimeConfiguration,
                android.bootClasspath
            )
        }
    }
}

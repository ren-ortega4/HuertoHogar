plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("org.jetbrains.kotlin.kapt")
}

// Cargar propiedades del keystore
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = java.util.Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(java.io.FileInputStream(keystorePropertiesFile))
}

android {
    namespace = "com.example.huertohogar"
    compileSdk = 36
    
    signingConfigs {
        create("release") {
            if (keystorePropertiesFile.exists()) {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    }

    defaultConfig {
        applicationId = "com.example.huertohogar"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-DEBUG"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }
    
    buildTypes {
        debug {
            enableUnitTestCoverage = true
        }
    }
}

kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
    }
}

// Configuración de JaCoCo para cobertura de código
apply(plugin = "jacoco")

tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest")
    
    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(false)
    }
    
    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*",
        "**/*\$ViewInjector*.*",
        "**/*\$ViewBinder*.*",
        "**/databinding/*",
        "**/android/databinding/*",
        "**/androidx/databinding/*",
        "**/di/module/*",
        "**/*MapperImpl*.*",
        "**/*\$Lambda$*.*",
        "**/*Companion*.*",
        "**/*Module*.*",
        "**/*Dagger*.*",
        "**/*Hilt*.*",
        "**/*MembersInjector*.*",
        "**/*_Factory*.*",
        "**/*_Provide*Factory*.*",
        "**/*Extensions*.*"
    )
    
    val javaTree = fileTree("${layout.buildDirectory.get()}/intermediates/javac/debug/classes") {
        exclude(fileFilter)
    }
    val kotlinTree = fileTree("${layout.buildDirectory.get()}/tmp/kotlin-classes/debug") {
        exclude(fileFilter)
    }
    
    classDirectories.setFrom(files(listOf(javaTree, kotlinTree)))
    
    val sourceDirs = listOf(
        "${project.projectDir}/src/main/java",
        "${project.projectDir}/src/main/kotlin"
    )
    
    sourceDirectories.setFrom(files(sourceDirs))
    executionData.setFrom(files("${layout.buildDirectory.get()}/outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"))
}

dependencies {

    //Kotest
    testImplementation("io.kotest:kotest-runner-junit5:5.8.0")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")

    //JUnit 5
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    
    //JUnit 4 (para compatibilidad con coroutines-test)
    testImplementation("junit:junit:4.13.2")

    //MockK
    testImplementation("io.mockk:mockk:1.13.10")

    //Coroutines Test
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    //Compose UI Test
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.4.3")
    debugImplementation("androidx.compose.ui:ui-test-manifest:1.6.2")
    //AndroiX
    // AndroidX Test
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation(libs.play.services.analytics.impl)
    testImplementation(libs.junit)
    testImplementation("io.mockk:mockk:1.13.8")
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation ("androidx.compose.ui:ui-test-junit4:1.5.0")
    androidTestImplementation("androidx.test:core:1.5.0")
    debugImplementation ("androidx.compose.ui:ui-tooling:1.5.0")
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation("androidx.navigation:navigation-compose:2.6.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.0-rc02")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.compose.material:material-icons-extended:1.7.8")
    implementation("androidx.datastore:datastore-preferences:1.1.7")
    implementation("org.osmdroid:osmdroid-android:6.1.18")
    implementation("com.airbnb.android:lottie-compose:6.1.0")
    implementation(platform("com.mercadopago.android.sdk:sdk-android-bom:0.1.3"))
    implementation("com.mercadopago.android.sdk:core-methods")
    implementation("com.mercadopago.android.px:checkout:4.53.2")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("androidx.browser:browser:1.4.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("com.auth0.android:jwtdecode:2.0.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    //Obligatorio para usar JUnit 5 - Comentado para usar JUnit 4 en StoreViewModelTest
    //tasks.withType<Test>().configureEach {
    //    useJUnitPlatform()
    //}

}

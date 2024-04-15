plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    //添加插件
//    id("androidx.navigation.safeargs.kotlin")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.healthapp"
    compileSdk = 34

    kotlin {
        jvmToolchain(8)
    }

    defaultConfig {
        applicationId = "com.example.healthapp"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
// 设置 Room 版本
val roomVersion = "2.6.1"
val compose_version = "1.3.1"
dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.compose.runtime:runtime-livedata:1.6.5")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.foundation:foundation-android")
    implementation("androidx.compose.material3:material3-window-size-class")
    implementation("androidx.navigation:navigation-runtime-ktx:2.7.6")
    implementation("androidx.navigation:navigation-compose:2.7.6")
    implementation("com.google.accompanist:accompanist-navigation-animation:0.34.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.room:room-runtime:$roomVersion")
//    kapt("androidx.room:room-compiler:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$roomVersion")

    // RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$roomVersion")

    // RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$roomVersion")

    // Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$roomVersion")

    // Test helpers
    testImplementation("androidx.room:room-testing:$roomVersion")

    // Paging 3 Integration
    implementation("androidx.room:room-paging:$roomVersion")

    //添加livedata的依赖
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // 添加连接mysql的依赖
    implementation("mysql:mysql-connector-java:8.0.33")

    //用图标替换按钮的拓展依赖
    implementation("androidx.compose.material:material-icons-extended")

    //为了可以使 UI 元素可以延伸到屏幕的边缘，包括状态栏和导航栏下方，从而提供更加沉浸式的用户体验
    implementation("androidx.activity:activity-ktx:1.9.0-rc01")

    /*
    implementation("com.google.accompanist:accompanist-insets:0.30.1")
    implementation("com.google.accompanist:accompanist-insets-ui:0.34.0")
    implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
    */
    // 从网络加载图片，或者从对应的文件夹加载图片
    implementation("io.coil-kt:coil-compose:2.6.0") // 确保使用最新版本

    // 使用MPAndroidChart的库函数来使用图表展示数据
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // 使用AAChartCore-Kotlin来绘制图表
    implementation("com.github.AAChartModel:AAChartCore-Kotlin:7.2.1")

    // 测试的绘图包
    // implementation("com.github.tehras:charts")
}
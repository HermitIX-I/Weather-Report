import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
}

val localProperties = Properties().apply {
    val localPropertiesFile = rootProject.file("local.properties")
    if (localPropertiesFile.exists()) {
        load(localPropertiesFile.inputStream())
    }
}

android {
    namespace = "com.rql.weatherreport"
    compileSdk = 36

    buildFeatures{
        viewBinding = true
        buildConfig = true
    }

    defaultConfig {
        applicationId = "com.rql.weatherreport"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "CAIYUN_TOKEN", "\"${localProperties.getProperty("caiyun.token", "")}\"")
        buildConfigField("String", "GAODE_TOKEN", "\"${localProperties.getProperty("gaode.token", "")}\"")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    // Retrofit 核心库
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    // Gson 转换器
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // OkHttp 日志拦截器（调试用）
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    // Jetpack组件
    // 核心依赖
    // Lifecycle 核心库（包含 Transformations）
    implementation("androidx.lifecycle:lifecycle-livedata:2.6.2")
    // ViewModel 依赖
    implementation("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    // 可选：Kotlin 扩展
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.activity:activity-ktx:1.8.0")

    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // 获得谷歌地图上的定位
    implementation("com.google.android.gms:play-services-location:21.0.1")
}
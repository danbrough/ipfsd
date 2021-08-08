plugins {
  id("com.android.library")
  kotlin("android")
  id("org.jetbrains.dokka")
  `maven-publish`

  id("kotlin-parcelize")
}

android {
  ndkVersion = ProjectVersions.NDK_VERSION
  compileSdk = ProjectVersions.SDK_VERSION

  defaultConfig {
    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    //versionCode = ProjectVersions.BUILD_VERSION
    //versionName = ProjectVersions.VERSION_NAME
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles("consumer-rules.pro")

    buildTypes {


      getByName("release") {
        isMinifyEnabled = true
        proguardFiles(
          getDefaultProguardFile("proguard-android-optimize.txt"),
          "proguard-rules.pro"
        )
      }
    }
  }

  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }


  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_JVM_VERSION
    //freeCompilerArgs = listOf("-Xjsr305=strict")
    freeCompilerArgs = freeCompilerArgs + listOf(
      "-Xopt-in=kotlinx.serialization.InternalSerializationApi"
    )
  }

  testOptions {
    unitTests.isIncludeAndroidResources = true
    unitTests.isReturnDefaultValues = true
  }

}


tasks.withType<Test> {
  useJUnit()
  testLogging {
    events("standardOut", "started", "passed", "skipped", "failed")
    showStandardStreams = true
    outputs.upToDateWhen {
      false
    }
  }
}

dependencies {
  /* api(project(":common_domain")) {
     exclude(group = "com.android", module = "android")
   }*/


  implementation(AndroidX.appCompat)
  implementation(project(":api"))

  implementation(Danbroid.utils.misc)

  //implementation("commons-io:commons-io:_")
  //implementation("ipfs.gomobile:core:0.8.0-dan04@aar")

  //api(Libs.kotlinx_coroutines_android)
  //implementation("com.google.code.gson:gson:_")
  //api(Libs.gson)

  implementation(AndroidX.core.ktx)


  //implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(KotlinX.coroutines.android)
  implementation(Google.android.material)

//  implementation(Square.okHttp3.okHttp)

  implementation(AndroidX.lifecycle.liveDataKtx)
  testImplementation(Testing.junit4)
  testImplementation(AndroidX.test.core)


  // testImplementation("net.sf.kxml:kxml2-min:_")
  //testImplementation(Libs.robolectric)
  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)
  androidTestImplementation(AndroidX.test.ext.junitKtx)
  androidTestImplementation("com.google.truth:truth:_")
}


/*
tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
  dokkaSourceSets {
    configureEach {
      includes.from("README.md")
    }
  }
}*/

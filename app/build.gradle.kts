plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  kotlin("android.extensions")
//kotlin("org.jetbrains.kotlin:kotlin-android-extensions-runtime:")
  id("androidx.navigation.safeargs.kotlin")
  id("org.jetbrains.dokka")

}



android {

  repositories {
    maven("https://h1.danbrough.org/maven")
  }

  compileSdkVersion(ProjectVersions.SDK_VERSION)

  defaultConfig {

    minSdkVersion(ProjectVersions.MIN_SDK_VERSION)
    targetSdkVersion(ProjectVersions.SDK_VERSION)
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

  }

  lintOptions {
    isAbortOnError = false
  }


  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }


  androidExtensions {
    isExperimental = true
  }

  kotlinOptions {
    jvmTarget = "1.8"
    //freeCompilerArgs = listOf("-Xjsr305=strict")
  }


/*
  kotlin.sourceSets.all {
    setOf(
      "kotlinx.coroutines.ExperimentalCoroutinesApi",
      "kotlinx.coroutines.FlowPreview"
    ).forEach {
      languageSettings.useExperimentalAnnotation(it)
    }
  }*/
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

/*
configurations.all {
  resolutionStrategy.force "com.squareup.okhttp3:okhttp:$okhttp_version"
}

project.afterEvaluate {
  android.applicationVariants.all { variant ->
    task "installRun${variant.name.capitalize()}"(type: Exec, dependsOn: "install${variant.name.capitalize()}", group: "run") {
      commandLine = ["adb", "shell", "monkey", "-p", variant.applicationId + " 1"]
      doLast {
        println "Launching ${variant.applicationId}"
      }
    }
  }
}
*/

dependencies {
  implementation(project(":ipfsd"))
  implementation(project(":api"))
  implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.coreKtx)
  implementation(AndroidX.appCompatResources)
  implementation(AndroidX.appCompat)
  implementation(Kotlin.stdlib.jdk8)
  //implementation("com.github.ligi:ipfs-api-kotlin:_")
  // implementation("com.github.ipfs:java-ipfs-http-client:_")

  //sliding panel library

  //implementation(Libs.slf4j_android)
  implementation("org.slf4j:slf4j-api:_")
  //implementation(Libs.slf4j)
  implementation(Danbroid.utils.menu)
  implementation(Danbroid.utils.slf4j)
  implementation(Danbroid.utils.misc)


  implementation(Square.okHttp3.okHttp)


//  implementation(project(":menu"))

  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)
  androidTestImplementation(AndroidX.test.ext.junitKtx)
  androidTestImplementation("com.google.truth:truth:_")
  androidTestImplementation(Danbroid.utils.slf4j)


  testImplementation("ch.qos.logback:logback-classic:_")
  testImplementation("ch.qos.logback:logback-core:_")


  implementation(AndroidX.navigation.fragmentKtx)
  implementation(AndroidX.navigation.uiKtx)
  implementation(AndroidX.constraintLayout)
  implementation(AndroidX.preferenceKtx)

  implementation(Google.android.material)


}

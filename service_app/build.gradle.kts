plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("android.extensions")
  id("org.jetbrains.dokka")
  `maven-publish`
}

android {
  ndkVersion = ProjectVersions.NDK_VERSION

  compileSdkVersion(ProjectVersions.SDK_VERSION)
  defaultConfig {
    minSdkVersion(ProjectVersions.MIN_SDK_VERSION)
    targetSdkVersion(ProjectVersions.SDK_VERSION)
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")

/*    javaCompileOptions {
      annotationProcessorOptions {
        argument("room.schemaLocation", "$projectDir/schemas")
        argument("room.incremental", "true")
        argument("room.expandProjection", "true")
      }
    }*/

  }

  buildTypes {

    getByName("release") {
      isMinifyEnabled = true
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }

  }
  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }




  kotlinOptions {
    jvmTarget = "1.8"
    //freeCompilerArgs = listOf("-Xjsr305=strict")
    freeCompilerArgs = mutableListOf("-Xopt-in=kotlin.ExperimentalStdlibApi").also {
      it.addAll(freeCompilerArgs)
    }
  }

  testOptions {
    unitTests.isIncludeAndroidResources = true
    unitTests.isReturnDefaultValues = true
  }

/*  val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
  }

  afterEvaluate {
    publishing {
      publications {
        val debug by registering(MavenPublication::class) {
          components.forEach {
            println("COMPONENT: ${it.name}")
          }
          from(components["debug_aab"])
          artifact(sourcesJar.get())
          artifactId = "service"
          groupId = ProjectVersions.GROUP_ID
          version = ProjectVersions.VERSION_NAME
        }
      }
    }
  }*/

}


/*val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(android.sourceSets.getByName("main").java.srcDirs)
}

afterEvaluate {
  publishing {
    publications {
      val release by registering(MavenPublication::class) {
        from(components["release"])
        artifact(sourcesJar.get())
        artifactId = "service"
        groupId = ProjectVersions.GROUP_ID
        version = ProjectVersions.VERSION_NAME
      }
    }
  }
}*/

tasks.withType<Test> {
  useJUnit()
  testLogging {
    events("standardOut", "started", "passed", "skipped", "failed")
    showStandardStreams = true
    /*  outputs.upToDateWhen {
        false
      }*/
  }
}


dependencies {
  /* api(project(":common_domain")) {
     exclude(group = "com.android", module = "android")
   }*/
  implementation(project(":service"))
  implementation(project(":client"))


  api("org.slf4j:slf4j-api:_")

  implementation(AndroidX.appCompat)
  implementation(Google.android.material)

  if (Danbroid.useLocalUtils) {
    implementation(project(":menu"))
    implementation(project(":misc"))
  } else {
    implementation(Danbroid.menu)
    implementation(Danbroid.misc)
  }

  implementation(Danbroid.slf4j)
  implementation("com.mikepenz:google-material-typeface:_")
  // implementation("com.mikepenz:fontawesome-typeface:_")

//

  implementation("commons-io:commons-io:_")
  //implementation("ipfs.gomobile:core:0.8.0-dan04@aar")

  //api(Libs.kotlinx_coroutines_android)
  //implementation("com.google.code.gson:gson:_")
  //api(Libs.gson)
  implementation(AndroidX.coreKtx)
  implementation(AndroidX.preference)


  //implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(KotlinX.coroutines.android)
//  implementation(Square.okHttp3.okHttp)

  implementation(AndroidX.lifecycle.liveDataKtx)
  testImplementation(Testing.junit4)
  testImplementation(AndroidX.test.core)

  testImplementation("ch.qos.logback:logback-core:_")
  testImplementation("ch.qos.logback:logback-classic:_")
  // testImplementation("net.sf.kxml:kxml2-min:_")
  //testImplementation(Libs.robolectric)
  androidTestImplementation(Testing.junit4)
  androidTestImplementation(Danbroid.slf4j)

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

plugins {
  id("com.android.library")
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

    buildTypes {
      forEach {
        it.buildConfigField("String", "ipfsd_scheme", "\"${Danbroid.IPFSD_SCHEME}\"")
      }

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
    jvmTarget = "1.8"
    //freeCompilerArgs = listOf("-Xjsr305=strict")
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
    val projectName = name
    publishing {
      publications {
        val release by registering(MavenPublication::class) {
          components.forEach {
            println("COMPONENT: ${it.name}")
          }
          from(components["release"])
          artifact(sourcesJar.get())
          artifactId = projectName
          groupId = ProjectVersions.GROUP_ID
          version = ProjectVersions.VERSION_NAME
        }
      }
    }
  }*/

}


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
  api("org.slf4j:slf4j-api:_")
  implementation(AndroidX.appCompat)
  api(project(":api"))

  implementation(Danbroid.misc)

  //implementation("commons-io:commons-io:_")
  //implementation("ipfs.gomobile:core:0.8.0-dan04@aar")

  //api(Libs.kotlinx_coroutines_android)
  //implementation("com.google.code.gson:gson:_")
  //api(Libs.gson)
  implementation(AndroidX.coreKtx)
  //implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(KotlinX.coroutines.android)
  api(Google.android.material)

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

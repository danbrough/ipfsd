plugins {
  id("org.jetbrains.dokka")
}

buildscript {

  repositories {
    google()
    mavenCentral()
  //  jcenter()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:_")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:_")
  }

}

apply("project.gradle.kts")

allprojects {

  repositories {
    google()
    //jcenter()
    mavenCentral()
    maven("https://jitpack.io")
   // maven("https://h1.danbrough.org/maven")
  }

  tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
      configureEach {
        includes.from(file("README.md"))
      }
    }
  }


}

subprojects {
  afterEvaluate {
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      kotlinOptions {
        jvmTarget = ProjectVersions.KOTLIN_JVM_VERSION
       // languageVersion = "11"
        // freeCompilerArgs = listOf("-Xjvm-default=enable")
        freeCompilerArgs = freeCompilerArgs + listOf(
          //  "-Xopt-in=kotlinx.serialization.InternalSerializationApi",
          "-Xopt-in=kotlinx.serialization.InternalSerializationApi",

          "-Xopt-in=kotlinx.coroutines.InternalCoroutinesApi",
          "-Xopt-in=kotlin.time.ExperimentalTime",
          "-Xopt-in=kotlin.ExperimentalStdlibApi"
        )
      }
    }


    (extensions.findByType(com.android.build.gradle.LibraryExtension::class)
      ?: extensions.findByType(com.android.build.gradle.AppExtension::class))?.apply {

      lintOptions.isAbortOnError = false

      defaultConfig {
        buildConfigField("String", "IPFSD_SCHEME", "\"${Danbroid.IPFSD_SCHEME}\"")
        buildConfigField("String", "IPFSD_API", "\"${Danbroid.IPFS_API}\"")

        manifestPlaceholders.put("ipfsdScheme", Danbroid.IPFSD_SCHEME)
      }


      compileOptions {
        sourceCompatibility = ProjectVersions.JAVA_VERSION
        targetCompatibility = ProjectVersions.JAVA_VERSION
      }


/*        tasks {
          kotlin.sourceSets.all {
            setOf(
              "kotlinx.coroutines.ExperimentalCoroutinesApi",
              "kotlinx.coroutines.FlowPreview",
              "kotlinx.coroutines.InternalCoroutinesApi",
              "kotlin.time.ExperimentalTime"
            ).forEach {
              languageSettings.useExperimentalAnnotation(it)
            }
          }
        }*/




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


     // if (this is com.android.build.gradle.LibraryExtension) {

        val publishing =
          extensions.findByType(PublishingExtension::class.java) ?: return@afterEvaluate


        val sourcesJar by tasks.registering(Jar::class) {
          archiveClassifier.set("sources")
          from(sourceSets.getByName("main").java.srcDirs)
        }


        afterEvaluate {
          publishing.apply {
            val projectName = name
            publications {
              val release by registering(MavenPublication::class) {
                from(components["release"])
                artifact(sourcesJar.get())
                artifactId = projectName
                groupId = ProjectVersions.GROUP_ID
               // println("PROJECT NAME IS $projectName setting version to ${Danbroid.bridge_version}")
                version = ProjectVersions.VERSION_NAME
              }
            }
          }
        }

    //  }
    }
  }
}



tasks.dokkaGfmMultiModule {
  outputDirectory.set(file("docs"))
}





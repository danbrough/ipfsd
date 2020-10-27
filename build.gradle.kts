plugins {

  id("org.jetbrains.dokka")

}

buildscript {

  repositories {
    google()
    jcenter()
  }

  dependencies {
    classpath("com.android.tools.build:gradle:4.1.0")
    //classpath("com.android.tools.build:gradle:4.2.0-alpha13")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
    classpath("org.jetbrains.dokka:dokka-gradle-plugin:_")

  }

}

apply("project.gradle.kts")

allprojects {

  repositories {
    mavenLocal()
    google()
    jcenter()
    maven("https://h1.danbrough.org/maven")
    maven("https://jitpack.io")
  }

  tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
      configureEach {
        includes.from(file("README.md"))
      }
    }
  }


  /* android {
     buildTypes {
       forEach {
         it.manifestPlaceholders(mapOf("ipfsdScheme" to Danbroid.IPFSD_SCHEME))

         it.buildConfigField("String", "IPFSD_SCHEME", "\"${Danbroid.IPFSD_SCHEME}\"")
       }
     }
   }*/


}

subprojects {
  afterEvaluate {
    (extensions.findByType(com.android.build.gradle.LibraryExtension::class)
      ?: extensions.findByType(com.android.build.gradle.AppExtension::class))?.apply {

      println("Found android subproject $name")
      lintOptions.isAbortOnError = false

      if (this is com.android.build.gradle.LibraryExtension) {
        println("$name is a library")
        compileSdkVersion(30)

      } else if (this is com.android.build.gradle.AppExtension) {
        println("$name is an application")
        compileSdkVersion(30)
      }

      defaultConfig {
        buildConfigField("String", "IPFSD_SCHEME", "\"${Danbroid.IPFSD_SCHEME}\"")
        manifestPlaceholders.put("ipfsdScheme", Danbroid.IPFSD_SCHEME)
      }

      if (this is com.android.build.gradle.LibraryExtension) {

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
                components.forEach {
                  println("Publication component: ${it.name}")
                }
                from(components["release"])
                artifact(sourcesJar.get())
                artifactId = projectName
                groupId = ProjectVersions.GROUP_ID
                version = ProjectVersions.VERSION_NAME
              }
            }
          }
        }
      }
    }
  }
}

/*


 */


tasks.dokkaGfmMultiModule {
  outputDirectory.set(file("docs"))
}





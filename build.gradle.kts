plugins {
  id("org.jetbrains.dokka")
}

buildscript {

  repositories {
    google()
    jcenter()
  }

  dependencies {
    // classpath("com.android.tools.build:gradle:4.1.0-rc02")
    classpath("com.android.tools.build:gradle:4.2.0-alpha13")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
    classpath(AndroidX.navigation.safeArgsGradlePlugin)
    //classpath("org.jetbrains.dokka:dokka-gradle-plugin:_")

  }

}


apply("project.gradle.kts")

allprojects {

  repositories {
    google()
    jcenter()
    mavenLocal()
    maven("https://h1.danbrough.org/maven")
   //s maven("https://jitpack.io")
  }

  tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
      configureEach {
      //named("main") { /* configure main source set */
        includes.from(file("README.md"))

        // platform.set(org.jetbrains.dokka.Platform.jvm)
      }
      /*configureEach {
        includes.from("README.md")
      }*/
    }
  }


  /*configurations.all {
    if (name.toLowerCase().contains("test")) {
      resolutionStrategy.dependencySubstitution {
        substitute(module(Libs.slf4j)).with(module(Libs.logback_classic))
      }
    }
  }*/
/*  tasks.withType<org.jetbrains.dokka.gradle.DokkaTask>().configureEach {
    dokkaSourceSets {
      configureEach {
        includes.from("README.md")
      }
    }
  }*/


}
tasks.dokkaGfmMultiModule {
  outputDirectory.set(file("docs"))
/*  this.gradleDokkaSourceSetBuilderFactory().create("main").apply {
    includes.from("README.md")
  }*/
}
/*
tasks.register("projectVersion") {
  this.description = "Prints Project.getVersionName()"
  doLast {
    println(ProjectVersions.getVersionName())
  }
}

tasks.register("nextProjectVersion") {
  this.description = "Prints Project.getIncrementedVersionName()"
  doLast {
    println(ProjectVersions.getIncrementedVersionName())
  }
}
*/



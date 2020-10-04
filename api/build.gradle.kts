plugins {
  `java-library`
  `maven-publish`
  kotlin("jvm")
  id("org.jetbrains.dokka")
}


java {
  sourceCompatibility = ProjectVersions.JAVA_VERSION
  targetCompatibility = ProjectVersions.JAVA_VERSION
}


tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
  kotlinOptions {
    jvmTarget = "1.8"
    freeCompilerArgs = listOf("-Xjvm-default=enable")
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

  testImplementation(Testing.junit4)

  testImplementation("ch.qos.logback:logback-core:_")
  testImplementation("ch.qos.logback:logback-classic:_")

  implementation("org.slf4j:slf4j-api:_")
//api(Libs.slf4j_api)
//implementation(Libs.kotlin_stdlib_jdk8)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.jdk8)
//api(Libs.kotlinx_coroutines_android)
  implementation("com.google.code.gson:gson:_")
//api(Libs.gson)
  implementation(Square.okHttp3.okHttp)


//testImplementation(Libs.robolectric)

}


val sourcesJar by tasks.registering(Jar::class) {
  archiveClassifier.set("sources")
  from(sourceSets.getByName("main").java.srcDirs)
}

group = ProjectVersions.GROUP_ID
version = ProjectVersions.VERSION_NAME

publishing {
  publications {
    create<MavenPublication>("default") {
      from(components["java"])
      artifact(sourcesJar)
    }
  }
  repositories {
    maven {
      url = uri("$buildDir/repository")
    }
  }
}

/*
publishing {
  publications {
    val release by registering(MavenPublication::class) {
      from(components["release"])
      artifact(sourcesJar.get())
      artifactId = "ipfsd"
      groupId = ProjectVersions.GROUP_ID
      version = ProjectVersions.VERSION_NAME
    }
  }

}*/

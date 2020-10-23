//build.gradle.kts tested with gradle 6.7

subprojects {
  afterEvaluate {
    (extensions.findByType(com.android.build.gradle.LibraryExtension::class)
      ?: extensions.findByType(com.android.build.gradle.AppExtension::class))?.apply {

      println("Found android subproject $name")
      lintOptions.isAbortOnError = false
      compileSdkVersion(30)

      if (this is com.android.build.gradle.AppExtension)
        println("$name is an application")

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

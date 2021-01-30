import de.fayard.refreshVersions.bootstrapRefreshVersions

buildscript {
  repositories { gradlePluginPortal() }
  dependencies.classpath("de.fayard.refreshVersions:refreshVersions:0.9.7")
}

bootstrapRefreshVersions()


include(":bridge",":service",":client",":api")


rootProject.name = "ipfsd"

if (false) {
  include(":menu", ":slf4j", ":misc")
  project(":menu").projectDir = file("../androidutils/menu")
  project(":slf4j").projectDir = file("../androidutils/slf4j")
  project(":misc").projectDir = file("../androidutils/misc")
}

/*import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies


buildscript {
  repositories {
    gradlePluginPortal()
    jcenter()
  }

  dependencies.classpath("de.fayard:dependencies:+")

  // println("Danbroid: ${Danbroid.local_utils}")
}


bootstrapRefreshVersionsAndDependencies()*/






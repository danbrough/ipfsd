import de.fayard.dependencies.bootstrapRefreshVersionsAndDependencies

buildscript {
  repositories {
    gradlePluginPortal()
    jcenter()
  }

  dependencies.classpath("de.fayard:dependencies:+")
}


bootstrapRefreshVersionsAndDependencies()

include(":app", ":ipfsd", ":api")

rootProject.name = "ipfs_daemon"

//include(":menu")
//project(":menu").projectDir = file("../androidutils/menu")

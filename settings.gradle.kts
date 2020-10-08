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


/*
include(":menu",":slf4j",":misc")
project(":menu").projectDir = file("../androidutils/menu")
project(":slf4j").projectDir = file("../androidutils/slf4j")
project(":misc").projectDir = file("../androidutils/misc")
*/


rootProject.name = "ipfs_daemon"



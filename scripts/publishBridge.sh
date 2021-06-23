#!/bin/bash

PKG_DIR="com/github/danbrough/ipfsd/"
REPO_SRV="dan@h1"
REPO_PATH="/srv/https/maven"

cd `dirname $0` && cd ..

JITPACK=1 ./gradlew publishReleasePublicationToMavenLocal || exit 1

cd ~/.m2/repository/
cd $PKG_DIR

find -type f -name maven-metadata-local.xml  | while read n; do
  mv "$n" $(echo "$n" | sed -e 's:maven-metadata-local:maven-metadata:g')
done

ssh "$REPO_SRV" mkdir -p "$REPO_PATH/$PKG_DIR"
rsync -avHSx   . "$REPO_SRV:$REPO_PATH/$PKG_DIR"


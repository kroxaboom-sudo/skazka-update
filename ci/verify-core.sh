#!/usr/bin/env bash
set -euo pipefail

rm -rf build/self-test
mkdir -p build/self-test

javac -encoding UTF-8 -d build/self-test \
  update-core/src/main/java/com/kroxaboom/skazka/update/*.java \
  tests/UpdateCoreSelfTest.java

java -cp build/self-test UpdateCoreSelfTest

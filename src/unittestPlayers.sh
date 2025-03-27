#!/bin/bash

# Define directories and paths
SOURCE_DIR="src"
TEST_DIR="src"
OUTPUT_DIR="out"
LIB_DIR="lib"
JUNIT_JAR="junit-platform-console-standalone.jar"
JSON_JAR="json.jar"
TEST_CLASS="unitests.playerAndDeckTests"

# Create output directories if they don't exist
mkdir -p $OUTPUT_DIR/production/PointSalad
mkdir -p $OUTPUT_DIR/test/PointSalad
cp -r src/resources/* "$OUTPUT_DIR/production/PointSalad/"
# Compile the source code
echo "Compiling source code..."
javac -d $OUTPUT_DIR/production/PointSalad \
    -cp $LIB_DIR/$JSON_JAR \
    $SOURCE_DIR/network/*.java \
    $SOURCE_DIR/player/*.java \
    $SOURCE_DIR/game/*.java \
    $SOURCE_DIR/card/*.java \
    $SOURCE_DIR/piles/*.java \
    $SOURCE_DIR/market/*.java \
    $SOURCE_DIR/scoring/*.java \
    $SOURCE_DIR/app/*.java

# Check if compilation was successful
if [ $? -ne 0 ]; then
    echo "Source code compilation failed."
    exit 1
fi

# Compile the test code
echo "Compiling test code..."
javac -d "$OUTPUT_DIR/test/PointSalad" \
    -cp "$OUTPUT_DIR/production/PointSalad;$LIB_DIR/$JUNIT_JAR;$LIB_DIR/$JSON_JAR" \
    "$TEST_DIR/unitests/PlayerAndDeckTests.java"

# Check if test compilation was successful
if [ $? -ne 0 ]; then
    echo "Test code compilation failed."
    exit 1
fi

# Run the PlayerCountTest
echo "Running PlayerCountTest..."
java -jar "$LIB_DIR/$JUNIT_JAR" \
    -cp "$OUTPUT_DIR/production/PointSalad;$OUTPUT_DIR/test/PointSalad;$LIB_DIR/$JSON_JAR" \
    --select-class "$TEST_CLASS"

# Check if the test run was successful
if [ $? -ne 0 ]; then
    echo "Test execution failed."
    exit 1
fi

echo "PlayerCountTest completed successfully."
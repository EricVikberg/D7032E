#!/bin/bash

# Define directories and paths
SOURCE_DIR="src"
TEST_DIR="src"
OUTPUT_DIR="out"
LIB_DIR="lib"
JUNIT_JAR="junit-platform-console-standalone.jar"
JSON_JAR="json.jar"

# List of test classes to run
TEST_CLASSES=(
  "unitests.PlayerAndDeckTests"
  "unitests.GameSetupAndTurnTests"
  "unitests.GameEndAndScoringTests"
)

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
    "$TEST_DIR/unitests/PlayerAndDeckTests.java" \
    "$TEST_DIR/unitests/GameSetupAndTurnTests.java" \
    "$TEST_DIR/unitests/GameEndAndScoringTests.java"

# Check if test compilation was successful
if [ $? -ne 0 ]; then
    echo "Test code compilation failed."
    exit 1
fi

# Run tests one by one with clear separation
overall_result=0
for test_class in "${TEST_CLASSES[@]}"; do
    echo -e "\n=============================================="
    echo "Running $test_class"
    echo "=============================================="

    java -jar "$LIB_DIR/$JUNIT_JAR" \
        -cp "$OUTPUT_DIR/production/PointSalad;$OUTPUT_DIR/test/PointSalad;$LIB_DIR/$JSON_JAR" \
        --select-class "$test_class"

    test_result=$?
    if [ $test_result -ne 0 ]; then
        echo -e "\n❌ $test_class FAILED"
        overall_result=1
    else
        echo -e "\n✅ $test_class PASSED"
    fi
done

# Final result
echo -e "\n=============================================="
echo "TEST SUMMARY"
echo "=============================================="
if [ $overall_result -eq 0 ]; then
    echo "All tests passed successfully!"
else
    echo "Some tests failed. See above for details."
fi

exit $overall_result
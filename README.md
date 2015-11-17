# phidget-sensornode
A software application to collect data from a set of Phidget sensors attached to a PhidgetSBC.

This code requires the phidget21.jar library to be on the Java classpath.

---------------
Compile and run from root of the package
Install the phidget21.jar using maven
mvn install:install-file -Dfile=<path-to-file> -DgroupId=<group-id> \
                                    -DartifactId=<artifact-id> -Dversion=<version> -Dpackaging=<packaging></packaging></version>

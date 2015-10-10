JCC = javac
FLAGS = -cp .:phidget21.jar
SRC_DIR = edu/hypower/gatech/phidget/

sources = $(wildcard $(SRC_DIR)*.java)
classes = $(sources:.java=.class)

all: $(classes)

%.class: %.java
	$(JCC) $(FLAGS) $<

clean: 
	rm $(SRC_DIR)*.class


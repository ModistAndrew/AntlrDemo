OUTPUT = bin/
SRC = src/main/java/
GENERATED_SRC = generated-src/
ANTLR_JAR = /ulib/antlr-4.13.1-complete.jar
ANNOTATIONS_JAR = /ulib/annotations-24.1.0.jar
MAIN_CLASS = modist.antlrdemo.Compiler
.PHONY: build
build:
	find $(GENERATED_SRC) $(SRC) -name '*.java' | xargs javac -d $(OUTPUT) -cp $(ANTLR_JAR):$(ANNOTATIONS_JAR)
.PHONY: run
run:
	java -cp $(OUTPUT):$(ANTLR_JAR) $(MAIN_CLASS)
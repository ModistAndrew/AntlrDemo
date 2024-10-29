OUTPUT = bin/
SRC = src/
GENERATED_SRC = build/generated-src/
CLANG_GENERATED = build/generated/clang/
ANTLR_JAR = /ulib/antlr-4.13.1-complete.jar
ANNOTATIONS_JAR = /ulib/annotations-24.1.0.jar
MAIN_CLASS = modist.antlrdemo.Compiler
.PHONY: build
build:
	find $(GENERATED_SRC) $(SRC) -name '*.java' | xargs javac -d $(OUTPUT) -cp $(ANNOTATIONS_JAR):$(ANTLR_JAR)
.PHONY: run
run:
	java -Xss1024m -cp $(OUTPUT):$(ANTLR_JAR) $(MAIN_CLASS)
	if [ $$? -eq 0 ]; then \
			cat $(CLANG_GENERATED)builtin.s; \
	fi

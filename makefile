OUTPUT = bin/
SRC = src/
GENERATED_SRC = build/generated-src/
CLANG_GENERATED = build/generated/clang/
GRAMMAR_LOCATION = src/main/antlr/modist/antlrdemo/frontend/grammar/Mx.g4
BUILTIN_LOCATION = src/main/c/builtin.c
ANTLR_JAR = /ulib/antlr-4.13.1-complete.jar
ANNOTATIONS_JAR = /ulib/annotations-24.1.0.jar
MAIN_CLASS = modist.antlrdemo.Compiler
.PHONY: build
build:
	clang -S -emit-llvm --target=riscv32-unknown-elf -O2 $(BUILTIN_LOCATION) -o $(CLANG_GENERATED)builtin.ll
	sed 's/_array_/_array./g;s/string_/string./g' $(CLANG_GENERATED)builtin.ll > $(CLANG_GENERATED)builtin_renamed.ll
	clang -S --target=riscv32-unknown-elf -O2 $(CLANG_GENERATED)builtin_renamed.ll -o $(CLANG_GENERATED)builtin.s
	antlr4 $(GRAMMAR_LOCATION) -o $(GENERATED_SRC)
	find $(GENERATED_SRC) $(SRC) -name '*.java' | xargs javac -d $(OUTPUT) -cp $(ANNOTATIONS_JAR):$(ANTLR_JAR)
.PHONY: run
run:
	cat $(CLANG_GENERATED)builtin.s
	java -cp $(OUTPUT):$(ANTLR_JAR) $(MAIN_CLASS)
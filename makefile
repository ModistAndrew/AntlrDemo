.PHONY: build
build:
	./gradlew build
.PHONY: run
run:
	java -jar build/libs/AntlrDemo-0.1-all.jar

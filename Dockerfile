FROM openjdk:slim
WORKDIR /
ADD ExpeditBot/build/libs/ExpeditBot.jar /expedit.jar
CMD java -jar expedit.jar
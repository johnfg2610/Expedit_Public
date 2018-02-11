FROM openjdk:slim
WORKDIR /
ADD expeditbot/build/libs/expeditbot-1.0.0-all.jar /expedit.jar
ADD configs /configs
CMD java -jar expedit.jar
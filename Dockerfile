FROM ubuntu:18.04
WORKDIR /usr/src/app
RUN apt-get update && apt-get upgrade && apt-get -y install openjdk-8-jre
RUN apt-get -y install curl unzip sudo
RUN apt-get -y install python

COPY MaltOptimizer-1.0.3.tar.gz ./
COPY maltparser-1.9.2.tar.gz ./

COPY data/VnDep-v06.1/VnDep-v06.Dev.conllu ./data/Dev.conllu
COPY data/VnDep-v06.1/VnDep-v06.Train.conllu ./data/Train.conllu
COPY data/VnDep-v06.1/VnDep-v06.Test.conllu ./data/Test.conllu

COPY setup.sh ./setup.sh
COPY optimize.sh ./optimize.sh


CMD [ "tail","-f","/dev/null" ]
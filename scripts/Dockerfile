FROM ubuntu:18.04
WORKDIR /srv/constituent
ENV PYTHONDONTWRITEBYTECODE 1
ENV PYTHONUNBUFFERED 1

ARG MALT_PARSER="http://maltparser.org/dist/maltparser-1.9.2.tar.gz"
ARG MALT_EVAL="https://raw.githubusercontent.com/sonlhcsuit/dotfiles/main/MaltEval-dist.zip"
ARG MALT_OPTMIZER="http://nil.fdi.ucm.es/maltoptimizer/MaltOptimizer-1.0.2.tar.gz"
ARG MST_PARSER="https://ryanmcd.github.io/MSTParser/MSTParser.tar.gz"

RUN apt-get update -y && \
	apt-get upgrade -y && \
	apt-get -y install curl unzip sudo wget htop openjdk-8-jre openjdk-8-jdk python3 python3-pip vim

RUN wget ${MALT_PARSER} && gunzip maltparser-1.9.2.tar.gz && tar -xvf maltparser-1.9.2.tar && \
	wget ${MALT_OPTMIZER} && gunzip MaltOptimizer-1.0.2.tar.gz && tar -xvf MaltOptimizer-1.0.2.tar && \
	# wget ${MALT_EVAL} && gunzip maltparser-1.9.2.tar.gz && tar -xvf maltparser-1.9.2.tar && \
	wget ${MST_PARSER} && gunzip MSTParser.tar.gz && tar -xvf MSTParser.tar

COPY ./requirements.txt ./
COPY ./main.py ./
RUN python3 -m pip install --upgrade pip && \
	python3 -m pip install -r requirements.txt

CMD [ "tail","-f","/dev/null" ]

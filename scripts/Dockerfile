FROM ubuntu:18.04
WORKDIR /srv/consituent
ARG MALT_PARSER="http://maltparser.org/dist/maltparser-1.9.2.tar.gz"
ARG MALT_EVAL="https://raw.githubusercontent.com/sonlhcsuit/dotfiles/main/MaltEval-dist.zip"
ARG MALT_OPTMIZER="http://nil.fdi.ucm.es/maltoptimizer/MaltOptimizer-1.0.2.tar.gz"
ARG MST_PARSER="https://www.seas.upenn.edu/~strctlrn/MSTParser/MSTParser.tar.gz"

RUN apt-get update -y && apt-get upgrade -y
RUN apt-get -y install openjdk-8-jre curl python3 python3-pip unzip sudo wget htop
RUN wget ${MALT_PARSER} && gunzip maltparser-1.9.2.tar.gz && tar -xvf maltparser-1.9.2.tar
RUN wget ${MALT_OPTMIZER} && gunzip MaltOptimizer-1.0.2.tar.gz && tar -xvf MaltOptimizer-1.0.2.tar
# RUN wget ${MALT_EVAL} && gunzip maltparser-1.9.2.tar.gz && tar -xvf maltparser-1.9.2.tar
RUN wget ${MST_PARSER} && gunzip MSTParser.tar.gz && tar -xvf MSTParser.tar

COPY ./data/VnDep-v06.1/ ./data/
COPY ./requirements.txt ./
COPY ./main.py ./
RUN python3 -m pip install --upgrade pip
RUN python3 -m pip install -r requirements.txt

CMD [ "tail","-f","/dev/null" ]

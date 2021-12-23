DIR=$(pwd)
LOG_INF="logs/info.log"
LOG_ERR="logs/error.log"
MALT_PARSER="http://maltparser.org/dist/maltparser-1.9.2.tar.gz"
MALT_EVAL="https://raw.githubusercontent.com/sonlhcsuit/dotfiles/main/MaltEval-dist.zip"
MST_PARSER="https://www.seas.upenn.edu/~strctlrn/MSTParser/MSTParser.tar.gz"

mkdir -p logs
#echo $DIR
#echo "DOWNLOADING MSTParser..."
#wget $DIR
#gunzip MSTParser.tar.gz
#tar -xvf MSTParser.tar
#rm MSTParser.tar

echo "DOWNLOADING MaltParser..."
#wget $MALT_MALT_PARSER $DIR
#gunzip maltparser-1.9.2.tar.gz
#tar -xvf maltparser-1.9.2.tar
#mv maltparser-1.9.2/ MaltParser/

wget $MALT_EVAL $DIR
unzip -o MaltEval-dist.zip
mv dist-20141005/ MaltEval/

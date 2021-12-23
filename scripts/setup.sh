DIR=$(pwd)
LOG_INF="logs/info.log"
LOG_ERR="logs/error.log"
mkdir -p logs
#echo $DIR
#echo "DOWNLOADING MSTParser..."
#wget https://www.seas.upenn.edu/~strctlrn/MSTParser/MSTParser.tar.gz $DIR
#gunzip MSTParser.tar.gz
#tar -xvf MSTParser.tar
#rm MSTParser.tar

echo "DOWNLOADING MaltParser..."
#wget http://maltparser.org/dist/maltparser-1.9.2.tar.gz $DIR
#gunzip maltparser-1.9.2.tar.gz
#tar -xvf maltparser-1.9.2.tar
mv maltparser-1.9.2/ MaltParser/

#rm MSTParser.tar

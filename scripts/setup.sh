DIR=$(pwd)
echo $DIR
wget https://www.seas.upenn.edu/~strctlrn/MSTParser/MSTParser.tar.gz $DIR
gunzip MSTParser.tar.gz
tar -xvf MSTParser.tar
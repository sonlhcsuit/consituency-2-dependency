cd /Users/sonlh/sonlh/thesis/consituency-2-dependency/MSTParser
javac -classpath ".:lib/trove.jar" mstparser/DependencyParser.java
echo "Training MST.Train.conllu..." >>log.txt
java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser train train-file:data/MST.Train.conllu model-name:models/Train.model #>> log.txt 2>>log.txt
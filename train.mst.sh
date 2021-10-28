cd /Users/sonlh/sonlh/projects/personal/consituency-2-dependency/MSTParser
javac -classpath ".:lib/trove.jar" mstparser/DependencyParser.java
echo "Training MST.Dev.conllu..." >>log.txt
java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser train train-file:data/MST.Dev.conllu model-name:models/Dev.model >> log.txt 2>>log.txt
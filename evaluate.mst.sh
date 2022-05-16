cd /Users/sonlh/sonlh/thesis/consituency-2-dependency/MSTParser
echo "Result on golden: MST.Test.conllu " >>Metrics.txt
java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser test model-name:models/Train.model test-file:data/MST.Test.conllu output-file:data/result.conllu >>log.txt 2>>log.txt
java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser eval gold-file:data/MST.Test.conllu output-file:data/result.conllu >>Metrics.txt
mv Metrics.txt /Users/sonlh/sonlh/thesis/consituency-2-dependency/ 

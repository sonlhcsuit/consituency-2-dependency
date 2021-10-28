echo $(pwd)
#cp -r data MSTParser/
cd MSTParser || exit
#javac -classpath ".:lib/trove.jar" mstparser/DependencyParser.java

echo "Training transposed.Dev.conllu..." >>log.txt
java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser train "train-file:data/transposed.Dev.conllu" model-name:models/transposed.Dev.conllu.model >>log.txt 2>>log.txt
#echo "Training transposed.Test.conllu..." >>log.txt
#java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser train train-file:data/transposed.Test.conllu model-name:models/transposed.Test.conllu.model >/dev/null 2>>log.txt
#echo "Training transposed.Train.conllu..." >>log.txt
#java -classpath ".:lib/trove.jar" -Xmx1800m mstparser.DependencyParser train train-file:data/transposed.Train.conllu model-name:models/transposed.Train.conllu.model >>log.txt 2>>log.txt

#start:
#	./venv/bin/python main.py util split -s 25 -p ./data/VnDep-v06
eval:
	./venv/bin/python main.py util eval
#split-5:
#	./venv/bin/python main.py util fold -p data/VnDep-v06 # >log.txt 2>>log.txt
#merge:
#	./venv/bin/python main.py util merge -p ./data/VnDep-v06
#stat:
#	./venv/bin/python main.py util stat -p ./data/VnDep-v06
#mst:
#	./venv/bin/python main.py train mst -p ./data/VnDep-v06

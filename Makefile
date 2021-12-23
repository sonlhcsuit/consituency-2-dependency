#start:
#	./venv/bin/python main.py >log.txt 2>>log.txt
merge:
	./venv/bin/python main.py util merge -p ./data/VnDep-v06
stat:
	./venv/bin/python main.py util stat -p ./data/VnDep-v06

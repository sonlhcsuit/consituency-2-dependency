#start:
#	./venv/bin/python main.py util split -s 25 -p ./data/VnDep-v06
#eval:
#	./venv/bin/python main.py util eval --gold "MaltParser/data/Above-25.Test.conllu" --predict "MaltParser/data/Above-25.Test.conllu.parsed"
#split-5:
#	./venv/bin/python main.py util fold -p data/VnDep-v06 # >log.txt 2>>log.txt
#merge:
#	./venv/bin/python main.py util merge -p ./data/VnDep-v06
stat:
	./venv/bin/python main.py util stat -p ./data/Original/NIIVTB-1
#mst:
#	./venv/bin/python main.py train mst -p ./data/VnDep-v06

convert:
	./venv/bin/python main.py convert ./data/Original ./data/VnDep

build:
	docker-compose -p consituent -f ./scripts/docker-compose.yml build
up:
	docker-compose -p consituent -f ./scripts/docker-compose.yml up -d
down:
	docker-compose -p consituent -f ./scripts/docker-compose.yml down
exec:
	docker exec -ti constituent su

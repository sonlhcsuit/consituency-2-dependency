# Consituency 2 Dependency
# Requirements
- Docker
- Good mental

# Project struture
```
.
├── Makefile
├── README.md
├── docker-compose.yml
├── main.py
├── requirements.txt
├── scripts
│   ├── Dockerfile
│   ├── sample-copy.sh
│   ├── sample-setup.sh
│   └── sample-train.sh
├── src
│   ├── main
│   │   ├── __init__.py
│   │   ├── converter
│   │   ├── menu.py
│   │   ├── sentence
│   │   ├── trainer
│   │   ├── treebank
│   │   └── utils
│   └── one-line.py
└── data
	└── example-dataset	
		├── Dev
		│   ├── Dev_01.conllu
		│   └── Dev_02.conllu
		├── Test
		│   ├── Test_01.conllu
		│   └── Test_02.conllu
		└── Train
			├── Train_01.conllu
			└── Train_02.conllu
```
- Your dataset should have the structure like `example-dataset` (DEV/TEST/TRAIN)
- Merge them into single file by `python3 main.py util merge-c -p data/` (since you dont need an extra level of `example-dataset`)
- It should be
```
data
├── Dev
│   ├── Dev_01.conllu
│   └── Dev_02.conllu
├── Test
│   ├── Test_01.conllu
│   └── Test_02.conllu
├── Train
│   ├── Train_01.conllu
│   └── Train_02.conllu
├── data.Dev.conllu
├── data.Test.conllu
└── data.Train.conllu
```

## How to use MST?
- go to `/srv/constituent`
- MST, require Dev Test Train as single files. 
```
python3 main.py train mst -p data/
```
- Formatted single files are generated. They also are copied to `MSTParser` within data folder
```
data
├── Dev
├── Test
├── Train
│   ├── Train_01.conllu
│   └── Train_02.conllu
├── data.Dev.conllu
├── data.Test.conllu
├── data.Train.conllu
├── MST.data.Dev.conllu
├── MST.data.Test.conllu
└── MST.data.Train.conllu
```
- 2 files generated, called `mst.evaluate.sh` & `mst.train.sh`. Create models folder in `MSTParser`
```
mkdir -p MSTParser/models
```
- Train, Evaluate, Watch Result. Run sequentially 
```
sh mst.train.sh
sh mst.evaluate.sh
cat Metrics.txt
```
- Details of training progress is logged in `MSTParser/log.txt`. In case you wanna track it, open new terminal, exec into the container by creating new session then run `tail -f MSTParser/log.txt` 

## How to use Malt?
- go to `/srv/constituent`
```
python3 main.py train malt -p data/
```

- 2 File are generated `model.test.xml` and `model.train.xml` in `MaltParser`. Then copy 3 single files to `MaltParser`
```
cp data/data.Dev.conllu MaltParser/
cp data/data.Train.conllu MaltParser/
cp data/data.Test.conllu MaltParser/
```
- Train by running
```
java -jar maltparser-1.9.2.jar -f model.train.xml
java -jar maltparser-1.9.2.jar -f model.test.xml
```
- Then result is with 'parsed' as posfix. For example: `Test.conllu.parsed` 
- Evaluate `Test.conllu.parsed` and `Test.conllu.parsed` by running 
```
root@c080d8f2f37f:/srv/constituent# python3 main.py util eval --predict MaltParser/Test.conllu.parsed --gold MaltParser/Test.conllu
```
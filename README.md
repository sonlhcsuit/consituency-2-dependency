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

## How to run it ?
- MST, require Dev Test Train as single files. 
```
python3 main.py train mst -p data/
```
- Formatted single files are generated. There are 2 files called `mst.evaluate.sh` & `mst.train.sh` are generated also.
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
- Copy them to `MSTParser`

from src.app import merge_conllu
from src.app import ConlluFile, ConlluSentence
import os
from typing import List

# folders = ["Dev", "Train", "Test"]
# source_data = "Converted"
# data_dir = os.path.join(os.getcwd(), "data")
# for f in folders:
# 	merge_conllu(os.path.join(data_dir, source_data, f), os.path.join(data_dir,f"{f}.conllu"))

DEV = os.path.join(os.getcwd(), "data", "Converted", "Train")
files = os.listdir(DEV)
files = sorted(files)
file = files[0]
print(files)

# for file in files:
# 	conllu_file: List[ConlluSentence] = ConlluFile.from_file(os.path.join(DEV, file))
# 	for sentence in conllu_file:
# 		if not sentence.check_dependency_order():
# 			print(file, sentence.get_index())
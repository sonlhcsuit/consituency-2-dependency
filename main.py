import re

from src.app import merge_conllu
from src.app import ConlluFile, ConlluSentence
import os
from src.app import train, mst_transpose
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
# for file in files:
#     try:
#         if len(result) > 0:
#             mst_transpose(os.path.join(DEV, file),)
#             break
#     except Exception as e :
#         print(e )
#         print(file)
#         pass
# print(files)
# train()
# tran
for file in files:
    try:
        conllu_file = ConlluFile(os.path.join(DEV, file))
        filename = re.findall("[0-9]+", file)
        if len(filename) > 0:
            conllu_file.transpose_mst(os.path.join(os.getcwd(), "temp", f"{filename[0]}.txt"))
    except Exception as e:
        print(e)
        print(file)
    # if not sentence.check_dependency_order():
    # 	print(file, sentence.get_index())

import re

from src.app import merge_conllu
from src.app import ConlluFile, ConlluSentence, MSTParser
import os
# from src.app import train, mst_transpose,MMSTParser
from typing import List

mst = MSTParser("MSTParser", os.path.join("data", "Converted"))
# mst.merge_folder()
# mst.process_data()
mst.generate_train_script(output="")
#
# # folders = ["Dev", "Train", "Test"]
# # source_data = "Converted"
# # data_dir = os.path.join(os.getcwd(), "data")
# # for f in folders:
# # 	merge_conllu(os.path.join(data_dir, source_data, f), os.path.join(data_dir,f"{f}.conllu"))
# #
# DEV = os.path.join(os.getcwd(), "data")
# files = ['Dev.conllu', 'Test.conllu', 'Train.conllu']
# # files = os.listdir(DEV)
# # files = sorted(files)
# # file = files[0]
#
# for file in files:
#     try:
#         conllu_file = ConlluFile(os.path.join(DEV, file))
#         conllu_file.transpose_mst(os.path.join(os.getcwd(), "data", f"transposed.{file}"))
#     except Exception as e:
#         print(file)
#         raise e
#
# # for file in files:
#     # conllu_file = ConlluFile(os.path.join(DEV, f"transposed.{file}"))
#     # try:
#     #     train(f"transposed.{file}")
#     # except Exception as e:
#     #     print(file)
#     #     print(e)

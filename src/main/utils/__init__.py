import os
import re
from pandas import DataFrame, read_csv
import numpy as np


class Util:
	FOLDER = ("Dev", "Train", "Test")

	@staticmethod
	def start(args: dict):
		choice = args.get("choice")
		data_dir = args.get("path")
		data_dir = os.path.normpath(os.path.join(
			os.getcwd(), data_dir
		))
		if not os.path.isdir(data_dir):
			raise Exception("`Path` of the data is not valid!")
		if choice == "stat":
			Util.__stat(data_base=data_dir)
		elif choice == "merge":
			Util.__merge_conllu(data_base=data_dir)

	@staticmethod
	def __merge_conllu(data_base: str, skip_id=False) -> None:
		"""
		:param folder: data folder, must include `Dev`, `Train`, `Test` folders
		:param output:
		:return:
		"""
		for data_folder in Util.FOLDER:
			base = os.path.basename(data_base)
			data_filedir = os.path.join(data_base, data_folder)
			merged_filename = os.path.join(data_base, f"{base}.{data_folder}.conllu")
			with open(merged_filename, "w") as writer:
				files = os.listdir(data_filedir)
				files = sorted(files)
				sentence_id = 1
				for file in files:
					sentence_file_id = 1
					with open(os.path.join(data_filedir, file), 'r') as reader:
						lines = reader.readlines()
						for i in range(len(lines)):
							match = re.match("^#\sID", lines[i], re.I)
							if match:
								if not skip_id:
									lines[i] = f"# ID = {sentence_id} - {file}:{sentence_file_id}\n"
									sentence_id += 1
									sentence_file_id += 1
								else:
									lines[i] = ""
						writer.write("".join(lines).strip())
						writer.write("\n")

	@staticmethod
	def __stat(data_base: str):
		token_lists = {}
		token_type = []
		dataframes = []
		for data_folder in Util.FOLDER:
			base = os.path.basename(data_base)
			merged_filename = os.path.join(data_base, f"{base}.{data_folder}.conllu")
			df = read_csv(merged_filename, names=list('abcdefghij'), delimiter="\t", skip_blank_lines=True, quoting=3,
						  header=None, encoding="utf-8")
			df.dropna(inplace=True)
			dataframes.append(df)
			tokens = np.array(list(df['h']))
			token_lists.setdefault(data_folder, tokens)
			token_type = list(set(token_type + list(df['h'])))
		d = {}
		for data_folder in Util.FOLDER:
			tokens = token_lists.get(data_folder)
			unique, counts = np.unique(tokens, return_counts=True)
			stats = dict(zip(unique, counts))
			d.setdefault(data_folder, stats)
		df = DataFrame(d)
		df.to_csv(path_or_buf="stat.csv")
		for data_folder in Util.FOLDER:
			df[data_folder] = df[data_folder] / len(token_lists.get(data_folder))
		df.to_csv(path_or_buf="stat.percent.csv")

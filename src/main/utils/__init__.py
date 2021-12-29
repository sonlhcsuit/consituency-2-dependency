import os
import re
from pandas import DataFrame, read_csv
import numpy as np

from ..treebank import CONLLU_FILE


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
		elif choice == "fold":
			Util.__split_data(data_path=data_dir, k=args.get("k_value"))

	@staticmethod
	def __split_data(data_path: str, k: int):
		files = os.listdir(data_path)
		conllu_files = []
		for file in files:
			fp = os.path.join(data_path, file)
			if os.path.isfile(fp) and 'conllu' in file.lower():
				conllu_files.append(fp)
		final = CONLLU_FILE("")
		for c in conllu_files:
			c_file = CONLLU_FILE.from_file(c)
			final = final.append(c_file)
		index = np.arange(final.get_size())
		np.random.shuffle(index)
		index = list(index)
		test_size = int(final.get_size() / k)
		segments = []
		for i in range(k):
			if i == 0:
				segments.append(index[:test_size])
			elif i == k - 1:
				segments.append(index[test_size * i:])
			else:
				segments.append(index[test_size * i:test_size * (i + 1)])
		folds = []
		for i in range(k):
			test = segments[i]
			train = []
			for j in range(k):
				if j == i:
					continue
				train = train + segments[j]
			folds.append([
				test, train
			])
		os.makedirs(os.path.join(data_path, f"fold-{k}"), exist_ok=True)
		i = 1
		for test_fold_index, train_fold_index in folds:
			test_fold_fp = os.path.join(data_path, f"fold-{k}", f"Fold-{i}.Test.conllu")
			train_fold_fp = os.path.join(data_path, f"fold-{k}", f"Fold-{i}.Train.conllu")
			test_fold = final.subset(test_fold_index)
			train_fold = final.subset(train_fold_index)
			test_fold.dump(test_fold_fp)
			train_fold.dump(train_fold_fp)
			i+=1
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
						writer.write("\n\n")

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

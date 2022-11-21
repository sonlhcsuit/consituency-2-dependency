import pandas as pd
import re
import os
from ..sentence import ConlluSentence, ConlluFile


class MSTParser:

	def __init__(self, location: str, data_location: str) -> None:
		"""
		Init an MSTParser
		:param location: path to MSTParser folder (original Java implementation) - relative to the root folder
		:param data_location: path to dependency tree (consists of 3 folder Dev, Train, Test) - relative to the root folder
		"""
		self.parser = os.path.join(os.getcwd(), location)
		self.data = os.path.join(os.getcwd(), data_location)
		if not os.path.isdir(self.parser):
			raise ValueError(f"`{location}` directory is not exists")
		if not os.path.isdir(self.data):
			raise ValueError(f"`{data_location}` directory is not exists")

	@staticmethod
	def generate_scripts(train_fp: str, test_fp: str, script_based: str, model_name="model"):
		train_content = f"""echo "Trainning {model_name} with {train_fp}"\n""" \
						f"""javac -classpath \".:lib/trove.jar\" mstparser/DependencyParser.java\n""" \
						f"""java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser """ \
						f"""train train-file:{train_fp} model-name:models/{model_name}.model >> log.txt 2>>log.txt \n"""
		test_content = f"""echo \"Result on golden: {test_fp} \" >>metrics.{model_name}.txt\n""" \
					   f"""java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser """ \
					   f"""test model-name:models/{model_name}.model test-file:{test_fp} output-file:{test_fp}.parsed >>log.txt 2>>log.txt\n""" \
					   f"""java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser """ \
					   f"""eval gold-file:{test_fp} output-file:{test_fp}.parsed >> metrics.{model_name}.txt\n """
		with open(os.path.join(script_based, f"{model_name}.train.sh"), "w") as writer:
			writer.write(train_content.strip())
		with open(os.path.join(script_based, f"{model_name}.test.sh"), "w") as writer:
			writer.write(test_content.strip())

	def generate_evaluate_script(self, model="Train",
								 name="mst.evaluate.sh",
								 result="result.conllu", scoreboard="Metrics.txt"):
		directories = os.listdir(self.data)
		directories = list(map(lambda d: os.path.join(self.data, d), directories))
		files = list(filter(lambda d: os.path.isfile(d), directories))
		testset = None 
		for filepath in files:
			filename = os.path.basename(filepath)
			if filename.startswith("MST") and 'test' in filename.lower():
				testset = filename
		if testset is None:
			raise Exception("Unable to find test file")
		with open(os.path.join(os.getcwd(), name), "w") as writer:
			writer.write(f"cd {self.parser}\n")
			writer.write(f"echo \"Result on golden: {testset} \" >>{scoreboard}\n")
			writer.write(f"java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser " +
						 f"test model-name:models/{model}.model test-file:data/{testset} output-file:data/{result} >> log.txt 2>> log.txt\n")
			writer.write(f"java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser " +
						 f"eval gold-file:data/{testset} output-file:data/{result} >> {scoreboard}\n")
			writer.write(f"mv {scoreboard} {os.getcwd()}/ \n")

	def generate_train_script(self, name: str = "mst.train.sh"):
		directories = os.listdir(self.data)
		directories = list(map(lambda d: os.path.join(self.data, d), directories))
		files = list(filter(lambda d: os.path.isfile(d), directories))
		for filepath in files:
			filename = os.path.basename(filepath)
			if filename.startswith("MST"):
				source = filepath
				destination = os.path.join(self.parser, "data", filename)
				os.system(f"cp {source} {destination}")

		with open(os.path.join(os.getcwd(), name), "w") as writer:
			writer.write(f"cd {self.parser}\n")
			writer.write(f"javac -classpath \".:lib/trove.jar\" mstparser/DependencyParser.java\n")
			for filepath in files:
				filename = os.path.basename(filepath)
				kind = filename.split(".")
				kind = kind[1]
				if kind.lower() in ["train"]:
					writer.write(f"echo \"Training MST.{filename}...\" >>log.txt\n")
					writer.write(f"java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser " +
								 f"train train-file:data/MST.{filename} " +
								 f"model-name:models/{kind}.model >> log.txt 2>>log.txt\n")

	def process_data(self):
		"""
		Convert Conllu file-format to mst-desired format (ulab format)
		:return:
		"""
		directories = os.listdir(self.data)
		directories = list(map(lambda d: os.path.join(self.data, d), directories))
		files = list(filter(lambda d: os.path.isfile(d), directories))
		for filepath in files:
			filename = os.path.basename(filepath)
			if not filename.startswith("MST"):
				conllu_file = ConlluFile(filepath)
				conllu_file.transpose_mst(os.path.join(self.data, f"MST.{filename}"))

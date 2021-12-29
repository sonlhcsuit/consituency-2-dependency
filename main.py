import re
from typing import List


from src.main.menu import ApplicationCLI
from nltk.parse import DependencyGraph, DependencyEvaluator
from src.main import CONLLU_FILE
if __name__ == '__main__':
	# app = ApplicationCLI()
	# app.start()
	CONLLU_FILE.from_file("data/VnDep-v06/VnDep-v06.Dev.conllu")
	pass

from typing import List
import os
import re


class ConlluSentence:

	def __init__(self, lines, index):
		self.words = lines
		self.index = index
		self.size = len(lines)

	def check_dependency_order(self) -> bool:
		words = list(map(lambda x: re.sub("^[0-9]+\t.+?\t", "", x), self.words))
		text = "".join(words)
		result = re.findall("[0-9]+", text)
		result = list(map(int, result))
		m = max(result)
		return m <= self.size

	def get_index(self):
		return self.index


class ConlluFile:

	@staticmethod
	def from_file(filepath: str) -> List[ConlluSentence]:
		if not os.path.isfile(filepath):
			raise ValueError("An valid filepath is expected")
		reader = open(filepath)
		lines = reader.readlines()
		result = []
		start = 0
		l = 0
		i = 0
		for index, value in enumerate(lines):
			if value.startswith("#"):
				result.append(ConlluSentence(lines[start + 1:start + l], i))
				i += 1
				start = index
				l = 0
			else:
				l += 1
		result.append(ConlluSentence(lines[start + 1:start + l], i))
		result.pop(0)
		reader.close()
		return result

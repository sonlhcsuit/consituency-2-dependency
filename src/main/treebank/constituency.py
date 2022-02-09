from nltk.tree import Tree
import re


class ConstituencyTree:
	pass


class ConstituencyFile:
	def __init__(self, **kwargs):
		self.__sentences = []

	@staticmethod
	def from_file(filepath):
		with open(filepath, 'r', encoding="utf8") as reader:
			content = reader.read()
			sent = []
			matches = re.findall("(?<=<s>).+?(?=<\/s>)", content, flags=re.M | re.I | re.S)
			for match in matches:
				sent.append(Tree.fromstring(
					match.strip()
				))
		f = ConstituencyFile()
		f.__sentences = sent
		return f

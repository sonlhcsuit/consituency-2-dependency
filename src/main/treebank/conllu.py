import re
from typing import List
import os


class CONLLU_ERROR(Exception):
	pass


class CONLLU_WORD:
	CONLLU_COLUMNS = [
		"ID", "FORM", "LEMMA", "UPOS", "XPOS", "FEATS", "HEAD", "DEPREL", "DEPS", "MISC"
	]

	def __init__(self, text: str, sep="\t"):
		cols = text.split(sep)
		if len(cols) != 10:
			print(text)
			raise CONLLU_ERROR("Number of columns is not valid! It must be 10!")
		if "-" in cols[0]:
			raise CONLLU_ERROR(f"Multiple words index {cols[0]}is not supported!")
		try:
			int(cols[0])
		except TypeError:
			raise CONLLU_ERROR(f"Index `{cols[0]}` is not a valid number .")
		try:
			int(cols[6])
		except TypeError:
			raise CONLLU_ERROR(f"Head `{cols[0]}` is not a valid number .")
		self._id = cols[0]
		self._form = cols[1]
		self._lemma = cols[2]
		self._upos = cols[3]
		self._xpos = cols[4]
		self._feats = cols[5]
		self._head = cols[6]
		self._deprel = cols[7]
		self._deps = cols[8]
		self._misc = cols[9]

	def to_plain_text(self, sep="\t"):
		return f"""{self._id}{sep}{self._form}{sep}{self._lemma}{sep}{self._upos}{sep}{self._xpos}{sep}""" \
			   f"""{self._feats}{sep}{self._head}{sep}{self._deprel}{sep}{self._deps}{sep}{self._misc}\n"""


class CONLLU_SENTENCE:
	def __init__(self, text: str, sep: str = "\t", newline="\n"):
		lines = text.split(newline)
		self.__comments = []
		self.__words = []
		for line in lines:
			if line.startswith("#"):
				self.__comments.append(line)
			elif line.strip() != "":
				self.__words.append(CONLLU_WORD(line, sep=sep))

	def to_plain_text(self, sep="\t"):
		text = ""
		for cmt in self.__comments:
			text += cmt
			text+="\n"
		for word in self.__words:
			text += word.to_plain_text(sep=sep)
		return text + "\n"

	def size(self):
		return len(self.__words)


class CONLLU_FILE:
	def __init__(self, plaintext: str):
		self.__sentences = []
		sents = re.split("\n{2,}", plaintext, flags=re.I | re.DOTALL)
		for sent in sents:
			self.__sentences.append(
				CONLLU_SENTENCE(sent)
			)

	def get_size(self):
		return len(self.__sentences)

	def dump(self, filepath):
		fp = filepath
		with open(fp, "w") as writer:
			writer.write(self.to_plain_text().strip())

	@classmethod
	def from_file(cls, filepath: str):
		fp = filepath
		# fp = os.path.join(os.getcwd(), filepath)
		with open(fp, "r") as reader:
			content = reader.read()
			data = cls(content)
		return data

	def to_plain_text(self):
		text = ""
		for sent in self.__sentences:
			text += sent.to_plain_text()
		return text

	def append(self, other):
		data: CONLLU_FILE = other
		text = self.to_plain_text()
		text = text + data.to_plain_text()
		return CONLLU_FILE(plaintext=text)

	def subset(self, index: List[int]):
		max_value = max(index)
		min_value = min(index)
		if min_value < 0:
			raise CONLLU_ERROR(f"Negative index `{min_value}` is not supported!")
		if max_value > self.get_size():
			raise CONLLU_ERROR(f"Invalid sentence index `{max_value}` with file have `{self.get_size()}` sentences")
		text = ""
		for element in index:
			text = text + self.__sentences[element].to_plain_text()
		return CONLLU_FILE(text)

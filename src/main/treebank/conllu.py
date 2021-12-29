import re

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
			   f"""{self._feats}{sep}{self._head}{sep}{self._head}{sep}{self._deprel}{sep}{self._deps}{sep}{self._misc}"""


class CONLLU_SENTENCE:
	def __init__(self, text: str, sep: str = "\t", newline="\n"):
		lines = text.split(newline)
		self.comments = []
		self.words = []
		for line in lines:
			if line.startswith("#"):
				self.comments.append(line)
			elif line.strip() != "":
				self.words.append(CONLLU_WORD(line, sep=sep))

	def to_plain_text(self, sep="\t"):
		text = ""
		for cmt in self.comments:
			text = text + "\n" + cmt
		for word in self.words:
			text = text + "\n" + word.to_plain_text(sep=sep)
		return text


class CONLLU_FILE:
	def __init__(self, plaintext: str):
		self._sentences = []
		sents = re.split("\n{2,}", plaintext, flags=re.I | re.DOTALL)
		for sent in sents:
			self._sentences.append(
				CONLLU_SENTENCE(sent)
			)

	@classmethod
	def from_file(cls, filepath: str):
		fp = os.path.join(os.getcwd(), filepath)
		with open(fp, "r") as reader:
			content = reader.read()
			CONLLU_FILE(content)
		pass

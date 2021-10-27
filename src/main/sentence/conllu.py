from typing import List


class ConlluSentence:

	def __init__(self, lines):
		self.words = lines

	def testing(self):
		print(self.words)

	@staticmethod
	def from_file_path(filepath: str) -> List[ConlluSentence]:
		ConlluSentence()
		pass

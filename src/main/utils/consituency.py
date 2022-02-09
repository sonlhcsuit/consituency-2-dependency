from ..treebank import ConstituencyFile
import os


class ConstituencyUtil:
	pass

	@staticmethod
	def analyze_coord(base: str):
		folders = os.listdir(base)
		folders = list(map(
			lambda child: os.path.join(base, child), folders
		))
		folders = list(filter(
			lambda folder: os.path.isdir(folder), folders
		))
		files = []
		for folder in folders:
			fs = os.listdir(folder)
			for file in fs:
				if file.endswith(".prd"):
					files.append(os.path.join(folder, file))
		files = sorted(files)
		cond_2 = 0
		for file in files:
			f = ConstituencyFile.from_file(file)
			break
		pass

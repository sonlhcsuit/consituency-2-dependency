import os


def merge_conllu(folder: str, output: str) -> None:
	"""
	:param folder:
	:param output:
	:return:
	"""
	writer = open(output, "w")
	filenames = os.listdir(folder)
	filenames = sorted(filenames)
	if not os.path.isdir(folder):
		raise ValueError(f"\"{folder}\" expected folder path, but got file path")
	id = 1
	for filename in filenames:
		with open(os.path.join(folder, filename), 'r') as reader:
			lines = reader.readlines()
			for i in range(len(lines)):
				if lines[i][0] == "#":
					lines[i] = ""
			# lines[i] = f"# ID = {id}\n"
			# id+=1
			writer.writelines("".join(lines))

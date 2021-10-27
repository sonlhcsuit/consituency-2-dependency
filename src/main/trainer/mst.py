import pandas as pd
import re

def mst_transpose(ip: str, op: str):
	df = pd.read_csv(ip, names=["COL_1", "COL_2", "COL_3", "COL_4", "COL_5", "COL_6", "COL_7", "COL_8", "COL_9",
								"COL_10"],
					 sep="\t", skip_blank_lines=False, doublequote=False, quotechar="'")
	df = df.fillna(-1)
	indexes = list(df[df["COL_1"] == -1].index)
	z = zip([0, *[x + 1 for x in indexes]], indexes[:-1])
	i = 0
	writer = open(op, 'w')

	for s, e in z:
		subset = df[["COL_2", "COL_4", "COL_8", "COL_7"]][s:e].T
		i += 1
		data = subset.to_string(header=False,
								index=False,
								index_names=False).split('\n')
		data = '\n'.join(map(lambda line: re.sub('\s+', "\t", line.strip()), data))
		writer.write(data.replace("\"", "\"\""))
		writer.write("\n\n")
	# writer.write("\n")
	writer.close()

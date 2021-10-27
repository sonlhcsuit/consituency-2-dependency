from src.app import merge_conllu
import os

folders = ["Dev", "Train", "Test"]
source_data = "Converted"
data_dir = os.path.join(os.getcwd(), "data")
for f in folders:
	merge_conllu(os.path.join(data_dir, source_data, f), os.path.join(data_dir,f"{f}.conllu"))

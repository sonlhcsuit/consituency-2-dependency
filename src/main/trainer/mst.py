import pandas as pd
import re
import os
from ..utils import merge_conllu
from ..sentence import ConlluSentence, ConlluFile


class MSTParser:

    def __init__(self, location: str, data_location: str) -> None:
        """
        Init an MSTParser
        :param location: path to MSTParser folder (original Java implementation) - relative to the root folder
        :param data_location: path to dependency tree (consists of 3 folder Dev, Train, Test) - relative to the root folder
        """
        self.parser = os.path.join(os.getcwd(), location)
        self.data = os.path.join(os.getcwd(), data_location)
        if not os.path.isdir(self.parser):
            raise ValueError(f"`{location}` directory is not exists")
        if not os.path.isdir(self.data):
            raise ValueError(f"`{data_location}` directory is not exists")

    def generate_train_script(self, output: str = "scripts", name: str = "train.sh"):
        self.merge_folder()
        self.process_data()
        with open(os.path.join(os.getcwd(),output,name),"w") as writer:
            pass
        pass

    def process_data(self):
        """
        Convert Conllu file-format to mst-desired format (ulab format)
        :return:
        """
        directories = os.listdir(self.data)
        directories = list(map(lambda d: os.path.join(self.data, d), directories))
        files = list(filter(lambda d: os.path.isfile(d), directories))
        for filepath in files:
            filename = os.path.basename(filepath)
            if not filename.startswith("MST"):
                conllu_file = ConlluFile(filepath)
                conllu_file.transpose_mst(os.path.join(self.data, f"MST.{filename}"))

    def merge_folder(self):
        """
        Merge all conllu file inside 1 file in the folder. File with ".conllu" prefix will be created.
        :return:
        """
        directories = os.listdir(self.data)
        for directory in directories:
            d_path = os.path.join(self.data, directory)
            if os.path.isdir(d_path):
                merge_conllu(d_path, output=str(os.path.join(self.data, f"{directory}.conllu")), skip_id=True)

# def train(filename: str):
#     MSTLocation = "MSTParser"
#     train_script = open(os.path.join(os.getcwd(), "scripts", "train.sh"), "a")
#     train_script.write(f"echo \"Training {filename}...\" >>log.txt\n")
#     train_script.write(
#         f"java -classpath \".:lib/trove.jar\" -Xmx1800m mstparser.DependencyParser train train-file:temp/{filename}.txt model-name:models/{filename}.model >/dev/null 2>>log.txt\n")
#     train_script.close()
#     pass

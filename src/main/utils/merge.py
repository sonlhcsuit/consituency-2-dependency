import os
import re


def merge_conllu(folder: str, output: str, skip_id=False) -> None:
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
    sentence_id = 1
    for filename in filenames:
        sentence_file_id = 1
        with open(os.path.join(folder, filename), 'r') as reader:
            lines = reader.readlines()
            for i in range(len(lines)):
                match = re.match("^#\sID", lines[i], re.I)
                if match:
                    if not skip_id:
                        lines[i] = f"# ID = {sentence_id} - {filename}:{sentence_file_id}\n"
                        sentence_id += 1
                        sentence_file_id += 1
                    else:
                        lines[i] = ""

            writer.write("".join(lines).strip())
            writer.write("\n")
    writer.close()

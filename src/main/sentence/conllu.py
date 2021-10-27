from typing import List
import os
import re
import numpy as np


class ConlluSentence:

    def __init__(self, lines, index):
        self.words = lines
        self.index = index
        self.size = len(lines)

    def check_dependency_order(self) -> bool:
        words = list(map(lambda x: re.sub("^[0-9]+\t.+?\t", "", x), self.words))
        text = "".join(words)
        result = re.findall("[0-9]+", text)
        result = list(map(int, result))
        m = max(result)
        return m <= self.size

    def transpose_mst(self):
        matrix = np.array([x.strip().split("\t") for x in self.words], dtype=str)
        matrix = np.delete(matrix, [0, 2, 4, 5, 8, 9], axis=1)
        matrix[:, [2, 3]] = matrix[:, [3, 2]]
        result = list(matrix.T)
        result = list(map(lambda line: " ".join(line), result))
        return "\n".join(result)

    def get_index(self):
        return self.index


class ConlluFile:
    def __init__(self, filepath: str):

        if not os.path.isfile(filepath):
            raise ValueError("An valid filepath is expected")

        reader = open(filepath)
        lines = reader.readlines()
        result = []
        start = 0
        l = 0
        i = 0
        for index, value in enumerate(lines):
            if value.startswith("#"):
                result.append(ConlluSentence(lines[start + 1:start + l], i))
                i += 1
                start = index
                l = 0
            else:
                l += 1
        result.append(ConlluSentence(lines[start + 1:start + l], i))
        result.pop(0)
        reader.close()
        self.sentences: List[ConlluSentence] = result

    def transpose_mst(self, op):
        with open(op, "w") as writer:
            for s in self.sentences:
                string = s.transpose_mst()
                writer.write(string)
                writer.write("\n\n")
        pass

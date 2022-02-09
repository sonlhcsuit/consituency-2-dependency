from nltk.tree import Tree
from typing import List


class Rule:
    def __init__(self, root_phrase: str, direction: bool, *sub_phrase):
        self.root_phrase = root_phrase
        self.direction = direction
        self.child_phrases = sub_phrase

    def __repr__(self):
        return f"{self.root_phrase}-{self.direction}-{'--'.join(self.child_phrases)}"


class HeadPercolation:
    def __init__(self, rules: List[Rule]):
        self.rules = rules
        pass

    @staticmethod
    def load(rule_fp: str):
        with open(rule_fp, "r") as reader:
            lines = reader.readlines()
            rules:List[Rule] = list(map(
                HeadPercolation.__rule_mapper,
                lines
            ))
        return HeadPercolation(*rules)
    @staticmethod
    def __rule_mapper(line: str):
        line = line.strip().replace("\"", "")
        elements = line.split(",")
        if len(elements) < 3:
            raise Exception("Rule is invalid format")
        return Rule(elements[0],
                    True if elements[1].lower() == "l" else False,
                    *elements[2:])
        pass

    def process(self, tree: Tree):
        pass

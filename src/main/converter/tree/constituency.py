from nltk.tree import Tree
from typing import List, Tuple


class ConstituencyTree:
    """
    Represent Constituent Tree
    """

    @classmethod
    def from_string(cls, string: str):
        tree = cls()
        tree.__root = Tree.fromstring(string)
        tree.__nroot = Tree.fromstring(string)

        for i,node_position in enumerate(tree.__nroot.treepositions("leaves"),start=1):
            tree.__nroot[node_position]=i

        # leaves = tree.__number_root.leaves()
        # print(leaves)
        # print(len(tree.__root))
        subs = tree.__nroot.subtrees(lambda t:t.height()>=2)
        for s in subs:
            print(s)

        phrases = tree.__nroot.treepositions()
        ps = sorted(phrases,key=lambda x:len(x))
        print(len(ps))
        for p in ps:
            print(p)
        for p in phrases:
            t = tree.__root[p]
            if isinstance(t,Tree):
                t.pretty_print()
        # pos = tree.__root.pos()
        # print(phrases,)
        return tree

    def __init__(self):
        self.__root: Tree = None
        self.__nroot: Tree = None
        self.__leaves: List[Tuple[str, str]]
        pass

    def print(self):
        pass
        # self.__root.pretty_print()

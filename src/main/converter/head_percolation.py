from nltk.tree import Tree
from typing import List
import re


class Rule:
    def __init__(self, root_phrase: str, direction: bool, *sub_phrase):
        self.root_phrase = root_phrase
        self.direction = direction
        self.child_phrases = sub_phrase
        self.index = 0

    def __repr__(self):
        return f"{self.root_phrase}-{self.direction}-{'--'.join(self.child_phrases)}"

    def __iter__(self):
        return self

    def __next__(self):
        try:
            if self.direction:
                indx = self.index
            else:
                indx = len(self.child_phrases) - 1 - self.index
            result = self.child_phrases[indx]
        except IndexError:
            raise StopIteration
        self.index += 1
        return result


class HeadPercolation:
    def __init__(self, *rules: List[Rule]):
        self.rules = {}
        for rule in rules:
            self.rules[rule.root_phrase] = rule
        pass

    @staticmethod
    def load(rule_fp: str):
        with open(rule_fp, "r") as reader:
            lines = reader.readlines()
            rules: List[Rule] = list(map(
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

    def find_head_for(self, tree):
        phrase_type = tree.label().split('-')[0]
        index_of_child = []
        pass
        head_label_result = []
        if phrase_type not in self.rules:
            for index, element in enumerate(tree):
                label_of_element = element.label()
                if rule_check("-H$", label_of_element):
                    head_index_result.append([index])
                    head_label_result.append(label_of_element)
            if head_index_result:
                return head_index_result, head_label_result
            else:
                return [[0]], [tree[0].label()]

        if phrase_type == 'RP':
            for rule in head_percolation_rules['RP']:
                if not head_index_result:
                    for index, element in zip(range(len(tree) - 1, -1, -1), reversed(tree)):
                        label_of_element = element.label()
                        if is_head(rule, label_of_element):
                            head_index_result.append([index])
                            head_label_result.append(label_of_element)
                else:
                    break

            if head_index_result:
                return head_index_result[::-1], head_label_result[::-1]
            else:
                return [[0]], [tree[0].label()]

        else:
            for rule in head_percolation_rules[phrase_type]:
                if not head_index_result:
                    for index, element in enumerate(tree):
                        label_of_element = element.label()
                        if is_head(rule, label_of_element):
                            head_index_result.append([index])
                            head_label_result.append(label_of_element)
                else:
                    break

            if head_index_result:
                return head_index_result, head_label_result
            else:
                return [[0]], [tree[0].label()]

    def normalized_tree(self, tree: Tree) -> Tree:
        normalized_tree: Tree = Tree.fromstring(str(tree))
        for index, leaf in enumerate(normalized_tree.treepositions('leaves')):
            normalized_tree[leaf] = str(index)
        return normalized_tree

    def get_subtree(self, tree: Tree, address: list) -> list:
        """
        Sử dụng hàm để tìm tất cả địa chỉ của các subtree của `tree`
        Địa chỉ là một list. Phần tử thứ `i` là thứ tự (tính từ trái sang phải) của node con ở depth (độ sâu ) là i
        Ví dụ [0 1 4 3 ] -> tính từ gốc -> con thứ nhất -> con thứ 2 -> con thứ 5 -> con thứ 3
        Lí do sử dụng cách này là bởi vì Tree sử dụng list
        """
        sub_tree = tree
        for index in address:
            sub_tree = sub_tree[index]
        return sub_tree

    def get_substree_index(self, tree: Tree, depth=1) -> list:
        """
        Dùng hàm này để lấy các node con của một node cha bất kỳ, với depth = 1, mặc định chỉ lấy các con kề trực tiếp (không lấy)
        Trả về dưới dạng list Mỗi phần tử thứ là thứ tự của con hợp lệ (hầu hết là hợp lệ)
        """
        subtrees = []
        for i in range(len(tree)):
            if type(tree[i]) != str:
                subtrees.append(i)
        return subtrees

    def get_subtree_addresses(self, tree: Tree) -> list:
        """
        Dùng thuật toán BFS để tìm tất cả các địa chỉ con của tất cả phrase con -> mục đích để xác định headword của từng phrase là từ nào
        """
        visited = []
        frontier = [[x] for x in self.get_substree_index(tree)]
        while frontier:
            node = frontier.pop(0)
            if node not in visited:
                # node is the address(list) of the subtree
                visited.append(node)
                subtree = self.get_subtree(tree, node)
                childs = self.get_substree_index(subtree)
                for child in childs:
                    frontier.append(node + [child])
        return visited

    def process(self, tree: Tree):
        sent = "(S (NP-SBJ (Nn_swsp (Nc-H Người) (Nn đàn_ông)) (VP (Ve-H có) (S-CMP (NP-SBJ (Nq nửa) (Nn-H đời) (NP (Nn-H người))) (VP (Vc-H là) (NP-CMP (Nn-H thợ_săn)))))) (VP (VP (Vv-H vờn) (Vv-H đuổi) (NP-DOB (Vv-H hổ) (ADJP (Aa-H dữ))) (PP-LOC (Cs-H trong) (NP (Nn-H rừng) (Aa rậm)))) (Cp mà) (VP (R không) (Vv-H kinh_sợ))) (PU .))"
        tree = Tree.fromstring(sent)
        norm = self.normalized_tree(tree)
        # self.find_head_for(tree)
        rs = self.get_subtree_addresses(norm)
        # rs = sorted(rs,key=lambda x: len(x),reverse=True)
        # for r in rs:
        #     print(r)
        tree.pretty_print()
        print(tree.label())


def rule_check(rule, label):
    return re.search(rule, label)

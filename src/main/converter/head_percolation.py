from nltk.tree import Tree
from typing import List
import re
from .rule import Rule


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
        pass

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


# Trả về list các phrase có chung headword
def from_phrase_to_headword(mother_tree, tree, tree_address):
    phrase_to_headword = [tree_address]
    if tree_address != 'root':
        tree = get_subtree(tree_address, mother_tree)
    P_of_C_dic = {}
    while type(tree[0]) != str:
        head_index_list, head_label_list = finding_head_of_tree(tree)

        if len(head_index_list) == 1:
            if tree_address != 'root':
                tree_address = tree_address + head_index_list[0]
            else:
                tree_address = head_index_list[0]
            tree = get_subtree(tree_address, mother_tree)
            phrase_to_headword.append(tree_address)
        else:
            address_list, label_list = deepen_head_list(mother_tree, tree_address, head_index_list)
            head_address_list = []
            for head_index in head_index_list:
                if tree_address != 'root':
                    head_address_list.append(tree_address + head_index)
                else:
                    head_address_list.append(head_index)
            result = identify_head(mother_tree, tree_address, address_list, label_list, head_address_list,
                                   head_label_list)
            tree_address = result[0]
            # print(tree_address, address_list, label_list)
            tree = get_subtree(tree_address, mother_tree)
            phrase_to_headword.append(tree_address)
            if len(result) == 2:
                P_of_C_dic.update(result[1])
    phrase_to_headword.append(tree[0])
    return [phrase_to_headword, P_of_C_dic]


# Kiểm tra có phải là trường hợp conj hay không
def is_conjunction(C_label_list):
    unique = set(C_label_list)
    if len(unique) == 3:
        if ('PU' in unique) and ((('Cp' in unique) or ('CONJP' in unique)) and (
                ('Cp' != C_label_list[0]) or ('CONJP' != C_label_list[0]))):
            return True
        elif (('PU' in unique) or ('Cp' in unique) or ('CONJP' in unique)) and has_SPL_and_S(unique):
            return True
    elif len(unique) == 2:
        if ('PU' in unique) or ((('Cp' in unique) or ('CONJP' in unique)) and (
                ('Cp' != C_label_list[0]) or ('CONJP' != C_label_list[0]))):
            return True
        elif has_SPL_and_S(unique):
            return True
    elif len(unique) == 1:
        return True
    return False


def has_same_phrase_type(C_label_list):
    phrase_type_set = set()
    for C_label in C_label_list:
        phrase_type = C_label.split('-')[0]
        phrase_type_set.add(phrase_type)
        if len(phrase_type_set) == 2:
            return False
    return True


# Dùng xác định head khi head list > 1
def identify_head(tree, P_address, C_address_list, C_label_list, head_C_address_list, head_C_label_list):
    if is_conjunction(C_label_list):
        P_of_C_dic = get_conjunction(P_address, C_address_list, C_label_list)
        return [C_address_list[0], P_of_C_dic]

    if has_same_phrase_type(head_C_label_list):
        if (('Cp' in C_label_list) or ('CONJP' in C_label_list)) and (C_label_list[0] != 'Cp') and (
                C_label_list[0] != 'CONJP'):
            P_of_C_dic = get_conjunction(P_address, C_address_list, C_label_list)
            return [C_address_list[0], P_of_C_dic]
        else:
            first_element = head_C_address_list[0]
            if first_element[-1] >= 1:
                pre_address_of_head_C_address_list = first_element[:-1] + [first_element[-1] - 1]
                pre_subtree = get_subtree(pre_address_of_head_C_address_list, tree)
                pre_subtree_label = pre_subtree.label()
                if re.search('^(Cp|CONJP)', pre_subtree_label):
                    P_of_C_dic = get_conjunction(P_address, C_address_list, C_label_list)
                    return [C_address_list[0], P_of_C_dic]

        for head_C_address, head_C_label in zip(head_C_address_list, head_C_label_list):
            if '-' not in head_C_label:
                return [head_C_address]

        for head_C_address, head_C_label in zip(head_C_address_list, head_C_label_list):
            if '-SBJ' not in head_C_label:
                return [head_C_address]
    else:
        if ('Cp' in C_label_list) and (C_label_list[0] != 'Cp'):
            P_of_C_dic = get_conjunction(P_address, C_address_list, C_label_list)
            return [C_address_list[0], P_of_C_dic]

        else:
            P_phrase_type = get_subtree(P_address, tree).label().split('-')[0]
            head_exception_rules = {
                "NP": ["^(Nn_swsp|Nn_w)(-|$)", "^(Nn|Nu|Nun|Nt)(-|$)", "^(Num|Nq|Nr)(-|$)", "^(Pd|Pp)"],
                "ADJP": ["^(Aa)"],
                "QP": ["^Nq(-|$)", "^Num(-|$)"],
                "Nn_swsp": ["^(Ncs|Nc)(-|$)"],
                "VP": ["^(Ve|Vc|D|Vcp|Vv)(-|$)"],
                "S": ["^(S|SQ|SPL)($)", "^(ADJP)"],
                "SBAR": ["^(S|SQ|SPL)($)"],
                "PP": ["^(Cs)"],
                "VP": ["^(Vv|Vc|Ve)", "^(Nq)"]  # Luật theo cây bị gán sai :))

            }
            for rule in head_exception_rules[P_phrase_type]:
                for head_C_address, head_C_label in zip(head_C_address_list, head_C_label_list):
                    if is_head(rule, head_C_label):
                        return [head_C_address]
    return "Nope"


# Do có thể xuất hiện nhiều từ giống nhau trong 1 câu
# Nên sẽ khó xác định đâu là headword của phrase
# -> Số hóa các từ trong câu: Mỗi từ sẽ biến đổi thành một số, số hóa bắt đầu từ số 1
def from_word_to_number(tree):
    for index, leafPos in enumerate(tree.treepositions('leaves')):
        tree[leafPos] = str(index + 1)
    return tree


# Khám phá tất cả các phrases.
# Sử dụng thuật toán bfs - tìm kiếm theo chiều rộng
def get_all_subtree_address(tree):
    queue = get_all_index_in_tree(tree)
    explored = []
    while queue:
        node = queue.pop(0)
        if node not in explored:
            explored.append(node)
            subtree = get_subtree(node, tree)
            index_subtree_list = get_all_index_in_tree(subtree)
            for index_subtree in index_subtree_list:
                queue.append(node + index_subtree)
    return explored


# Trả về cây con thông qua địa chỉ
def get_subtree(subtree_address, tree):
    if type(subtree_address) == str:
        if subtree_address == 'root':
            return tree
        else:
            subtree_address = str_to_list(subtree_address)
    for index in subtree_address:
        tree = tree[index]
    return tree


# Lấy tất cả index của các cây con
def get_all_index_in_tree(tree):
    result = []
    for index, subtree in enumerate(tree):
        if type(subtree) != str:
            result.append([index])
    return result


# Vì lúc tìm headword cho các address(kiểu list) của các phrase
# thì return về dic[address]=headword nhưng dic không lưu đc dạng list -> đổi list thành string
# Nên khi sài lại address thì cần chuyển đổi list(kiểu string) thành real list
def str_to_list(s):
    result = []
    num = ''
    for i in s:
        if i != '[' and i != ']':
            if i != ',':
                if i.isnumeric():
                    num += i
            else:
                result.append(int(num))
                num = ''
    result.append(int(num))
    return result


# Trong lúc đề xuất luật infer dependency label cần xét cả nhãn gốc của từ
# Nhãn gốc là node gần nhất với node lá(từ)
def get_POS_of_word(tree):
    result = {}
    for leafPos in tree.treepositions('leaves'):
        word = tree[leafPos]
        POS = tree[leafPos[:-1]].label()
        result[word] = POS
    return result


# Trả về list các vị trí(index) có thể làm head và list label ứng với list các index
def finding_head_of_tree(tree):
    # Một số thay đổi:
    # đổi VP, S ở S, và thêm PRD và VP, S ở SBAR thành VP, S, SBAr
    # THÊM sQ vào S ở S và SBAR
    # SQ: VP, Sq, S -> thêm VP và S
    # thêm SPL
    # Xem SPL như S ở S và SBAR
    # Đảo -PRD trc S|SQ|SPL của S
    # tÁCH VP thành 2 loại ở S
    head_percolation_rules = {
        "S": ["-H$", "^VP(-CMP|_|$)", "-PRD$", "^VP-", "^(S|SQ|SPL)(-|_|$)", "^ADJP(-|_|$)", "^NP(-|_|$)"],
        "SBAR": ["-H$", "^VP(-|_|$)", "^(S|SQ|SPL)(-|_|$)", "^SBAR(-|_|$)", "^ADJP(-|_|$)", "^NP(-|_|$)"],
        "SQ": ["-H$", "^VP(-|_|$)", "^QVP(-|_|$)", "^SQ(-|_|$)", "^S(-|_|$)", "^ADJP(-|_|$)", "^NP(-|_|$)",
               "^QPP(-|_|$)"],
        "NP": ["-H$", "^NP(-|_|$)", "^(Nc|Ncs|Nu|Nun|Nt|Nq|Num|Nw|Nr|Nn)(-|_|$)", "^(Pd|Pp)(-|_|$)", "^VP(-|_|$)"],
        "VP": ["-H$", "^VP(-|_|$)", "^(Ve|Vc|D|Vcp|Vv)(-|_|$)", "^(An|Aa)(-|_|$)", "^ADJP(-|_|$)",
               "^(Nc|Ncs|Nu|Nun|Nt|Nq|Num|Nw|Nr|Nn)(-|_|$)", "^NP(-|_|$)", "^SBAR(-|_|$)", "^S(-|_|$)", "^R(-|_|$)",
               "^RP(-|_|$)", "^PP(-|_|$)"],
        "ADJP": ["-H$", "^ADJP(-|_|$)", "^(An|Aa)(-|_|$)", "^(Nc|Ncs|Nu|Nun|Nt|Nq|Num|Nw|Nr|Nn)(-|_|$)", "^S(-|_|$)"],
        "RP": ["-H$", "^RP(-|_|$)", "^R(-|_|$)", "^NP(-|_|$)"],
        "PP": ["-H$", "^PP(-|_|$)", "^Cs(-|_|$)", "^VP(-|_|$)", "^SBAR(-|_|$)", "^ADJP(-|_|$)", "^QP(-|_|$)"],
        # "ADJP":["-H$","^ADJP(-|_|$)","^A(-|_|$)","^N(-|_|$)","^S(-|_|$)"],
        "QP": ["-H$", "^QP(-|_|$)", "^Nq(-|_|$)", "^Num(-|_|$)", "^Nw(-|_|$)"],
        # "XP":["-H$","^XP(-|_|$)","^X(-|_|$)"],
        # "YP":["-H$","^YP(-|_|$)","^Y(-|_|$)"],
        "MDP": ["-H$", "^MDP(-|_|$)", "^Cs(-|_|$)", "^(An|Aa)(-|_|$)", "^(Pd|Pp)(-|_|$)", "^R(-|_|$)", "^X(-|_|$)"],
        "QNP": ["-H$", "^QNP(-|_|$)", "^NP(-|_|$)", "^(Nc|Ncs|Nu|Nun|Nt|Nq|Num|Nw|Nr|Nn)(-|_|$)", "^(Pd|Pp)(-|_|$)"],
        "QADJP": ["-H$", "^QADJP(-|_|$)", "^(An|Aa)(-|_|$)", "^(Nc|Ncs|Nu|Nun|Nt|Nq|Num|Nw|Nr|Nn)(-|_|$)",
                  "^(Ve|Vc|D|Vcp|Vv)(-|_|$)", "^(Pd|Pp)(-|_|$)", "^X(-|_|$)"],
        "QRP": ["-H$", "^QRP(-|_|$)", "^(Pd|Pp)(-|_|$)", "^Cs(-|_|$)", "^X(-|_|$)"],
        "QPP": ["-H$", "^QPP(-|_|$)", "^Cs(-|_|$)", "^(Pd|Pp)(-|_|$)", "^X(-|_|$)"],
        # "QXP":["-H$","^XP(-|_|$)","^X(-|_|$)"],
        # "QVP":["-H$","^(Ve|Vc|D|Vcp|Vv)(-|_|$)"],
        "UCP": ["-H$"],
        "SPL": ["-H$", "^VP(-|_|$)", "^SPL(-|_|$)", "^ADJP(-|_|$)", "^NP(-|_|$)"]
    }

    phrase_type = tree.label().split('-')[0]

    head_index_result = []
    head_label_result = []
    if phrase_type not in head_percolation_rules:
        for index, element in enumerate(tree):
            label_of_element = element.label()
            if is_head("-H$", label_of_element):
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


# Follow theo Choi's guideline, lưu function_tag như đặc trưng phụ, được điền vào format CONLLU
# Trả về function_tag của C(node cao nhất của headword)
def get_function_tag(tree):
    headword_of_phrase = assign_headword_for_phrase(tree)[0]
    C_of_headword = get_C_of_headword(headword_of_phrase)
    function_tags_of_word = {}
    for word in tree.leaves():
        C_label = get_subtree(C_of_headword[word], tree).label()
        function_tags = C_label.split('-')
        temp = []
        for function_tag in function_tags:
            if function_tag in ['PRD', 'CMP', 'LGS', 'CMP', 'MDP', 'TMP', 'LOC', 'MNR', 'PRP', 'ADV', 'CND',
                                'CNC']:
                temp.append(function_tag)
        if temp:
            temp = '-'.join(temp)
            function_tags_of_word[word] = temp
        else:
            function_tags_of_word[word] = '_'
    return function_tags_of_word


# Trả về danh sách các nhãn gốc của từ, dùng để điền vào format CONLLU
# Lưu ý: đối vs các nhãn NULL thì không lấy 'NONE' làm nhãn gốc
# Vì NONE no-use trong việc relink khi NULL làm head ở phần hậu xử lý
def get_all_POS(tree):
    result = []
    for leafPos in tree.treepositions('leaves'):
        i = -1
        POS = tree[leafPos[:i]].label().split('-')[0]
        while POS == 'NONE':
            i = i - 1
            POS = tree[leafPos[:i]].label().split('-')[0]
        result.append(POS)
    return result


def assign_headword_for_phrase(tree):
    P_of_C_dic = {}
    headword_of_phrase = {}
    phrase_address_list = ['root'] + get_all_subtree_address(tree)
    # Tìm head
    for phrase_address in phrase_address_list:
        if str(phrase_address) not in headword_of_phrase:
            if phrase_address != 'root':
                subtree = get_subtree(phrase_address, tree)
            else:
                subtree = tree
            if type(subtree[0]) != str:
                result = from_phrase_to_headword(tree, subtree, phrase_address)
                phrase_to_headword = result[0][:-1]
                headword = result[0][-1]
                P_of_C_dic.update(result[1])
                for head_phrase_address in phrase_to_headword:
                    headword_of_phrase[str(head_phrase_address)] = headword
            else:
                headword_of_phrase[str(phrase_address)] = subtree[0]
    print(headword_of_phrase)
    for phrase_address in get_all_subtree_address(tree):
        subtree = get_subtree(phrase_address, tree)
        label = subtree.label().split('-')[0]
        if label == 'UCP':
            C_address_list = []
            C_label_list = []
            for index_subtree in range(len(subtree)):
                subtree_address = phrase_address + [index_subtree]
                subtree_label = get_subtree(subtree_address, tree).label()
                C_address_list.append(subtree_address)
                C_label_list.append(subtree_label)
            P_of_C_dic.update(get_conjunction(phrase_address, C_address_list, C_label_list))
    return [headword_of_phrase, P_of_C_dic]


def is_head(rule, label):
    return re.search(rule, label)


# Hàm dùng trong trường hợp các label trong head list khác nhau:
# Hàm lấy các label trong khoảng từ first label -> last label trong head list
# Để xét coi có Cp, UCP, CONJP
def deepen_head_list(mother_tree, tree_address, head_index_list):
    deep_address_list = []
    deep_label_list = []
    first_index = head_index_list[0][0]
    last_index = head_index_list[-1][0]
    for index in range(first_index, last_index + 1):
        if tree_address != 'root':
            subtree_address = tree_address + [index]
        else:
            subtree_address = [index]
        deep_address_list.append(subtree_address)
        deep_label_list.append(get_subtree(subtree_address, mother_tree).label())
    return deep_address_list, deep_label_list


# Dán nhãn conjunction
def get_conjunction(P_address, C_address_list, C_label_list):
    P_of_C_dic = {}
    previous_C_address = str(C_address_list[0])
    for C_address, C_label in zip(C_address_list[1:], C_label_list[1:]):
        if C_label == 'PU':
            P_of_C_dic[str(C_address)] = (previous_C_address, 'PUNCT')
        elif re.search('^(Cp|CONJP)', C_label):
            P_of_C_dic[str(C_address)] = (previous_C_address, 'CC')
        else:
            P_of_C_dic[str(C_address)] = (previous_C_address, 'CONJ')
        if (C_label != 'PU') and (C_label != 'Cp') and ('CONJP' not in C_label):
            previous_C_address = str(C_address)
    return P_of_C_dic


def get_C_of_headword(headword_of_phrase):
    duplicate_headword_of_phrase = {}
    duplicate_headword_of_phrase.update(headword_of_phrase)
    del duplicate_headword_of_phrase['root']
    merge = {}
    for key, value in sorted(duplicate_headword_of_phrase.items()):
        merge.setdefault(value, []).append(key)
    C = {}
    for headword in merge:
        C[headword] = min(merge[headword], key=len)
    return C


# Lấy P của C
def get_P_of_C(phrase_address, root_address):
    if not str_to_list(phrase_address)[:-1]:
        return root_address
    return str(str_to_list(phrase_address)[:-1])


def has_SPL_and_S(unique):
    has_S = False
    has_SPL = False
    for element in unique:
        if re.search('^S(-|$)', element):
            has_S = True
        elif re.search('^SPL(-|$)', element):
            has_SPL = True
    if has_S and has_SPL:
        return True
    else:
        return False

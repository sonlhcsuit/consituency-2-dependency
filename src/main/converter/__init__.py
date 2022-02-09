from .tree.constituency import ConstituencyTree
from .head_percolation import HeadPercolation
SAMPLE = "(S (NP-SBJ (Nn_swsp (Nc-H Người) (Nn đàn_ông)) (VP (Ve-H có) (S-CMP (NP-SBJ (Nq nửa) (Nn-H đời) (NP (Nn-H người))) (VP (Vc-H là) (NP-CMP (Nn-H thợ_săn)))))) (VP (VP (Vv-H vờn) (Vv-H đuổi) (NP-DOB (Vv-H hổ) (ADJP (Aa-H dữ))) (PP-LOC (Cs-H trong) (NP (Nn-H rừng) (Aa rậm)))) (Cp mà) (VP (R không) (Vv-H kinh_sợ))) (PU .))"


class Converter:
    def __init__(self):
        ct = ConstituencyTree.from_string(SAMPLE)
        ct.print()
        pass


    @staticmethod
    def start(**kwargs):
        rule_fp = "./head-percolation.txt"
        HeadPercolation.load(rule_fp)
        print(kwargs)

from nltk.parse import DependencyGraph, DependencyEvaluator
from .conllu import CONLLU_FILE


class Evaluator:
	@staticmethod
	def eval(gold: str, parsed: str):
		gold_conllu = CONLLU_FILE.from_file(gold)
		parsed_conllu = CONLLU_FILE.from_file(parsed)
		gold_dps = gold_conllu.to_dependency_graph()
		parsed_dps = parsed_conllu.to_dependency_graph()
		# print(len(gold_dps),len(parsed_dps))
		de = DependencyEvaluator(parsed_dps, gold_dps)
		las, uas = de.eval()
		return las, uas

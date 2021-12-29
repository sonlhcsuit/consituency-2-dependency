# deli = "#####"
# gold_p = open("temp/VnDep-v06.Test.conllu", "r")
# parsed_p = open("temp/VnDep-v06.Test.parsed.conllu", "r")
# gold_lines = gold_p.readlines()
# gold_lines = list(filter(lambda x: False if x[0] == "#" else True, gold_lines))
# parsed_lines = parsed_p.readlines()
# parsed_lines = list(filter(lambda x: False if x[0] == "#" else True, parsed_lines))
# for i, item in enumerate(gold_lines):
# 	if item == "\n":
# 		gold_lines[i] = deli
# for i, item in enumerate(parsed_lines):
# 	if item == "\n":
# 		parsed_lines[i] = deli
# gold = "".join(gold_lines).split(deli)
# parsed = "".join(parsed_lines).split(deli)
# gold_dps = []
# parsed_dps = []
# for sent in gold:
# 	gold_dps.append(DependencyGraph(sent.strip()))
# for sent in parsed:
# 	parsed_dps.append(DependencyGraph(sent.strip()))
#
# de = DependencyEvaluator(parsed_dps, gold_dps)
# las, uas = de.eval()
# print(las, uas)
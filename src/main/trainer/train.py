import os
from .mst import MSTParser
from .malt import MaltParser

class Train:
	@staticmethod
	def start(args: dict):
		parser = args.get("choice")

		if parser == "mst":
			mst = MSTParser("MSTParser", os.path.join(os.getcwd(), args.get("path")))
			mst.process_data()
			mst.generate_train_script()
			mst.generate_evaluate_script()
		elif parser == "malt":
			malt = MaltParser("MaltParser", os.path.join(os.getcwd(), args.get("path")))
			malt.gen_script()
			pass

	pass

import os
from .mst import MSTParser


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
			pass

	pass

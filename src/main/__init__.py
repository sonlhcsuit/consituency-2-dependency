import os
import re
from argparse import ArgumentParser
# from .sentence import ConlluSentence,ConlluFile
# from .trainer import *
# from .converter import *
from .utils import Util


class ApplicationCLI:

	def __init__(self):
		pass

	@staticmethod
	def __parse_args_util(parser: ArgumentParser) -> ArgumentParser:
		choices = [
			"stat",
			"merge"
		]
		parser.add_argument("choice", choices=choices, help="Data utilities. `merge` and `stat` are available.")
		parser.add_argument("-p", "--path",
							help="Path of data. Must be provied. If the util is `merge`. 3 CoNLLU file will be created based on 3 folders. If the utils is `stat`, 3 file CoNLLU will be selected for analytic. ")
		return parser

	@staticmethod
	def __parse_args():
		parser = ArgumentParser()
		subparser = parser.add_subparsers(title="Action", dest="action", help="Type of the action. train, util")
		train_parser = subparser.add_parser("action")
		util_parser = subparser.add_parser("util")
		util_parser = ApplicationCLI.__parse_args_util(util_parser)
		args = parser.parse_args()
		return vars(args)

	@staticmethod
	def __utils(args: dict):
		Util.start(args)

	def start(self):
		args = self.__parse_args()
		action = args.get("action")
		if action == "util":
			self.__utils(args)

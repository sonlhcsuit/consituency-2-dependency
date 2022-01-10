import os
import re
from .trainer import Train
from argparse import ArgumentParser
# from .sentence import ConlluSentence,ConlluFile
# from .trainer import *
# from .converter import *
from .utils import Util


class ApplicationCLI:

	def __init__(self):
		pass

	@staticmethod
	def __parse_args_parser(parser: ArgumentParser) -> ArgumentParser:
		choices = [
			"mst",
			"malt"
		]
		parser.add_argument("choice", choices=choices, help="`mst` and `malt` are available.")
		parser.add_argument("-p", "--path",
							help="Path of data. 3 CoNLLU will be selected!")
		return parser

	@staticmethod
	def __parse_args_util(parser: ArgumentParser) -> ArgumentParser:
		choices = [
			"stat",
			"merge",
			"split",
			"eval"
		]
		parser.add_argument("choice", choices=choices, help="Data utilities. `merge` and `stat` are available.")
		parser.add_argument("--gold", help="Gold data")
		parser.add_argument("--predict", help="Predicted data")
		parser.add_argument("-f", "--fold", help="K-fold number. Default is 5.")
		parser.add_argument("-s", "--sentence-length",
							help="Number of words in a sentence to be splited. If `-f` options is provided, `-s` have higher priority")
		parser.add_argument("-p", "--path",
							help="Path of data. Must be provied. If the util is `merge`. 3 CoNLLU file will be created based on 3 folders. If the utils is `stat`, 3 file CoNLLU will be selected for analytic. ")

		parser.set_defaults(fold=5)
		parser.set_defaults(sentence_length=-1)
		return parser

	@staticmethod
	def __parse_args():
		parser = ArgumentParser()
		subparser = parser.add_subparsers(title="Action", dest="action", help="Type of the action. train, util")
		train_parser = subparser.add_parser("train")
		util_parser = subparser.add_parser("util")

		train_parser = ApplicationCLI.__parse_args_parser(train_parser)
		util_parser = ApplicationCLI.__parse_args_util(util_parser)
		args = parser.parse_args()
		return vars(args)

	@staticmethod
	def __utils(args: dict):
		Util.start(args)

	@staticmethod
	def __train(args: dict):
		Train.start(args)

	def start(self):
		args = self.__parse_args()
		action = args.get("action")
		if action == "util":
			self.__utils(args)
		elif action == "train":
			self.__train(args)

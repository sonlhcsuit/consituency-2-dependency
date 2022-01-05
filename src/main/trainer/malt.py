import os


class MaltParser:

	@staticmethod
	def generate_scripts(train_fp, test_fp, script_based: str, model_name="model"):
		train_content = f"""
		<?xml version="1.0" encoding="UTF-8"?>
		<experiment>
			<optioncontainer>
				<optiongroup groupname="config">
					<option name="name" value="{model_name}"/>
					<option name="flowchart" value="learn"/>
				</optiongroup>
				<optiongroup groupname="singlemalt">
					<option name="parsing_algorithm" value="nivreeager"/>
				</optiongroup>
				<optiongroup groupname="input">
					<option name="infile" value="{train_fp}"/>
					<option name="format" value="conllu"/>
				</optiongroup>
				<optiongroup groupname="nivre">
					<option name="allow_root" value="false"/>
					<option name="allow_reduce" value="false"/>
					<option name="enforce_tree" value="true"/>
				</optiongroup>
				<optiongroup groupname="guide">
					<option name="learner" value="liblinear"/>
					<option name="kbest" value="20"/>
				</optiongroup>
				<optiongroup groupname="lib">
					<option name="verbosity" value="all"/>
				</optiongroup>
			</optioncontainer>
		</experiment>
		"""
		test_content = f"""
		<?xml version="1.0" encoding="UTF-8"?>
		<experiment>
			<optioncontainer>
				<optiongroup groupname="config">
					<option name="name" value="{model_name}"/>
					<option name="flowchart" value="parse"/>
				</optiongroup>
				<optiongroup groupname="input">
					<option name="infile" value="{test_fp}"/>
					<option name="format" value="conllu"/>
				</optiongroup>
				<optiongroup groupname="output">
					<option name="outfile" value="{test_fp}.parsed"/>
					<option name="format" value="conllu"/>
				</optiongroup>
			</optioncontainer>
		</experiment>
		"""
		with open(os.path.join(script_based, f"{model_name}.train.xml"), "w") as writer:
			writer.write(train_content.strip())
		with open(os.path.join(script_based, f"{model_name}.test.xml"), "w") as writer:
			writer.write(test_content.strip())

import os
import re
import shutil
import numpy as np
from pandas import DataFrame, read_csv
from ..trainer import MaltParser, MSTParser
from ..treebank import CONLLU_FILE, Evaluator
from .consituency import ConstituencyUtil


class Util:
    FOLDER = ("Dev", "Train", "Test")

    @staticmethod
    def start(args: dict):
        choice = args.get("choice")
        data_dir = args.get("path")
        if data_dir:
            data_dir = os.path.normpath(os.path.join(
                os.getcwd(), data_dir
            ))
            if not os.path.isdir(data_dir):
                raise Exception("`Path` of the data is not valid!")
        if choice == "stat":
            Util.__stat(data_base=data_dir)
        elif choice == "merge-d":
            Util.__merge_conllu(data_base=data_dir)
        elif choice == "merge-c":
            Util.__merge_constituent(data_base=data_dir)
        elif choice == "split":
            k_fold = args.get("fold")
            sentence_length = args.get("sentence_length")
            if sentence_length != -1:
                Util.__split_data_l(data_path=data_dir, sent_length=int(sentence_length))
            else:
                Util.__split_data_k(data_path=data_dir, k=k_fold)
        elif choice == "eval":
            # parsed = args.get("predict")
            # gold = args.get("gold")
            # if (not os.path.exists(os.path.join(os.getcwd(), parsed))) or \
            #         (not os.path.exists(os.path.join(os.getcwd(), gold))):
            #     print("Invalid `gold` filepath or `predicted` filepath")
            Util.__eval(
            #    parsed=parsed, gold=gold
            )

    @staticmethod
    def __eval(parsed: str=None, gold: str=None):
        sources = []
        for i in range(1,11):
            sources.append(
                (f"maltparser-1.9.2/data/Fold-{i}.Test.conllu.parsed",f"maltparser-1.9.2/data/Fold-{i}.Test.conllu")
            )
        # sources = [
        #     (parsed, gold)
        # ]
        for g, p in sources:
            las, uas = Evaluator.eval(
                os.path.join(os.getcwd(), g), os.path.join(os.getcwd(), p)
            )
            print(g, p)
            print("_" * 23)
            print(f"|{'LAS':<10}|{'UAS':<10}|")
            print(f"|{'-' * 21}|")
            print(f"|{las:1.8f}|{uas:1.8f}|")

    @staticmethod
    def __split_data_l(data_path: str, sent_length: int):
        files = os.listdir(data_path)
        conllu_files = []
        for file in files:
            fp = os.path.join(data_path, file)
            if os.path.isfile(fp) and 'conllu' in file.lower():
                conllu_files.append(fp)

        base_dir = os.path.join(data_path, f"sent-len-{sent_length}")
        malt_dp = os.path.join(base_dir, "malt")
        mst_dp = os.path.join(base_dir, "mst")
        malt_ddp = os.path.join(malt_dp, "data")
        mst_ddp = os.path.join(mst_dp, "data")
        shutil.rmtree(base_dir)

        os.makedirs(base_dir)
        os.makedirs(malt_dp)
        os.makedirs(malt_ddp)
        os.makedirs(mst_dp)
        os.makedirs(mst_ddp)
        for file in conllu_files:
            below = []
            above = []
            conllu = CONLLU_FILE.from_file(filepath=file)
            sizes = conllu.sents_size()
            cat = ""
            if "train" in str(file).lower():
                cat = "Train"
            elif "dev" in str(file).lower():
                cat = "Dev"
            elif "test" in str(file).lower():
                cat = "Test"
            for index, item in enumerate(sizes):
                if item >= sent_length:
                    above.append(index)
                else:
                    below.append(index)
            below_fp = os.path.join(
                malt_ddp,
                f"Below-{sent_length}.{cat}.conllu"
            )
            above_fp = os.path.join(
                malt_ddp,
                f"Above-{sent_length}.{cat}.conllu"
            )
            below_conllu = conllu.subset(below)
            above_conllu = conllu.subset(above)
            below_conllu.dump(below_fp)
            above_conllu.dump(above_fp)
            ## mst
            below_fp = os.path.join(
                mst_ddp,
                f"Below-{sent_length}.{cat}.txt"
            )
            above_fp = os.path.join(
                mst_ddp,
                f"Above-{sent_length}.{cat}.txt"
            )
            below_conllu = conllu.subset(below)
            above_conllu = conllu.subset(above)
            below_conllu.dump(below_fp, mst=True)
            above_conllu.dump(above_fp, mst=True)

        MSTParser.generate_scripts(
            script_based=mst_dp, model_name=f"Below-mst-{sent_length}",
            train_fp=os.path.join("data", f"Below-{sent_length}.Train.txt"),
            test_fp=os.path.join("data", f"Below-{sent_length}.Test.txt")
        )
        MSTParser.generate_scripts(
            script_based=mst_dp, model_name=f"Above-mst-{sent_length}",
            train_fp=os.path.join("data", f"Above-{sent_length}.Train.txt"),
            test_fp=os.path.join("data", f"Above-{sent_length}.Test.txt")
        )

        MaltParser.generate_scripts(
            script_based=malt_dp, model_name=f"Below-malt-{sent_length}",
            train_fp=os.path.join("data", f"Below-{sent_length}.Train.conllu"),
            test_fp=os.path.join("data", f"Below-{sent_length}.Test.conllu")
        )
        MaltParser.generate_scripts(
            script_based=malt_dp, model_name=f"Above-malt-{sent_length}",
            train_fp=os.path.join("data", f"Above-{sent_length}.Train.conllu"),
            test_fp=os.path.join("data", f"Above-{sent_length}.Test.conllu")
        )

    @staticmethod
    def __split_data_k(data_path: str, k: int):
        files = os.listdir(data_path)

        conllu_files = []
        for file in files:
            fp = os.path.join(data_path, file)
            if os.path.isfile(fp) and 'conllu' in file.lower():
                conllu_files.append(fp)
        final = CONLLU_FILE("")
        for c in conllu_files:
            c_file = CONLLU_FILE.from_file(c)
            final = final.append(c_file)
        index = np.arange(final.get_size())
        np.random.shuffle(index)
        index = list(index)
        test_size = int(final.get_size() / k)
        segments = []
        for i in range(k):
            if i == 0:
                segments.append(index[:test_size])
            elif i == k - 1:
                segments.append(index[test_size * i:])
            else:
                segments.append(index[test_size * i:test_size * (i + 1)])
        folds = []
        for i in range(k):
            test = segments[i]
            train = []
            for j in range(k):
                if j == i:
                    continue
                train = train + segments[j]
            folds.append([
                test, train
            ])
        base_dir = os.path.join(data_path, f"fold-{k}")
        malt_dp = os.path.join(data_path, f"fold-{k}", "malt")
        mst_dp = os.path.join(data_path, f"fold-{k}", "mst")
        malt_ddp = os.path.join(data_path, f"fold-{k}", "malt", "data", )
        mst_ddp = os.path.join(data_path, f"fold-{k}", "mst", "data", )
        try:
            shutil.rmtree(base_dir)
        except Exception:
            pass
        os.makedirs(base_dir, exist_ok=True)
        os.makedirs(malt_dp, exist_ok=True)
        os.makedirs(mst_dp, exist_ok=True)
        os.makedirs(os.path.join(mst_dp,"models"), exist_ok=True)
        os.makedirs(malt_ddp, exist_ok=True)
        os.makedirs(mst_ddp, exist_ok=True)

        i = 1
        for test_fold_index, train_fold_index in folds:
            test_fold = final.subset(test_fold_index)
            train_fold = final.subset(train_fold_index)
            # Malt Parser preprocess
            test_fold_fp = os.path.join(malt_ddp, f"Fold-{i}.Test.conllu")
            train_fold_fp = os.path.join(malt_ddp, f"Fold-{i}.Train.conllu")
            test_fold.dump(test_fold_fp)
            train_fold.dump(train_fold_fp)
            MaltParser.generate_scripts(
                script_based=malt_dp, model_name=f"Fold-{i}",
                train_fp=os.path.join("data", f"Fold-{i}.Train.conllu"),
                test_fp=os.path.join("data", f"Fold-{i}.Test.conllu")
            )
            f = open(os.path.join(malt_dp,"run.sh"),"a")
            f.write(f"java -jar maltparser-1.9.2.jar -f Fold-{i}.train.xml\n")
            f.write(f"java -jar maltparser-1.9.2.jar -f Fold-{i}.test.xml\n\n")
            f.close()
            # MST Parser preprocess
            test_fold_fp = os.path.join(mst_dp, "data", f"MST.Fold-{i}.Test.txt")
            train_fold_fp = os.path.join(mst_dp, "data", f"MST.Fold-{i}.Train.txt")
            test_fold.dump(test_fold_fp, mst=True)
            train_fold.dump(train_fold_fp, mst=True)
            MSTParser.generate_scripts(
                script_based=mst_dp, model_name=f"Fold-{i}",
                train_fp=os.path.join("data", f"MST.Fold-{i}.Train.txt"),
                test_fp=os.path.join("data", f"MST.Fold-{i}.Test.txt")
            )
            f = open(os.path.join(mst_dp, "run.sh"), "a")
            f.write(f". Fold-{i}.train.sh\n")
            f.write(f". Fold-{i}.test.sh\n")
            f.close()
            i += 1

    @staticmethod
    def __merge_conllu(data_base: str, skip_id=False) -> None:
        """
        :param folder: data folder, must include `Dev`, `Train`, `Test` folders
        :param output:
        :return:
        """
        for data_folder in Util.FOLDER:
            base = os.path.basename(data_base)
            data_filedir = os.path.join(data_base, data_folder)
            merged_filename = os.path.join(data_base, f"{base}.{data_folder}.conllu")
            with open(merged_filename, "w") as writer:
                files = os.listdir(data_filedir)
                files = sorted(files)
                sentence_id = 1
                for file in files:
                    sentence_file_id = 1
                    with open(os.path.join(data_filedir, file), 'r') as reader:
                        lines = reader.readlines()
                        for i in range(len(lines)):
                            match = re.match("^#\sID", lines[i], re.I)
                            if match:
                                if not skip_id:
                                    lines[i] = f"# ID = {sentence_id} - {file}:{sentence_file_id}\n"
                                    sentence_id += 1
                                    sentence_file_id += 1
                                else:
                                    lines[i] = ""
                        writer.write("".join(lines).strip())
                        writer.write("\n\n")

    @staticmethod
    def __merge_constituent(data_base, skip_id=False):
        for data_folder in Util.FOLDER:
            base = os.path.basename(data_base)
            data_filedir = os.path.join(data_base, data_folder)
            merged_filename = os.path.join(data_base, f"{base}.{data_folder}.prd")
            with open(merged_filename, "w") as writer:
                files = os.listdir(data_filedir)
                files = sorted(files)
                sentence_id = 1
                for file in files:
                    sentence_file_id = 1
                    if not file.endswith(".prd"):
                        continue
                    with open(os.path.join(data_filedir, file), 'r') as reader:
                        lines = reader.readlines()
                        sentences = lines
                        # sentences = re.findall("(?<=<s>).*?(?=</s>)",lines,flags=re.I|re.DOTALL)
                        for index, sent in enumerate(sentences):
                            writer.write(re.sub("[\s]+", " ", sent).strip())
                            writer.write("\n")
                # 	break
                # break
                # 	# break
                # 	# for i in range(len(lines)):
                # 	# 	match = re.match("^#\sID", lines[i], re.I)
                # 	# 	if match:
                # 	# 		if not skip_id:
                # 	# 			lines[i] = f"# ID = {sentence_id} - {file}:{sentence_file_id}\n"
                # 	# 			sentence_id += 1
                # 	# 			sentence_file_id += 1
                # 	# 		else:
                # 	# 			lines[i] = ""
                # 	#writer.write("".join(lines).strip())
                # 	#writer.write("\n\n")

    @staticmethod
    def __stat(data_base: str):
        ConstituencyUtil.analyze_coord(base=data_base)
    # token_lists = {}
    # token_type = []
    # dataframes = []
    # for data_folder in Util.FOLDER:
    # 	base = os.path.basename(data_base)
    # 	merged_filename = os.path.join(data_base, f"{base}.{data_folder}.conllu")
    # 	df = read_csv(merged_filename, names=list('abcdefghij'), delimiter="\t", skip_blank_lines=True,
    # 				  quoting=3,
    # 				  header=None, encoding="utf-8")
    # 	df.dropna(inplace=True)
    # 	dataframes.append(df)
    # 	tokens = np.array(list(df['h']))
    # 	token_lists.setdefault(data_folder, tokens)
    # 	token_type = list(set(token_type + list(df['h'])))
    # d = {}
    # for data_folder in Util.FOLDER:
    # 	tokens = token_lists.get(data_folder)
    # 	unique, counts = np.unique(tokens, return_counts=True)
    # 	stats = dict(zip(unique, counts))
    # 	d.setdefault(data_folder, stats)
    # df = DataFrame(d)
    # df.to_csv(path_or_buf="stat.csv")
    # for data_folder in Util.FOLDER:
    # 	df[data_folder] = df[data_folder] / len(token_lists.get(data_folder))
    # df.to_csv(path_or_buf="stat.percent.csv")

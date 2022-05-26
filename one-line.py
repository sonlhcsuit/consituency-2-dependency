import os
import re
from nltk.tree import Tree
base = os.path.join(os.getcwd(),"data","VLSP")
base_oneline = os.path.join(os.getcwd(),"data","VLSP-oneline")
files = os.listdir(base)
files = list(filter(lambda x: True if 'prd' in x.lower() else False ,files) )
files = sorted(files)
for file in files:
    fr = open(os.path.join(base,file),"r")
    fw = open(os.path.join(base_oneline,f"oneline-{file}"),"w")
    content = fr.read()
    matches = re.findall("(?<=<s>).+?(?=<\/s>)", content, flags=re.M | re.I | re.S)
    for match in matches:
        fw.write(re.sub("\s+"," ",match).strip())
        fw.write("\n")
    fr.close()
    fw.close()

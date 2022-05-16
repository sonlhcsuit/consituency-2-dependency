import re
from typing import List
from src.main.menu import ApplicationCLI
if __name__ == '__main__':
    app = ApplicationCLI()
    app.start()
    pass
#  import os.path
#  import re
#  fp = "data/Original/NIIVTB-1/NIIVTB-1.Train.prd"
#  def check_null_tag(line:str):
#      return re.search("\*",line)

#  with open( os.path.join(os.getcwd(),fp), "r") as reader:
#      lines = reader.readlines()
#      with open("sample.txt", "w") as writer:

#      for index, line in enumerate(lines):
#          match = check_null_tag(line)
#          if match:
#              writer.write(line)

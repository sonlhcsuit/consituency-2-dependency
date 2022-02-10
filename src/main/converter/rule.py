class Rule:
    def __init__(self, root_phrase: str, direction: bool, *sub_phrase):
        self.root_phrase = root_phrase
        self.direction = direction
        self.child_phrases = sub_phrase
        self.index = 0

    def __repr__(self):
        return f"{self.root_phrase}-{self.direction}-{'--'.join(self.child_phrases)}"

    def __iter__(self):
        return self

    def __next__(self):
        try:
            if self.direction:
                indx = self.index
            else:
                indx = len(self.child_phrases) - 1 - self.index
            result = self.child_phrases[indx]
        except IndexError:
            raise StopIteration
        self.index += 1
        return result


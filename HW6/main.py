import string
'''
Accepts the string 'message' and shifts all of the alphabetical characters in
it 'shift' steps in the alphabet. If no value is given for shift the value
defaults to 13.
'''
def encode(*args):
    message = args[0]
    if len(args) > 1:
        shift = args[1]
    else:
        shift = 13

    alphabet = ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o",
                "p","q","r","s","t","u","v","w","x","y","z"]
    shiftAlphabet = {}
    for i in range(0, len(alphabet)):
        shiftAlphabet[alphabet[i]] = alphabet[(i + shift) % len(alphabet)]

    output = ""
    for x in message.lower():
        if x in shiftAlphabet:
            x = shiftAlphabet[x]
        output += x
    return output

'''
tryShifts attempts to decode the string 'message' by finding the most used
letter in the encoded message and by attempting to decrypt it based on the
frequency of letters used in the english alphabet.
'''
def tryShifts(message):
    letterTable = ["e","t","a","o","i","n","s","h","r","d","l","u","c","m","f",
                    "w","y","p","v","b","g","k","q","j","x","z"]
    alphabet = ["a","b","c","d","e","f","g","h","i","j","k","l","m","n","o",
                "p","q","r","s","t","u","v","w","x","y","z"]
    frequencies = {}
    for i in message.lower():
        if i.isalpha():
            if i in frequencies:
                frequencies[i] = frequencies[i] + 1
            else:
                frequencies[i] = 1
    mostFreq = sorted(frequencies.iteritems(), key=lambda (k,v): (v,k))[-1]
    mostFreq = mostFreq[0]
    output = []
    mostFreqLoc = -1
    replaceLoc = -1
    for i in range(0,len(letterTable)):
        for j in range(0,len(alphabet)):
            if alphabet[j] == mostFreq:
                mostFreqLoc = j
            if alphabet[j] == letterTable[i]:
                replaceLoc = j
            if replaceLoc >= 0 and mostFreqLoc >= 0:
                if mostFreqLoc > replaceLoc:
                    output.append(encode(message,(mostFreqLoc-replaceLoc)))
                    replaceLoc = -1
                    mostFreqLoc = -1
                elif mostFreqLoc < replaceLoc:
                    output.append(encode(message,-(replaceLoc-mostFreqLoc)))
                    replaceLoc = -1
                    mostFreqLoc = -1
                else:
                    output.append(encode(message,0))
                    replaceLoc = -1
                    mostFreqLoc = -1
    return output.__iter__()

'''
The Trie class is a prefix-tree data structure which helps the program
quickly accept or reject potential words for decoding based on it's common
prefixes list which it builds from a dictionary file on creation.
'''
class Trie:
    def __init__(self):
        self.root = self.Node(None,None) #generate root Node (empty, no parent)
        self.commonPrefixes = []

#    @property
#    def commonPrefixes(self):
#        if len(self.commonPrefixes) > 0:
#            currentCounts = [0,0,0,0,0,0,0,0,0,0]
#            node = root
#            for i in currentCounts:
#                (resume,d,c) = node.fetch(node)
#                if c > currentCounts[i]:
#                    commonPrefixes[i] = d
#                    currentCounts[i] = c
#                    node = resume
#        return self.commonPrefixes

    def __contains__(self,data):
        if self.root.find(data,1) == None:
            return False
        else:
            return True

    def add(self, data):
        self.root.add(data,self.root)

    def find(self, data):
        return self.root.find(data,1)

    class Node:
        def __init__(self, data, parent):
            self.children = []
            self.parent = parent
            if self.parent != None:
                self.depth = self.parent.depth + 1
                self.data = self.parent.data + data
            else:
                self.depth = 0 #only applicable to the root node
                self.data = ""
            if self.depth > 2: #only care about prefixes with length > 2
                self.count = 1
            else:
                self.count = None


        def add(self, data, parent):
            if data != "":
                for i in range(0,len(self.children)):
                    if (parent.data + data[0]) == self.children[i].data:
                        child = self.children[i]
                        if child.count != None:
                            child.count += 1
                        child.add(data[1:], child)
                        return True
                child = newNode(data[0],parent)
                parent.children.append(child)
                child.add(data[1:],child)

        def find(self, data, i):
            node = None
            for k in range(0,len(self.children)):
                if data[0:i] == self.children[k].data:
                    node = self.children[k]
                    if i == len(data):
                        return node.data
                    else:
                        node = node.find(data,i+1)
            return node

        def fetch(self, caller):
            if self.count == None:
                for i in self.children:
                    children[i].fetch(children[i])
            if self.count != None:
                return (caller,self.data,self.count)

def newNode(data,parent):
    return Trie.Node(data,parent)

'''
This function attempts to decode the string 'message' by utilizing the
tryShifts function above. After calculating the potential decodings it checks
the validity of the words in the output message against a Trie built from
'dictfilename'. If over 50% of the words in the output message are deemed to
be real words then that decoded message and Trie are returned as a tuple.
'''
def decode(message, dictfilename):
    t = Trie()
    infile = open(dictfilename)
    [t.add(w.strip()) for w in infile]
    iterator = tryShifts(message)
    for i in iterator:
        words = str.split(i)
        total = len(words)
        current = 0
        for j in words:
            word = j.translate(string.maketrans("",""), string.punctuation)
            if word in t:
                current += 1
            if float(current)/float(total) >= float(0.5):
                return i, t
    return (None,t)
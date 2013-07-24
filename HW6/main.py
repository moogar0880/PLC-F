import string
'''
Accepts the string 'message' and shifts all of the alphabetical characters in
it 'shift' steps in the alphabet. If no value is given for shift the value
defaults to 13.
'''
def encode(message,shift=13):
    alphabet = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
                'p','q','r','s','t','u','v','w','x','y','z']
    shiftAlphabet = {}
    for i in range(len(alphabet)):
        shiftAlphabet[alphabet[i]] = alphabet[(i + shift) % len(alphabet)]
    output = ''
    for x in message.lower():
        if x in shiftAlphabet:
            output += shiftAlphabet[x]
        else:
            output += x
    return output

'''
tryShifts attempts to decode the string 'message' by finding the most used
letter in the encoded message and decrypting it based on the frequency of
letters used in the english alphabet.
'''
def tryShifts(message):
    letterTable = ['e','t','a','o','i','n','s','h','r','d','l','u','c','m','f',
                   'w','y','p','v','b','g','k','q','j','x','z']
    alphabet    = ['a','b','c','d','e','f','g','h','i','j','k','l','m','n','o',
                   'p','q','r','s','t','u','v','w','x','y','z']
    frequencies = {}
    for i in message.lower():
        if i.isalpha():
            if i in frequencies:
                frequencies[i] = frequencies[i] + 1
            else:
                frequencies[i] = 1
    mostFreq = sorted(frequencies.iteritems(), key=lambda (k,v): (v,k))[-1][0]
    output   = []
    mostFreqLoc = alphabet.index(mostFreq)
    for i in range(len(letterTable)):
        if mostFreqLoc > i:
            output.append(encode(message,(mostFreqLoc-i)))
        elif mostFreqLoc < i:
            output.append(encode(message,-(i-mostFreqLoc)))
        else:
            output.append(encode(message,0))
    return output.__iter__()

'''
The Trie class is a prefix-tree data structure which helps the program
quickly accept or reject potential words for decoding based on it's common
prefixes list which it builds from a dictionary file on creation.
'''
class Trie:
    #0 arg constructor
    def __init__(self):
        self.root = {} #generate root of Trie
        self.commonPrefixes = []

    #Contains method for 'in' operator use
    def __contains__(self,word):
        return self.find(word) != None

    #Adds word into Trie
    def add(self,word):
        cur = self.root
        for letter in word:
            if letter not in cur:
                cur[letter] = ([0],{})
                cur = cur[letter][1]
            else:
                cur = cur[letter][1]

    #Find word in Trie, if word not found None is returned
    def find(self,word):
        cur = self.root
        for i,letter in enumerate(word):
            if letter not in cur:
                return None
            elif i == len(word) - 1:
                break
            else:
                cur = cur[letter][1]
        return word

    #Update count of 'word' if word is in Trie
    def update(self,word):
        cur = self.root
        for i, letter in enumerate(word):
            if letter not in cur:
                break
            elif i == len(word) - 1:
                pass
            else:
                cur = cur[letter][1]
        cur[word[len(word)-1]][0][0] += 1

    #Return count of word in Trie if word exists in Trie
    def count(self,word):
        cur = self.root
        for i, letter in enumerate(word):
            if letter not in cur:
                return 0
            elif i == len(word) - 1:
                pass
            else:
                cur = cur[letter][1]
        return cur[word[len(word)-1]][0][0]

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
    total = len(message.split(' '))
    for i in tryShifts(message):
        words = str.split(i)
        current = 0
        for j in words:
            word = j.translate(string.maketrans('',''), string.punctuation)
            if word in t:
                current += 1
                t.update(word)
        if float(current)/float(total) >= float(0.5):
            return i, t
    return (None,t)
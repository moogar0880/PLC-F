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
letter in the encoded message and by attempting to decrypt it based on the
frequency of letters used in the english alphabet.
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
    def __init__(self):
        self.root = {} #generate root of Trie
        self.commonPrefixes = []

    def __contains__(self,word):
        return self.find(word) != None

    def add(self,word):
        cur = self.root
        for letter in word:
            if letter not in cur:
                cur[letter] = ([0],{})
                cur = cur[letter][1]
            else:
                cur = cur[letter][1]

    #Recursive searcher
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

    #Update word count
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
        #print i
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
'''
dictfile = "dictionary.txt"
v_entries = " ".join([w.strip() for w in open(dictfile).readlines() if w.strip()[0] == "v"])
v_entries_encoded = "i inpnapvrf inpnapl inpnag inpnagyl inpngr inpngrq inpngrf inpngvat inpngvba inpngvbarq inpngvbare inpngvbaref inpngvbavat inpngvbaf inppvangr inppvangrq inppvangrf inppvangvat inppvangvba inppvangvbaf inppvar inppvarf inpvyyngr inpvyyngrq inpvyyngrf inpvyyngvat inpvyyngvba inpvyyngvbaf inphn inphvgl inphbhf inphbhfyl inphhz inphhzrq inphhzvat inphhzf intnobaq intnobaqrq intnobaqvat intnobaqf intnevrf intnel intvan intvanr intvany intvanf intenapl intenag intenagf inthr inthryl inthrarff inthre inthrfg inva invare invarfg invatybevbhf invatybel invayl inynapr inynaprf inyr inyrqvpgbevna inyrqvpgbevnaf inyrqvpgbevrf inyrqvpgbel inyrapr inyraprf inyragvar inyragvarf inyrf inyrg inyrgrq inyrgvat inyrgf inyvnag inyvnagyl inyvq inyvqngr inyvqngrq inyvqngrf inyvqngvat inyvqngvba inyvqngvbaf inyvqvgl inyvqyl inyvqarff inyvfr inyvfrf inyyrl inyyrlf inybe inybebhf inyhnoyr inyhnoyrf inyhngvba inyhngvbaf inyhr inyhrq inyhryrff inyhrf inyhvat inyir inyirq inyirf inyivat inzbbfr inzbbfrq inzbbfrf inzbbfvat inzc inzcrq inzcvat inzcver inzcverf inzcf ina inanqvhz inaqny inaqnyvfz inaqnyvmr inaqnyvmrq inaqnyvmrf inaqnyvmvat inaqnyf inar inarf inathneq inathneqf inavyyn inavyynf inavfu inavfurq inavfurf inavfuvat inavfuvatf inavgvrf inavgl inaarq inaavat inadhvfu inadhvfurq inadhvfurf inadhvfuvat inaf inagntr inagntrf incvq incvqvgl incvqarff incbe incbevmngvba incbevmr incbevmrq incbevmre incbevmref incbevmrf incbevmvat incbebhf incbef inevnovyvgl inevnoyr inevnoyrf inevnoyl inevnapr inevnaprf inevnag inevnagf inevngr inevngvba inevngvbaf inevpbyberq inevpbfr inevrq inevrtngr inevrtngrq inevrtngrf inevrtngvat inevrf inevrgvrf inevrgl inevbhf inevbhfyl ineyrg ineyrgf inezvag inezvagf ineavfu ineavfurq ineavfurf ineavfuvat inefvgvrf inefvgl inel inelvat infphyne infr infrpgbzvrf infrpgbzl infrf inffny inffnyntr inffnyf infg infgre infgrfg infgyl infgarff infgf ing ingf inggrq inggvat inhqrivyyr inhyg inhygrq inhygre inhygref inhygvat inhygf inhag inhagrq inhagvat inhagf irny irpgbe irpgberq irpgbevat irpgbef irrc irrcf irre irrerq irrevat irref irtna irtnaf irtrgnoyr irtrgnoyrf irtrgnevna irtrgnevnavfz irtrgnevnaf irtrgngr irtrgngrq irtrgngrf irtrgngvat irtrgngvba irtrgngvir irttvr irttvrf irurzrapr irurzrag irurzragyl iruvpyr iruvpyrf iruvphyne irvy irvyrq irvyvat irvyf irva irvarq irvavat irvaf iryq iryqf iryqg iryqgf iryyhz irybpvgvrf irybpvgl irybhe irybhef iryirg iryirgrra iryirgvre iryirgvrfg iryirgl irany iranyvgl iranyyl iraq iraqrq iraqre iraqref iraqrggn iraqrggnf iraqvat iraqbe iraqbef iraqf irarre irarrerq irarrevat irarref irarenoyr irarengr irarengrq irarengrf irarengvat irarengvba irarerny iratrnapr iratrshy iratrshyyl iravny iravfba irabz irabzbhf irabzbhfyl irabhf irag iragrq iragvyngr iragvyngrq iragvyngrf iragvyngvat iragvyngvba iragvyngbe iragvyngbef iragvat irageny iragevpyr iragevpyrf iragevphyne iragevybdhvfz iragevybdhvfg iragevybdhvfgf iragf iragher iragherq iragherf iragherfbzr iraghevat iraghebhf irahr irahrf irenpvbhf irenpvgl irenaqn irenaqnu irenaqnuf irenaqnf ireo ireony ireonyvmr ireonyvmrq ireonyvmrf ireonyvmvat ireonyyl ireonyf ireongvz ireoran ireoranf ireovntr ireobfr ireobfvgl ireof ireqnag ireqvpg ireqvpgf ireqvtevf ireqvtevfrq ireqvtevfrf ireqvtevfvat ireqher iretr iretrq iretrf iretvat irevre irevrfg irevsvnoyr irevsvpngvba irevsvrq irevsvrf irevsl irevslvat irevyl irevfvzvyvghqr irevgnoyr irevgnoyl irevgvrf irevgl irezvpryyv irezvyvba irezvyyvba irezva irezvabhf irezbhgu ireanphyne ireanphynef ireany irefngvyr irefngvyvgl irefr irefrq irefrf irefvsvpngvba irefvsvrq irefvsvrf irefvsl irefvslvat irefvat irefvba irefvbaf irefhf iregroen iregroenr iregroeny iregroenf iregroengr iregroengrf iregrk iregrkrf iregvpny iregvpnyyl iregvpnyf iregvprf iregvtvabhf iregvtb ireir irel irfvpyr irfvpyrf irfcre irfcref irffry irffryf irfg irfgrq irfgvohyr irfgvohyrf irfgvtr irfgvtrf irfgvtvny irfgvat irfgzrag irfgzragf irfgevrf irfgel irfgf irg irgpu irgpurf irgrena irgrenaf irgrevanevna irgrevanevnaf irgrevanevrf irgrevanel irgb irgbrq irgbrf irgbvat irgf irggrq irggvat irk irkngvba irkngvbaf irkngvbhf irkrq irkrf irkvat ivn ivnovyvgl ivnoyr ivnqhpg ivnqhpgf ivny ivnyf ivnaq ivnaqf ivor ivorf ivoenapl ivoenag ivoenagyl ivoencubar ivoencubarf ivoengr ivoengrq ivoengrf ivoengvat ivoengvba ivoengvbaf ivoengb ivoengbe ivoengbef ivoengbf ivoheahz ivoheahzf ivpne ivpnentr ivpnentrf ivpnevbhf ivpnevbhfyl ivpnef ivpr ivprq ivprebl ivpreblf ivprf ivpulffbvfr ivpvat ivpvavgl ivpvbhf ivpvbhfyl ivpvbhfarff ivpvffvghqr ivpvffvghqrf ivpgvz ivpgvzvmngvba ivpgvzvmr ivpgvzvmrq ivpgvzvmrf ivpgvzvmvat ivpgvzf ivpgbe ivpgbevrf ivpgbevbhf ivpgbevbhfyl ivpgbef ivpgbel ivpghny ivpghnyrq ivpghnyvat ivpghnyyrq ivpghnyyvat ivpghnyf ivqrb ivqrbpnffrggr ivqrbpnffrggrf ivqrbqvfp ivqrbqvfpf ivqrbf ivqrbgncr ivqrbgncrq ivqrbgncrf ivqrbgncvat ivr ivrq ivrf ivrj ivrjrq ivrjre ivrjref ivrjsvaqre ivrjsvaqref ivrjvat ivrjvatf ivrjcbvag ivrjcbvagf ivrjf ivtvy ivtvynapr ivtvynag ivtvynagr ivtvynagrf ivtvynagvfz ivtvynagyl ivtvyf ivtarggr ivtarggrq ivtarggrf ivtarggvat ivtbe ivtbebhf ivtbebhfyl ivyr ivyryl ivyrarff ivyre ivyrfg ivyvsvpngvba ivyvsvrq ivyvsvrf ivyvsl ivyvslvat ivyyn ivyyntr ivyyntre ivyyntref ivyyntrf ivyynva ivyynvavrf ivyynvabhf ivyynvaf ivyynval ivyynf ivyyrva ivyyrvaf ivz ivanvterggr ivaqvpngr ivaqvpngrq ivaqvpngrf ivaqvpngvat ivaqvpngvba ivaqvpngvbaf ivaqvpngbe ivaqvpngbef ivaqvpgvir ivaqvpgviryl ivaqvpgvirarff ivar ivartne ivartnel ivarf ivarlneq ivarlneqf ivagntr ivagntrf ivagare ivagaref ivaly ivalyf ivby ivbyn ivbynoyr ivbynf ivbyngr ivbyngrq ivbyngrf ivbyngvat ivbyngvba ivbyngvbaf ivbyngbe ivbyngbef ivbyrapr ivbyrag ivbyragyl ivbyrg ivbyrgf ivbyva ivbyvavfg ivbyvavfgf ivbyvaf ivbyvfg ivbyvfgf ivbybapryyb ivbybapryybf ivbyf ivcre ivcref iventb iventbrf iventbf iveny iverb iverbf ivetva ivetvany ivetvanyf ivetvavgl ivetvaf ivethyr ivethyrf ivevyr ivevyvgl ivebybtl iveghny iveghnyyl iveghr iveghrf iveghbfv iveghbfvgl iveghbfb iveghbfbf iveghbhf iveghbhfyl iveghbhfarff ivehyrapr ivehyrag ivehyragyl ivehf ivehfrf ivfn ivfnrq ivfntr ivfntrf ivfnvat ivfnf ivfpren ivfpreny ivfpvq ivfpbfvgl ivfpbhag ivfpbhagrff ivfpbhagrffrf ivfpbhagf ivfpbhf ivfphf ivfr ivfrq ivfrf ivfvovyvgl ivfvoyr ivfvoyl ivfvat ivfvba ivfvbanevrf ivfvbanel ivfvbarq ivfvbavat ivfvbaf ivfvg ivfvgngvba ivfvgngvbaf ivfvgrq ivfvgvat ivfvgbe ivfvgbef ivfvgf ivfbe ivfbef ivfgn ivfgnf ivfhny ivfhnyvmngvba ivfhnyvmr ivfhnyvmrq ivfhnyvmrf ivfhnyvmvat ivfhnyyl ivfhnyf ivgny ivgnyvgl ivgnyvmr ivgnyvmrq ivgnyvmrf ivgnyvmvat ivgnyyl ivgnyf ivgnzva ivgnzvaf ivgvngr ivgvngrq ivgvngrf ivgvngvat ivgvngvba ivgvphygher ivgerbhf ivgevby ivgevbyvp ivghcrengr ivghcrengrq ivghcrengrf ivghcrengvat ivghcrengvba ivghcrengvir ivin ivinpr ivinpvbhf ivinpvbhfyl ivinpvbhfarff ivinpvgl ivinf ivivq ivivqre ivivqrfg ivivqyl ivivqarff ivivsvrq ivivsvrf ivivsl ivivslvat ivivcnebhf ivivfrpgvba ivkra ivkravfu ivkraf ivmvre ivmvref ivmbe ivmbef ibpnohynevrf ibpnohynel ibpny ibpnyvp ibpnyvfg ibpnyvfgf ibpnyvmngvba ibpnyvmngvbaf ibpnyvmr ibpnyvmrq ibpnyvmrf ibpnyvmvat ibpnyyl ibpnyf ibpngvba ibpngvbany ibpngvbaf ibpngvir ibpngvirf ibpvsrengr ibpvsrengrq ibpvsrengrf ibpvsrengvat ibpvsrengvba ibpvsrebhf ibpvsrebhfyl ibqxn ibthr ibthrf ibthvfu ibvpr ibvprq ibvpryrff ibvprf ibvpvat ibvq ibvqrq ibvqvat ibvqf ibvyr ibyngvyr ibyngvyvgl ibypnavp ibypnab ibypnabrf ibypnabf ibyr ibyrf ibyvgvba ibyyrl ibyyrlonyy ibyyrlonyyf ibyyrlrq ibyyrlvat ibyyrlf ibyg ibygntr ibygntrf ibygnvp ibygzrgre ibygzrgref ibygf ibyhovyvgl ibyhoyr ibyhoyl ibyhzr ibyhzrf ibyhzvabhf ibyhzvabhfyl ibyhagnevrf ibyhagnevyl ibyhagnel ibyhagrre ibyhagrrerq ibyhagrrevat ibyhagrref ibyhcghnevrf ibyhcghnel ibyhcghbhf ibyhcghbhfyl ibyhcghbhfarff ibzvg ibzvgrq ibzvgvat ibzvgf ibbqbb ibbqbbrq ibbqbbvat ibbqbbvfz ibbqbbf ibenpvbhf ibenpvbhfyl ibenpvgl ibegrk ibegrkrf ibegvprf ibgnevrf ibgnel ibgr ibgrq ibgre ibgref ibgrf ibgvat ibgvir ibhpu ibhpurq ibhpure ibhpuref ibhpurf ibhpuvat ibhpufnsr ibhpufnsrq ibhpufnsrf ibhpufnsvat ibj ibjrq ibjry ibjryf ibjvat ibjf iblntr iblntrq iblntre iblntref iblntrf iblntvat iblrhe iblrhevfz iblrhevfgvp iblrhef if ihypnavmngvba ihypnavmr ihypnavmrq ihypnavmrf ihypnavmvat ihytne ihytnere ihytnerfg ihytnevfz ihytnevfzf ihytnevgvrf ihytnevgl ihytnevmngvba ihytnevmr ihytnevmrq ihytnevmrf ihytnevmvat ihytneyl ihyarenovyvgvrf ihyarenovyvgl ihyarenoyr ihyarenoyl ihygher ihygherf ihyin ihyinr ihyinf ilvat"
def testLongMultiShift():
    msg = v_entries_encoded
    res = v_entries
    print res.split(' ')
    r, t = decode(msg, dictfile)
testLongMultiShift()'''
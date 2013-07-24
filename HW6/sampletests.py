import unittest
from timeout import timeout
from time import time
import main

class TestEncode(unittest.TestCase):

    def testOne(self):
        msg = sidewalk
        coded = main.encode(msg, 1)
        test = "uifsf jt b qmbdf xifsf uif tjefxbml foet"
        self.assertEqual(coded[:len(test)], test)

    def testDefault(self):
        msg = sidewalk
        coded = main.encode(msg)
        test = "gurer vf n cynpr jurer gur fvqrjnyx raqf"
        self.assertEqual(coded[:len(test)], test)

    def testNonalpha(self):
        msg = sidewalk
        coded = main.encode(msg)
        self.assertEqual(coded[69], ",")
        self.assertEqual(coded[-1], ".")

    def testReverse(self):
        msg = sidewalk
        coded = main.encode(sidewalk, 2)
        self.assertEqual(sidewalk, main.encode(coded, -2))

    @timeout(1)
    def testLarge(self):
        msg = hamlet
        coded = main.encode(msg, 2)
        self.assertTrue(True)

class TestTryShifts(unittest.TestCase):

    @timeout(1)
    def testSingleShift(self):
        msg = sidewalk
        results = main.tryShifts(msg)
        self.assertEqual(len(results.next()), len(msg))

    @timeout(3)
    def testNumShifts(self):
        msg = sidewalk
        results = main.tryShifts(msg)
        i=0
        for result in results:
            i+=1
        self.assertTrue(i == 26)

    @timeout(3)
    def testAllShifts(self):
        msg = sidewalk
        results = main.tryShifts(msg)
        for result in results:
            self.assertEqual(len(result), len(msg))

    @timeout(5)
    def testLarge(self):
        msg = hamlet
        results = main.tryShifts(msg)
        for result in results:
            self.assertEqual(len(result), len(msg))

class TestTrie(unittest.TestCase):

    @timeout(1)
    def testAddSingle(self):
        t = main.Trie()
        t.add("test")
        self.assertTrue("test" in t)

    @timeout(10)
    def testAddDictionary(self):
        t = main.Trie()
        infile = open(dictfile)
        [t.add(w.strip()) for w in infile]
        [self.assertTrue(w.strip() in t) for w in infile]

    @timeout(1)
    def testUpdate(self):
        t = main.Trie()
        t.add("cat")
        t.add("cats")
        t.add("cattle")
        for i in range(10):
            t.update("cat")
        self.assertEquals(t.count("cat"), 10)
        for i in range(3):
            t.update("cats")
        self.assertEquals(t.count("cat"), 10)
        self.assertEquals(t.count("cats"), 3)
        for i in range(15):
            t.update("cattle")
        self.assertEquals(t.count("cat"), 10)
        self.assertEquals(t.count("cats"), 3)
        self.assertEquals(t.count("cattle"), 15)

class TestDecode(unittest.TestCase):

    @timeout(3)
    def testShortSingleShift(self):
        msg = sidewalk_encoded
        res = sidewalk
        r, t = main.decode(msg, dictfile)
        self.assertEqual(r, res)

    @timeout(5)
    def testShortMultiShift(self):
        msg = cat_encoded
        res = cat
        r, t = main.decode(msg, dictfile)
        self.assertEqual(r, res)

    @timeout(10)
    def testLongSingleShift(self):
        msg = hamlet_encoded
        res = hamlet
        r, t = main.decode(msg, dictfile)
        self.assertEqual(r, res)

    @timeout(15)
    def testLongMultiShift(self):
        msg = v_entries_encoded
        res = v_entries
        r, t = main.decode(msg, dictfile)
        self.assertEqual(r, res)

    @timeout(3)
    def testCounts(self):
        msg = sidewalk
        r, t = main.decode(msg, dictfile)
        l = [("there",4),("sidewalk",1),("and",5),("wind",1),("the",6)]
        for w,c in l:
            self.assertEqual(t.count(w), c)

cat = "The sun did not shine. It was too wet to play. So we sat in the house All that cold, cold, wet day. I sat there with Sally, we sat there we two. And I said, 'How I wish we had something to do!'"
cat = cat.lower()

cat_encoded = "gur fha qvq abg fuvar. vg jnf gbb jrg gb cynl. fb jr fng va gur ubhfr nyy gung pbyq, pbyq, jrg qnl. v fng gurer jvgu fnyyl, jr fng gurer jr gjb. naq v fnvq, 'ubj v jvfu jr unq fbzrguvat gb qb!'"

sidewalk = "There is a place where the sidewalk ends And before the street begins, And there the grass grows soft and white, And there the sun burns crimson bright, And there the moon-bird rests from his flight To cool in the peppermint wind."
sidewalk = sidewalk.lower()

sidewalk_encoded = "gurer vf n cynpr jurer gur fvqrjnyx raqf naq orsber gur fgerrg ortvaf, naq gurer gur tenff tebjf fbsg naq juvgr, naq gurer gur fha oheaf pevzfba oevtug, naq gurer gur zbba-oveq erfgf sebz uvf syvtug gb pbby va gur crccrezvag jvaq."

hamlet = """To be, or not to be, that is the question:
Whether 'tis Nobler in the mind to suffer
The Slings and Arrows of outrageous Fortune,
Or to take Arms against a Sea of troubles,
And by opposing end them: to die, to sleep
No more; and by a sleep, to say we end
The Heart-ache, and the thousand Natural shocks
That Flesh is heir to? 'Tis a consummation
Devoutly to be wished. To die, to sleep,
To sleep, perchance to Dream; Aye, there's the rub,
For in that sleep of death, what dreams may come,
When we have shuffled off this mortal coil,
Must give us pause. There's the respect
That makes Calamity of so long life:
For who would bear the Whips and Scorns of time,
The Oppressor's wrong, the proud man's Contumely,
The pangs of despised Love, the Law's delay,
The insolence of Office, and the Spurns
That patient merit of the unworthy takes,
When he himself might his Quietus make
With a bare Bodkin? Who would Fardels bear,
To grunt and sweat under a weary life,
But that the dread of something after death,
The undiscovered Country, from whose bourn
No Traveller returns, Puzzles the will,
And makes us rather bear those ills we have,
Than fly to others that we know not of.
Thus Conscience does make Cowards of us all,
And thus the Native hue of Resolution
Is sicklied o'er, with the pale cast of Thought,
And enterprises of great pitch and moment,
With this regard their Currents turn awry,
And lose the name of Action. Soft you now,
The fair Ophelia? Nymph, in thy Orisons
Be all my sins remembered."""
hamlet = hamlet.lower()

hamlet_encoded = """gb or, be abg gb or, gung vf gur dhrfgvba:\njurgure 'gvf aboyre va gur zvaq gb fhssre\ngur fyvatf naq neebjf bs bhgentrbhf sbeghar,\nbe gb gnxr nezf ntnvafg n frn bs gebhoyrf,\nnaq ol bccbfvat raq gurz: gb qvr, gb fyrrc\nab zber; naq ol n fyrrc, gb fnl jr raq\ngur urneg-npur, naq gur gubhfnaq angheny fubpxf\ngung syrfu vf urve gb? 'gvf n pbafhzzngvba\nqribhgyl gb or jvfurq. gb qvr, gb fyrrc,\ngb fyrrc, crepunapr gb qernz; nlr, gurer'f gur eho,\nsbe va gung fyrrc bs qrngu, jung qernzf znl pbzr,\njura jr unir fuhssyrq bss guvf zbegny pbvy,\nzhfg tvir hf cnhfr. gurer'f gur erfcrpg\ngung znxrf pnynzvgl bs fb ybat yvsr:\nsbe jub jbhyq orne gur juvcf naq fpbeaf bs gvzr,\ngur bccerffbe'f jebat, gur cebhq zna'f pbaghzryl,\ngur cnatf bs qrfcvfrq ybir, gur ynj'f qrynl,\ngur vafbyrapr bs bssvpr, naq gur fcheaf\ngung cngvrag zrevg bs gur hajbegul gnxrf,\njura ur uvzfrys zvtug uvf dhvrghf znxr\njvgu n oner obqxva? jub jbhyq sneqryf orne,\ngb tehag naq fjrng haqre n jrnel yvsr,\nohg gung gur qernq bs fbzrguvat nsgre qrngu,\ngur haqvfpbirerq pbhagel, sebz jubfr obhea\nab geniryyre ergheaf, chmmyrf gur jvyy,\nnaq znxrf hf engure orne gubfr vyyf jr unir,\nguna syl gb bguref gung jr xabj abg bs.\nguhf pbafpvrapr qbrf znxr pbjneqf bs hf nyy,\nnaq guhf gur angvir uhr bs erfbyhgvba\nvf fvpxyvrq b're, jvgu gur cnyr pnfg bs gubhtug,\nnaq ragrecevfrf bs terng cvgpu naq zbzrag,\njvgu guvf ertneq gurve pheeragf ghea njel,\nnaq ybfr gur anzr bs npgvba. fbsg lbh abj,\ngur snve bcuryvn? alzcu, va gul bevfbaf\nor nyy zl fvaf erzrzorerq."""

dictfile = "dictionary.txt"

v_entries = " ".join([w.strip() for w in open(dictfile).readlines() if w.strip()[0] == "v"])
v_entries_encoded = "i inpnapvrf inpnapl inpnag inpnagyl inpngr inpngrq inpngrf inpngvat inpngvba inpngvbarq inpngvbare inpngvbaref inpngvbavat inpngvbaf inppvangr inppvangrq inppvangrf inppvangvat inppvangvba inppvangvbaf inppvar inppvarf inpvyyngr inpvyyngrq inpvyyngrf inpvyyngvat inpvyyngvba inpvyyngvbaf inphn inphvgl inphbhf inphbhfyl inphhz inphhzrq inphhzvat inphhzf intnobaq intnobaqrq intnobaqvat intnobaqf intnevrf intnel intvan intvanr intvany intvanf intenapl intenag intenagf inthr inthryl inthrarff inthre inthrfg inva invare invarfg invatybevbhf invatybel invayl inynapr inynaprf inyr inyrqvpgbevna inyrqvpgbevnaf inyrqvpgbevrf inyrqvpgbel inyrapr inyraprf inyragvar inyragvarf inyrf inyrg inyrgrq inyrgvat inyrgf inyvnag inyvnagyl inyvq inyvqngr inyvqngrq inyvqngrf inyvqngvat inyvqngvba inyvqngvbaf inyvqvgl inyvqyl inyvqarff inyvfr inyvfrf inyyrl inyyrlf inybe inybebhf inyhnoyr inyhnoyrf inyhngvba inyhngvbaf inyhr inyhrq inyhryrff inyhrf inyhvat inyir inyirq inyirf inyivat inzbbfr inzbbfrq inzbbfrf inzbbfvat inzc inzcrq inzcvat inzcver inzcverf inzcf ina inanqvhz inaqny inaqnyvfz inaqnyvmr inaqnyvmrq inaqnyvmrf inaqnyvmvat inaqnyf inar inarf inathneq inathneqf inavyyn inavyynf inavfu inavfurq inavfurf inavfuvat inavfuvatf inavgvrf inavgl inaarq inaavat inadhvfu inadhvfurq inadhvfurf inadhvfuvat inaf inagntr inagntrf incvq incvqvgl incvqarff incbe incbevmngvba incbevmr incbevmrq incbevmre incbevmref incbevmrf incbevmvat incbebhf incbef inevnovyvgl inevnoyr inevnoyrf inevnoyl inevnapr inevnaprf inevnag inevnagf inevngr inevngvba inevngvbaf inevpbyberq inevpbfr inevrq inevrtngr inevrtngrq inevrtngrf inevrtngvat inevrf inevrgvrf inevrgl inevbhf inevbhfyl ineyrg ineyrgf inezvag inezvagf ineavfu ineavfurq ineavfurf ineavfuvat inefvgvrf inefvgl inel inelvat infphyne infr infrpgbzvrf infrpgbzl infrf inffny inffnyntr inffnyf infg infgre infgrfg infgyl infgarff infgf ing ingf inggrq inggvat inhqrivyyr inhyg inhygrq inhygre inhygref inhygvat inhygf inhag inhagrq inhagvat inhagf irny irpgbe irpgberq irpgbevat irpgbef irrc irrcf irre irrerq irrevat irref irtna irtnaf irtrgnoyr irtrgnoyrf irtrgnevna irtrgnevnavfz irtrgnevnaf irtrgngr irtrgngrq irtrgngrf irtrgngvat irtrgngvba irtrgngvir irttvr irttvrf irurzrapr irurzrag irurzragyl iruvpyr iruvpyrf iruvphyne irvy irvyrq irvyvat irvyf irva irvarq irvavat irvaf iryq iryqf iryqg iryqgf iryyhz irybpvgvrf irybpvgl irybhe irybhef iryirg iryirgrra iryirgvre iryirgvrfg iryirgl irany iranyvgl iranyyl iraq iraqrq iraqre iraqref iraqrggn iraqrggnf iraqvat iraqbe iraqbef iraqf irarre irarrerq irarrevat irarref irarenoyr irarengr irarengrq irarengrf irarengvat irarengvba irarerny iratrnapr iratrshy iratrshyyl iravny iravfba irabz irabzbhf irabzbhfyl irabhf irag iragrq iragvyngr iragvyngrq iragvyngrf iragvyngvat iragvyngvba iragvyngbe iragvyngbef iragvat irageny iragevpyr iragevpyrf iragevphyne iragevybdhvfz iragevybdhvfg iragevybdhvfgf iragf iragher iragherq iragherf iragherfbzr iraghevat iraghebhf irahr irahrf irenpvbhf irenpvgl irenaqn irenaqnu irenaqnuf irenaqnf ireo ireony ireonyvmr ireonyvmrq ireonyvmrf ireonyvmvat ireonyyl ireonyf ireongvz ireoran ireoranf ireovntr ireobfr ireobfvgl ireof ireqnag ireqvpg ireqvpgf ireqvtevf ireqvtevfrq ireqvtevfrf ireqvtevfvat ireqher iretr iretrq iretrf iretvat irevre irevrfg irevsvnoyr irevsvpngvba irevsvrq irevsvrf irevsl irevslvat irevyl irevfvzvyvghqr irevgnoyr irevgnoyl irevgvrf irevgl irezvpryyv irezvyvba irezvyyvba irezva irezvabhf irezbhgu ireanphyne ireanphynef ireany irefngvyr irefngvyvgl irefr irefrq irefrf irefvsvpngvba irefvsvrq irefvsvrf irefvsl irefvslvat irefvat irefvba irefvbaf irefhf iregroen iregroenr iregroeny iregroenf iregroengr iregroengrf iregrk iregrkrf iregvpny iregvpnyyl iregvpnyf iregvprf iregvtvabhf iregvtb ireir irel irfvpyr irfvpyrf irfcre irfcref irffry irffryf irfg irfgrq irfgvohyr irfgvohyrf irfgvtr irfgvtrf irfgvtvny irfgvat irfgzrag irfgzragf irfgevrf irfgel irfgf irg irgpu irgpurf irgrena irgrenaf irgrevanevna irgrevanevnaf irgrevanevrf irgrevanel irgb irgbrq irgbrf irgbvat irgf irggrq irggvat irk irkngvba irkngvbaf irkngvbhf irkrq irkrf irkvat ivn ivnovyvgl ivnoyr ivnqhpg ivnqhpgf ivny ivnyf ivnaq ivnaqf ivor ivorf ivoenapl ivoenag ivoenagyl ivoencubar ivoencubarf ivoengr ivoengrq ivoengrf ivoengvat ivoengvba ivoengvbaf ivoengb ivoengbe ivoengbef ivoengbf ivoheahz ivoheahzf ivpne ivpnentr ivpnentrf ivpnevbhf ivpnevbhfyl ivpnef ivpr ivprq ivprebl ivpreblf ivprf ivpulffbvfr ivpvat ivpvavgl ivpvbhf ivpvbhfyl ivpvbhfarff ivpvffvghqr ivpvffvghqrf ivpgvz ivpgvzvmngvba ivpgvzvmr ivpgvzvmrq ivpgvzvmrf ivpgvzvmvat ivpgvzf ivpgbe ivpgbevrf ivpgbevbhf ivpgbevbhfyl ivpgbef ivpgbel ivpghny ivpghnyrq ivpghnyvat ivpghnyyrq ivpghnyyvat ivpghnyf ivqrb ivqrbpnffrggr ivqrbpnffrggrf ivqrbqvfp ivqrbqvfpf ivqrbf ivqrbgncr ivqrbgncrq ivqrbgncrf ivqrbgncvat ivr ivrq ivrf ivrj ivrjrq ivrjre ivrjref ivrjsvaqre ivrjsvaqref ivrjvat ivrjvatf ivrjcbvag ivrjcbvagf ivrjf ivtvy ivtvynapr ivtvynag ivtvynagr ivtvynagrf ivtvynagvfz ivtvynagyl ivtvyf ivtarggr ivtarggrq ivtarggrf ivtarggvat ivtbe ivtbebhf ivtbebhfyl ivyr ivyryl ivyrarff ivyre ivyrfg ivyvsvpngvba ivyvsvrq ivyvsvrf ivyvsl ivyvslvat ivyyn ivyyntr ivyyntre ivyyntref ivyyntrf ivyynva ivyynvavrf ivyynvabhf ivyynvaf ivyynval ivyynf ivyyrva ivyyrvaf ivz ivanvterggr ivaqvpngr ivaqvpngrq ivaqvpngrf ivaqvpngvat ivaqvpngvba ivaqvpngvbaf ivaqvpngbe ivaqvpngbef ivaqvpgvir ivaqvpgviryl ivaqvpgvirarff ivar ivartne ivartnel ivarf ivarlneq ivarlneqf ivagntr ivagntrf ivagare ivagaref ivaly ivalyf ivby ivbyn ivbynoyr ivbynf ivbyngr ivbyngrq ivbyngrf ivbyngvat ivbyngvba ivbyngvbaf ivbyngbe ivbyngbef ivbyrapr ivbyrag ivbyragyl ivbyrg ivbyrgf ivbyva ivbyvavfg ivbyvavfgf ivbyvaf ivbyvfg ivbyvfgf ivbybapryyb ivbybapryybf ivbyf ivcre ivcref iventb iventbrf iventbf iveny iverb iverbf ivetva ivetvany ivetvanyf ivetvavgl ivetvaf ivethyr ivethyrf ivevyr ivevyvgl ivebybtl iveghny iveghnyyl iveghr iveghrf iveghbfv iveghbfvgl iveghbfb iveghbfbf iveghbhf iveghbhfyl iveghbhfarff ivehyrapr ivehyrag ivehyragyl ivehf ivehfrf ivfn ivfnrq ivfntr ivfntrf ivfnvat ivfnf ivfpren ivfpreny ivfpvq ivfpbfvgl ivfpbhag ivfpbhagrff ivfpbhagrffrf ivfpbhagf ivfpbhf ivfphf ivfr ivfrq ivfrf ivfvovyvgl ivfvoyr ivfvoyl ivfvat ivfvba ivfvbanevrf ivfvbanel ivfvbarq ivfvbavat ivfvbaf ivfvg ivfvgngvba ivfvgngvbaf ivfvgrq ivfvgvat ivfvgbe ivfvgbef ivfvgf ivfbe ivfbef ivfgn ivfgnf ivfhny ivfhnyvmngvba ivfhnyvmr ivfhnyvmrq ivfhnyvmrf ivfhnyvmvat ivfhnyyl ivfhnyf ivgny ivgnyvgl ivgnyvmr ivgnyvmrq ivgnyvmrf ivgnyvmvat ivgnyyl ivgnyf ivgnzva ivgnzvaf ivgvngr ivgvngrq ivgvngrf ivgvngvat ivgvngvba ivgvphygher ivgerbhf ivgevby ivgevbyvp ivghcrengr ivghcrengrq ivghcrengrf ivghcrengvat ivghcrengvba ivghcrengvir ivin ivinpr ivinpvbhf ivinpvbhfyl ivinpvbhfarff ivinpvgl ivinf ivivq ivivqre ivivqrfg ivivqyl ivivqarff ivivsvrq ivivsvrf ivivsl ivivslvat ivivcnebhf ivivfrpgvba ivkra ivkravfu ivkraf ivmvre ivmvref ivmbe ivmbef ibpnohynevrf ibpnohynel ibpny ibpnyvp ibpnyvfg ibpnyvfgf ibpnyvmngvba ibpnyvmngvbaf ibpnyvmr ibpnyvmrq ibpnyvmrf ibpnyvmvat ibpnyyl ibpnyf ibpngvba ibpngvbany ibpngvbaf ibpngvir ibpngvirf ibpvsrengr ibpvsrengrq ibpvsrengrf ibpvsrengvat ibpvsrengvba ibpvsrebhf ibpvsrebhfyl ibqxn ibthr ibthrf ibthvfu ibvpr ibvprq ibvpryrff ibvprf ibvpvat ibvq ibvqrq ibvqvat ibvqf ibvyr ibyngvyr ibyngvyvgl ibypnavp ibypnab ibypnabrf ibypnabf ibyr ibyrf ibyvgvba ibyyrl ibyyrlonyy ibyyrlonyyf ibyyrlrq ibyyrlvat ibyyrlf ibyg ibygntr ibygntrf ibygnvp ibygzrgre ibygzrgref ibygf ibyhovyvgl ibyhoyr ibyhoyl ibyhzr ibyhzrf ibyhzvabhf ibyhzvabhfyl ibyhagnevrf ibyhagnevyl ibyhagnel ibyhagrre ibyhagrrerq ibyhagrrevat ibyhagrref ibyhcghnevrf ibyhcghnel ibyhcghbhf ibyhcghbhfyl ibyhcghbhfarff ibzvg ibzvgrq ibzvgvat ibzvgf ibbqbb ibbqbbrq ibbqbbvat ibbqbbvfz ibbqbbf ibenpvbhf ibenpvbhfyl ibenpvgl ibegrk ibegrkrf ibegvprf ibgnevrf ibgnel ibgr ibgrq ibgre ibgref ibgrf ibgvat ibgvir ibhpu ibhpurq ibhpure ibhpuref ibhpurf ibhpuvat ibhpufnsr ibhpufnsrq ibhpufnsrf ibhpufnsvat ibj ibjrq ibjry ibjryf ibjvat ibjf iblntr iblntrq iblntre iblntref iblntrf iblntvat iblrhe iblrhevfz iblrhevfgvp iblrhef if ihypnavmngvba ihypnavmr ihypnavmrq ihypnavmrf ihypnavmvat ihytne ihytnere ihytnerfg ihytnevfz ihytnevfzf ihytnevgvrf ihytnevgl ihytnevmngvba ihytnevmr ihytnevmrq ihytnevmrf ihytnevmvat ihytneyl ihyarenovyvgvrf ihyarenovyvgl ihyarenoyr ihyarenoyl ihygher ihygherf ihyin ihyinr ihyinf ilvat"

if __name__ == "__main__":
    suite = unittest.TestSuite()
    classes = [TestEncode, TestTryShifts, TestTrie, TestDecode]
    for c in classes:
        suite.addTests(unittest.TestLoader().loadTestsFromTestCase(c))

    import sys
    r = unittest.TestResult()
    suite.run(r)
    if r.wasSuccessful():
        print "All tests passed."
    else:
        print "Failed test traces:"
        for f in r.failures:
            for x in f: print x
        print "Errored test traces:"
        for f in r.errors:
            for x in f: print x


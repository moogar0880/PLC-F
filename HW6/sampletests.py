import unittest
from timeout import timeout
from time import time
from main import encode, tryShifts, Trie, decode

class TestEncode(unittest.TestCase):
    points = 0
    possible = 20

    def testOne(self): # 4 points
        msg = heavens
        coded = encode(msg, 1)
        test = "ibe j uif ifbwfot"
        self.assertEqual(coded[:len(test)], test)
        self.__class__.points += 4

    def testDefault(self): # 3 points
        msg = heavens
        coded = encode(msg)
        test = "unq v gur urniraf"
        self.assertEqual(coded[:len(test)], test)
        self.__class__.points += 3

    def testNonalpha(self): # 3 points
        msg = cthulhu
        coded = encode(msg)
        self.assertEqual(coded[17], "\'")
        self.__class__.points += 3

    def testReverse(self): # 5 points
        msg = heavens
        coded = encode(heavens, 2)
        self.assertEqual(heavens, encode(coded, -2))
        self.__class__.points += 5

    @timeout(1)
    def testLarge(self): # 5 points
        msg = dream
        coded = encode(msg, 2)
        self.assertTrue(True)
        self.__class__.points += 5

class TestTryShifts(unittest.TestCase):
    points = 0
    possible = 30

    @timeout(1)
    def testSingleShift(self): # 5 points
        msg = heavens
        results = tryShifts(msg)
        self.assertEqual(len(results.next()), len(msg))
        self.__class__.points += 5

    @timeout(3)
    def testNumShifts(self): # 5 points
        msg = heavens
        results = tryShifts(msg)
        i=0
        for result in results:
            if result is None: break
            i+=1
        self.assertTrue(i == 26 or i == 25)
        self.__class__.points += 5

    @timeout(3)
    def testAllShifts(self): # 10 points
        msg = heavens
        results = tryShifts(msg)
        for result in results:
            if result is None: break
            self.assertEqual(len(result), len(msg))
        self.__class__.points += 10

    @timeout(5)
    def testLarge(self): # 10 points
        msg = dream
        results = tryShifts(msg)
        for result in results:
            if result is None: break
            self.assertEqual(len(result), len(msg))
        self.__class__.points += 10

class TestTrie(unittest.TestCase):
    points = 0
    possible = 30

    @timeout(1)
    def testAddSingle(self): # 10 points
        t = Trie()
        t.add("test")
        self.assertTrue("test" in t)
        self.__class__.points += 10

    @timeout(10)
    def testAddDictionary(self): # 10 points
        t = Trie()
        infile = open(dictfile)
        [t.add(w.strip()) for w in infile]
        [self.assertTrue(w.strip() in t) for w in infile]
        self.__class__.points += 10

    @timeout(20)
    def testCommonPrefixes(self): # 5 points
        msg = heavens
        r, t = decode(msg, dictfile)
        t1 = time()
        cp = t.commonPrefixes
        t1 = time() - t1
        t2 = time()
        cp = t.commonPrefixes
        t2 = time() - t2
        self.assertTrue(t2 < t1)
        self.__class__.points += 5

    @timeout(20)
    def testCommonPrefixesTime(self): # 5 points
        msg = sidewalk
        r, t = decode(msg, dictfile)
        t1 = time()
        cp = t.commonPrefixes
        l = ['arr', 'chi', 'end', 'gro', 'mea', 'pla',
             'sid', 'slo', 'str', 'tha', 'the', 'wal', 'win', 'wit', 'whe']
        self.assertEqual(cp, [x for x in cp if x in l])
        self.__class__.points += 5

class TestDecode(unittest.TestCase):
    points = 0
    possible = 20

    @timeout(10)
    def testShortSingleShift(self): # 3 points
        msg = heavens
        r, t = decode(encode(msg), dictfile)
        self.assertEqual(r, msg)
        TestDecode.points += 3

    @timeout(12)
    def testShortMultiShift(self): # 5 points
        msg = fire
        r, t = decode(encode(msg), dictfile)
        self.assertEqual(r, msg)
        self.__class__.points += 5

    @timeout(12)
    def testLongSingleShift(self): # 5 points
        msg = dream
        r, t = decode(encode(msg), dictfile)
        self.assertEqual(r, msg)
        self.__class__.points += 5

    @timeout(20)
    def testLongMultiShift(self): # 7 points
        msg = w_entries
        r, t = decode(encode(msg), dictfile)
        self.assertEqual(r, msg)
        self.__class__.points += 7

cthulhu = "In his house at R'lyeh, dead Cthulhu waits dreaming."
cthulhu = cthulhu.lower()

heavens = """Had I the heavens' embroidered cloths,
Enwrought with golden and silver light,
The blue and the dim and the dark cloths
Of night and light and the half-light,
I would spread the cloths under your feet:
But I, being poor, have only my dreams;
I have spread my dreams under your feet;
Tread softly because you tread on my dreams."""
heavens = heavens.lower()

fire = """Some say the world will end in fire,
Some say in ice.
From what I've tasted of desire
I hold with those who favor fire.
But if it had to perish twice,
I think I know enough of hate
To say that for destruction ice
Is also great
And would suffice."""
fire = fire.lower()

mississippi = "Mississippi is bordered on the north by Tennessee, on the east by Alabama, on the south by Louisiana and a narrow coast on the Gulf of Mexico and on the west, across the Mississippi River, by Louisiana and Arkansas."
mississippi = mississippi.lower()

sidewalk = """There is a place where the sidewalk ends
and before the street begins,
and there the grass grows soft and white,
and there the sun burns crimson bright,
and there the moon-bird rests from his flight
to cool in the peppermint wind.

Let us leave this place where the smoke blows black
and the dark street winds and bends.
Past the pits where the asphalt flowers grow
we shall walk with a walk that is measured and slow
and watch where the chalk-white arrows go
to the place where the sidewalk ends.

Yes we'll walk with a walk that is measured and slow,
and we'll go where the chalk-white arrows go,
for the children, they mark, and the children, they know,
the place where the sidewalk ends."""
sidewalk = sidewalk.lower()

dream = open("dream.txt").read().lower()

dictfile = "dictionary.txt"

w_entries = " ".join([w.strip() for w in open(dictfile).readlines() if w.strip()[0] == "w"])

if __name__ == "__main__":
    suite = unittest.TestSuite()
    classes = [TestEncode, TestTryShifts, TestTrie, TestDecode]
    for c in classes:
        suite.addTests(unittest.TestLoader().loadTestsFromTestCase(c))

    import sys
    r = unittest.TestResult()
    suite.run(r)
    if r.wasSuccessful(): "All tests passed."
    else:
        print "Failed test traces:"
        for f in r.failures:
            for line in f[1].split("\n")[:-2]: print line
            print ""

    total = 0.0
    totalPos = 0.0
    for c in classes:
        total += c.points
        totalPos += c.possible

    print "Grade:", (total / totalPos)*100, "/ 100"

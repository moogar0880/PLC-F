import unittest
from time import time
import random

from exam import binSearch, A, storeinputs

random.seed(999)

class TestBinSearch(unittest.TestCase):
    points = 0
    possible = 5
    
    def testEmpty(self):
        self.assertEqual(binSearch(3, []), None)
        self.__class__.points += 1
    
    def testSmall(self):
        self.small = sorted([random.randint(0,100) for i in range(10)])
        self.smalli = random.randint(0, len(self.small))
        self.smallv = self.small[self.smalli]
        self.small = zip(self.small, list(range(10)))
        r = binSearch(self.smallv, self.small)
        self.assertEqual(r, self.small[self.smalli][1])
        self.__class__.points += 2
    
    def testLarge(self):
        self.large = sorted([random.randint(0,1000) for i in range(100)])
        self.largei = random.randint(0,len(self.large))
        self.largev = self.large[self.largei]
        self.large = zip(self.large, [random.randint(0,100000) for i in range(1000)])
        r = binSearch(self.largev, self.large)
        self.assertEqual(r, self.large[self.largei][1])
        self.__class__.points += 2

class TestBuiltins(unittest.TestCase):
    points = 0
    possible = 10

    def testIndexing(self):
        a = A()
        a.l = [1,2,3,4]
        self.assertEqual(a[2], a.l[2])
        self.__class__.points += 2

    def testAdding(self):
        a = A()
        a += [1,2,3,4]
        a += [5]
        self.assertEqual(a[-1], 5)
        self.assertEqual(a[1], 2)
        self.__class__.points += 4

    def testRange(self):
        a = A()
        l = [1,2,3,4,5,6,7]
        a.l = [1,2,3,4,5,6,7]
        self.assertEqual(a[2:4], l[2:4])
        self.__class__.points += 4
        

class TestInputs(unittest.TestCase):
    points = 0
    possible = 15

    def testEmpty(self):
        @storeinputs
        def longer(a, b):
            return [a,b][len(a) < len(b)]

        self.assertEqual(longer.valid, [])
        self.assertEqual(longer.invalid, [])
        self.__class__.points += 2

    def testValid(self):
        @storeinputs
        def longer(a, b):
            return [a,b][len(a) < len(b)]

        r = longer([], [1,2,3])
        self.assertEqual(longer.valid, [([], [1,2,3])])
        self.assertEqual(r, [1,2,3])
        self.__class__.points += 5

    def testInvalid(self):
        @storeinputs
        def longer(a, b):
            return [a,b][len(a) < len(b)]

        try:
            r = longer(654, [1,2,3])
        except Exception:
            pass
        self.assertEqual(longer.invalid, [(654, [1,2,3])])
        self.__class__.points += 5

    def testException(self):
        @storeinputs
        def longer(a, b):
            return [a,b][len(a) < len(b)]

        try:
            r = longer(654, [1,2,3])
        except Exception:
            self.__class__.points += 3
            return

        self.assertTrue(False)

        
if __name__ == "__main__":
    suite = unittest.TestSuite()
    classes = [TestBinSearch, TestBuiltins, TestInputs]
    for c in classes:
        suite.addTests(unittest.TestLoader().loadTestsFromTestCase(c))

    import sys
    r = unittest.TestResult()
    suite.run(r)
    if r.wasSuccessful(): "All tests passed."
    else:
        print "Failed test traces:"
        for f in r.failures:
            print f[1]
            #for line in f[1].split("\n")[:-2]: print line

    total = 0.0
    totalPos = 0.0
    for c in classes:
        total += c.points
        totalPos += c.possible

    print "Grade:", total, "/", totalPos        

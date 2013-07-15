import string
import math
'''
Takes a function, f, and a list, lst, and returns a list containing only
elements of lst for which f evaluates to True
'''
def filter(f, l=[]):
    toRet = []
    for item in l:
        if f(item):
            toRet.append(item)
    return toRet

'''
Looks at a string, s, and determines whether all brackets ((), {}, [], <>)
have a matched pair and are closed in the same order in which they were
opened
'''
def matchBrackets(s):
    openBrackets = ['(','{','[','<']
    closBrackets = [')','}',']','>']
    stack        = []
    for char in s:
        if char in openBrackets:
            stack.append(char)
        elif char in closBrackets:
            if openBrackets.index(stack.pop()) != closBrackets.index(char):
                return False
    return len(stack) == 0

'''
Looks at the words in the string, s, and returns a list of tuples in the
form (word,count) sorted by highest count to lowest
'''
def wordCounts(s):
    words = {}
    toRet = []
    for word in s.split():
        word = word.lower()
        if word[0] in string.punctuation:
            word = word[1:]
        if word[len(word)-1] in string.punctuation:
            word = word[:-1]
        if word in words.keys():
            words[word] = words[word] + 1
        else:
            words[word] = 1
    for word in words.keys():
        toRet.append((word,words[word]))

    return sorted(toRet, key=lambda wordcount: wordcount[1], reverse=True)

'''
Returns the nth prime number, starting with nthPrime(0) returning 1. The
function should make use of memoization to drastically improve the speed
of results
'''
def nthPrime(n):
    if nthPrime.primes != None:
        pass
    else:
        nthPrime.primes = [1]
    n = n+1
    if len(nthPrime.primes) >= n:
        return nthPrime.primes[n-1]
    else:
        size = len(nthPrime.primes)
        i = nthPrime.primes[size - 1] + 1
        while len(nthPrime.primes) < n:
            prime = True
            if i == 2 and 2 not in nthPrime.primes:
                pass
            elif i % 2 == 0:
                prime = False
            else:
                for v in nthPrime.primes:
                    if v != 1 and i % v == 0:
                        prime = False
                        break
            if prime:
                nthPrime.primes.append(i)
                if len(nthPrime.primes) == n:
                    return i
            i += 1
nthPrime.primes = None

class USDollar(object):
    """docstring for USDollar"""
    def __init__(self,value=0.0):
        super(USDollar, self).__init__()
        self.val = self.truncate(value)
        #print self.__dict__

    def truncate(self,value):
        tmp = int(value*100)
        return float(tmp)/100.0

    def __repr__(self):
        return "${0:.2f}".format(float(self.val))

    def __add__(self,other): #+
        if isinstance(other,float) or isinstance(other,int):
           return USDollar(self.truncate(self.val) + self.truncate(other))
        elif isinstance(other,USDollar):
           return USDollar(self.truncate(self.val) + self.truncate(other.val))
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __sub__(self,other): #-
        if isinstance(other,float) or isinstance(other,int):
           return USDollar(self.truncate(self.val) - self.truncate(other))
        elif isinstance(other,USDollar):
           return USDollar(self.truncate(self.val) - self.truncate(other.val))
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __iadd__(self,other): #+=
        if isinstance(other,float) or isinstance(other,int):
           self.val += self.truncate(other)
           return self
        elif isinstance(other,USDollar):
           self.val += self.truncate(other.val)
           return self
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __isub__(self,other): #-=
        if isinstance(other,float) or isinstance(other,int):
           self.val -= self.truncate(other)
           return self
        elif isinstance(other,USDollar):
           self.val -= self.truncate(other.val)
           return self
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __eq__(self,other): # ==
        if isinstance(other,float) or isinstance(other,int):
           return self.truncate(self.val) == self.truncate(other)
        elif isinstance(other,USDollar):
           return self.truncate(self.val) == self.truncate(other.val)
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __le__(self,other): #<=
        if isinstance(other,float) or isinstance(other,int):
           return self.truncate(self.val) <= self.truncate(other)
        elif isinstance(other,USDollar):
           return self.truncate(self.val) <= self.truncate(other.val)
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __ge__(self,other): #>=
        if isinstance(other,float) or isinstance(other,int):
           return self.truncate(self.val) >= self.truncate(other)
        elif isinstance(other,USDollar):
           return self.truncate(self.val) >= self.truncate(other.val)
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __lt__(self,other): #<
        if isinstance(other,float) or isinstance(other,int):
           return self.truncate(self.val) < self.truncate(other)
        elif isinstance(other,USDollar):
           return self.truncate(self.val) < self.truncate(other.val)
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __gt__(self,other): #>
        if isinstance(other,float) or isinstance(other,int):
           return self.truncate(self.val) > self.truncate(other)
        elif isinstance(other,USDollar):
           return self.truncate(self.val) > self.truncate(other.val)
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __str__(self):
        return self.__repr__()

class Tester(object):
    """docstring for Tester"""
    def __init__(self):
        super(Tester, self).__init__()
        self.score = self.total = 0

    def printResults(self,results,name):
        for r in results:
            self.total += 1
            if r:
                print "Pass {}, test {}".format(name, results.index(r))
                self.score += 1
            else:
                print "Fail {}, test {}".format(name, results.index(r))

    def filterTests(self):
        results = []
        def check(v):
            return v % 2 == 0
        #filter(check,range(20)
        results.append(filter(check,range(20)) == [0, 2, 4, 6, 8, 10, 12, 14, 16, 18])
        self.printResults(results,'filterTests')

    def matchBracketTests(self):
        results = []
        #matchBrackets("{{}")
        results.append(matchBrackets("{{}") == False)
        #matchBrackets("({)}")
        results.append(matchBrackets("({)}") == False)
        #matchBrackets("({})")
        results.append(matchBrackets("({})") == True)
        self.printResults(results,'matchBracketTests')

    def wordCountsTests(self):
        results = []
        #wordCounts("a a a a liggity giggity lizzist")
        results.append(wordCounts("a a a a liggity giggity lizzist") == [('a', 4), ('giggity', 1), ('lizzist', 1), ('liggity', 1)])
        self.printResults(results,'wordCountsTests')

    def nthPrimeTests(self):
        results = []
        results.append(nthPrime(10) == 29)
        print nthPrime(10)
        #print nthPrime.primes
        self.printResults(results,'nthPrimeTests')

    def uSDTests(self):
        results = []
        d1 = USDollar()
        d1.val = 2.001
        d2 = USDollar(100.0)
        #Check innate rounding, d1.val == 2.0
        results.append(d1.val == 2.0)
        #Check __repr__
        print "\t", d1.__repr__() == '$2.00'
        results.append(d1.__repr__() == '$2.00')
        #Check += float
        d1 += 2.0
        results.append(d1.val == 4.0)
        #Check -= float
        d1 -= 2.0
        results.append(d1.val == 2.0)
        #Check +  float
        #Check -  float
        #Check >= float
        results.append(d2 >= 2.0 == True)
        #Check <= float
        results.append(2.0 <= d2 == True)
        #Check >  float
        results.append(d2 > 2.0 == True)
        #Check <  float
        results.append(2.0 < d2 == True)
        #Check += int
        d1 += 2
        results.append(d1.val == 4.0)
        #Check -= int
        d1 -= 2
        results.append(d1.val == 2.0)
        #Check += USDollar
        d1 += d2
        results.append(d1.val == 102.0)
        #Check -= USDollar
        d1 -= d2
        results.append(d1.val == 2.0)
        self.printResults(results,'uSDTests')

    def runTests(self):
        self.filterTests()
        self.matchBracketTests()
        self.wordCountsTests()
        self.nthPrimeTests()
        self.uSDTests()
        print "{}/{} => {}".format(self.score,self.total,100.0*float(self.score/self.total))

#t = Tester()
#t.runTests()
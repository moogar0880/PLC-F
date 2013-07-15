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
    if 'primes' not in locals():
        primes = [2]
        locals()['primes'] = primes
    if len(locals()['primes']) >= n:
        return locals()['primes'][n]
    else:
        counter = index = len(locals()['primes'])
        for i in range(locals()['primes'][index-1],n+1):
            if i not in primes:
                print i
                for nextPrime in range(i*i,n+1,i):
                    primes.append(nextPrime)
                    counter += 1
                    print "{},{}".format(counter,n)
                    if counter == n:
                        print nextPrime
                        return nextPrime
        for i in xrange(2, n+1):
            if i not in multiples:
                print i
                for j in xrange(i*i, n+1, i):
                    multiples.append(j)
    
class USDollar(object):
    """docstring for USDollar"""
    def __init__(self,value=0.0):
        super(USDollar, self).__init__()
        self.value = round(value,2)
        #print self.__dict__

    def __repr__(self):
        return "${0:.2f}".format(float(self.value))

    def __add__(self,other): #+
        if isinstance(other,float):
           return USDollar(round(self.value,2) + round(other,2))
        elif isinstance(other,int):
           return USDollar(round(self.value,2) + round(other,2))
        elif isinstance(other,USDollar):
           return USDollar(round(self.value,2) + round(other.value,2))
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __sub__(self,other): #-
        if isinstance(other,float):
           return round(self.value,2) - round(other,2)
        elif isinstance(other,int):
           return round(self.value,2) - round(float(other),2)
        elif isinstance(other,USDollar):
           return round(self.value,2) - round(other.value,2)
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __iadd__(self,other): #+=
        if isinstance(other,float):
           self.value += round(other,2)
           return self
        elif isinstance(other,int):
           self.value += round(float(other),2)
           return self
        elif isinstance(other,USDollar):
           self.value += round(other.value,2)
           return self
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __isub__(self,other): #-=
        if isinstance(other,float):
           self.value -= round(other,2)
           return self
        elif isinstance(other,int):
           self.value -= round(float(other),2)
           return self
        elif isinstance(other,USDollar):
           self.value -= round(other.value,2)
           return self
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __eq__(self,other): # ==
        if isinstance(other,float):
           return self.value == round(other,2)
        elif isinstance(other,int):
           return self.value == round(float(other),2)
        elif isinstance(other,USDollar):
           return self.value == other.value
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __le__(self,other): #<=
        if isinstance(other,float):
           return self.value <= round(other,2)
        elif isinstance(other,int):
           return self.value <= round(float(other),2)
        elif isinstance(other,USDollar):
           return self.value <= other.value
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __ge__(self,other): #>=
        if isinstance(other,float):
           return self.value >= round(other,2)
        elif isinstance(other,int):
           return self.value >= round(float(other),2)
        elif isinstance(other,USDollar):
           return self.value >= other.value
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __lt__(self,other): #<
        if isinstance(other,float):
           return self.value < round(other,2)
        elif isinstance(other,int):
           return self.value < round(float(other),2)
        elif isinstance(other,USDollar):
           return self.value < other.value
        else:
           raise TypeError("{} is an invalid type".format(type(other)))

    def __gt__(self,other): #>
        if isinstance(other,float):
           return self.value > round(other,2)
        elif isinstance(other,int):
           return self.value > round(float(other),2)
        elif isinstance(other,USDollar):
           return self.value > other.value
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
        self.printResults(results,'nthPrimeTests')

    def uSDTests(self):
        results = []
        d1 = USDollar()
        d1.value = 2.001
        d2 = USDollar(100.0)
        #Check innate rounding, d1.value == 2.0
        results.append(d1.value == 2.0)
        #Check __repr__
        results.append(d1.__repr__() == '$2.00')
        #Check += float
        d1 += 2.0
        results.append(d1.value == 4.0)
        #Check -= float
        d1 -= 2.0
        results.append(d1.value == 2.0)
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
        results.append(d1.value == 4.0)
        #Check -= int
        d1 -= 2
        results.append(d1.value == 2.0)
        #Check += USDollar
        d1 += d2
        results.append(d1.value == 102.0)
        #Check -= USDollar
        d1 -= d2
        results.append(d1.value == 2.0)
        self.printResults(results,'uSDTests')
    
    def runTests(self):
        self.filterTests()
        self.matchBracketTests()
        self.wordCountsTests()
        self.nthPrimeTests()
        self.uSDTests()
        print "{}/{} => {}".format(self.score,self.total,100.0*float(self.score/self.total))

t = Tester()
t.runTests()
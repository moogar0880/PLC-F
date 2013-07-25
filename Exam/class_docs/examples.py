class A:
  def __init__(self, name):
    self.name = name
  def __eq__(self, other):
    if isinstance(other, A):
      print "A"
      return self.name == other.name
    if isinstance(other, str):
      print "string"
      return self.name == other

a = A("test")
b = A("test")
c = A("not")
print b == a
print c == a
print a == "test"

class C:
    def __init__(self):
        self._x = None
    def getx(self):
        return self._x
    def setx(self, value):
        self._x = (value-1)
    def delx(self):
        del self._x
    x = property(getx, setx, delx, "X coordinate.")

c = C()
c.x = 3
print "x was actually", c.x

def fibonacci():
  c=0
  n=1
  while(True):
    yield c
    n,c=n+c,n
    

f = fibonacci()
print f.next()
print f.next()
print f.next()

for x in f:
  if x > 100: break
  else: print x



def factorial(n):
  def fact(n):
    if n in factorial.facts:
      return factorial.facts[n]
    else:
      ret = n*fact(n-1)
      factorial.facts[n] = ret
      return ret
  return fact(n)
  
factorial.facts = {1:1}

#OR

def memoize(fn):
  fn.facts = {}
  def wrapped(n):
    if n in fn.facts:
      return fn.facts[n]
    else:
      ret = fn(n)
      fn.facts[n] = ret
      return ret
  return wrapped

@memoize
def fact(n):
  if n == 1:
    return 1
  else:
    return n*fact(n-1)

#OR

class Memoize:
  def __init__(self, method):
    self.fn = method
    method.facts = {}
  def __call__(self, n):
    if n in self.fn.facts:
      return self.fn.facts[n]
    else:
      ret = self.fn(n)
      self.fn.facts[n] = ret
      return ret

@Memoize
def fact(n):
  if n == 1:
    return 1
  else:
    return n*fact(n-1)

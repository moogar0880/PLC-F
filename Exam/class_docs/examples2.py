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

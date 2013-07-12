def filter(f, l=[]):
	toRet = []
	for item in l:
		if f(item):
			toRet.append(item)
	return toRet

def matchBrackets(s):
	print s

def wordCounts(s):
	toRet = {}
	for word in s.split():
		if word in toRet.keys():
			toRet[word] = toRet[word] + 1
		else:
			toRet[word] = 1
	return toRet

def nthPrime(n):
	print n

class USDollar(object):
	"""docstring for USDollar"""
	def __init__(self,value=0.0):
		super(USDollar, self).__init__()
		self.value = round(value,2)

	def __repr__(self):
		return "{0:.2f}".format(float(self.value))

	def __add__(self,other): #+
		if isinstance(other,float):
			return USDollar(self.value + round(other,2))
		elif isinstance(other,int):
			return USDollar(self.value + round(other,2))
		elif isinstance(other,USDollar):
			return USDollar(self.value + round(other.value,2))
		else:
			raise TypeError("{} is an invalid type".format(type(other)))

	def __sub__(self,other): #-
		if isinstance(other,float):
			return self.value - round(other,2)
		elif isinstance(other,int):
			return self.value - round(float(other),2)
		elif isinstance(other,USDollar):
			return self.value - other.value
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
			self.value += other.value
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
			self.value -= other.value
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

d = USDollar()
d.value = 2.001
print type(d)
d += 2.0
print d
d += 2
print d
d2 = USDollar()
d2.value = 100.0
d += d2
print d
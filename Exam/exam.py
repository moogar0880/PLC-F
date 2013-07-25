def binSearch ( val, l ):
	for i in l:
		if i[0] == val:
			return i[1]
	return None
	
class A:
	def __init__(self):
		self.l = []
		
	def __getitem__(self,x):
		return self.l[x]
		
	def __iadd__(self,li):
		for i in li:
			self.l.append(i)
		return self

#a = A()
#a += [1,2,3]
#a += [4,5,6]
#print a[0]
#print a[1]

#print binSearch(3,[(1,"cat"),(3,"dog")])

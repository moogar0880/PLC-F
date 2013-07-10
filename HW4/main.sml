exception NotImplemented
exception Found of int

datatype 'a permutationSeq = Cons of 'a option * (unit -> 'a permutationSeq)

(* Returns one of our permutationSeq structures that can lazily generate all
   permutations of the list given to it
   val permutation = fn : 'a list -> 'a permutationSeq 30pts *)
fun permutation l =
  let
    (* Get the next permutation of a list *)
    fun getNextPerm lst =
      let
        (* Search by index *)
        fun find ([],_) = []
          | find (((x,y)::rol),v) = if x = v then ((x,y)::rol) else find(rol,v);
        (* Find largest value that is less than the value next to it  *)
        fun getPos (((x1,v1)::(x2,v2)::[]),i)  = if x1 < x2 andalso x1 > i then x1 else i
          | getPos (((x1,v1)::(x2,v2)::rol),i) = if x1 < x2 andalso x1 > i then getPos((x2,v2)::rol, x1) else getPos((x2,v2)::rol,i);
        (*val lst = [(0,0),(1,1),(2,2)];*)
        (* Simple sorting method for lists/tuples *)
        fun sort []  = []
          | sort [v] = [v]
          | sort ((v,w)::(x,z)::rol) = if v >= x then sort ((x,z)::(sort ((v,w)::rol))) else (v,w)::(sort ((x,z)::rol))
        (* Return the value with the smallest value that is bigger than v *)
        fun nextLargest ((x::[]),v)  = x
          | nextLargest ((x::rol),v) = hd (tl((sort(rol))));
        (* Swap two elements of a list *)
        fun swap ([],_,_) = []
          | swap ((x::rol),v1,v2) = if x = v1 then v2::swap(rol,v1,v2) else if x = v2 then v1::swap(rol,v1,v2) else x::swap(rol,v1,v2);
        (* Get the index of a tuple index in a list *)
        fun listIndex (((x,_)::rol),i,v) = if x = v then i else listIndex(rol,i+1,v);
        (* Given an index, return the tuple at that index *)
        fun getTuple(((x,y)::rol),i) = if x = i then (x,y) else getTuple(rol,i);
        (* Get a list of all indexes, in order of appearance in list *)
        fun indexList [] = []
          | indexList ((x,_)::rol) = x::indexList(rol);
        (* Determine if there are no more permutations *)
        fun done lst = if List.rev(lst) = List.tabulate(List.length(lst), fn x => x) then true else false
        val pos     = getPos(lst,~1);
        val newList = swap(lst,getTuple(lst,pos),nextLargest(lst,pos));
      in
        List.take(newList,listIndex(newList,0,pos))@sort(find(newList,pos))
      end
    fun perm lst = Cons(SOME lst, fn() => perm(getNextPerm lst))
  in
      perm(ListPair.zip(List.tabulate(List.length(l), fn x => x),l))
  end

(* Gets the next permutation in the sequence
   val next = fn : ’a permutationSeq -> ’a option 10pts *)
fun next s =
  let
    fun getValueList [] = []
      | getValueList ((_,y)::rol)   = y::getValueList(rol)
    fun getNext (Cons(NONE, ros))   = getNext(ros())
      | getNext (Cons(SOME x, ros)) = SOME (getValueList(x))
  in
      getNext(s)
  end

(* Gets the permutationSeq representing the rest of the possible permutations
   val rest = fn : ’a permutationSeq -> ’a permutationSeq 10pts *)
fun rest s =
  let
    fun getRest (Cons(NONE, ros))   = getRest(ros())
      | getRest (Cons(SOME x, ros)) = ros()
  in
      getRest(s)
  end

(* One by one print all the permutations represented by a string permutationSeq
   val printPermutations = fn : string permutationSeq -> unit 5pts
fun printPermutations s =
  let
    fun done lst = if List.rev(lst) = List.tabulate(List.length(lst), fn x => x) then true else false
    fun valueList [] = []
      | valueList ((_,y)::rol) = y::valueList(rol)
    fun listPrint ((_,y : string)::[])  = y
      | listPrint ((_,y : string)::rol) = y^", "^listPrint(rol)
    fun printHelper (Cons(NONE, ros)) = printHelper(ros())
      | printHelper (Cons(SOME x,ros)) = if done(valueList(x)) then "["^listPrint(x)^"]" else "["^listPrint(x)^"], "^printHelper(rest s)
  in
    print(printHelper s)
  end*)

(* ================================EXTRA CREDIT================================
   Takes a ’a permutationSeq and a function fn : ’a -> bool that will give the
   ﬁrst result from the sequence that matches the criteria for the boolean
   function, or NONE if no such permutation exists. The return from this
   function must be a tuple of the found value (or NONE) and the remainder of
   the sequence after the found value
   val find = (’a -> bool) -> ’a permutationSeq -> ’a * ’a permutationSeq 10pts *)
fun find f s =
  let
    fun finder (Cons(NONE, ros)) = finder (ros())
      | finder (Cons(SOME (a,b), ros)) = if f(b) then (SOME b, ros()) else finder(ros())
  in
    finder(s)
  end
(* ========================================================================== *)

(* Estimates the integral of function f over the range from x1 to x2 using the
   Riemann sum with increments of .1
   val integral = fn : (fn : real -> real) -> real -> real -> real 20pts *)
fun integral f = raise NotImplemented

(* returns a function which will behave the same as integral but will use stored
   information on previously calculated integral values. For many cases, this should
   perform more quickly than subsequent calls to integral on the same value of f.
   It must at least make use of information for the individual “rectangles”, but can
   also make use of information about the ranges and increase performance in some cases
   val integralMem = (fn : real -> real) -> fn : real -> real -> real 25pts *)
fun integralMem f = raise NotImplemented
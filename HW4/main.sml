exception NotImplemented

datatype 'a permutationSeq = Cons of 'a option * (unit -> 'a permutationSeq)

(* Returns one of our permutationSeq structures that can lazily generate all
   permutations of the list given to it 
   val permutation = fn : 'a list -> 'a permutationSeq 30pts *)
fun permutation [] = Cons(NONE, fn() => permutation [])
  | permutation l =
  let
    fun convert (_,[]) = []
      | convert (i,(x::rol)) = (i, x)::convert(i+1, rol)
    fun perm lst = Cons(SOME lst, fn() => perm lst)
  in
      perm (convert(0,l))
  end

(* Gets the next permutation in the sequence 
   val next = fn : ’a permutationSeq -> ’a option 10pts *)
fun next s = 
  let
    fun getNext (Cons(NONE, ros))   = getNext(ros())
      | getNext (Cons(SOME x, ros)) = SOME x
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
    fun printHelper (s) = (next s)^", "^printHelper(rest s)
  in
    print(printHelper(s))
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
      | finder (Cons(SOME x, ros)) = if f(x) then (SOME x, ros()) else finder(ros())
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
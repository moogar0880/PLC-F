exception NotImplemented

datatype 'a permutationSeq = Cons of 'a option * (unit -> 'a permutationSeq)

(* Returns one of our permutationSeq structures that can lazily generate all
   permutations of the list given to it 
   val permutation = fn : 'a list -> 'a permutationSeq *)
fun permutation l =
  let 
    fun perm (x::[]) = Cons(SOME x, fn() => perm([]))
      | perm (x::rol) = Cons(SOME x, fn() => perm(rol))
  in
      perm l
  end

(* Gets the next permutation in the sequence 
   val next = fn : ’a permutationSeq -> ’a option *)
fun next s = 
  let
    fun getNext (Cons(NONE, ros))   = getNext(ros())
      | getNext (Cons(SOME x, ros)) = SOME x
  in
      getNext(s)
  end

(* Gets the permutationSeq representing the rest of the possible permutations 
   val rest = fn : ’a permutationSeq -> ’a permutationSeq *)
fun rest s = 
  let
    fun getRest (Cons(NONE, ros))   = getRest(ros())
      | getRest (Cons(SOME x, ros)) = ros()
  in
      getRest(s)
  end

(* One by one print all the permutations represented by a string permutationSeq 
   val printPermutations = fn : string permutationSeq -> unit *)
fun printPermutations s = 
  let
      fun printHelper s = print(next s)^printHelper(rest s)
  in
    printHelper(s)
  end

(* ================================EXTRA CREDIT================================
   Takes a ’a permutationSeq and a function fn : ’a -> bool that will give the
   ﬁrst result from the sequence that matches the criteria for the boolean
   function, or NONE if no such permutation exists. The return from this
   function must be a tuple of the found value (or NONE) and the remainder of
   the sequence after the found value 
   val find = (’a -> bool) -> ’a permutationSeq -> ’a * ’a permutationSeq *)
fun find f s = raise NotImplemented
(* ========================================================================== *)

(* Estimates the integral of function f over the range from x1 to x2 using the
   Riemann sum with increments of .1 
   val integral = fn : (fn : real -> real) -> real -> real -> real *)
fun integral f = raise NotImplemented

(* returns a function which will behave the same as integral but will use stored 
   information on previously calculated integral values. For many cases, this should
   perform more quickly than subsequent calls to integral on the same value of f. 
   It must at least make use of information for the individual “rectangles”, but can
   also make use of information about the ranges and increase performance in some cases 
   val integralMem = (fn : real -> real) -> fn : real -> real -> real *)
fun integralMem f = raise NotImplemented
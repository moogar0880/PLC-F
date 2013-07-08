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

(* Gets the next permutation in the sequence *)
fun next s = raise NotImplemented

(* Gets the permutationSeq representing the rest of the possible permutations *)
fun rest s = raise NotImplemented

(* One by one print all the permutations represented by a string
   permutationSeq *)
fun printPermutations s = raise NotImplemented

(* ================================EXTRA CREDIT================================
   Takes a ’a permutationSeq and a function fn : ’a -> bool that will give the
   ﬁrst result from the sequence that matches the criteria for the boolean
   function, or NONE if no such permutation exists. The return from this
   function must be a tuple of the found value (or NONE) and the remainder of
   the sequence after the found value *)
fun find f s = raise NotImplemented
(* ========================================================================== *)

(* Estimates the integral of function f over the range from x1 to x2 using the
   Riemann sum with increments of .1 *)
fun integral f = raise NotImplemented

(* returns a function which will behave the same as integral
but will use stored information on previously calculated integral values. For many cases, this should
perform more quickly than subsequent calls to integral on the same value of f. It must at least make
use of information for the individual “rectangles”, but can also make use of information about the
ranges and increase performance in some cases *)
fun integralMem f = raise NotImplemented
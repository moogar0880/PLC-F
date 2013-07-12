exception NotImplemented
exception Error of string

datatype 'a permutationSeq = Cons of 'a option * (unit -> 'a permutationSeq)

fun nullSeq lst = Cons(NONE, fn() => nullSeq lst)

(* Returns one of our permutationSeq structures that can lazily generate all
   permutations of the list given to it
   val permutation = fn : 'a list -> 'a permutationSeq 30pts *)
fun permutation [] = Cons(SOME [], fn() => nullSeq [])
  | permutation l = if List.length(l) = 1 then Cons(SOME [(0,(hd l))], fn() => nullSeq []) else
  let
    (* Get the next permutation of a list *)
    fun getNextPerm lst =
      let
        (* Search by index *)
        fun find ([],_) = []
          | find (((x,y)::rol),v) = if x = v then ((x,y)::rol) else find(rol,v);
        (* Search by list index, not entries index *)
        fun findByLI ([],_,_) = []
          | findByLI ((x::rol),v,i) = if i = v then (x::rol) else findByLI(rol,v,i+1)
        (* Find largest value that is less than the value next to it  *)
        fun getPos ([],i)                      = i (*Should never happen*)
          | getPos (((x1,v1)::[]),_)           = x1 (* Should only happen when permutation is called on a list of length 1 *)
          | getPos (((x1,v1)::(x2,v2)::[]),i)  = if x1 < x2 andalso x1 > i then x1 else i
          | getPos (((x1,v1)::(x2,v2)::rol),i) = if x1 < x2 andalso x1 > i then getPos((x2,v2)::rol, x1) else getPos((x2,v2)::rol,i)
        (* Simple sorting method for lists/tuples *)
        fun sort []  = []
          | sort [v] = [v]
          | sort ((v,w)::(x,z)::rol) = if v >= x then sort ((x,z)::(sort ((v,w)::rol))) else (v,w)::(sort ((x,z)::rol));
        (* Return the value with the smallest value that is bigger than v *)
        fun nextLargest (((x,y)::[]),v)  = (x,y)
          | nextLargest (((x,y)::rol),v) =
          let
            val sorted = sort(rol);
            val value  = #1 (hd sorted);
          in
            if value > x andalso value <> v then hd sorted else nextLargest(sorted,v)
          end
        (* Swap two elements of a list *)
        fun swap ([],_,_) = []
          | swap ((x::rol),v1,v2) = if x = v1 then v2::swap(rol,v1,v2) else if x = v2 then v1::swap(rol,v1,v2) else x::swap(rol,v1,v2)
        (* Get the index of a tuple index in a list *)
        fun listIndex ([],i,_)           = i (*Should never happen*)
          | listIndex (((x,_)::rol),i,v) = if x = v then i else listIndex(rol,i+1,v)
        (* Given an index, return the tuple at that index *)
        fun getTuple (((x,y)::[]),i)  = (x,y) (*Should never happen*)
          | getTuple (((x,y)::rol),i) = if x = i then (x,y) else getTuple(rol,i)
        (* Get a list of all indexes, in order of appearance in list *)
        fun indexList [] = []
          | indexList ((x,_)::rol) = x::indexList(rol)
        (* Determine if there are no more permutations *)
        fun done []  = true
          | done lst = if List.rev(lst) = List.tabulate(List.length(lst), fn x => x) then true else false
        val pos      = getPos(lst,~1)
        val nLargest = nextLargest(lst,pos)
        val newList  = swap(lst,getTuple(lst,pos),nLargest)
        val li       = listIndex(newList,0,(#1 nLargest))
        val pivot    = tl(findByLI(newList,li,0))
        val nextPerm = List.take(newList,li+1)@sort(pivot)
      in
        if done(indexList(nextPerm)) then Cons(SOME nextPerm, fn() => nullSeq lst) else Cons(SOME nextPerm, fn() => getNextPerm nextPerm)
      end
    fun perm lst = Cons(SOME lst, fn() => getNextPerm lst)
  in
    perm(ListPair.zip(List.tabulate(List.length(l), fn x => x),l))
  end

(* Gets the next permutation in the sequence
   val next = fn : ’a permutationSeq -> ’a option 10pts *)
fun next s =
  let
    fun getNext (Cons(NONE, ros))   = NONE
      | getNext (Cons(SOME x, ros)) = SOME (#2 (ListPair.unzip(x)))
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
   val printPermutations = fn : string permutationSeq -> unit 5pts *)
fun printPermutations s =
  let
    fun listPrint []       = ""
      | listPrint (s::[])  = s
      | listPrint (s::rol) = s^", "^listPrint(rol)
    fun removeOption (SOME opt) = opt
      | removeOption  NONE      = raise Error "Error in printPermutations"
    fun printHelper s = if next(s) <> NONE then "["^listPrint(removeOption (next s))^"], "^printHelper(rest(s)) else ""
  in
    print(printHelper s)
  end

(* ================================EXTRA CREDIT================================
   Takes a ’a permutationSeq and a function fn : ’a -> bool that will give the
   ﬁrst result from the sequence that matches the criteria for the boolean
   function, or NONE if no such permutation exists. The return from this
   function must be a tuple of the found value (or NONE) and the remainder of
   the sequence after the found value
   val find = (’a -> bool) -> ’a permutationSeq -> ’a * ’a permutationSeq 10pts *)
fun find f s =
  let
    fun finder (Cons(NONE, ros)) = (NONE, nullSeq [])
      | finder (Cons(SOME (a,b), ros)) = if f(b) then (SOME b, ros()) else finder(ros())
  in
    finder(s)
  end
(* ========================================================================== *)

(* Estimates the integral of function f over the range from x1 to x2 using the
   Riemann sum with increments of .1
   val integral = fn : (fn : real -> real) -> real -> real -> real 20pts *)
fun integral f x1 x2 = if Real.<(x2,x1) then 0.0 else
  let
    val stopAt = Real.round(Real.*(10.0,Real.-(x2,x1)))
    fun integrate (cur,sum,count) = if count >= stopAt then sum else integrate((cur + 0.1), (sum + f(cur) * 0.1),Int.+(count,1))
  in
    integrate(x1,0.0,0)
  end

(* Returns a function which will behave the same as integral but will use stored
   information on previously calculated integral values. For many cases, this should
   perform more quickly than subsequent calls to integral on the same value of f.
   It must at least make use of information for the individual “rectangles”, but can
   also make use of information about the ranges and increase performance in some cases
   val integralMem = (fn : real -> real) -> fn : real -> real -> real 25pts *)
fun integralMem f =
  let
    (* Hashtable Implementation *)
    val size = 1000

    (* Super simple hash function *)
    fun hash v = Real.round(Real.rem(v,Real.fromInt(size)))

    (* Adding Functions *)
    fun addToBucket ((key,value),[])       = [(key,value)]
      | addToBucket ((key,value),(x::rol)) = if Real.==(value,#2 x) then x::rol else x::addToBucket((key,value),rol);

    fun insert(key,value,A) =
      let
        val bucket = hash(key)
        val L = Array.sub(A,bucket)
      in
        Array.update(A,bucket,addToBucket((key,value),L));
        value
      end

    (* Lookup Functions *)
    fun search(v,[])     = false
      | search(v,(k,_)::rol) = if Real.==(v,k) then true else search(v,rol)

    fun lookup(x,A) = search(x,Array.sub(A,hash(x)))

    (* Get Functions - Must make call to lookup first, get will not check to ensure key, value pair exists *)
    fun find(v,[])          = v (*Should never get here*)
      | find(v,(k,va)::rol) = if Real.==(v,k) then va else find(v,rol)

    fun get(v,A) = find(v,Array.sub(A,hash(v)))

    (* Hashtable Variable *)
    val table = Array.array(size, [] : (real * real) list)
    fun memIntegral x1 x2 =
      let
        val stopAt = Real.round(Real.*(10.0,Real.-(x2,x1)))
        fun integrate (cur,sum,count) =
          if count >= stopAt then sum
          else if lookup(cur,table) then integrate((cur + 0.1), (sum + get(cur,table) * 0.1),Int.+(count,1))
          else integrate((cur + 0.1), (sum + insert(cur,f(cur),table) * 0.1),Int.+(count,1))
      in
          integrate(x1,0.0,0)
      end
  in
    memIntegral
  end
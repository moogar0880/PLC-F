exception NotImplemented
(* val modulo = fn : (int * int) -> int *)
fun modulo (_,0) = 0
  | modulo (0,_) = 0
  | modulo (x,y) = if y > x then x else
  let
      fun getMod(z) = if z >= y then getMod(z-y) else z
  in
      getMod(x)
  end

(* val andEval = fn : (bool * bool) -> bool *)
fun andEval (x,y) = if (x() andalso y()) andalso (y() andalso x()) then true else false

(* val orEval = fn : (bool * bool) -> bool *)
fun orEval (x,y) = if (x() orelse y()) andalso (y() orelse x()) then true else false

(* val nOutOfOrder = fn : (('a * 'a) -> bool) -> a' list -> int *)
fun nOutOfOrder opr [] = 0
  | nOutOfOrder opr l  =
  let
      fun orderCount (i,x,[]) = i
        | orderCount (i,x,rol) =
      if opr(x, hd rol) then orderCount(i,hd rol,tl rol)
      else orderCount(i+1,hd rol,tl rol)
   in
       orderCount(0,hd l, tl l)
   end

(* val permutations = fn : 'a list -> 'a list list *)
fun permutations [] = [[]]
  | permutations lst =
  let
      fun append (x,[]) = x::[]
        | append (x,l) = x::l
      fun cycle (x,[]) = [[x]]
        | cycle (x,l) = (x::l)::(List.map (fn l2 => append(hd l,l2)) (cycle (x,tl l)))
      fun permute [] = [[]]
        | permute l = List.concat(List.map(fn l2 => cycle (hd l,l2)) (permute (tl l)))
  in
      permute(lst)
  end

(* val combinarions = fn : (int * 'a list) -> 'a list list *)
fun combinations (0,l)   = [[]]
  | combinations (i,[])  = [[]]
  | combinations (i,lst) =
  let
      fun easy [] = []
        | easy l  = List.take(l,i)::easy(tl l)
        handle Subscript => easy []
      fun permute [] = []
        | permute (h::rol) = (permutations h)@(permute rol)
  in
      permute(easy(lst))
  end

(* val bestAlign = fn : (string * string) -> (string * int) *)
fun bestAlign "" "" = ("",0)

(* val printAlign = fn : string -> string -> string -> int -> () *)
fun printAlign s1 s2 match shift = 
  let
    fun printSpaces(0) = ""
      | printSpaces(i) = if i > 0 then " "^printSpaces(i-1) else " "^printSpaces(i+1)
  in
    if shift >= 0 then print(s1^"\n"^printSpaces(shift)^s2^"\n"^printSpaces(shift)^match)
    else print(printSpaces(shift)^s1^"\n"^s2^"\n"^printSpaces(shift)^match)
  end

(* -----------------------EXTRA CREDIT----------------------- *)
(* val commonFactors = fn : (int * int) -> (int * int) list *)
fun commonFactors (x,y) = 
  let
      fun getEx (x,y,z) = if x mod y = 0 then getEx(x div y, y, z+1) else z
      fun pow (a,0) = 1
        | pow (a,1) = a
        | pow (a,b) = a * pow (a,b-1)
      fun factorize (n,i) = if i > n then [] else
        if n mod i = 0 then (i,getEx(n,i,0))::factorize(n div (pow(i,getEx(n,i,0))),i+1) else factorize(n,i+1)
      fun search (_,[]) = []
        | search ((a,b), (c,d)::rol) = if a = c andalso b < d then [(a,b)] 
                                       else if a = c andalso d < b then [(a,d)] 
                                       else search((a,b), rol)
      fun inCommon ([],_) = []
        | inCommon (_,[]) = []
        | inCommon (x::rol,li) = search(x,li)@inCommon(rol,li)
  in
      inCommon(factorize(x,2),factorize(y,2))
  end

(* val lcm = fn : (int * int) ->int *)
fun lcm (0,0) = 0
  | lcm (x,y) = 
  let
    fun pullOut (a,b) = a
  in
    pullOut(hd (commonFactors(x,y)))
    handle Empty => if x = y then x else 0
  end
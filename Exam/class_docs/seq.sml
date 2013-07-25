datatype 'a seq = Cons of 'a option * (unit -> 'a seq);

fun tabulate f =
    let fun tab n = Cons(SOME (f n), (fn () => tab (n+1)))
    in tab 0
    end

fun hd (Cons(NONE, t)) = hd (t())
  | hd (Cons(SOME x, t)) = x

fun tl (Cons(SOME _, t)) = (t())
  | tl (Cons(NONE, t)) = tl (t())

fun first 0 _ = []
  | first n s = hd s :: first (n-1) (tl s)

fun nth (s,0) = hd s
  | nth (s,n) = nth (tl s, n-1)

fun filter f (Cons(SOME x, t)) =
    if f x then Cons(SOME x, (filter f o t))
    else Cons(NONE, (filter f o t))
  | filter f (Cons(NONE, t)) = Cons(NONE, (filter f o t))

val nat = tabulate (fn x => x)

fun sieve (Cons(NONE, t)) = Cons(NONE, (sieve o t))
  | sieve (Cons(SOME x, t)) =
    let fun ndivx y = y mod x <> 0
    in Cons(SOME x, (sieve o (filter ndivx) o t))
    end

val primes = sieve (tl (tl nat));

(* maps function f over the members of s, altering them before they're 
   lazily returned *)
fun map f (Cons(NONE, t)) = Cons(NONE, (map f o t))
  | map f (Cons(SOME x, t)) = Cons(SOME (f x), (map f o t))

(* returns true if this sequence contains an element staisfying f,
   false otherwise *)
fun exists f (Cons(NONE, t)) = exists f (t ())
  | exists f (Cons(SOME s, t)) = if (f s) then true
                           else exists f (t ())

fun exists f (Cons(NONE, t)) = exists f (t())
  | exists f s = ((f (hd s)) orelse exists f (tl s))

(* returns true if all members of s satisfy f *)
fun all f s = not (exists (fn x => not (f x)) s)

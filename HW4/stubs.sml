exception NotImplemented

datatype 'a permutationSeq = Cons of 'a option * (unit -> 'a permutationSeq)

fun permutation l = 
    let fun perm () = Cons(NONE, perm)
    in
        perm()
    end
fun next s = raise NotImplemented
fun rest s = raise NotImplemented
fun printPermutations s = raise NotImplemented
fun find f s = raise NotImplemented
fun integral f = raise NotImplemented
fun integralMem f = raise NotImplemented

datatype 'a binTree = EmptyTree | Node of 'a * 'a binTree * 'a binTree

exception EmptyBinTree

fun left (Node(_,l,_)) = l
  | left _ = raise EmptyBinTree

fun right (Node(_,_,r)) = r
  | right EmptyTree = raise EmptyBinTree

fun root (Node(x,_,_)) = x
  | root _ = raise EmptyBinTree

fun isEmpty EmptyTree = true
 | isEmpty _ = false

fun size tree =
    if isEmpty tree then 0
    else 1 + size (left tree) + size (right tree)

fun toListPre EmptyTree = []
  | toListPre (Node(x,l,r)) = x :: toListPre l @ toListPre r;

fun fromListPre [] = EmptyTree
  | fromListPre (x :: t) =
    let val l = length t div 2
        val a = List.take(t,l)
        val b = List.drop(t,l)
    in
        Node(x, fromListPre a, fromListPre b)
    end

(* returns us a node with value n *)
fun find (Node(v, l, r)) n = if v = n then (SOME (Node(v, l, r)))
                           else 
                               let val lRes = (find l n)
                                   val rRes = (find r n)
                               in 
                                   if lRes <> NONE then lRes
                                   else if rRes <> NONE then rRes
                                   else NONE
                               end
  | find EmptyTree _ = NONE

(* gets the highest value stored in tree t *)
(* fun maxNode t = *)

(* returns the largest depth from the root in tree t *)
(* fun maxDepth t = *)

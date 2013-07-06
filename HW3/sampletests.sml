use "stubs.sml";
use "main.sml";
use "tester.sml";

(* tests for modulo *)
fun test1 () = modulo (3, 3) = 0
fun test2 () = modulo (7, 3) = 1
fun test3 () = modulo (398623429, 7) = 1

(* tests for lcm *)
fun test4 () = lcm (2, 2) = 2
fun test5 () = lcm (24337349, 7) = 170361443

(* tests for andEval *)
infix andEval
local
    val x = ref 3;
    val decr = (fn () => x := !x-1)
    fun decrFalse () = (decr (); false)
    fun decrTrue () = (decr (); true)
in
fun test6 () = ((fn () => true) andEval (fn () => true)) = true
fun test7 () = let val orig = !x
               in
                   (decrTrue andEval decrTrue; !x = orig-2)
               end
end

(* tests for orEval *)
infix orEval
local
    val x = ref 3;
    val decr = (fn () => x := !x-1)
    fun decrFalse () = (decr (); false)
    fun decrTrue () = (decr (); true)
in
fun test8 () = ((fn () => true) orEval (fn () => true)) = true
fun test9 () = let val orig = !x
               in
                   (decrTrue orEval decrTrue; !x = orig-2)
               end
end

(* tests for nOutOfOrder *)
local
    fun strLess (s1, s2) =
        let fun isLess LESS = true
              | isLess _ = false
        in
            isLess (String.compare (s1, s2))
        end
    val strList = ["rat", "bat", "cat", "sat", "hat", "mat"]
in
fun test10 () = nOutOfOrder (op <) [] = 0
fun test11 () = nOutOfOrder (op <) [3] = 0
fun test12 () = nOutOfOrder (op <) [3,4,5,6] = 0
fun test13 () = nOutOfOrder (op <) [6,2,4,1,9,10] = 2
fun test14 () = nOutOfOrder strLess strList = 2
end

(* tests for permutations *)
fun sameList l1 l2 = 
    (List.length l1) = (List.length l2) andalso
    (List.all (fn x => (List.find (fn y => y = x) l2) <> NONE) l1)

fun test15 () = (permutations []) = [[]]
fun test16 () = sameList (permutations [1,2,3]) 
                [[1,2,3],[2,1,3],[2,3,1],[1,3,2],[3,1,2],[3,2,1]]

(* tests for combinations *)
fun sameCombs l1 l2 =
    (List.length l1) = (List.length l2) andalso
    (List.all (fn x => (List.find (fn y => (sameList x y)) l2) <> NONE) l1)

fun test17 () = combinations (0, [6,7,8,9]) = [[]]
fun test18 () = combinations (3, []) = [[]]
fun test19 () = sameCombs (combinations (2, [1,2,3])) [[1,2],[1,3],[2,3]]

(* tests for commonFactors *)
fun test20 () = commonFactors (11, 29) = []
fun test21 () = commonFactors (9,27) = [(3,2)]
fun test22 () = commonFactors (450, 540) = [(2,1),(3,2),(5,1)]

(* tests for bestAlign *)
fun test23 () = bestAlign "" "" = ("", 0)
fun test24 () = bestAlign "" "test" = ("", 0)
fun test25 () = bestAlign "same" "same" = ("same", 0)
fun test26 () = bestAlign " space" "space" = ("space", ~1)
fun test27 () = bestAlign "the quick fox jumped over the lazy dog"
                          "the quacking duck" = ("the qu*ck", 0)

(* tests for printAlign WILL NEED TO BE RUN MANUALLY TO SEE *)
fun test28 () = (printAlign "." "." "." 0; true)
fun test29 () = (printAlign "these cats are a menace" "cats ate mice" 
                            "cats a*e " ~6; true)

val allTests =
[(1, "modulo (3, 3)", test1),
(1, "modulo (7, 3)", test2),
(1, "modulo (398623429, 7)", test3),
(1, "lcm (2, 2)", test4),
(1, "lcm (24337349, 7)", test5),
(1, "andEval true and true", test6),
(1, "andEval with decrement (both true)", test7),
(1, "orEval true and true", test8),
(1, "orEval with decrement (both true)", test9),
(1, "nOutOfOrder (op <) []", test10),
(1, "nOutOfOrder (op <) [3]", test11),
(1, "nOutOfOrder (op <) [3,4,5,6]", test12),
(1, "nOutOfOrder (op <) [6,2,4,1,9,10]", test13),
(1, "nOutOfOrder with strings", test14),
(1, "permutations of empty list", test15),
(1, "permutations for [1,2,3]", test16),
(1, "combinations (0, [6,7,8,9])", test17),
(1, "combinations (3, [])", test18),
(1, "combinations (2, [1,2,3])", test19),
(1, "commonFactors of primes", test20),
(1, "commonFactors (9,27)", test21),
(1, "commonFactors (450, 540)", test22),
(1, "bestAlign with empty strings", test23),
(1, "bestAlign perfect match", test24),
(1, "bestAlign with one empty string", test25),
(1, "bestAlign perfect match, except one shift", test26),
(1, "bestAlign with longer strings", test27),
(1, "printAlign with no shift", test28),
(1, "printAlign with negative shift", test29)]

val _ = Grading.saveToFile 100 (Grading.run allTests) "sampletests.res"

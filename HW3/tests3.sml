use "stubs.sml";
use "main.sml";
use "tester.sml";

local
    val s = ref ""
    fun pr x = s := !s ^ x
    val print = SMLofNJ.Internals.prHook
    val truePrint = !print
in
fun getString f x =
    (s := "";
     print := pr;
     f x;
     print := truePrint;
     !s)
end

(* tests for modulo (5 pts) *)
(* 1 *) fun test1 () = modulo (2, 2) = 0
(* 1 *) fun test2 () = modulo (0, 12) = 0
(* 1 *) fun test3 () = modulo (17, 2) = 1
(* 2 *) fun test4 () = modulo (398623429, 5) = 4

(* tests for lcm (5 pts) *)
(* 1 *) fun test5 () = lcm (2, 2) = 2
(* 1 *) fun test6 () = lcm (3, 5) = 15
(* 1 *) fun test7 () = lcm (12, 2) = 12
(* 2 *) fun test8 () = lcm (24337349, 5) = 121686745

(* tests for andEval (5 pts) *)
infix andEval
local
    val x = ref 3;
    val incr = (fn () => x := !x+1)
    fun incrFalse () = (incr (); false)
    fun incrTrue () = (incr (); true)
in
(* 1 *) fun test9 () = ((fn () => true) andEval (fn () => true)) = true
(* 1 *) fun test10 () = ((fn () => false) andEval (fn () => true)) = false
(* 1 *) fun test11 () = let val orig = !x
                        in
                            (incrTrue andEval incrTrue; !x = orig+2)
                        end
(* 2 *) fun test12 () = let val orig = !x
                        in
                            (incrFalse andEval incrTrue; !x = orig+2)
                        end
end

(* tests for orEval (5 pts) *)
infix orEval
local
    val x = ref 3;
    val incr = (fn () => x := !x+1)
    fun incrFalse () = (incr (); false)
    fun incrTrue () = (incr (); true)
in
(* 1 *) fun test13 () = ((fn () => false) orEval (fn () => true)) = true
(* 1 *) fun test14 () = ((fn () => false) orEval (fn () => false)) = false
(* 1 *) fun test15 () = let val orig = !x
                        in
                            (incrFalse orEval incrTrue; !x = orig+2)
                        end
(* 2 *) fun test16 () = let val orig = !x
                        in
                            (incrTrue orEval incrTrue; !x = orig+2)
                        end
end

(* tests for nOutOfOrder (10 pts) *)
local
    fun xor (a,b) = (a andalso not b) orelse (b andalso not a)
    val boolList = [true, true, true, false, true, false, false]
in
(* 1 *) fun test17 () = nOutOfOrder (op <) [] = 0
(* 1 *) fun test18 () = nOutOfOrder (op <) [3] = 0
(* 1 *) fun test19 () = nOutOfOrder (op <) [3,4,5,6] = 0
(* 1 *) fun test20 () = nOutOfOrder (op <) [3,2] = 1
(* 2 *) fun test21 () = nOutOfOrder (op <) [6,2,4,1,9,10] = 2
(* 2 *) fun test22 () = nOutOfOrder (op >) [6,2,4,1,9,10] = 3
(* 2 *) fun test23 () = nOutOfOrder xor boolList = 3
end

(* tests for permutations (15 pts) *)
val r1 = [[1,2,3,4],[1,2,4,3],[1,3,2,4],[1,3,4,2],[1,4,2,3],[1,4,3,2],[2,1,3,4],[2,1,4,3],[2,3,1,4],[2,3,4,1],[2,4,1,3],[2,4,3,1],[3,1,2,4],[3,1,4,2],[3,2,1,4],[3,2,4,1],[3,4,1,2],[3,4,2,1],[4,1,2,3],[4,1,3,2],[4,2,1,3],[4,2,3,1],[4,3,1,2],[4,3,2,1]]

fun sameList l1 l2 = 
    (List.length l1) = (List.length l2) andalso
    (List.all (fn x => (List.find (fn y => y = x) l2) <> NONE) l1)

(* 8 *) fun test24 () = sameList r1 (permutations [1,2,3,4])
(* 5 *) fun test25 () = 
            let val forward = permutations ["a", "b", "c"]
                val backward = permutations ["b", "c", "a"]
            in
                sameList forward backward
            end
(* 2 *) fun test26 () = (permutations []) = [[]]

(* tests for combinations (15 pts) *)
val r2 = [[1,2,3,4],[1,2,3,5],[1,2,3,6],[1,2,3,7],[1,2,3,8],[1,2,3,9],[1,2,3,10],[1,2,4,5],[1,2,4,6],[1,2,4,7],[1,2,4,8],[1,2,4,9],[1,2,4,10],[1,2,5,6],[1,2,5,7],[1,2,5,8],[1,2,5,9],[1,2,5,10],[1,2,6,7],[1,2,6,8],[1,2,6,9],[1,2,6,10],[1,2,7,8],[1,2,7,9],[1,2,7,10],[1,2,8,9],[1,2,8,10],[1,2,9,10],[1,3,4,5],[1,3,4,6],[1,3,4,7],[1,3,4,8],[1,3,4,9],[1,3,4,10],[1,3,5,6],[1,3,5,7],[1,3,5,8],[1,3,5,9],[1,3,5,10],[1,3,6,7],[1,3,6,8],[1,3,6,9],[1,3,6,10],[1,3,7,8],[1,3,7,9],[1,3,7,10],[1,3,8,9],[1,3,8,10],[1,3,9,10],[1,4,5,6],[1,4,5,7],[1,4,5,8],[1,4,5,9],[1,4,5,10],[1,4,6,7],[1,4,6,8],[1,4,6,9],[1,4,6,10],[1,4,7,8],[1,4,7,9],[1,4,7,10],[1,4,8,9],[1,4,8,10],[1,4,9,10],[1,5,6,7],[1,5,6,8],[1,5,6,9],[1,5,6,10],[1,5,7,8],[1,5,7,9],[1,5,7,10],[1,5,8,9],[1,5,8,10],[1,5,9,10],[1,6,7,8],[1,6,7,9],[1,6,7,10],[1,6,8,9],[1,6,8,10],[1,6,9,10],[1,7,8,9],[1,7,8,10],[1,7,9,10],[1,8,9,10],[2,3,4,5],[2,3,4,6],[2,3,4,7],[2,3,4,8],[2,3,4,9],[2,3,4,10],[2,3,5,6],[2,3,5,7],[2,3,5,8],[2,3,5,9],[2,3,5,10],[2,3,6,7],[2,3,6,8],[2,3,6,9],[2,3,6,10],[2,3,7,8],[2,3,7,9],[2,3,7,10],[2,3,8,9],[2,3,8,10],[2,3,9,10],[2,4,5,6],[2,4,5,7],[2,4,5,8],[2,4,5,9],[2,4,5,10],[2,4,6,7],[2,4,6,8],[2,4,6,9],[2,4,6,10],[2,4,7,8],[2,4,7,9],[2,4,7,10],[2,4,8,9],[2,4,8,10],[2,4,9,10],[2,5,6,7],[2,5,6,8],[2,5,6,9],[2,5,6,10],[2,5,7,8],[2,5,7,9],[2,5,7,10],[2,5,8,9],[2,5,8,10],[2,5,9,10],[2,6,7,8],[2,6,7,9],[2,6,7,10],[2,6,8,9],[2,6,8,10],[2,6,9,10],[2,7,8,9],[2,7,8,10],[2,7,9,10],[2,8,9,10],[3,4,5,6],[3,4,5,7],[3,4,5,8],[3,4,5,9],[3,4,5,10],[3,4,6,7],[3,4,6,8],[3,4,6,9],[3,4,6,10],[3,4,7,8],[3,4,7,9],[3,4,7,10],[3,4,8,9],[3,4,8,10],[3,4,9,10],[3,5,6,7],[3,5,6,8],[3,5,6,9],[3,5,6,10],[3,5,7,8],[3,5,7,9],[3,5,7,10],[3,5,8,9],[3,5,8,10],[3,5,9,10],[3,6,7,8],[3,6,7,9],[3,6,7,10],[3,6,8,9],[3,6,8,10],[3,6,9,10],[3,7,8,9],[3,7,8,10],[3,7,9,10],[3,8,9,10],[4,5,6,7],[4,5,6,8],[4,5,6,9],[4,5,6,10],[4,5,7,8],[4,5,7,9],[4,5,7,10],[4,5,8,9],[4,5,8,10],[4,5,9,10],[4,6,7,8],[4,6,7,9],[4,6,7,10],[4,6,8,9],[4,6,8,10],[4,6,9,10],[4,7,8,9],[4,7,8,10],[4,7,9,10],[4,8,9,10],[5,6,7,8],[5,6,7,9],[5,6,7,10],[5,6,8,9],[5,6,8,10],[5,6,9,10],[5,7,8,9],[5,7,8,10],[5,7,9,10],[5,8,9,10],[6,7,8,9],[6,7,8,10],[6,7,9,10],[6,8,9,10],[7,8,9,10]]

fun sameCombs l1 l2 =
    (List.length l1) = (List.length l2) andalso
    (List.all (fn x => (List.find (fn y => (sameList x y)) l2) <> NONE) l1)

(* 7 *) fun test27 () = sameCombs r2 (combinations (4, [1,2,3,4,5,6,7,8,9,10]))
(* 4 *) fun test28 () = 
            let val forward = combinations (2, ["a", "b", "c"])
                val backward = combinations (2, ["b", "c", "a"])
            in
                sameCombs forward backward
            end
(* 2 *) fun test29 () = sameCombs [[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
                                  (combinations (2, [1,2,3,4]))
(* 1 *) fun test30 () = combinations (0, [1,2,3,4,5]) = [[]]
(* 1 *) fun test31 () = combinations (5, []) = [[]]

(* tests for bestAlign (20 pts) *)
local
    val s1 = "long string is long string is long string is long string is long string is long string is long string is long string is long string is long string is long string is long string is long"
    val s2 = "this string is a long string is long string is long string is long string is long string is long string is long string is long string is long string is long string is long"
    val result = "long string is long string is long string is long string is long string is long string is long string is long string is long string is long string is long"
    val shift = 17
in
(* 1 *) fun test32 () = bestAlign "" "" = ("", 0)
(* 1 *) fun test33 () = bestAlign "thing" "" = ("", 0)
(* 1 *) fun test34 () = bestAlign "almost perfect" " almost perfect" = 
                        ("almost perfect", 1)
(* 2 *) fun test35 () = #1(bestAlign "bdefi" "nomatch") = ""
(* 2 *) fun test36 () = (bestAlign "cat" "bat") = ("*at", 0)
(* 3 *) fun test37 () = (bestAlign "the quick fox jumped over the lazy dog" 
                                   "quick fax me a resume") = ("quick f*x ",~4)
(* 3 *) fun test38 () = 
            let val orig = (bestAlign 
                                "the quick fox jumped over the lazy dog" 
                                "quick fax me a resume")
                val switch = (bestAlign 
                                  "quick fax me a resume"
                                  "the quick fox jumped over the lazy dog")
            in
                #1orig = #1switch andalso #2orig = ~(#2switch)
            end
(* 3 *) fun test39 () = bestAlign s1 s2 = (result, shift)
(* 4 *) fun test40 () = 
            bestAlign "some stuff then match mismatch then lots of match" 
                      "match but none then lots of match" =
            ("match ******** then lots of match",~16)
end

(* tests for printAlign (5 pts) *)
(* 1 *) fun test41 () = getString (printAlign "-" "-" "-") 0 = "-\n-\n-\n"
(* 2 *) fun test42 () = getString (printAlign "a" "this is a string" "a") 8 = "        a\nthis is a string\n        a\n"
(* 2 *) fun test43 () = getString (printAlign "this is a string" "a" "a") ~8 = "this is a string\n        a\n        a\n"

val allTests =
    [(1.25, "modulo (2, 2)", test1),
     (1.25, "modulo of 0", test2),
     (1.25, "modulo (17, 2)", test3),
     (2.5, "modulo of large number", test4),

     (1.25, "lcm of number and itself", test5),
     (1.25, "lcm of primes", test6),
     (1.25, "lcm of (12, 2)", test7),
     (2.5, "lcm of large number", test8),

     (1.25, "andEval true and true", test9),
     (1.25, "andEval false and true", test10),
     (1.25, "andEval with increment (both true)", test11),
     (2.5, "andEval with increment (first true)", test12),

     (1.25, "orEval false and true", test9),
     (1.25, "orEval false and false", test10),
     (1.25, "orEval with increment (both true)", test11),
     (2.5, "orEval with increment (first true)", test12),

     (1.25, "nOutOfOrder empty list", test17),
     (1.25, "nOutOfOrder singleton", test18),
     (1.25, "nOutOfOrder all in order", test19),
     (1.25, "nOutOfOrder (op <) [3,2]", test20),
     (2.5, "nOutOfOrder (op <) with 2 out of order", test21),
     (2.5, "nOutOfOrder (op >) with 3 out of order", test22),
     (2.5, "nOutOfOrder using xor", test23),

     (6.25, "permutations for [1,2,3,4]", test24),
     (3.75, "gives same result (ignoring order) for list and its reverse", test25),
     (2.5, "permutations of empty list", test26),

     (8.75, "combinations of 4 on [1,2,3,4,5,6,7,8,9,10]", test27),
     (5.0, "gives same result (ignoring order) for list and its reverse", test28),
     (2.5, "conbimations of 2 on [1,2,3,4]", test29),
     (1.25, "conbimations of size 0", test30),
     (1.25, "combinations of empty list", test31),

     (1.25, "bestAlign with empty strings", test32),
     (1.25, "bestAlign with one empty string", test33),
     (1.25, "bestAlign perfect match, except one shift", test34),
     (2.5, "bestAlign no match", test35),
     (2.5, "bestAlign one mismatch", test36),
     (3.75, "bestAlign long shift, short match", test37),
     (3.75, "bestAlign switching strings gives opposite shift", test38),
     (3.75, "bestAlign with long strings", test39),
     (5.0, "bestAlign with big mismatch but good match after", test40),

     (1.25, "printAlign with no shift", test41),
     (2.5, "printAlign with positive shift", test42),
     (2.5, "printAlign with negative shift", test43)]

val _ = Grading.saveToFile 100 (Grading.run allTests) "tests.res"

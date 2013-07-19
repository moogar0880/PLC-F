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

(* permutation tests *)
val singleSeq = ref (permutation [1])
val smallSeq = ref (permutation [1])
val largeSeq = ref (permutation ["1"])
val hugeSeq = ref (permutation [1])

val r1 = [[1,2,3,4],[1,2,4,3],[1,3,2,4],[1,3,4,2],[1,4,2,3],[1,4,3,2],[2,1,3,4],[2,1,4,3],[2,3,1,4],[2,3,4,1],[2,4,1,3],[2,4,3,1],[3,1,2,4],[3,1,4,2],[3,2,1,4],[3,2,4,1],[3,4,1,2],[3,4,2,1],[4,1,2,3],[4,1,3,2],[4,2,1,3],[4,2,3,1],[4,3,1,2],[4,3,2,1]]

val largeSize = 3628800
val s1 = ["2", "3", "4", "5", "0", "6", "7", "8", "9", "1"]
val s2 = ["7", "3", "2", "0", "4", "1", "6", "5", "9", "8"]
val s3 = ["9", "8", "7", "6", "5", "4", "3", "2", "1", "0"]

fun contains l (SOME i) = List.exists (fn x => x = i) l
  | contains _ NONE = false

exception optionException

fun unOption (SOME x) = x
  | unOption NONE = raise optionException

fun empty (Cons(NONE, _)) = true
  | empty _ = false

fun len (Cons(NONE, _)) i = i
  | len (Cons(_, f)) i = (len (f ()) (i+1))

(* 2 *) fun test1 () = (smallSeq := permutation [1,2,3,4]; not (empty (!smallSeq)))
(* 3 *) fun test2 () = (largeSeq := permutation (List.map (Int.toString) (List.tabulate (10, (fn x => x)))); not (empty (!largeSeq)))
(* 5 *) fun test3 () = (hugeSeq := permutation (List.tabulate (100, (fn x => x))); not (empty (!hugeSeq)))
(* 5 *) fun test4 () = let val s1 = permutation [1,2]
                           val s2 = permutation ["a","b"]
                       in
                           contains [[1,2],[2,1]] (next (rest s1)) andalso
                           contains [["a","b"],["b","a"]] (next (rest s2))
                       end
(* 2 *) fun test5 () = let val seq = !singleSeq
                        in 
                            next(seq) = SOME [1] andalso next(rest(seq)) = NONE
                        end
(* 3 *) fun test6 () = let val seq = (test1 (); !smallSeq)
                        in
                            contains r1 (next (rest (rest (rest seq))))
                        end
(* 5 *) fun test7 () = let val seq = (test1 (); !smallSeq)
                        in
                            let fun f s = if next(s) <> NONE then
                                         (contains r1 (next s)) andalso 
                                         (f (rest s))
                                      else
                                          true
                            in
                                f seq
                            end
                        end
(* 5 *) fun test8 () = let val seq = (test2 (); !largeSeq)
                        in
                            let fun f s [] = true
                                  | f s l = if (next s) <> NONE then
                                                (f (rest s) 
                                                   (List.filter 
                                                        (fn x => x<>(unOption (next s))) l))
                                          else
                                              false
                            in
                                f seq [s1, s2, s3]
                            end
                        end
(* 4 *) fun test9 () = (test2 (); len (!largeSeq) 0 = largeSize)
(* 5 *) fun test10 () = let val seq = !smallSeq
                        in
                            let fun f s acc = 
                                    if next(s) <> NONE then
                                        (not (contains acc (next s)))
                                        andalso
                                        (f (rest s) ((unOption (next s))::acc))
                                    else
                                        true
                            in
                                (f seq [])
                            end
                        end
(* 5 *) fun test11 () = (getString printPermutations) (permutation ["1","2","3","4"]) = "[1,2,3,4]\n[2,1,3,4]\n[2,3,1,4]\n[2,3,4,1]\n[1,3,2,4]\n[3,1,2,4]\n[3,2,1,4]\n[3,2,4,1]\n[1,3,4,2]\n[3,1,4,2]\n[3,4,1,2]\n[3,4,2,1]\n[1,2,4,3]\n[2,1,4,3]\n[2,4,1,3]\n[2,4,3,1]\n[1,4,2,3]\n[4,1,2,3]\n[4,2,1,3]\n[4,2,3,1]\n[1,4,3,2]\n[4,1,3,2]\n[4,3,1,2]\n[4,3,2,1]\n"

(* integral tests *)
fun eq (x, y) inc = (Real.abs (x-y)) < (inc / 2.0)

fun time f x1 x2 =
    let val rt = Timer.startRealTimer()
        val res = f x1 x2
        val t = Time.toReal(Timer.checkRealTimer rt)
    in
        t
    end

fun constant x = 5.0

fun yEqualsX x = x

fun parabola x = Math.pow (x, 2.0)

fun lowerParab x = (Math.pow (x, 2.0)) - 1.0

fun negParabola x = ~(Math.pow (x, 2.0))

fun polynomial l x = 
    let fun poly (c::ct) e = (c*(Math.pow (x, e))) + (poly ct (e-1.0))
          | poly _ _ = 0.0
    in
        poly l (Real.fromInt (length l))
    end

val coeff = [~3.0, 17.0, 12.5, ~4.7, ~23.7, 10.0, ~8.0, 2.0, 0.0, ~4.3]
fun makeCoeff 0 = []
  | makeCoeff n = coeff@(makeCoeff (n-1))
val coeff1 = makeCoeff 5
val coeff2 = makeCoeff 20
             

(* 5 *) fun test12 () = eq ((integral negParabola 0.0 10.0), ~330.0) 6.0
(* 3 *) fun test13 () = eq ((integral yEqualsX 0.0 5.0), 12.25) 0.5
(* 2 *) fun test14 () = eq ((integral constant 2.9 3.9), 5.0) 0.01

(* 7 *) fun test15 () = 
            let val f = time (integralMem (polynomial coeff2))
            in
                ((f 0.0 400.0) > (f 0.0 400.0))
            end
(* 5 *) fun test16 () = let val f = time (integralMem (polynomial coeff1))
                         in
                             ((f 0.0 400.0) > (f 0.0 400.0))
                         end
(* 5 *) fun test17 () = let val f1 = integralMem parabola
                            val f2 = integralMem constant
                            val f1r1 = f1 0.0 10.0
                            val f2r1 = f2 0.0 10.0
                            val f1r2 = f1 0.0 10.0
                            val f2r2 = f2 0.0 10.0
                        in
                            eq (f1r1, f1r2) 0.01 andalso eq (f2r1, f2r2) 0.01
                            andalso not (eq (f1r1, f2r1) 0.01)
                            andalso not (eq (f1r2, f2r2) 0.01)
                        end
(* 5 *) fun test18 () = let val f1 = time (integralMem (polynomial coeff2))
                             val f2 = time (integralMem (polynomial coeff2))
                             val orig = (f1 0.0 400.0)
                             val partial = ((f2 100.0 300.0); (f2 0.0 400.0))
                         in
                             orig > partial
                         end
(* 3 *) fun test19 () = let val f = integralMem parabola
                        in
                            eq ((f 0.0 10.0), (f 0.0 10.0)) 0.01
                        end


val allTests =
    [(3, "create a small sequence [1...4]", test1),
     (4, "create a medium sized sequence [0...10]", test2),
     (5, "create a large sequence [0...100]", test3),
     (6, "two sequences", test4),
     (4, "sequence for [1] has one value, then gives NONE", test5),
     (6, "item within small sequence is one of the valid permutations", test6),
     (6, "small sequence contains all permutations", test7),
     (7, "large sequence contains a few particular permutations", test8),
     (4, "large sequence has correct length", test9),
     (5, "no repeats in permutationSeq", test10),
     (5, "printPermutations (will double check manually)", test11),

     (8, "integral of y = -(x^2) from 0.0 to 10.0", test12),
     (7, "integral of y = x from 0.0 to 5.0", test13),
     (5, "integral of constant over range of length 1.0", test14),

     
     (7, "integralMem faster on second run with 200th degree polynomial from 0.0 to 100.0", test15),
     (5, "integralMem faster on second run with 50th degree polynomial from 0.0 to 300.0", test16),
     (5, "integralMem on two different functions won't cause interference", test17),
     (5, "integralMem faster when half of range was calculated before", test18),
     (3, "integralMem returns same value when called twice", test19)]

val _ = Grading.saveToFile 100 (Grading.run allTests) "tests.res"

use "stubs.sml";
use "main.sml";
use "tester.sml";

(* permutation tests *)
val singleSeq = ref (permutation [1])
val smallSeq = ref (permutation [1])
val medSeq = ref (permutation [1])
val largeSeq = ref (permutation ["1"])

val r1 = [[1,2,3],[2,1,3],[2,3,1],[1,3,2],[3,1,2],[3,2,1]]
val s1 = ["2", "3", "4", "5", "6", "7", "8", "9", "0", "1"]
val s2 = ["7", "6", "3", "2", "0", "4", "1", "5", "9", "8"]
val s3 = ["9", "8", "7", "6", "5", "4", "3", "2", "1", "0"]

fun contains l (SOME i) = List.exists (fn x => x = i) l
  | contains _ NONE = false

exception optionException

fun unOption (SOME x) = x
  | unOption NONE = raise optionException

fun empty (Cons(NONE, _)) = true
  | empty _ = false

(*===============================================permutationSeq Tests===============================================*)
fun test1 () = (smallSeq := permutation [1,3,2]; not (empty (!smallSeq)))
fun test2 () = (medSeq := permutation (List.tabulate (5, (fn x => x))); not (empty (!medSeq)))
fun test3 () = (largeSeq := permutation (List.map (Int.toString) (List.tabulate (10, (fn x => x)))); not (empty (!largeSeq)))
fun test4 () = 
  let 
    val s1 = permutation [5,6]
    val s2 = permutation ["hat","cat"]
  in
    contains [[5,6],[6,5]] (next (rest s1)) andalso contains [["cat","hat"],["hat","cat"]] (next (rest s2))
  end

(* test 5 *)
fun test5 () = 
  let 
    val seq = permutation []
  in 
    next(seq) = SOME [] andalso next(rest(seq)) = NONE
  end

(* test 6 *)
fun test6 () = 
  let 
    val seq = !singleSeq
  in 
    next(seq) = SOME [1] andalso next(rest(seq)) = NONE
  end

(* test 7 *)
fun test7 () = 
  let 
    val seq = !smallSeq
  in
    contains r1 (next (rest (rest (rest seq))))
  end

(* test 8 *)
fun test8 () = 
  let 
    val seq = !smallSeq
  in
    let fun f s = if next(s) <> NONE then (contains r1 (next s)) andalso (f (rest s)) else true
    in
      f seq
    end
  end

(* test 9 *)
fun test9 () = false(*
  let 
    val seq = !largeSeq
  in
    let 
      fun f s [] = true
        | f s l  = if (next s) <> NONE then f (rest s) (List.filter (fn x => x <> (unOption (next s))) l) else false
    in 
      f seq [s1, s2, s3]
    end
  end*)

(* test 10 *)                        
fun test10 () = 
  let 
    val seq = !smallSeq
  in
    let 
      fun f s acc = if next(s) <> NONE then (not (contains acc (next s))) andalso (f (rest s) ((unOption (next s))::acc)) else true
    in
      (f seq [])
    end
  end 

(* test 11 *)                        
fun test11 () = (printPermutations (permutation ["1","2","3"]); true)

(*==================================================Integral Tests==================================================*)
fun eq (x, y) inc = (Real.abs (x-y)) < (inc / 2.0)

fun constant x = 5.0

fun yEqualsX x = x

fun parabola x = Math.pow (x, 2.0)

fun test12 () = eq ((integral parabola 0.0 10.0), 328.35) 3.0
fun test13 () = eq ((integral yEqualsX 5.0 12.0), 59.15) 0.1
fun test14 () = eq ((integral constant 1.7 4.5), 14.0) 0.01

(*================================================Integralmem Tests================================================*)
fun time f x1 x2 =
  let 
    val rt = Timer.startRealTimer()
    val res = f x1 x2
    val t = Time.toReal(Timer.checkRealTimer rt)
  in
    t
  end;

val coeff = [1.0, ~4.0, 12.7, 9.3, ~18.0, 0.3, 1.0];
fun makeCoeff 0 = []
  | makeCoeff n = coeff@(makeCoeff (n-1));
val coeff1 = makeCoeff 3;
val coeff2 = makeCoeff 10;

fun polynomial l x = 
  let 
    fun poly (c::ct) e = (c*(Math.pow (x, e))) + (poly ct (e-1.0))
      | poly _ _ = 0.0
  in
    poly l (Real.fromInt (length l))
  end

(* test 15 *)
fun test15 () = 
  let 
    val f = time (integralMem (polynomial coeff1));
  in
    ((f 0.0 100.0) > (f 0.0 100.0))
  end

(* test 16 *)
fun test16 () = 
  let 
    val f1 = integralMem parabola;
    val f2 = integralMem constant
    val f1r1 = f1 0.0 10.0
    val f2r1 = f2 0.0 1.0
    val f1r2 = f1 0.0 10.0
    val f2r2 = f2 0.0 1.0
  in
    eq (f1r1, f1r2) 0.01 andalso eq (f2r1, f2r2) 0.01
  end

(* test 17 *)
fun test17 () = 
  let 
    val f = time (integralMem (polynomial coeff2))
  in
    ((f 0.0 300.0) > (f 0.0 300.0))
  end

(*====================================================All Tests====================================================*)
val allTests =
    [(1, "test1 create a small sequence [1...3]", test1),
     (1, "test2 create a medium sized sequence [0...5]", test2),
     (1, "test3 create a large sequence [0...50]", test3),
     (1, "test4 two sequences won't cause interference", test4),
     (1, "test5 sequence for [] gives [], then gives NONE", test5),
     (1, "test6 sequence for [1] gives [1], then gives NONE", test6),
     (1, "test7 item within small sequence is one of the valid permutations", test7),
     (1, "test8 small sequence contains all permutations", test8),
     (1, "test9 large sequence contains a few particular permutations", test9),
     (1, "test10 no repeats in permutationSeq", test10),
     (1, "test11 printPermutations (double check manually)", test11),

     (1, "test12 test1integral of y = x^2 from 0.0 to 10.0", test12),
     (1, "test13 test1integral of y = x from 0.0 to 5.0", test13),
     (1, "test14 test1integral of constant from 1.7 to 4.5", test14),

     (1, "test15 test1integralMem faster on second run with 21st degree polynomial from 0.0 to 100.0", test15),
     (1, "test16 test1integralMem on two different functions won't cause interference", test16),
     (1, "test17 test1integralMem faster on second run with 70th degree polynomial from 0.0 to 300.0", test17)]

val _ = Grading.saveToFile 100 (Grading.run allTests) "sampletests.res"

(* $Id: tester.sml 381 2010-11-09 21:07:41Z charpov $ *)

signature TESTER_INPUT = sig

  type test

  val getNote : test -> string
  val getValue : test -> int
  val getRunnable : test -> unit -> bool
end

signature TESTER = sig

  type test
  type result

  val run : test list -> result
  val run1 : test -> result

  val getTime : result -> real

  val getPassed : result -> test list
  val getFailed : result -> test list
  val getExceptions : result -> test list

end

functor Tester (I : TESTER_INPUT) : TESTER where type test = I.test = struct

open I

type result = real * test list * test list * test list

local
    datatype outcome = PASS | FAIL | EXN
in
fun run1 test =
    let val run = getRunnable test
      val rt = Timer.startRealTimer()
      val r = (if run () then PASS else FAIL) handle _ => EXN
      val time = Time.toReal(Timer.checkRealTimer rt)
    in
      print ("Test ["^(getNote test)^"] completed: ");
      case r of
        PASS => (print "PASS\n"; (time, [test], [], []))
      | FAIL => (print "FAIL\n"; (time, [], [test], []))
      | EXN  => (print "exception\n"; (time, [], [], [test]))
    end
fun run l =
    let fun r (test, (t,p,f,e)) =
        let val (t1,p1,f1,e1) = run1 test
        in (t1 + t, p1 @ p, f1 @ f, e1 @ e) end
      val (t,p,f,e) = List.foldl r (0.0,[],[],[]) l
    in
      (t, rev p, rev f, rev e)
    end
end

fun getTime (t,_,_,_) = t
fun getPassed (_,p,_,_) = p
fun getFailed (_,_,f,_) = f
fun getExceptions (_,_,_,e) = e
end

structure Grading : sig type result
                        type test = int * string * (unit -> bool)
                        val run : test list -> result
                        val run1 : test -> result
                        val score : int -> result -> string
                        val saveToFile : int -> result -> string -> string
                    end = struct

structure I = struct
  type test = int * string * (unit -> bool)
  fun getNote (_,n,_) = n
  fun getValue (v,_,_) = v
  fun getRunnable (_,_,r) = r
end

structure G = Tester (I)
open G

fun getGrade result =
    let val (p,f) = (getPassed result, getFailed result @ getExceptions result)
      fun add (x,n) = n + I.getValue x
      val x = foldl add 0 p
      val y = foldl add x f
    in
      real x / real y
    end

fun score n result =
    Real.toString (Real.realRound ((getGrade result) * (real n) * 10.0) / 10.0)
    ^ " / " ^ (Int.toString n)

fun saveToFile n result file =
    let val grade = score n result
        val out = TextIO.openOut file
        fun print s = (TextIO.print s; TextIO.output (out, s))
        fun pr t = print ("["^(I.getNote t)^"]")
        fun prList [] = ()
          | prList (x::l) = (pr x; List.app (fn s => (print ", ";pr s)) l)
    in
      print "Tests passed: ";
      prList (getPassed result);
      print "\n\nTests failed: ";
      prList (getFailed result);
      print "\n\nTests failed with an exception: ";
      prList (getExceptions result);
      print "\n\nTotal time: ";
      print (Real.toString (getTime result));
      print "\nGrade: ";
      print grade;
      print "\n";
      TextIO.closeOut out;
      grade
    end
end

fun score1 n l file =
    let open Grading
      open TextIO
      val t as (w,s,_) = List.nth (l, n-1)
      val out = openAppend file
    in
      output (out, s);
      output (out, ": ");
      output (out, score w (run1 t));
      output (out, "\n");
      closeOut out
    end

(*
fun test1 () = true
fun test2 () = false
fun test3 () = raise Fail "failure"
fun test4 () = (Posix.Process.sleep (Time.fromSeconds 5); true)

val toRun = [
    (4, "test1", test1),
    (2, "test2", test2),
    (2, "test3", test3),
    (1, "test4", test4)
]

local
  val res = Grading.run toRun
in
val _ = Grading.saveToFile 100 res "/tmp/foo"
end
*)

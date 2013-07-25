use "exam.sml";
use "/home/csadmin/cs671/tester.sml";
(*use "tester.sml";*)

fun incr x = x+1;

fun test1 () = (isLonger [] [] = false)
fun test2 () = (isLonger [1,2,3] ["a","b"] = true)
fun test3 () = ((isLonger (List.tabulate (101, incr)) 
                          (List.tabulate (100, incr))) = true)

fun test4 () = (Stack.isEmpty (Stack.fromList [])) = true
fun test5 () = #1(Stack.pop (Stack.fromList [1,2,3])) = 1
fun test6 () = #1(Stack.pop (Stack.push (Stack.push (Stack.fromList [2]) 3) 4)) = 4
fun test7 () = #1(Stack.pop (#2(Stack.pop (Stack.push (Stack.push (Stack.fromList [2]) 3) 4)))) = 3

fun test8 () = (fromList [] (Int.<)) = EmptyBST
fun test9 () = (fromList [1,2,3] (Int.<)) = 
               (Node(1, Node(2, EmptyBST, EmptyBST), Node(3, EmptyBST, EmptyBST)))
fun test10 () = (contains (fromList [10,7,3,5,6] (Int.<)) 3 (Int.<))
fun test11 () = let val tr = (fromList [10,7,3,5,6] (Int.<))
                in
                    let fun doAll [] = true
                          | doAll (x :: t) = (contains tr x (Int.<)) andalso 
                                             (doAll t)
                    in
                        doAll [10,7,3,5,6]
                    end
                end
                
val allTests =
    [(1, "isLonger: empty lists", test1),
     (2, "isLonger: [1,2,3] [\"a\",\"b\"]", test2),
     (2, "isLonger: [0...100] [0...99]", test3),

     (1, "", test4),
     (4, "", test5),
     (5, "", test6),
     (5, "", test7),

     (2, "BST from empty list", test8),
     (3, "BST from sorted list", test9),
     (5, "BST from more balanced list", test10),
     (10, "BST contains all", test11)
    ]

val _ = Grading.saveToFile 35 (Grading.run allTests) "tests.res"

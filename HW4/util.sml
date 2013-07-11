fun time f s =
  let
    val rt = Timer.startRealTimer()
    val res = f s
    val t = Time.toReal(Timer.checkRealTimer rt)
  in
    t
  end
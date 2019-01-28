import org.scalatest.{FunSuite, Matchers}

/**
  * Created by mtumilowicz on 2019-01-28.
  */
class OpTest extends FunSuite with Matchers {
  test("size of the collection basing on the list") {
    val sizer: Op[Int, List[String]] = _.size

    sizer.map((set: Set[String]) => set.toList).apply(Set()) should be(0)
    sizer.map((set: Set[String]) => set.toList).apply(Set("a")) should be(1)
    sizer.map((set: Set[String]) => set.toList).apply(Set("a", "b", "c")) should be(3)
  }

  test("predicate: isEven") {
    val isEven: Op[Boolean, Int] = _ % 2 == 0

    isEven.map((s: String) => s.toInt).apply("2") should be(true)
    isEven.map((s: String) => s.toInt).apply("3") should be(false)
  }

}

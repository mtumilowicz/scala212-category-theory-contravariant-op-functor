/**
  * Created by mtumilowicz on 2019-01-28.
  */
trait Op[R, A] extends (A => R) {
  def map[B](f: B => A): Op[R, B] = this.compose(f).apply
}
[![Build Status](https://travis-ci.com/mtumilowicz/scala212-category-theory-contravariant-op-functor.svg?branch=master)](https://travis-ci.com/mtumilowicz/scala212-category-theory-contravariant-op-functor)

# scala212-category-theory-contravariant-op-functor
_Reference_: http://hackage.haskell.org/package/contravariant-1.5/docs/Data-Functor-Contravariant.html  
_Reference_: https://ocharles.org.uk/blog/guest-posts/2013-12-21-24-days-of-hackage-contravariant.html  
_Reference_: https://bartoszmilewski.com/2015/02/03/functoriality/  

# preview
Referring my other github projects could be helpful:
* Functor's basics: https://github.com/mtumilowicz/java11-category-theory-optional-is-not-functor
* Functor related to `Op`: https://github.com/mtumilowicz/java11-category-theory-reader-functor

# functor intuition
```
class Functor f where
  map :: (a -> b) -> f a -> f b
```

* Functor is a sort of "producer of output": if `f` is a functor, then `f a` represents a 
"producer" of type `a`
* Functor can have its type adapted
* Using a function `a->b` we can modify output to type `b` 
(using `map`)
* Three easy examples of functors (and modification of output):
    1. `Option[A]`: using `map` we can adapt potentially `None[A]`
    to `None[B]`
    1. `List[A]`: using `map` we can adapt `List[A]`
    to `List[B]`
    1. `Reader[R, A]` (`r -> a`) - we can think about it as a
    functions taking `r` as input and producing `a`; using `map`
    we can convert output to `b`
    
**map allows us to change the output, but we are unable to change
the input** - and that's where contravariance comes in

# contravariance
```
class Contravariant f where
  contramap :: (b -> a) -> f a -> f b
```

* For contravariant `f`, `f a` represents input
* Using a function `b -> a` we can modify input to type `b` 
(using `contramap`)
* Notice that a contravariant functor is just a regular 
functor from the opposite category (category with reversed
morphisms)

# project description
We provide example of contravariant functor with tests.
* haskell is more expressive:
    ```
    type Op r a = a -> r
    
    instance Contravariant (Op r) where
        -- (b -> a) -> Op r a -> Op r b
        contramap f g = g . f
    ```
* and the Scala code comes here:
    ```
    trait Op[R, A] extends (A => R) {
      def map[B](f: B => A): Op[R, B] = this.compose(f).apply
    }
    ```
    * **Notice that the function f inserts itself before 
    the contents of `Op` - the function `g`.**
* tests:
    * we have function counting list's size, and we want to modify 
    it to accept `Set`:
        ```
        val sizer: Op[Int, List[String]] = _.size
        
        sizer.map((set: Set[String]) => set.toList).apply(Set()) should be(0)
        sizer.map((set: Set[String]) => set.toList).apply(Set("a")) should be(1)
        sizer.map((set: Set[String]) => set.toList).apply(Set("a", "b", "c")) should be(3)
        ```
    * we have predicate to check if a given int is even, and
    we want to transform it to accept ints given as a string
        ```
        val isEven: Op[Boolean, Int] = _ % 2 == 0
        
        isEven.contramap((s: String) => s.toInt).apply("2") should be(true)
        isEven.contramap((s: String) => s.toInt).apply("3") should be(false)
        ```

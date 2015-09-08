(ns cats.data-spec
  (:require [cats.data :as d]
            [cats.builtin :as b]
            [cats.protocols :as p]
            [cats.monad.maybe :as maybe]

            #?(:cljs [cats.context :as ctx :include-macros true]
               :clj  [cats.context :as ctx])

            #?(:cljs [cljs.test :as t]
               :clj  [clojure.test :as t])

            #?(:cljs [cats.core :as m :include-macros true]
               :clj  [cats.core :as m])))

(t/deftest pair-monoid
  (t/testing "mempty"
    (ctx/with-context (d/pair-monoid b/string-monoid)
      (t/is (= (d/pair "" "") (m/mempty))))

    (ctx/with-context (d/pair-monoid b/sum-monoid)
      (t/is (= (d/pair 0 0) (m/mempty)))))

  (t/testing "mappend"
    (t/is (= (d/pair "Hello buddy" "Hello mate")
             (m/mappend
              (d/pair "Hello " "Hello ")
              (d/pair "buddy" "mate")))))

  (t/testing "mappend with other-context"
    (ctx/with-context (d/pair-monoid b/sum-monoid)
      (t/is (= (d/pair 10 20)
               (m/mappend
                (d/pair 3 5)
                (d/pair 3 5)
                (d/pair 4 10)))))))

(t/deftest functor-test
  (t/testing "It maps a function over the second value of the pair"
    (= (d/pair 0 42)
       (m/fmap inc (d/pair 0 41)))))

(t/deftest foldable-test
  (t/testing "Foldl"
    (t/is (= 1/3
             (m/foldl / 1 (d/pair 0 3)))))

  (t/testing "Foldr"
    (t/is (= 3
             (m/foldr / 1 (d/pair 0 3))))))

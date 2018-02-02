(ns compare-poker-hands.core-test
  (:require [clojure.test :refer :all]
            [compare-poker-hands.core :refer :all]))

(deftest codefights-test
  (testing "First test of codefights"
    (is (= (ComparePokerHands "2H 3H 4H 5H 6H" "KS AS TS QS JS") 1))))
  ; (testing "Second test"
  ;   (is (= (ComparePokerHands "2H 3H 4H 5H 6H" "AS AD AC AH JD") -1))))

(deftest transformations
  (testing "str-to-card"
    (is (= (str-to-card "KD") {:value 13 :suit \D}))))

(deftest hands
  (testing "Straight flush"
    (is (= (:name (hand "KS AS TS QS JS")) :straight-flush)))
  (testing "Straight flush"
    (is (= (:name (hand "2H 3H 4H 5H 6H")) :straight-flush)))
  (testing "Four of a kind"
    (is (= (:name (hand "AS AD AC AH JD")) :four_of_a_kind))))

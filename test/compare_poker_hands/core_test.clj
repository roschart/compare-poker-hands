(ns compare-poker-hands.core-test
  (:require [clojure.test :refer :all]
            [compare-poker-hands.core :refer :all]))

(deftest codefights-test
  (testing "First test of codefights"
    (is (= (ComparePokerHands "2H 3H 4H 5H 6H" "KS AS TS QS JS") 1))))
(deftest compare-hands-test
  (testing "My personal test to break tie"
    (is (= (ComparePokerHands "2S 2D 2C 2H JD" "3S 3D 3C 3H 7D") 1))
    (is (= (compare-same-hand (hand "2S 2D 2C 2H JD") (hand "3S 3D 3C 3H 7D")) 1))
    (is (= (ComparePokerHands "AS AD AC 2H 2D" "KS KD KC JH JD") -1))
    (is (= (ComparePokerHands "AS 5S 3S 7S KS" "AD 5D 3D 7D KD") 0))
    (is (= (ComparePokerHands "AS 5S 3S 8S KS" "AD 5D 3D 7D KD") -1))
    (is (= (ComparePokerHands "AS QS TS 8D 7S" "AD 5D QS 7D KD") 1)))
  (testing "Second test"
    (is (= (ComparePokerHands "2H 3H 4H 5H 6H" "AS AD AC AH JD") -1))))

(deftest transformations
  (testing "str-to-card"
    (is (= (str-to-card "KD") {:value 13 :suit \D})))
  (testing "str-to-card"
    (is (= (str-to-card "TD") {:value 10 :suit \D}))))

(deftest hands
  (testing "Straight flush"
    (is (= (:name (hand "KS AS TS QS JS")) :straight-flush))
    (is (= (:name (hand "2H 3H 4H 5H 6H")) :straight-flush)))
  (testing "Four of a kind"
    (is (= (:name (hand "AS AD AC AH JD")) :four-of-a-kind))
    (is (= (:name (hand "3S 3D 3C 3H 7D")) :four-of-a-kind)))
  (testing "Full house"
    (is (= (:name (hand "AS AD AC KH KD")) :full-house)))
  (testing "Fluss"
    (is (= (:name (hand "AD 5D 3D 7D KD")) :flush)))
  (testing "Straight"
    (is (= (:name (hand "QS JD 9H TH 8H")) :straight)))
  (testing "tree-of-a-kind"
    (is (= (:name (hand "QS QD QH TH 8H")) :three-of-kind)))
  (testing "two-pair"
    (is (= (:name (hand "QS QD TD TH 8H")) :two-pair)))
  (testing "one-pair"
    (is (= (:name (hand "QS QD 3H 4H 8H")) :one-pair)))
  (testing "high-card"
    (is (= (:name (hand "QS 2D 3H TH 8H")) :high-card))))

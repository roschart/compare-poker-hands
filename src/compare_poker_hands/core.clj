(ns compare-poker-hands.core)

(def card-values
  {\2 2 \3 3 \4 4 \5 5 \6 6 \7 7 \8 8 \9 9 \T 10 \J 11 \Q 12 \K 13 \A 14})

(def hand-ranking
  {:straight-flush  0
   :four-of-a-kind  1
   :full-house      2
   :high-card       20})

(defn str-to-card
  "5S -> {:value 5 :suit S}
   KD -> {:value 13 :suit D}"
  [card]
  {:value (card-values (first card))
   :suit (second card)})

(defn hand-to-cards
  "Take a string a return a list of cards"
  [cards]
  (-> cards
    (clojure.string/split #" ")
    (->> (map str-to-card))))

(defn straight-analisys [hand]
  (let [sorted (->> hand
                   (map :value)
                   (sort))
        higer (last sorted)
        lower (first sorted)
        straight? (= 4 (- higer lower))]
      {:result straight? :to-break-tie higer :name :straight-flush}))

(defn hand-analisys [hand]
  (let [groups (->> hand
                   (map :value)
                   (group-by identity)
                   (map (fn [[value group]] [(count group) value])))
        sorted (reverse (sort groups))
        kinds (map first sorted)
        values (map second sorted)
        sorted-values (sort values)
        lower (first sorted-values)
        higer (last sorted-values)
        posible-straight (= 4 (- higer lower))
        same-suit (->> hand (map :suit) (apply =))]
      (cond
        (= [4 1] kinds) {:result kinds :to-break-tie (into [] sorted) :name :four-of-a-kind}
        (= [3 2] kinds) {:result kinds :to-break-tie (into [] sorted) :name :full-house}
        (= [1 1 1 1 1] kinds)
        (cond
          (and posible-straight same-suit)
          {:result kinds :to-break-tie higer :name :straight-flush}
          same-suit        {:result kinds :to-break-tie (into [] sorted-values) :name :flush}
          posible-straight {:result kinds :to-break-tie higer :name :straight})
        :else {:result kinds :to-break-tie sorted-values :name :not-match :posible-straight posible-straight :same-suit same-suit})))


(defn hand [hand-str]
  "Create a map with all data of the a hand from a String"
  (let [cards (-> hand-str
                  hand-to-cards)
        analisys (hand-analisys cards)]
   {:cards cards :analisys analisys :name (:name analisys)}))

(defn compare-same-hand
  [firstHand secondHand]
  (compare (get-in secondHand [:analisys :to-break-tie])
           (get-in firstHand  [:analisys :to-break-tie])))


(defn ComparePokerHands
  [firstHand secondHand]
  (let [fh (hand firstHand)
        sh (hand secondHand)
        compare-hand   (compare (hand-ranking (:name fh))
                                (hand-ranking (:name sh)))]
    (if (= 0 compare-hand)
        (compare-same-hand fh sh)
        compare-hand)))

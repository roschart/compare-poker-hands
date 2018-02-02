(ns compare-poker-hands.core)

(def card-values
  {\2 2 \3 3 \4 4 \5 5 \6 6 \7 7 \8 8 \9 9 \T 10 \J 11 \Q 12 \K 13 \A 14})

(def hand-ranking
  {:straight-flush 0
   :high-card 10})

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

(defn straight-flush-analisys [hand]
  (let [sorted (->> hand
                   (map :value)
                   (sort))
        higer (last sorted)
        lower (first sorted)
        result (= 4 (- higer lower))]
      {:result result :to_break_tie higer :name :straight-flush}))


(defn hand-analisys [hand]
  "From list of carst to name"
  (cond
    (:result (straight-flush-analisys hand)) (straight-flush-analisys hand)
    :else :high-card))

(defn hand [hand-str]
  "Create a map with all data of the a hand from a String"
  (let [cards (-> hand-str
                  hand-to-cards)
        analisys (hand-analisys cards)]
   {:cards cards :analisys analisys :name (:name analisys)}))

(defn compare-same-hand
  [firstHand secondHand hand-type]
  (case hand-type
    :high-card 45
    42))

(defn ComparePokerHands
  [firstHand secondHand]
  (let [fh (hand firstHand)
        sh (hand secondHand)
        compare-hand   (compare (hand-ranking (:name fh))
                                (hand-ranking (:name sh)))]
    (if (= 0 compare-hand)
        (compare-same-hand fh sh (:name fh))
        compare-hand)))

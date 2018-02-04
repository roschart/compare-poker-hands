(ns compare-poker-hands.core)

(def card-values
  {\2 2 \3 3 \4 4 \5 5 \6 6 \7 7 \8 8 \9 9 \T 10 \J 11 \Q 12 \K 13 \A 14})

(def hand-ranking
  {:straight-flush 10
   :high-card 0})

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

(defn same-kind [hand]
  (let [values (->> hand
                   (map :value)
                   (group-by identity)
                   (map (fn [[value group]] [(count group) value])))
        sorted (reverse (sort values))]
      (cond
        (= 4 (first (first sorted))) {:result sorted :to-break-tie sorted :name :four-of-a-kind}
        :else {:result true :to-break-tie sorted :name :same-kind})))



(defn hand-analisys [hand]
  "From list of carst to name"
  (cond
    (:result (straight-analisys hand)) (straight-analisys hand)
    (:result (same-kind hand)) (same-kind hand)
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
    :straight-flush (compare (get-in secondHand  [:analisys :to-break-tie])
                             (get-in firstHand [:analisys :to-break-tie]))
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

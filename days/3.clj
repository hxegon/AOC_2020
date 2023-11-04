(ns days.3
  (:require [utils :as u]))

(def toboggan-map
  (->> "day3_toboggan_map.txt"
       u/input-filepath
       u/read-lines
       (map char-array)
       (map cycle)))

(defn slope-iteration
  "Returns a list of coordinates that successively move right and down
  by the numbers specified."
  ([right down] (slope-iteration [0 0] right down))
  ([start-xy right down]
   (iterate (fn [[x y]] [(+ right x) (+ down y)])
            start-xy)))

(defn slope-coords
  [right down]
  "Slope goes right infinitely but has a fixed height. Get all coordinates for a slope
   that remain in bounds."
  (let [height (-> toboggan-map count dec)
        coord-iter (slope-iteration right down)]
    (take-while #(<= (second %) height) coord-iter)))

(defn count-slope-trees [right down]
  (->> (slope-coords right down)
       (map (fn [[x y]] (-> toboggan-map (nth y) (nth x))))
       (filter (partial = \#))
       count))

(defn solution-part1 []
  (count-slope-trees 3 1))

;; (solution-part1)

(defn solution-part2 []
  (let [slopes [[1 1] [3 1] [5 1] [7 1] [1 2]]]
    (->> slopes
         (map #(apply count-slope-trees %))
         (apply *))))

;; (solution-part2)

(ns days.5
  (:require [utils :as u]
            [failjure.core :as f]))

(def rows (into [] (range 0 128)))
(def columns (into [] (range 0 8)))

(def example-input "FBFBBFFRLR")

(def example-output {:row 44 :column 5 :id 357})

(defn bsp-find
  "divide coll according to next :L or :R in bspsteps.
   The problem guarantees that coll never has an odd of
   items until the end, so there's no notion of splitting
   an odd number of items."
  [coll bspsteps]
  (if (or (empty? bspsteps) (-> coll count (<= 1)))
    coll
    (let [mid-point (-> coll count (/ 2))
          step      (first bspsteps)
          divider   (case step :L take :R drop nil)]
      (if-not divider
        (f/fail (str "invalid bspstep " step))
        (recur (divider mid-point coll) (rest bspsteps))))))

(defn fb->rl
  "\"FBBFBF\" -> \"LRRLRL\""
  [fb]
  (->> fb (map {\F \L \f \L \B \R \b \R}) (apply str)))

(defn parse-seat [line]
  (let [[row-steps column-steps] (split-with #{\F \B} line)
        row (->> row-steps fb->rl (map (comp keyword str)) (bsp-find rows) first)
        column (->> column-steps (map (comp keyword str)) (bsp-find columns) first)]
    {:row row
     :column column
     :id (-> row (* 8) (+ column))}))

(= (parse-seat example-input) example-output)

(defn solution []
  (->> "day5_boarding_passes.txt"
       u/input-filepath
       u/read-lines
       (map parse-seat)
       (map :id)
       (apply max)))

;; (solution)

(defn solution2 []
  (let [ids (->> "day5_boarding_passes.txt"
                   u/input-filepath
                   u/read-lines
                   (map parse-seat)
                   (map :id)
                   (sort))
        last-seat-id (last ids)
        result (reduce #(if (not= (inc %1) %2) (reduced (inc %1)) %2)
                       ids)]
    (when (not= last-seat-id result) result)))

(solution2)

(ns days.1
  (:require [utils :as u]
            [clojure.math.combinatorics :as mc]))

;; https://adventofcode.com/2020/day/1
;; Problem 1 is finding 2 numbers that sum to 2022 and multiplying them together.
;; Problem 2 is the same thing, but with 3 numbers so I only had to add a parameter.

(defn solution
  [input-filename n_of_entries]
  (->> input-filename
       u/input-filepath
       u/read-lines
       (map #(Integer/parseInt %))
       (#(mc/combinations % n_of_entries)) ; All combinations of entries of length n_of_entries
       (filter #(= (apply + %) 2020)) ; Only pairs summing to 2022
       first
       (apply *)))

;; Part 1
;; (solution "day1_expense_report.txt" 2)

;; Part 2
;; (solution "day1_expense_report.txt" 3)

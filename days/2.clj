(ns days.2
  (:require [utils :as u]
            [debug :as dbg]))

;; https://adventofcode.com/2020/day/2

(defn decode-line
  [line]
  (let [rgx #"(\d+)-(\d+) (\w): (.+)$"
        ;; make a map of caputured regex groups
        groups (zipmap [:a :b :letter :password]
                       (->> line (re-matches rgx) rest))]
    ;; Parse min and max values into integers
    (reduce (fn [gs k] (update gs k #(Integer/parseInt %)))
            groups
            [:a :b])))

(defn validate-part1
  [{:keys [a b letter password]}]
  (let [lcount (->> password (re-seq (re-pattern letter)) count)]
    (and (<= a lcount) (>= b lcount))))

(defn validate-part2
  [{:keys [a b letter password]}]
  (->> (map dec [a b]) ;; positions are 1-indexed
       (map #(str (.charAt password %)))
       (filter (partial = letter))
       count
       (= 1)))

(defn solution
  [validation input-filename]
  (->> input-filename
       u/input-filepath
       u/read-lines
       (map decode-line)
       (filter validation)
       count))

(solution validate-part1 "day2_corrupt_passwords.txt")
(solution validate-part2 "day2_corrupt_passwords.txt")

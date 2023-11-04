(ns days.4
  (:require [utils :as u]
            [clojure.string :as cs]
            [clojure.set :as cset]))

(def passport-abbrevs
  {"byr" :birth-year
   "iyr" :issue-year
   "eyr" :expiration-year
   "hgt" :height
   "hcl" :hair-color
   "ecl" :eye-color
   "pid" :passport-id
   "cid" :country-id})

(def decode-passport-lines-xf
  (comp (mapcat #(cs/split % #" ")) ; pp lines -> list of field strings
        (map #(->> % (re-matches #"\s?(\w\w\w):(\S+)") rest vec)) ; -> capture groups
        (map #(update % 0 passport-abbrevs)))) ; ->

(def field-parsers
  {:birth-year u/parse-int
   :issue-year u/parse-int
   :expiration-year u/parse-int
   :height (fn [fstr]
             (-> (some->> (re-matches #"(\d+)(cm|in)" fstr)
                          rest
                          (zipmap [:quantity :unit]))
                 (update :quantity u/parse-int)))})

(defn decode-passport [passport-lines]
  (into {}
        (comp decode-passport-lines-xf
              (map (fn [[k v]]
                     (let [parser (or (k field-parsers) identity)]
                       [k (parser v)]))))
        passport-lines))

(defn read-passports [filename]
  (->> filename
       u/input-filepath
       u/read-lines
       (into []
             (comp
              (partition-by empty?)      ; passports are separated by blank lines
              (remove (partial = '(""))) ; remove groups that are just those blank lines
              (map decode-passport)))))

(defn has-required-fields? [passport]
  (cset/superset? (-> passport keys set)
                  ;; passport must have these fields
                  #{:birth-year :issue-year :expiration-year :height :hair-color :eye-color :passport-id}))

(defn solution1 []
  (->> (read-passports "day4_passports.txt")
       (filter has-required-fields?)
       count))

(solution1)

(def field-validators
  {:birth-year #(u/in-range? % 1920 2002)
   :issue-year #(u/in-range? % 2010 2020)
   :expiration-year #(u/in-range? % 2020 2030)
   :height #(case (:unit %)
              "cm" (u/in-range? (:quantity %) 150 193)
              "in" (u/in-range? (:quantity %) 59 76)
              nil)
   :hair-color (partial re-matches #"#[\dabcdef]{6}")
   :eye-color #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"}
   :passport-id (partial re-matches #"\d{9}")})

(defn validate [x x-validators]
  (every? (fn [[x-key validator]]
            (some-> (get x x-key) validator))
          x-validators))

(validate
 {:birth-year 1920
  :issue-year 2010
  :expiration-year 2020
  :height {:unit "cm" :quantity 193}
  :hair-color "#12345a"
  :eye-color "blu"
  :passport-id "123456789"}
 field-validators)

(defn solution2 []
  (->> (read-passports "day4_passports.txt")
       (filter #(validate % field-validators))
       count))

(solution2)

(ns utils
  (:require [babashka.fs :as fs]
            [failjure.core :as f]
            [clojure.string :as string]))

(defn input-filepath
  [input-filename]
  (-> (fs/cwd)
       str
       (fs/path "inputs" input-filename)
       str))

(defn read-lines [filepath]
  (-> filepath slurp string/split-lines))

(defn fail-unless
  "Version of failjure/assert-with that plays well with ->>"
  [predfn formatstr val]
  (f/assert-with predfn val (format formatstr val)))

(defn parse-int
  "Returns a parsed integer or nil if it couldn't parse"
  [s]
  (try (Integer/parseInt s)
       (catch Exception _e nil)))

(defn in-range? [n min max]
  (and (>= n min) (<= n max)))

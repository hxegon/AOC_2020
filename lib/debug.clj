(ns debug)

(defn tap->>
  ([x] (tap->> x identity))
  ([f x] (println (f x)) x))

(defn tap->
  ([x] (tap-> x identity))
  ([x f] (println (f x)) x))

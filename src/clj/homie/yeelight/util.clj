(ns homie.yeelight.util
  (:require [byte-streams :as bs]
            [homie.constant :as const]))


(defn gen-light-identifier
  "Produces a unique identifier for a light given a map of it's discovery information"
  [{:keys [id]}]
  (keyword id))


(defn parse-udp-message
  "Given a socket response, {:sender ... :message ...}
  returns the decoded message bytestream as a string"
  [{:keys [message]}]
  (bs/to-string message))


(defn light-name
  "Takes a light identifier and returns a light name"
  [id]
  (let [[room-key room-info] (first (filter (fn [[_ info]] (some #{id} (:yeelights info))) const/rooms))]
    (str (:name room-info) " Light")))

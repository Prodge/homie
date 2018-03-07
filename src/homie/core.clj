(ns homie.core
  (:require [clojure.string :as str]

            [aleph.tcp :as tcp]
            [aleph.udp :as udp]
            [manifold.stream :as stream]
            [byte-streams :as bs])
  (:gen-class))




(def n-broadcast-results 20)

(def search-broadcast-message
"M-SEARCH * HTTP/1.1\r
HOST: 239.255.255.250:1982\r
MAN: \"ssdp:discover\"\r
ST: wifi_bulb")


(defn parse-message
  "Given a socket response, {:sender ... :message ...}
  returns the decoded message bytestream as a string"
  [{:keys [message]}]
  (bs/to-string message))


(defn extract-discover-message-contents
  "Given a discovery message response string, (K: V\r\nK: V...)
  returns all contained data as a map"
  [message]
  (->> (str/split message #"\r\n")
       (filter (fn [line] (str/includes? line ":")))
       (map (fn [line] (str/split line #": ")))
       (filter (fn [pair] (= (count pair) 2)))
       (map (fn [[k v]] [(keyword (str/lower-case k)) v]))
       (into {})))


(defn gen-light-identifier
  "Produces a unique identifier for a light given a map of it's discovery information"
  [{:keys [location]}]
  (keyword location))


(defn key-light-map
  "Takes light info and returns a map keyed by the light identifier"
  [info]
  {(gen-light-identifier info) info})


(defn parse-light-discover-response
  "Loads the information from a light discovery broadcast response into state"
  [response]
  (->> response
       parse-message
       extract-discover-message-contents
       key-light-map))


(defn apply-to-state
  "Given an atom, applies a swap with the given swap-func with the swap argument
  produced by the given function applied to the given argument"
  [state swap-func func argument]
  (swap! state swap-func (func argument)))


(defn discover-lights!
  "blocks while broadcasting for any local yeelights
  then async consumes all results into the given atom"
  [state]
  (let [client-socket @(udp/socket {})
        lights (atom [])]

    (stream/put! client-socket
                 {:host "239.255.255.250"
                  :port 1982,
                  :broadcast? true
                  :message search-broadcast-message})

    (stream/consume
      (partial apply-to-state state conj parse-light-discover-response)
      client-socket)))


(defn -main
  [& args]
  (let [lights (atom {})]
    (println "starting")
    (discover-lights! lights)
    (println "done")
    (clojure.pprint/pprint @lights)
    (Thread/sleep 2000)
    (println "---")
    (clojure.pprint/pprint @lights)
    (println "---")
    (clojure.pprint/pprint (keys @lights))

    )
  )

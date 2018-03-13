(ns homie.yeelight.event
  (:require [clojure.string :as str]
            [clojure.core.async :as async]

            [aleph.tcp :as tcp]
            [aleph.udp :as udp]
            [manifold.stream :as stream]

            [homie.common :as c]
            [homie.yeelight.util :as y-util])
  (:gen-class))

(def search-broadcast-message
  "M-SEARCH * HTTP/1.1\r
HOST: 239.255.255.250:1982\r
MAN: \"ssdp:discover\"\r
ST: wifi_bulb")

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

(defn parse-light-discover-response
  "Loads the information from a light discovery broadcast response into state"
  [response]
  (->> response
       y-util/parse-udp-message
       extract-discover-message-contents))

(defn discover-lights!
  "blocks while broadcasting for any local yeelights
  then async consumes all results into the returned channel"
  []
  (let [client-socket @(udp/socket {})
        result-channel (async/chan)]

    (stream/put! client-socket
                 {:host "239.255.255.250"
                  :port 1982
                  :broadcast? true
                  :message search-broadcast-message})

    (stream/consume
     (fn [msg]
       (async/put! result-channel (c/encode-event :yeelight-discover (parse-light-discover-response msg))))
     client-socket)
    result-channel))

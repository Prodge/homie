(ns homie.common
  (:require #? (:clj [clj-time.core :as t]
                :cljs [cljs-time.core :as t])
            #? (:clj [clj-time.format :as f]
                :cljs [cljs-time.format :as f])))

(defn encode-event
  "Encodes a standard format with the given event name and messages contents"
  [event-name message]
  {:name event-name
   :message message
   :timestamp (f/unparse (f/formatters :basic-date-time) (t/now))})

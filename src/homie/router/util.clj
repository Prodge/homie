(ns homie.router.util
  (:require [clj-time.core :as t]))


(defn encode-event
  "Encodes a standard format with the given event name and messages contents"
  [event-name message]
  {:name event-name
   :message message
   :timestamp (t/now)})

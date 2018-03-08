(ns homie.state.query
  (:require [homie.state.core :refer [state]]))


(defn yeelight-exists?
  "Returns true when a yeelight is already known of."
  [identifier]
  (contains? (:yeelight @state) identifier))

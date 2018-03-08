(ns homie.state.alter
  (:require [homie.state.core :refer [state]]))


(defn yeelight-update! [identifier info]
  (swap! state assoc identifier info))

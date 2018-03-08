(ns homie.yeelight.handler
  (:require [homie.state.alter :as s-alter]
            [homie.yeelight.util :as y-util]))

(defn discover
  "Handles yeelight discovery broadcast messages"
  [{:keys [message timestamp]}]
    (s-alter/yeelight-update!
      (y-util/gen-light-identifier message)
      (assoc message :last-seen timestamp)))

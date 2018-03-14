(ns homie.yeelight.handler
  (:require [clojure.tools.logging :as log]

            [homie.state.alter :as s-alter]
            [homie.yeelight.util :as y-util]))

(defn discover
  "Handles yeelight discovery broadcast messages"
  [{:keys [message timestamp]}]
    (s-alter/yeelight-update!
      (y-util/gen-light-identifier message)
      (assoc message :last-seen timestamp)))

(defn set-op
  "Handles the set_ operations"
  [{:keys [message]}]
    (log/info "set" (:set message) "to" (:to message)))

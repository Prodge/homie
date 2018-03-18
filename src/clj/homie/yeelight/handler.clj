(ns homie.yeelight.handler
  (:require [clojure.tools.logging :as log]

            [homie.state.alter :as s-alter]
            [homie.yeelight.util :as y-util]))

(defn discover
  "Handles yeelight discovery broadcast messages"
  [{:keys [message timestamp]}]
    (let [id (y-util/gen-light-identifier message)]
    (s-alter/yeelight-update!
      id
      (assoc message :last-seen timestamp :name (y-util/light-name id)))))

(defn set-op
  "Handles the set_ operations"
  [{:keys [message]}]
    (log/info "set" (:set message) "to" (:to message)))

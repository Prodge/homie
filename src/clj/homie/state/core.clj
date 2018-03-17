(ns homie.state.core
  (:require [clojure.core.async :as async :refer [go <! >!]]
            [clojure.tools.logging :as log]))

(def state
  (atom {:yeelight {}}))

(def event-pullers
  (atom []))

(defn push-state-diff [c-key, reference, old-state, new-state]
  "Function to watch the state atom and propogate changes to the watcher channels"
  (log/info "Propogating state change")
  (doall (map #(go (>! %  new-state)) @event-pullers)))

(defn register-event-puller [channel]
  (log/info "Registering event puller")
  (swap! event-pullers conj channel))

(add-watch state :watcher push-state-diff)

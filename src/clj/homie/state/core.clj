(ns homie.state.core
  (:require [clojure.core.async :as async :refer [go <! >!]]))

(def state
  (atom {:yeelight {}}))

(def event-pullers
  (atom []))

(defn push-state-diff [c-key, reference, old-state, new-state]
  "Function to watch the state atom and propogate changes to the watcher channels"
  (go (for [c @event-pullers]
        (>! c new-state))))

(add-watch state :watcher push-state-diff)

(ns homie.state.core
  (:require [clojure.core.async :as async :refer [go <! >!]]))

(def state
  (atom {:yeelight {}}))

(def watcher-channels
  (atom []))

(defn push-state-diff [c-key, reference, old-state, new-state]
  "Function to watch the state atom and propogate changes to the watcher channels"
  (go (for [c @watcher-channels]
        (>! c new-state))))

(add-watch state :watcher push-state-diff)

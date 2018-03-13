(ns homie.socket
  (:require [cljs.core.async :refer [<! >! chan]]
            [re-frame.core :as re-frame]

            [chord.client :refer [ws-ch]])
  (:require-macros [cljs.core.async.macros :refer [go]]))

(def event-stream (chan))

(defn establish-state-comms
  "Pull the current state of the house from the server.
  Read only socket."
  []
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8000/ws/state"))]
      (if error
        (console.log error)
        (loop []
          (let [res (<! ws-channel)]
          (re-frame/dispatch-sync [:new-home (:message res)] )
          (when res (recur))))))))

(defn establish-event-comms
 "Pipe events from the event-stream channel into the ws-channel.
 Write Only Socket."
 []
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8000/ws/event"))]
      (if error
        (console.log error)
        (while true
             (>! ws-channel (<! event-stream)))))))

(ns homie.core
  (:require [clojure.core.async :as async :refer [go <! >! <!! >!!]]

            [homie.router.core :as router]
            [homie.yeelight.event :as e-yeelight]
            [homie.web.core :as web])
  (:gen-class))

(defn stop-app []
  (println "stopped")
  (shutdown-agents))

(defn start-event-stream [args]
  (go
    (let [event-chan (async/merge [(e-yeelight/discover-lights!)])]
      (while true
        (router/router (<! event-chan))))))

(defn -main
  [& args]
  (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app))
  (web/start-server "8000")
    ; Unresolving blocking take - Keeps us running forever.
  (<!! (start-event-stream args)))

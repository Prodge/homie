(ns homie.core
  (:require [clojure.core.async :as async :refer [go <! >! <!! >!!]]

            [homie.router.core :as router]
            [homie.yeelight.event :as e-yeelight])
  (:gen-class))


(defn stop-app []
  (println "stopped")
  (shutdown-agents))


(defn start-app [args]
  (go
    (let [event-chan (async/merge [(e-yeelight/discover-lights!)])]
      (while true
        (router/router (<! event-chan))))))


(defn -main
  [& args]
    (.addShutdownHook (Runtime/getRuntime) (Thread. stop-app))
    ; Unresolving blocking take - Keeps us running forever.
    (<!! (start-app args)))

(ns homie.core
  (:require [cljs.core.async :refer [<! >!]]

            [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [chord.client :refer [ws-ch]]

            [homie.events :as events]
            [homie.views :as views]
            [homie.config :as config])
  (:require-macros [cljs.core.async.macros :refer [go]]))


(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn establish-socket-comms []
  (go
    (let [{:keys [ws-channel error]} (<! (ws-ch "ws://localhost:8000/state-ws"))]
      (if error
        (console.log error)
        (loop []
          (println (<! ws-channel))
          (recur))))))

(defn ^:export init []
  (console.log "Mounted")
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)

  (establish-socket-comms))

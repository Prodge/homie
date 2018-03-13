(ns homie.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]

            [homie.events :as events]
            [homie.effect :as effect]
            [homie.views :as views]
            [homie.socket :as socket]
            [homie.config :as config]))

(defn on-js-reload []
	(console.log "Reloading Figwheel"))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)
    (println "dev mode")))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (console.log "Mounted")
  (re-frame/dispatch-sync [::events/initialize-db])
  (dev-setup)
  (mount-root)
  (socket/establish-state-comms)
  (socket/establish-event-comms))

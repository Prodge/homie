(ns homie.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [homie.db :as db]))

(reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db :new-home
  ; Write the new version of the home
    (fn [db [_ home-state]]
      (assoc db :home home-state)))

(reg-event-fx :yeelight-power
  (fn  [cofx  [_ id]]
    (let [power (if (= "on" (get-in (:db cofx) [:home :yeelight id :power])) "off" "on")]
       {:db (assoc-in (:db cofx) [:home :yeelight id :power] power)
        :publish-event [:yeelight-set {:set "power" :to power}]})))

(reg-event-fx :yeelight-brightness
  (fn  [cofx  [_ id brightness]]
    {:db (assoc-in (:db cofx) [:home :yeelight id :bright] brightness)
     :publish-event [:yeelight-set {:set "bright" :to brightness}]}))

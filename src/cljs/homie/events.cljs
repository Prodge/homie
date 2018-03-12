(ns homie.events
  (:require [re-frame.core :refer [reg-event-db]]
            [homie.db :as db]))

(reg-event-db
 ::initialize-db
 (fn  [_ _]
   db/default-db))

(reg-event-db :new-home
  ; Write the new version of the home
    (fn [db [_ home-state]]
      (assoc db :home home-state)))


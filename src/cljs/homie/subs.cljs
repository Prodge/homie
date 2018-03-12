(ns homie.subs
  (:require [re-frame.core :as re-frame]))

(re-frame/reg-sub
  ::name
  (fn [db]
    (:name db)))

(re-frame/reg-sub
  :yeelight-ids
  (fn [db]
    (sort (keys (get-in db [:home :yeelight])))))

(re-frame/reg-sub
  :yeelight-info
  (fn [db [_ id]]
    (get-in db [:home :yeelight id])))

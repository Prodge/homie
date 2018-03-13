(ns homie.effect
  (:require [cljs.core.async :refer  [chan <! >! put!]]
            [re-frame.core :refer  [reg-fx]]
            [homie.socket :as socket]
            [homie.common :as c])
  (:require-macros [cljs.core.async.macros :refer  [go]]))

(reg-fx :log
  (fn  [& args]
    (apply println args)))

(reg-fx :publish-event
  (fn [[ event-name event-info]]
    (go (>! socket/event-stream (c/encode-event event-name event-info)))))

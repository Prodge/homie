(ns homie.router.routes
  (:require [homie.yeelight.handler :as h-yeelight]))


(def routes {
  :yeelight-discover h-yeelight/discover
  :yeelight-set h-yeelight/set-op
})

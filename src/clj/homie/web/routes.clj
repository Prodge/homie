(ns homie.web.routes
  (:require [clojure.core.async :refer [go <! >! chan]]
            [clojure.tools.logging :as log]

            [compojure.core :refer [defroutes GET POST]]
            [chord.http-kit :refer [with-channel]]

            [homie.state.core :as state]
            [homie.router.core :as router]))

(defn home [request]
  "
  <head>
    <link rel=\"stylesheet\" type=\"text/css\" href=\"css/main.css\">
  </head>
  <body>
    <div id=\"app\"></div>
    <script src=\"js/compiled/app.js\" type=\"text/javascript\"></script>
    <script>homie.core.init();</script>
  </body>
  ")

(defn state-sync [request]
  (with-channel request ws-ch
    (go
      (let [state-stream (chan)]
        (state/register-event-puller state-stream)
          (loop []
            (>! ws-ch (<! state-stream))
            (log/info "Pushing state change to client")
            (recur))))))

(defn event-stream [request]
  (with-channel request ws-ch
    (go
      (loop []
        (let [res (<! ws-ch) ]
          (log/info "Received event from client")
          (router/router (:message res))
          (when res (recur)))))))

(defroutes web-routes
  (GET "/" [] home)
  (GET "/ws/state" [] state-sync)
  (GET "/ws/event" [] event-stream))

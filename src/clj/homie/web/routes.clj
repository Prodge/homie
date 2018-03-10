(ns homie.web.routes
  (:require [clojure.core.async :refer [go <! >!]]

            [compojure.core :refer [defroutes GET POST]]
            [chord.http-kit :refer [with-channel]]

            [homie.state.core :as state]))

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
      (>! ws-ch @state/state)
      (>! ws-ch {:test {:foo "bar"}}))))

(defroutes web-routes
  (GET "/" [] home)
  (GET "/state-ws" [] state-sync))

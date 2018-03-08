(ns homie.web.routes
  (:require [compojure.core :refer [defroutes GET POST]]))

(defn home []
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

(defroutes web-routes
(GET "/" [] (home)))

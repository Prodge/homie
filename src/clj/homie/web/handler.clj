(ns homie.web.handler
  (:require [clojure.tools.logging :as log]

            [compojure.core :refer [defroutes routes]]
            [compojure.handler :as handler]
            [compojure.route :as route]

            [homie.web.routes :refer [web-routes]]))

(defn init []
  (log/info "web service is starting"))

(defn destroy []
  (log/info "web service is shutting down"))

(defroutes app-routes
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (routes web-routes app-routes)
      (handler/site)))

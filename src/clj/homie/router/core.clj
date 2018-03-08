(ns homie.router.core
  (:require [clojure.tools.logging :as log]

            [homie.router.routes :as routes]))


(defn router
  "Routes events based on their name to the appropriate handler fn"
  [event]
  (log/info "Routing Event:" event)
  (((:name event) routes/routes) event))

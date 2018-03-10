(ns homie.web.core
  (:require [clojure.tools.logging :as log])
  (:use homie.web.handler
        ;ring.server.standalone
        org.httpkit.server))

(defonce server (atom nil))

(defn get-handler []
  #'app)

(defn start-server
  "used for starting the server in development mode from REPL"
  [& [port]]
  (let [port (if port (Integer/parseInt port) 8080)]
    (reset! server
            (run-server (get-handler)
                        {:port port
                         :init init
                         :auto-reload? true
                         :destroy destroy
                         :join true}))
    (log/info (str "You can view the site at http://localhost:" port))))

(defn stop-server []
  (.stop @server)
  (reset! server nil))

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
        (swap! state/event-pullers conj state-stream)

        ;(>! ws-ch (<! state-stream))

        (>! ws-ch {:yeelight {:0x0000000000155555543f {:location "yeelight://192.168.1.239:55443"
                                                               :server "POSIX UPnP/1.0 YGLC/1"
                                                               :id "0x0000000000155555543f"
                                                               :model "color"
                                                               :fw_ver 18
                                                               :support "get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_rgb"
                                                               :power "on"
                                                               :bright "100"
                                                               :color_mode "2"
                                                               :ct "4000"
                                                               :rgb "16711680"
                                                               :hue "100"
                                                               :sat "35"
                                                               :name "Yeelight"}}})
        (>! ws-ch {:yeelight {
                              :0x000000000015243f {:location "yeelight://192.168.1.239:55443"
                                                               :server "POSIX UPnP/1.0 YGLC/1"
                                                               :id "0x000000000015243f"
                                                               :model "color"
                                                               :fw_ver 18
                                                               :support "get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_rgb"
                                                               :power "off"
                                                               :bright "100"
                                                               :color_mode "2"
                                                               :ct "4000"
                                                               :rgb "16711680"
                                                               :hue "100"
                                                               :sat "35"
                                                               :name "Yeelight"}
                              :0x0000000000152553f {:location "yeelight://192.168.1.239:55443"
                                                              :server "POSIX UPnP/1.0 YGLC/1"
                                                               :id "0x0000000000155543f"
                                                               :model "color"
                                                               :fw_ver 18
                                                               :support "get_prop set_default set_power toggle set_bright start_cf stop_cf set_scene cron_add cron_get cron_del set_ct_abx set_rgb"
                                                               :power "on"
                                                               :bright "75"
                                                               :color_mode "2"
                                                               :ct "4000"
                                                               :rgb "16711680"
                                                               :hue "75"
                                                               :sat "35"
                                                               :name "Yeelight"}}}))
        )))

(defn event-stream [request]
  (with-channel request ws-ch
    (go
      (loop []
        (let [res (<! ws-ch) ]
          (router/router (:message res))
          (when res (recur)))))))

(defroutes web-routes
  (GET "/" [] home)
  (GET "/ws/state" [] state-sync)
  (GET "/ws/event" [] event-stream))

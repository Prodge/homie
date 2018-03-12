(defproject homie "0.1.0-SNAPSHOT"
  :description "Home Automation Brain"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/tools.logging "0.4.0"]
                 [org.clojure/clojurescript "1.9.908"]

                 [aleph "0.4.4"]
                 [manifold "0.1.6"]
                 [byte-streams "0.2.3"]
                 [clj-time "0.14.2"]
                 [compojure "1.5.2"]
                 [ring-server "0.4.0"]
                 [http-kit "2.2.0"]
                 [reagent "0.8.0-alpha2"]
                 [re-frame "0.10.5"]
                 [com.andrewmcveigh/cljs-time "0.5.0"]
                 [garden "1.3.2"]
                 [reagent-material-ui "0.2.5"]
                 [jarohen/chord "0.8.1"]]

  :plugins [[lein-figwheel "0.5.15"]
			[lein-cljsbuild "1.1.5"]
            [lein-garden "0.2.8"]]

  :source-paths ["src/clj"]


  :clean-targets ^{:protect false} ["resources/public/js/compiled"
									"resources/public/css"]

  :garden {:builds [{:id           "main"
                     :source-paths ["src/clj"]
                     :stylesheet   style.core/main
                     :compiler     {:output-to "resources/public/css/main.css"
					 :pretty-print? true}}]}

  :main ^:skip-aot homie.core

  :target-path "target/%s"

  :cljsbuild
    {:builds
     [{:id           "dev"
       :source-paths ["src/cljs"]
       :figwheel     {:on-jsload "homie.core/on-js-reload"}
       :compiler     {:main                 homie.core
                      :output-to            "resources/public/js/compiled/app.js"
                      :output-dir           "resources/public/js/compiled/out"
                      :asset-path           "js/compiled/out"
                      :source-map-timestamp true
                      :preloads             [devtools.preload
                                             day8.re-frame-10x.preload
                                             re-frisk.preload]
                      :closure-defines      {"re_frame.trace.trace_enabled_QMARK_" true}
                      :external-config      {:devtools/config {:features-to-install :all}}}}

      {:id           "min"
       :source-paths ["src/cljs"]
       :jar true
       :compiler     {:main            homie.core
                      :output-to       "resources/public/js/compiled/app.js"
                      :optimizations   :advanced
                      :closure-defines {goog.DEBUG false}
                      :pretty-print    false}}]}

  :figwheel {:css-dirs ["resources/public/css"]}

  :profiles {:uberjar {:aot :all}
             :production { }
             :dev {:dependencies [[binaryage/devtools "0.9.4"]
                                  [day8.re-frame/re-frame-10x "0.2.0"]
                                  [re-frisk "0.5.3"]]}})

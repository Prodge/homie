(defproject homie "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/core.async "0.4.474"]
                 [org.clojure/tools.logging "0.4.0"]

                 [aleph "0.4.4"]
                 [manifold "0.1.6"]
                 [byte-streams "0.2.3"]
                 [clj-time "0.14.2"]
                 [compojure "1.5.2"]
                 [ring-server "0.4.0"]]

  :main ^:skip-aot homie.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})

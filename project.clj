(defproject fbielejec/cljs-firebase-client "0.0.9-SNAPSHOT"
  :description "Clojurescript library for interacting with Firebase"
  :url "https://github.com/"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojurescript "1.10.439"]
                 [re-frame "0.10.6"]]

  :npm {:dependencies [["firebase" "6.2.3"]
                       ["firebase-admin" "7.3.0"]
                       ["source-map-support" "0.5.13"]]}

  :profiles {:dev {:dependencies [[org.clojure/clojure "1.9.0"]]
                   :plugins [[lein-npm "0.6.2"]]}}

  :deploy-repositories [["snapshots" {:url "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]
                        ["releases"  {:url "https://clojars.org/repo"
                                      :username :env/clojars_username
                                      :password :env/clojars_password
                                      :sign-releases false}]]

  :release-tasks [["vcs" "assert-committed"]
                  ["change" "version" "leiningen.release/bump-version" "release"]
                  ["deploy"]])

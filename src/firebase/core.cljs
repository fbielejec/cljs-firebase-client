(ns firebase.core
  (:require ["firebase" :as Firebase]))

(defonce firebase-instance (atom nil))

(defn init [config]
  (when-not @firebase-instance
    (reset! firebase-instance (-> Firebase (.initializeApp (clj->js config))))))

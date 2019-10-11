(ns firebase.admin
  (:require ["firebase-admin" :as admin]))

(defonce admin-instance (atom nil))

(defn server-timestamp []
  (-> admin .-firestore .-FieldValue .serverTimestamp))

(defn credential [cert]
  (-> admin .-credential (.cert cert)))

(defn init [config]
  (when-not @admin-instance
    (do (prn "initializing firebase-admin")
        (reset! admin-instance
                (-> admin (.initializeApp (clj->js config)))))))

(defn list-users []
  (-> admin .auth (.listUsers 100)))

(defn delete-user [uid]
  (-> admin .auth (.deleteUser uid)))

(ns firebase.auth
  (:require ["firebase" :as Firebase]))

(defn email-sign-in [{:keys [:email :password]}]
  (-> Firebase
      .auth
      (.signInWithEmailAndPassword email password)))

(defn email-create-user [{:keys [:email :password]}]
  (-> Firebase
      .auth
      (.createUserWithEmailAndPassword email password)))

(defn on-auth-change [{:keys [:on-change]}]
  (-> Firebase
      .auth
      (.onAuthStateChanged (fn [user]
                             (on-change user)))))

(defn current-user []
  (-> Firebase .auth .-currentUser))

(defn update-user-profile [user {:keys [:photo-url :display-name]}]
  (-> user
      (.updateProfile (clj->js {:displayName display-name
                                :photoURL photo-url}))))

(defn logout []
  (-> Firebase .auth .signOut))

(defn email-login [{:keys [:email :password :on-success :on-error]}]
  (-> (email-sign-in {:email email :password password})
      (.then (fn [result]
               (on-success result)))
      (.catch (fn [error]
                (on-error error)))))

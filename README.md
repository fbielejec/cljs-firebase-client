# nodrama/cljs-firebase-client

ClojureScript library for calling the [Firebase API](https://firebase.google.com) <br>
<br>

## Installation
Latest released version of this library: <br>
[![Clojars Project]()](https://clojars.org/nodrama/cljs-firebase-client)

## API Overview

Stick with the api of the javascript client [docs](https://firebase.google.com/docs/web/setup), all of the documented methods have their version in the JS library.
There are helper methods to kebab-case and keywordize the return values and responses.

- [firebase.core](#core)
  - [init!](#init)
- [firebase.auth](#auth)
  - [email-sign-in](#email-sign-in)
  - [email-create-user](#email-create-user)
  - [on-auth-change](#on-auth-change)
  - [current-user](#current-user)
  - [update-user-profile](#update-user-profile)
  - [logout](#logout)
  - [email-login](#email-login)
- [firebase.admin](#admin)
   - [init](#init)
   - [server-timestamp](#server-timestamp)
   - [credential](#credential)
   - [list-users](#list-users)
   - [delete-user](#delete-user)
- [firebase.firestore](#firebase.firestore)
   - [coll-ref](#coll-ref)
   - [doc-ref](#doc-ref)
   - [document-add](#document-add)
   - [document-set](#document-set)
   - [document->clj](#document->clj)
   - [snapshot->clj](#snapshot->clj)
   - [query](#query)
   - [get-collection](#get-collection)
   - [get-document](#get-document)
   - [get-document-field-value](#get-document-field-value)
   - [delete-document](#delete-document)
   - [where](#where)
   - [order-by](#order-by)
   - [start-at](#start-at)
   - [start-after](#start-after)
   - [limit](#limit)
- [events.firebase](#events)

### <a name="firebase.core"></a>`firebase.core`

#### <a name="init"></a>`init`

Initialize Firebase client. Takes a map of [config](https://firebase.google.com/docs/web/setup) values as an argument. Example:

```
(firebase/init {:apiKey "key"
                :authDomain "my-1b84b.firebaseapp.com"
                :databaseURL "https://my-1b84b.firebaseio.com"
                :projectId "my-1b84b"
                :storageBucket "my-1b84b.appspot.com"
                :messagingSenderId "386365028401"})
```
### <a name="firebase.auth"></a>`firebase.auth`

#### <a name="email-sign-in"></a>`email-sign-in`

#### <a name="email-create-user"></a>`email-create-user`

Example:

```
(-> (auth/email-create-user {:email "me@here.com"
                             :password "pass"})
    (.then #(-> % .-user .-uid)))
```

#### <a name="on-auth-change"></a>`on-auth-change`

#### <a name="current-user"></a>`current-user`

#### <a name="update-user-profile"></a>`update-user-profile`

#### <a name="logout"></a>`logout`

#### <a name="email-login"></a>`email-login`

### <a name="firebase.admin"></a>`firebase.admin`

Admin SDK lets you interact with Firebase from privileged environments.

---
**NOTE**

This namespace will only work in a NodeJS runtime environment.

---

#### <a name="credential"></a>`credential`

Create [google application credentials](https://cloud.google.com/docs/authentication/production#providing_credentials_to_your_application) from a service account file.

#### <a name="init"></a>`init`

Initialize [Firebase Admin SDK](https://firebase.google.com/docs/admin/setup/#initialize_the_sdk). Example:

```clojure
(ns my-app
  (:require [app.firebase.admin :as admin]))

(defonce service-account (js/require "./assets/my-1b84b-firebase-adminsdk-svaar-a0a51894fa.json"))

(admin/init {:databaseURL "https://my-1b84b.firebaseio.com"
             :credential (admin/credential service-account)})
```

#### <a name="server.timestamp"></a>`server.timestamp`

#### <a name="list-users"></a>`list-users`

Returns a JS/Promise with a list of all users:

```
(list-users)
```
#### <a name="delete-user"></a>`delete-user`

Deletes a user, take suser id as an argument:
Example (deletes all users):

```
(-> (list-users)
    (.then #(js/Promise.all (->> (js->clj % :keywordize-keys true)
                                 :users
                                 (map (fn [user]
                                        (let [uid (.-uid user)]
                                          (delete-user uid)
                                          uid)))))))
```

### <a name="firebase.firestore"></a>`firebase.firestore`

#### <a name="coll-ref"></a>`coll-ref`

#### <a name="doc-ref"></a>`doc-ref`

#### <a name="document-set"></a>`document-set`

Sets (creates) a document with a given id under a cpecified collection.

Creates or overwrite a single document inside a collection, returns a JS/Promise.
If the documetn does not exist it will be created, unless the merge option is specified.
Example:

```clojure
(-> (db/document-set {:collection "followers"
                      :id "VFdERNhp94PIJijeuxjJYXTLEZm2"
                      :document {"ml8mnGIEINP1eol2PSWSfnlen0Q2" true}}
                     {:merge true})
    (.then #(db/document-set {:collection "following"
                              :id "ml8mnGIEINP1eol2PSWSfnlen0Q2"
                              :document {"VFdERNhp94PIJijeuxjJYXTLEZm2" true}}
                             {:merge true})))
```

#### <a name="document-add"></a>`document-add`

Same as [document-set](#document-set) but without specifying an id, instead lets Cloud Firestore auto-generate an ID.
Returns a JS/Promise which resolves to a generated [doc-ref](#doc-ref).
Example:

```clojure
(let [sender-id "ml8mnGIEINP1eol2PSWSfnlen0Q2"
      receiver-id "VFdERNhp94PIJijeuxjJYXTLEZm2"
      message "Hi Bob!"
      doc {:message message
           :sender-id sender-id
           :receiver-id receiver-id
           :created-at (.getTime (js/Date.))}]
  (-> (db/document-add {:path (string/join "/" ["messages" receiver-id sender-id])
                        :document doc})
      (.then #(db/document-add {:path (string/join "/" ["messages" sender-id receiver-id])
                                :document doc}))))
```


#### <a name="document->clj"></a>`document->clj`

#### <a name="snapshot->clj"></a>`snapshot->clj`

#### <a name="query"></a>`query`

#### <a name="get-collection"></a>`get-collection`

#### <a name="get-document"></a>`get-document`

#### <a name="get-document-field-value"></a>`get-document-field-value`

#### <a name="delete-document"></a>`delete-document`

Deletes document from a collection. Takes a `path` or collection and document id as arguments.

```
(delete-document "followers" "ml8mnGIEINP1eol2PSWSfnlen0Q2")
;; same as
(delete-document "followers/ml8mnGIEINP1eol2PSWSfnlen0Q2")
```
#### <a name="where"></a>`where`

#### <a name="order-by"></a>`order-by`

#### <a name="start-at"></a>`start-at`

#### <a name="start-after"></a>`start-after`

#### <a name="limit"></a>`limit`

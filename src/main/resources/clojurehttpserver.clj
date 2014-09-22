(ns com.m3958.vertx.backuper.clojurehttpserver
  (:require [vertx.core :as core]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.logging :as log]
            [vertx.eventbus :as eb]
            [vertx.http :as http]))

(-> (http/server)
    (http/on-request
      (fn [req]
        (-> req
            http/server-response
            (http/send-file
              (str "web/"
                (let [path (.path req)]
                  (cond
                    (= path "/") "index.html"
                    (not (re-find #"\.\." path)) path
                    :default "error.html")))))))
    (http/listen 8080))

(ns example.server
  (:require [vertx.core :as core]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.logging :as log]
            [vertx.eventbus :as eb]
            [vertx.repl :as repl]))

;when single-arity fn supplied, will got message body.
;core class type
(eb/on-message "test.address"
  (fn [message]
    (println "Got message body" (type message))))


(eb/on-message "test.address1"
  (core/as-handler
  (fn [message]
    (println "Got message body" (type message)))))

(core/deploy-verticle "com.m3958.vertx.backuper.BlockedVerticle" :instance 1 :handler
  (fn [exp id]
    (do (println exp id)
      (dotimes [i 10000]
        (eb/send "ping-verticle" "ping")))))


(repl/start 54470)

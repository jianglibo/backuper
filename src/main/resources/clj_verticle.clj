(ns example.server
  (:require [vertx.core :as core]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.logging :as log]
            [vertx.eventbus :as eb]
            [vertx.repl :as repl]))

;when single-arity fn supplied, will got message body.
(eb/on-message "test.address"
  (fn [message]
    (println "Got message body" (-> message (.getClass) (.getName)))))


(eb/on-message "test.address1"
  (core/as-handler
  (fn [message]
    (println "Got message body" (-> message (.getClass) (.getName))))))

(repl/start 54470)

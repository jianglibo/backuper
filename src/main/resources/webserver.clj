
(ns example.server
  (:require [vertx.core :as core]
            [vertx.net :as net]
            [vertx.stream :as stream]
            [vertx.logging :as log]
            [vertx.http :as http]
            [vertx.eventbus :as eb])
  (:import [org.vertx.mods.web WebServerBase]))


;  @Override
;  protected RouteMatcher routeMatcher() {
;    RouteMatcher matcher = new RouteMatcher();
;    matcher.noMatch(staticHandler());
;    return matcher;
;  }

;(proxy [WebServerBase] []
;  (routeMatcher []
;    (let [mt (matcher)]
;      (http.route/not-match mt (.staticHandler))
;      mt)))

package com.m3958.vertx.backuper;
/*
 * Copyright 2013 Red Hat, Inc.
 *
 * Red Hat licenses this file to you under the Apache License, version 2.0
 * (the "License"); you may not use this file except in compliance with the
 * License.  You may obtain a copy of the License at:
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/*
This is a simple Java verticle which receives `ping` messages on the event bus and sends back `pong` replies
 */
public class Starter extends Verticle {

  public void start() {

    container.deployVerticle("com.m3958.vertx.backuper.PingVerticle", 1);
    container.deployWorkerVerticle("com.m3958.vertx.backuper.DirectoryWatcherVerticle",new JsonObject(), 1, true, new Handler<AsyncResult<String>>() {

      @Override
      public void handle(AsyncResult<String> result) {
        container.logger().info(result.result());
        vertx.eventBus().send("dir-watcher", true);
      }} );
    
    long timerID = vertx.setPeriodic(1000, new Handler<Long>() {
      public void handle(Long timerID) {
        vertx.eventBus().send("ping-address", "Ping!");
      }
  });
  }
}

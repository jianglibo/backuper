package com.m3958.vertx.backuper;

/*
 * Copyright 2013 Red Hat, Inc.
 * 
 * Red Hat licenses this file to you under the Apache License, version 2.0 (the "License"); you may
 * not use this file except in compliance with the License. You may obtain a copy of the License at:
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 * 
 * @author <a href="http://tfox.org">Tim Fox</a>
 */

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.Handler;
import org.vertx.java.core.json.JsonObject;
import org.vertx.java.platform.Verticle;

/*
 * This is a simple Java verticle which receives `ping` messages on the event bus and sends back
 * `pong` replies
 */
public class Starter extends Verticle {

  public void start() {

    container.deployVerticle(BlockedVerticle.class.getName(), 1);
    
    container.deployWorkerVerticle(DirectoryWatcherVerticle.class.getName(), new JsonObject(), 1,
        true, new Handler<AsyncResult<String>>() {
          @Override
          public void handle(AsyncResult<String> result) {
            container.logger().info(result.result());
            vertx.eventBus().send(DirectoryWatcherVerticle.ADDRESS, true);
          }
        });
    
    container.deployWorkerVerticle(TimerVerticle.class.getName(), new JsonObject(), 1,
      true, new Handler<AsyncResult<String>>() {
        @Override
        public void handle(AsyncResult<String> result) {
          container.logger().info(result.result());
          vertx.eventBus().send(TimerVerticle.ADDRESS, true);
        }
      });

  }
}

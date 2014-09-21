package com.m3958.vertx.backuper.integration.java;
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

import static org.vertx.testtools.VertxAssert.assertEquals;
import static org.vertx.testtools.VertxAssert.assertNotNull;
import static org.vertx.testtools.VertxAssert.assertTrue;
import static org.vertx.testtools.VertxAssert.testComplete;

import java.nio.file.Paths;

import org.junit.Test;
import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.testtools.TestVerticle;

import com.m3958.vertx.backuper.PingVerticle;

/**
 * Example Java integration test that deploys the module that this project builds.
 *
 * Quite often in integration tests you want to deploy the same module for all tests and you don't want tests
 * to start before the module has been deployed.
 *
 * This test demonstrates how to do that.
 */
public class MissingMessageTest extends TestVerticle {
  
  private int numbers = 10;

  @Test
  public void testPing() {
    container.logger().info("in testPing()");
    
    for(int i=0; i < numbers; i++) {
      vertx.eventBus().send("ping-address", "ping!");
    }
    //只要等待的时间足够久，就可以完成。
    //但是发送太多的信息会引起系统资源的衰竭。
    try {
      Thread.sleep(10000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    vertx.eventBus().send("ping-address", PingVerticle.QUERY_MESSAGE_NUMBER, new Handler<Message<String>>() {

      @Override
      public void handle(Message<String> message) {
        assertEquals(String.valueOf(numbers), message.body());
        assertEquals(String.valueOf(numbers * PingVerticle.BUFF_TIMES), String.valueOf(PingVerticle.bufindex));
        container.logger().info(Paths.get(".").toAbsolutePath());
//        String[] fns = vertx.fileSystem().readDirSync(PingVerticle.FIXTURE_DIR);
//        for(String fn : fns) {
//          vertx.fileSystem().deleteSync(fn);
//        }
        testComplete();
      }});
    
  }

  @Test
  public void testSomethingElse() {
    // Whatever
    testComplete();
  }


  @Override
  public void start() {
    // Make sure we call initialize() - this sets up the assert stuff so assert functionality works correctly
    initialize();
    // Deploy the module - the System property `vertx.modulename` will contain the name of the module so you
    // don't have to hardecode it in your tests
    container.deployModule(System.getProperty("vertx.modulename"), new AsyncResultHandler<String>() {
      @Override
      public void handle(AsyncResult<String> asyncResult) {
      // Deployment is asynchronous and this this handler will be called when it's complete (or failed)
      assertTrue(asyncResult.succeeded());
      assertNotNull("deploymentID should not be null", asyncResult.result());
      // If deployed correctly then start the tests!
      startTests();
      }
    });
  }

}

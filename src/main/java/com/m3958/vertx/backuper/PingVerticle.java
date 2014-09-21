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
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.platform.Verticle;


public class PingVerticle extends Verticle {
  
  private int messageNumbers = 0;
  
  public static String QUERY_MESSAGE_NUMBER = "queryMessageNumber";
  
  public static String FIXTURE_DIR = "fixtures";
  
  public static int BUFF_TIMES = 50000;
  
  public static String sb = "";
  
  public static int bufindex = 0;
  
//  private static Buffer buff = new Buffer();
//  
//  static {
//    for(int i=0;i<100;i++) {
//      buff.appendString("aaaaaaaaaaaaaaaa\\n");
//    }
//  }

  public void start() {
    
    if (!vertx.fileSystem().existsSync(FIXTURE_DIR)) {
      vertx.fileSystem().mkdirSync(FIXTURE_DIR);
    }

    vertx.eventBus().registerHandler("ping-address", new Handler<Message<String>>() {
      @Override
      public void handle(Message<String> message) {
        String s = message.body();
        
        if (QUERY_MESSAGE_NUMBER.equals(s)) {
          container.logger().info(messageNumbers);
          message.reply(String.valueOf(messageNumbers));
        } else {
          messageNumbers++;
          container.logger().info("message received:" + messageNumbers);
          pumpToFile(FIXTURE_DIR + "/" + messageNumbers + ".txt");
        }
      }
    });
    container.logger().info("PingVerticle started");
  }
  
  private void pumpToFile(String fn) {
    vertx.fileSystem().open(fn, new AsyncResultHandler<AsyncFile>() {
      public void handle(AsyncResult<AsyncFile> ar) {
          if (ar.succeeded()) {
            AsyncFile asyncFile = ar.result();
            Buffer buff = new Buffer("fooooooooooooooooooooooooooooooooooooooooooooooooofooooooooooooooooooooooooooooooooooooooooooooooooo\\n");
            for (int i = 0; i < BUFF_TIMES; i++) {
                asyncFile.write(buff, buff.length() * i, new AsyncResultHandler<Void>() {
                    public void handle(AsyncResult ar) {
                        if (ar.succeeded()) {
                            bufindex++;
                        } else {
                        }
                    }
                });
            }
          } else {
            container.logger().error("Failed to open file", ar.cause());
          }
      }
  });
  }
}

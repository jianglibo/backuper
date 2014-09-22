package com.m3958.vertx.backuper;

import org.vertx.java.core.AsyncResult;
import org.vertx.java.core.AsyncResultHandler;
import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.core.file.AsyncFile;
import org.vertx.java.platform.Verticle;


public class BlockedVerticle extends Verticle {

  private int messageNumbers = 0;

  public static String QUERY_MESSAGE_NUMBER = "queryMessageNumber";

  public static String FIXTURE_DIR = "fixtures";

  public static int BUFF_TIMES = 50000;

  public static String sb = "";

  public static int bufindex = 0;

  public static String ADDRESS = "ping-verticle";

  public void start() {

    if (!vertx.fileSystem().existsSync(FIXTURE_DIR)) {
      vertx.fileSystem().mkdirSync(FIXTURE_DIR);
    }

    vertx.eventBus().registerHandler(ADDRESS, new Handler<Message<String>>() {
      @Override
      public void handle(Message<String> message) {
        String s = message.body();

        if (QUERY_MESSAGE_NUMBER.equals(s)) {
          container.logger().info("query message number received.");
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

  /**
   * 因为是异步执行，除非等到结束，否则bufindex可能不会全部执行完。
   * 
   * @param fn
   */
  private void pumpToFile(String fn) {
    vertx.fileSystem().open(fn, new AsyncResultHandler<AsyncFile>() {
      public void handle(AsyncResult<AsyncFile> ar) {
        if (ar.succeeded()) {
          AsyncFile asyncFile = ar.result();
          Buffer buff =
              new Buffer(
                  "fooooooooooooooooooooooooooooooooooooooooooooooooofooooooooooooooooooooooooooooooooooooooooooooooooo\\n");
          for (int i = 0; i < BUFF_TIMES; i++) {
            asyncFile.write(buff, buff.length() * i, new AsyncResultHandler<Void>() {
              public void handle(AsyncResult<Void> ar) {
                if (ar.succeeded()) {
                  bufindex++;
                } else {}
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

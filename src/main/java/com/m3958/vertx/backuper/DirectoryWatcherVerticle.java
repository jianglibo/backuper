package com.m3958.vertx.backuper;

import java.io.IOException;
import java.nio.file.Paths;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

public class DirectoryWatcherVerticle extends Verticle {

  @Override
  public void start() {
    vertx.eventBus().registerHandler("dir-watcher", new Handler<Message<Boolean>>() {
      @Override
      public void handle(Message<Boolean> message) {
        try {
          if (message.body()) {
            new WatchDir(Paths.get("C:\\asset"), true).processEvents();
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    });
  }
}

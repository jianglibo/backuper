package com.m3958.vertx.backuper;

import org.vertx.java.core.Handler;
import org.vertx.java.core.eventbus.Message;
import org.vertx.java.platform.Verticle;

public class TimerVerticle extends Verticle {

  public static String ADDRESS = "timer-verticle-address";

  private long timerID;

  @Override
  public void start() {

    vertx.eventBus().registerHandler(ADDRESS, new Handler<Message<Boolean>>() {
      @Override
      public void handle(Message<Boolean> message) {
        if (message.body()) {
//          timerID = vertx.setPeriodic(1000, new Handler<Long>() {
//            public void handle(Long timerID) {
//              vertx.eventBus().send("ping-address", "Ping!");
//            }
//          });
        } else {
          if (timerID != 0) {
            vertx.cancelTimer(timerID);
          }
        }
      }
    });

  }
}

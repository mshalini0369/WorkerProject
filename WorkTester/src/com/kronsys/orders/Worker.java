package com.kronsys.orders;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Worker implements Runnable {
  Logger logger = Logger.getLogger(Worker.class.getName());
  private BlockingQueue<Order> orderQueue;
  private OrderProcessor orderProcessor;

  public Worker(final BlockingQueue<Order> orderQueue, final OrderProcessor orderProcessor) {
    super();
    this.orderQueue = orderQueue;
    this.orderProcessor = orderProcessor;
  }

  public void run() {
    try {
      while (true) {
        Order order = orderQueue.take();
        order.setStatus(OrderStatus.FULFILLED);
        logger.info("FULFILLED: " + order);
        orderProcessor.incrementFinishedOrderCount();
      }
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }
}

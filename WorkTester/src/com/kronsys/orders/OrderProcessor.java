package com.kronsys.orders;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

public class OrderProcessor {
  Logger logger = Logger.getLogger(OrderProcessor.class.getName());
  private BlockingQueue<Order> orderQueue;

  // Queue size
  private final static int QUEUE_SIZE = 5;

  // 3 order fullfilment threads
  private int orderProcessThreadPoolSize = 3;

  private ExecutorService orderProcessThreadPool =
      Executors.newFixedThreadPool(orderProcessThreadPoolSize);

  private AtomicLong totalOrderCount = new AtomicLong(0);

  private AtomicLong finishedOrderCount = new AtomicLong(0);

  public OrderProcessor() {
    orderQueue = new ArrayBlockingQueue<Order>(QUEUE_SIZE);
    // Create workers waiting for orders
    for (int i = 0; i < orderProcessThreadPoolSize; i++) {
      orderProcessThreadPool.submit(new Worker(orderQueue, this));
    }
  }

  public void addOrder(Order order) {
    try {
      logger.info("NEW ORDER: " + order);
      // Put will wait till space become available
      orderQueue.put(order);
      totalOrderCount.incrementAndGet();
    } catch (InterruptedException e) {
      throw new IllegalStateException(e);
    }
  }

  public long getTotalOrderCount() {
    return totalOrderCount.get();
  }

  public long getFinishedOrderCount() {
    return finishedOrderCount.get();
  }

  public void incrementFinishedOrderCount() {
    finishedOrderCount.incrementAndGet();
  }

  public void stop() {
    orderProcessThreadPool.shutdown();
    // Used await termination instead of sleep.
    try {
    	orderProcessThreadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		orderProcessThreadPool.shutdownNow();
	}
  }
}

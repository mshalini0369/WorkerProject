package com.kronsys.orders;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class WorkTester {

  public static void main(String[] args) throws InterruptedException {
    final OrderProcessor processor = new OrderProcessor();
    
    // Submit orders in parallel from 4 threads
    final ExecutorService orderSubmissionThreadPool = Executors.newFixedThreadPool(4);
    
    //Create order tasks and submit to above thread pool
    final AtomicInteger orderNumber = new AtomicInteger(1);
    Collection<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
    int totalOrders = 10;
    for (int i = 0; i < totalOrders; i++) {
      tasks.add(new Callable<Integer>() {
        @Override
        public Integer call() throws Exception {
          final Integer orderNumberInt = orderNumber.getAndIncrement();
          processor.addOrder(new Order(orderNumberInt, OrderStatus.NEW));
          return orderNumberInt;
        }
      });
    }
    orderSubmissionThreadPool.invokeAll(tasks);

    //Used await termination instead of sleep.
    orderSubmissionThreadPool.shutdown();
    try {
    	orderSubmissionThreadPool.awaitTermination(100, TimeUnit.MILLISECONDS);
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		orderSubmissionThreadPool.shutdownNow();
	}
    
    processor.stop();
  
    System.out.println("Total submitted: " + processor.getTotalOrderCount());
    System.out.println("Total finished: " + processor.getFinishedOrderCount());

    // Make sure we finished all the orders
    assert(processor.getTotalOrderCount() == processor.getFinishedOrderCount());
  }

}

/*
 第十二周作业：

端午节粽叶飘香，在食堂售卖的粽子先由后厨制作，再放在销售区售卖，
学生从销售区购买粽子，销售区一次最多只能放200个粽子，
如果后厨想制作更多的粽子，销售区会让后厨停一停，
如果销售区有空位，再通知后厨，继续生产，
如果销售区没有了粽子，销售区会告诉学生等一下，
如果销售区有了粽子再通知学生取走产品。请编写程序描述上述过程。
 */
package Class5_20;

import java.util.LinkedList;
import java.util.Queue;

class SalesArea {
    private final int MAX_CAPACITY = 20; // 销售区的最大容量
    private Queue<String> zongziQueue = new LinkedList<>(); // 用于存放粽子的队列
    private int totalProduced = 0; // 统计总共生产的粽子数量
    private final int MAX_PRODUCTION = 50; // 设定最大生产数量50
    private boolean productionActive = true; // 标志生产是否活跃

    // 后厨制作粽子的方法
    public synchronized void produceZongzi() throws InterruptedException {
        while (zongziQueue.size() == MAX_CAPACITY) {
            System.out.println("销售区满了，后厨停一停");
            wait(); // 如果销售区满了，后厨等
        }
        if (totalProduced < MAX_PRODUCTION) {
            zongziQueue.add("Zongzi");
            totalProduced++;
            System.out.println("后厨制作了一个粽子，销售区现在有 " + zongziQueue.size() + " 个粽子");
            notifyAll(); // 通知消费者可以购买粽子
        } else {
            productionActive = false;
            System.out.println("达到最大生产量450，不再生产！");
            notifyAll(); // 通知所有等待的消费者线程
        }
    }

    // 学生购买粽子的方法
    public synchronized void consumeZongzi() throws InterruptedException {
        while (zongziQueue.isEmpty() && productionActive) {
            System.out.println("销售区没有粽子了，学生等一下");
            wait(); // 如果销售区没有粽子了，学生等待
        }
        if (!zongziQueue.isEmpty()) {
            zongziQueue.poll();
            System.out.println("Stu学生购买了一个粽子，销售区现在有 " + zongziQueue.size() + " 个粽子");
            notifyAll(); // 通知后厨可以制作粽子
        } else if (!productionActive) {
            System.out.println("粽子售罄！");
            return;
        }
    }

    // Getter方法
    public boolean isProductionActive() {
        return productionActive;
    }

    public boolean isZongziQueueEmpty() {
        return zongziQueue.isEmpty();
    }
}

class Producer implements Runnable {
    private SalesArea salesArea;

    public Producer(SalesArea salesArea) {
        this.salesArea = salesArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                salesArea.produceZongzi();
                Thread.sleep(50); // 模拟后厨制作粽子的时间
                if (!salesArea.isProductionActive()) {
                    break; // 如果生产停止，结束线程
                }
            }
        } catch (InterruptedException e) {
        	Thread.currentThread().interrupt();
//            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable {
    private SalesArea salesArea;

    public Consumer(SalesArea salesArea) {
        this.salesArea = salesArea;
    }

    @Override
    public void run() {
        try {
            while (true) {
                salesArea.consumeZongzi();
                Thread.sleep(1000); // 模拟学生购买粽子的时间
                if (!salesArea.isProductionActive() && salesArea.isZongziQueueEmpty()) {
                    break; // 如果生产停止且没有粽子了，结束线程
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            e.printStackTrace();
        }
    }
}

public class Zongzi_sale {
    public static void main(String[] args) {
        SalesArea salesArea = new SalesArea(); // 创建销售区对象
        Thread producerThread = new Thread(new Producer(salesArea)); // 创建后厨线程
        Thread consumerThread = new Thread(new Consumer(salesArea)); // 创建学生线程

        producerThread.start(); // 启动后厨线程
        consumerThread.start(); // 启动学生线程

        try {
            producerThread.join(); // 等待后厨线程结束
            consumerThread.join(); // 等待学生线程结束
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}


/*
代码说明：
SalesArea 类：代表销售区，包含一个固定容量的队列来存放粽子。

MAX_CAPACITY：销售区的最大容量，设置为200。
produceZongzi()：后厨制作粽子的方法。如果销售区已满，后厨线程会等待；否则，后厨制作一个粽子并通知消费者线程。
consumeZongzi()：学生购买粽子的方法。如果销售区没有粽子，学生线程会等待；否则，学生购买一个粽子并通知后厨线程。
Producer 类：表示后厨，是一个生产者线程。

run()：不断调用produceZongzi()方法来制作粽子，并模拟制作时间的延迟。
Consumer 类：表示学生，是一个消费者线程。

run()：不断调用consumeZongzi()方法来购买粽子，并模拟购买时间的延迟。
Zongzi_sale 类：主程序入口。

创建一个SalesArea对象。
创建并启动后厨线程和学生线程。
使用join()方法让主线程等待后厨和学生线程的完成。
这个程序模拟了后厨制作粽子、销售区存放粽子、学生购买粽子的整个过程，并处理了同步和线程间的通信，确保销售区不会超过最大容量，并且生产者和消费者可以协调工作。


改进：
totalProduced：添加了一个变量 totalProduced 来统计总共生产的粽子数量。
MAX_PRODUCTION：设定了最大生产数量（这里设置为500），一旦达到该数量，后厨停止生产。
produceZongzi 方法改进：
在条件判断中加入了 totalProduced >= MAX_PRODUCTION。
如果达到最大生产数量，通知所有等待线程，并返回结束生产。
consumeZongzi 方法改进：
在等待时检查 totalProduced < MAX_PRODUCTION，防止消费者无限等待。
增加了判断条件以处理粽子售罄的情况，并输出相应信息。
主线程：主线程等待生产者和消费者线程的结束。
通过这些改进，程序将能够在生产达到最大数量后正确停止，避免无限循环。
*/

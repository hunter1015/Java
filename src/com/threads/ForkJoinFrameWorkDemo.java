package com.threads;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;


/*利用这些等待未完成线程的线程“帮助”哪些还未完成的线程的任务工作，那么就有效的利用了系统资源。
Fork/Join框架主要由两个核心类构成
1. ForkJoinTask
使用Fork/Join框架，必须首先创建一个Fork/Join任务；而这个类就是一个将在ForkJoinPool中执行的任务的基类。
ForkJoinTask类用于实现Fork/Join框架的任务
Fork/Join框架提供了在一个任务里执行fork和join操作的机制和控制任务状态的方法。通常，为了实现Fork/Join任务，需要实现一个以下两个类之一的子类（继承于ForkJoinTask）。
RecursiveAction（递归操作）：用于任务没有返回结果的场景。
RecursiveTask（递归作业）：用于任务有返回结果的场景。
ForkJoinTask是实现Future的另一种有返回结果的实现方法，比Future多了两个重要的方法：
fork(): 异步执行任务。该方法在发送任务到线程池时，如果线程池中有空闲的工作者线程或创建一个新的线程，开始执行这个任务，并立即返回。
join(): 同步调用；该方法负责在任务完成后返回结果，等待发送到线程池中的所有子任务都执行完成。
invokeAll(ForkJoinTask<?>...tasks):  同步执行指定的ForkJoinTask，并等待task完成。
get(long,TimeUnit): （由于继承了Futrue接口）如果任务的结果未准备好，将等待指定的时间；如果等待的时间超出，而结果仍未准备好，那方法就返回null。

2. ForkJoinPool
这个类实现了ExecutorService接口和工作窃取算法。它管理工作者线程，并提供任务的状态信息，以及任务的执行信息。
该类提供了三个用于调度子任务的方法：
execute(): 在线程池里异步执行指定任务。（在execute方法中，如果传入的是runnable对象，ForkJoinPool类就不采用工作窃取算法，ForkJoinPool类仅在使用ForkJoinTask类（callable对象）时才使用工作窃取算法。）
submit(): 在线程池里异步执行指定的任务，并立即返回一个Futrue对象。
invoke(ForkJoinTask<T> task)和invokeAll(ForkJoinTask<?>...tasks): 在线程池里同步执行指定的任务，等待任务完成并返回结果。 


ForkJoinTask需要通过ForkJoinPool来执行。任务分割出的子任务会添加到当前工作线程所维护的双端队列中，进入队列的头部。当一个工作线程的队列中暂时没有任务时，它会随机从其他工作线程的队列的尾部获取一个任务。
*/

public class ForkJoinFrameWorkDemo {
	
    //构建一个求和的Fork/Join任务。注意：这里说明，实际业务处理任务要继承自ForkJoinTask的两个实现类之一
	//
    //由于是求和任务，因此该任务继承具有返回值的RecursiveTask
	//
    static class SumTask extends RecursiveTask<Integer> {

	//任务切割的参考大小（任务的最小规模）
	private int taskDefaultSize = 10;
	private int mStart, mEnd;

	public SumTask(int start, int end) {
	    mStart = start;
	    mEnd = end;
	}

	@Override
	protected Integer compute() {
	    int sum = 0;
			
	    //判断任务是否已经切割到最小规模，如果到达则可执行计算
	    boolean canCompute = (mEnd - mStart) <= taskDefaultSize;

	    if (canCompute) {
		//最小的可执行任务
		for (int i = mStart; i <= mEnd; i++) {
		    sum = sum + i;
		}
	    } else {
		// 拆分两个子任务。
		int middle = (mStart + mEnd) / 2;
		//拆分后，再新建新的对象（构造函数中明确了任务分解的范围）
		SumTask firstTask = new SumTask(mStart, middle);
		SumTask secondTask = new SumTask(middle + 1, mEnd);
				
		//注意！！！将拆分的两个任务丢入线程池，异步执行  分解后丢给线程池
		firstTask.fork(); 
		secondTask.fork();

		//阻塞式的获得第一个子任务结果,得到结果后才继续执行
		//获取结果！！阻塞式
		int firstResult = firstTask.join(); 
		int secondResult = secondTask.join();

		// 合并两个子任务的结果。
		sum = firstResult + secondResult;
	    }
	    //返回最终的计算结果
	    return sum;
	}
    }
	
    //测试客户端
    public static void main(String[] args) throws InterruptedException,
        ExecutionException {

	//采用默认的构造函数构建用于执行任务的ForkJoinPool对象
	ForkJoinPool forkJoinPool = new ForkJoinPool();

	SumTask task = new SumTask(1, 20);
	
	//注意！！forkJoinPool线程返回的结果，如果是ForkJoinTask的返回实现，那就是Future类型（异步获取结果）
	Future<Integer> result = forkJoinPool.submit(task);
	
	//利用get阻塞式获取计算结果
	System.out.println("sum(1,20) = " + result.get());

	SumTask task2 = new SumTask(1, 100);
	Future<Integer> result2 = forkJoinPool.submit(task2);
	//利用get阻塞式获取计算结果
	System.out.println("sum(1,100) = " + result2.get());
		
	forkJoinPool.shutdown();
    }
}
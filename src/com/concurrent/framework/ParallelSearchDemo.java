package com.concurrent.framework;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 实现对一个无序数据集的并发查找，提升查找性能。
 * 适用场景:在大数据容量中查找相应的目标值数据量大得list
 */
public class ParallelSearchDemo {
    // 定义需要查询的无序数组
    static int[] arrayData = 
	{ 8, 4, 7, 2, 90, 12, 18, 23, 45, 69, 17, 19 };
	
    // 定义查找的目标值
    static int target = 7;
	
    // 定义线程池
    static ExecutorService threadPool = 
	Executors.newCachedThreadPool();
	
    // 定义同时运行的数据查找线程的数目
    static final int thread_num = 3;
	
    //result变量用于储存查找到的变量在目标数组中的索引
    //如果没有查找到目标变量则值为-1
    static AtomicInteger result = new AtomicInteger(-1);// 初始值定位-1

    /**
      * 在特定数据范围内，查找特定目标值的工具函数
      * @param searchValue 查找的目标值
      * @param beginPos	  查找范围的开始位置
      * @param endPos	  查找范围的结束位置
      * @return	  目标值在查找范围的位置
      */
    public static int search(int searchValue, int beginPos, int endPos) {
        int j = 0;
	for (j = beginPos; j < endPos; j++) {
	    //如果其他线程已经找到目标值，则直接返回
	    //结束该线程，避免无效查找
	    if (result.get() >= 0) {
		return result.get();
	    }

	    if (arrayData[j] == searchValue) {
		//与-1进行比较，如果不等于-1，则表示其他线程已经“捷足先登”找到了目标值
		//原子更新失败；并执行if块内的语句，返回result内的值，结束该线程
		if (!result.compareAndSet(-1, j)) {
		    return result.get();
		}
		//如果等于-1，则表示其他线程还没有找到目标值，
                //以原子方式将该值设置为给定的更新值
		//并且直接返回，结束该线程
		return j;
	    }
	}
	//该范围没有找到目标值，结束该线程
	return -1;
    }

    //数据查找线程
    public static class SearchTask implements Callable<Integer> {
	int begin, end, searchValue;

	public SearchTask(int searchValue, int begin, int end) {
	    this.begin = begin;
	    this.end = end;
	    this.searchValue = searchValue;
	}
		
	//Callable接口需要实现的call函数
        public Integer call() throws Exception {
	    int re = search(searchValue, begin, end);
	    return re;
	}
    }

    //将整个查找范围切割为N个查找域
    //一个线程负责一个查找域；开启N个线程同时对这N个查找域进行查找
    public static int searchWorker(int searchValue) throws InterruptedException, ExecutionException {
        int subArrSize = arrayData.length / thread_num + 1;
	List<Future<Integer>> re = new ArrayList<Future<Integer>>();
	
	//对查找范围进行切割，并开启一个线程对切割的查找域进行查找
	for (int i = 0; i < arrayData.length; i += subArrSize) {
	    int end = i + subArrSize;
	    if (end >= arrayData.length) end = arrayData.length;
		//开启线程
		re.add(threadPool.submit(new SearchTask(searchValue, i, end)));
        }
		
        //阻塞式的打捞计算结果
        for (Future<Integer> fu : re) {
            if (fu.get() >= 0) return (fu.get());
        }
	return -1;
    }

    //测试客户端
    public static void main(String[] args) {
        try {
	    int index = searchWorker(target);
	    System.out.println("欲查找的数据为:"+target+";在目标数组中的索引为:"+index);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	} catch (ExecutionException e) {
	    e.printStackTrace();
	}
    }
}
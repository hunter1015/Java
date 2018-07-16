package com.threads;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;


public class ForkJoinDemoFileCrawler {
	static class FileCrawlerTask extends RecursiveTask<List<String>>{
		private Path path;
		public FileCrawlerTask(Path _pPath) {
			// TODO 自动生成的构造函数存根
			path=_pPath;
		}
		@Override
		protected List<String> compute() {
			// TODO 自动生成的方法存根
			List<String> fileList=new ArrayList<>();
			List<FileCrawlerTask> subTasks=new ArrayList<FileCrawlerTask>();
			
			try {
				DirectoryStream<Path> dirs=Files.newDirectoryStream(path);
				for (Path subDir:dirs) {
					if (Files.isDirectory(subDir,LinkOption.NOFOLLOW_LINKS)) {
						FileCrawlerTask  task=new FileCrawlerTask(subDir);
						task.fork();
						subTasks.add(task);
						
					}else {
						//如果是文件，则就可以处理
                        String strPath = subDir.toAbsolutePath().toString();
                        //发现文件是以.log结尾，则添加路径
                        if(strPath.endsWith(".log")){
                            fileList.add(strPath);
                        }
					}
					
				}
				 //合并计算结果
                if(!subTasks.isEmpty()){
                    for(FileCrawlerTask task : subTasks){
                        //获取每个子任务的处理结果
                        List<String> files = task.join();
                        if(files != null && files.size() != 0){
                            fileList.addAll(files);
                        }
                    }
                }
			} catch (IOException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
				return null;
			}
			
			return fileList;
		}
		
	}
	public static void main(String[] args) {
		ForkJoinPool pool=new ForkJoinPool();
        //启动任务，并且记录任务的执行耗时
		
        long a=System.currentTimeMillis();
        List<String> fileList = pool.invoke(new FileCrawlerTask(Paths.get("..//")));
		long b=System.currentTimeMillis();
        System.out.println("Cost Time : "+ (b-a) + " ms");
        for(String fileName : fileList){
            System.out.println(fileName);
        }
	}
}

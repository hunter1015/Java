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
			// TODO �Զ����ɵĹ��캯�����
			path=_pPath;
		}
		@Override
		protected List<String> compute() {
			// TODO �Զ����ɵķ������
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
						//������ļ�����Ϳ��Դ���
                        String strPath = subDir.toAbsolutePath().toString();
                        //�����ļ�����.log��β�������·��
                        if(strPath.endsWith(".log")){
                            fileList.add(strPath);
                        }
					}
					
				}
				 //�ϲ�������
                if(!subTasks.isEmpty()){
                    for(FileCrawlerTask task : subTasks){
                        //��ȡÿ��������Ĵ�����
                        List<String> files = task.join();
                        if(files != null && files.size() != 0){
                            fileList.addAll(files);
                        }
                    }
                }
			} catch (IOException e) {
				// TODO �Զ����ɵ� catch ��
				e.printStackTrace();
				return null;
			}
			
			return fileList;
		}
		
	}
	public static void main(String[] args) {
		ForkJoinPool pool=new ForkJoinPool();
        //�������񣬲��Ҽ�¼�����ִ�к�ʱ
		
        long a=System.currentTimeMillis();
        List<String> fileList = pool.invoke(new FileCrawlerTask(Paths.get("..//")));
		long b=System.currentTimeMillis();
        System.out.println("Cost Time : "+ (b-a) + " ms");
        for(String fileName : fileList){
            System.out.println(fileName);
        }
	}
}

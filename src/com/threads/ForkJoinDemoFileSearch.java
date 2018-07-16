package com.threads;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/*高级全面fork/join应用
模拟文件系统关键字检索*/

//文档类
class Document {
    // 一个文档由若干内容行构成
    private final List<String> lines;
    
    // 构造函数
    Document(List<String> lines) {
        this.lines = lines;
    }
    
    // 获取文档的所有内容（以行为单位）
    List<String> getLines() {
        return this.lines;
    }
    
    // 从一个文件中读取全部内容
    static Document fromFile(File file) throws IOException {
        List<String> lines = new LinkedList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
        }
        return new Document(lines);
    }
}

// 文件夹类
class Folder {
    // 一个文件夹由若干子文件夹和若干文件构成
    private final List<Folder> subFolders;
    private final List<Document> documents;
   
    // 构造函数
    Folder(List<Folder> subFolders, List<Document> documents) {
        this.subFolders = subFolders;
        this.documents = documents;
    }
    
    // 获取子文件夹列表
    List<Folder> getSubFolders() {
        return this.subFolders;
    }
    
    // 获取文件列表
    List<Document> getDocuments() {
        return this.documents;
    }
    
    // 从一个目录中构建若干子文件夹和若干文件
    static Folder fromDirectory(File dir) throws IOException {
        List<Document> documents = new LinkedList<>();
        List<Folder> subFolders = new LinkedList<>();
        for (File entry : dir.listFiles()) {
            if (entry.isDirectory()) {
                subFolders.add(Folder.fromDirectory(entry));
            } else {
                documents.add(Document.fromFile(entry));
            }
        }
        return new Folder(subFolders, documents);
    }
}

// 查找关键字类
public class ForkJoinDemoFileSearch {
    // 根据正则表达式，把一行内容切割成单词组
    // 切割原则：单词之间是以空格或标点符号为分割的
    String[] wordsIn(String line) {
        // \\p{Punct} 标点符号：!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
        // \\s空格
        // 代表1个或多个上述字符
        return line.trim().split("(\\s|\\p{Punct})+");
    }
    
    // 统计单词searchedWord在文档document中的出现次数
    Long occurrencesCount(Document document, String searchedWord) {
        long count = 0;
        for (String line : document.getLines()) {
            for (String word : wordsIn(line)) {
                if (searchedWord.equals(word)) {
                    count = count + 1;
                }
            }
        }
        return count;
    }
    
    // 以单线程的方式进行工作
    Long countOccurrencesOnSingleThread(Folder folder, String searchedWord) {
        long count = 0;
        for (Folder subFolder : folder.getSubFolders()) {
            count = count
                    + countOccurrencesOnSingleThread(subFolder, searchedWord);
        }
        for (Document document : folder.getDocuments()) {
            count = count + occurrencesCount(document, searchedWord);
        }
        return count;
    }
    
    // 定义有返回值的ForkJoinTask
    // 以文件为粒度统计关键词在文件中出现的次数
    class DocumentSearchTask extends RecursiveTask<Long> {
        private final Document document;
        private final String searchedWord;
        DocumentSearchTask(Document document, String searchedWord) {
            super();
            this.document = document;
            this.searchedWord = searchedWord;
        }
        @Override
        protected Long compute() {
            return occurrencesCount(document, searchedWord);
        }
    }
    
    // 定义有返回值的ForkJoinTask
    // 以文件夹为粒度统计关键词在文件中出现的次数
    class FolderSearchTask extends RecursiveTask<Long> {
        private final Folder folder;
        private final String searchedWord;
        FolderSearchTask(Folder folder, String searchedWord) {
            super();
            this.folder = folder;
            this.searchedWord = searchedWord;
        }
        @Override
        protected Long compute() {
            long count = 0L;
            List<RecursiveTask<Long>> forks = new LinkedList<>();
            for (Folder subFolder : folder.getSubFolders()) {
                FolderSearchTask task = new FolderSearchTask(subFolder,
                        searchedWord);
                forks.add(task);
                task.fork();
            }
            for (Document document : folder.getDocuments()) {
                DocumentSearchTask task = new DocumentSearchTask(document,
                        searchedWord);
                forks.add(task);
                task.fork();
            }
            for (RecursiveTask<Long> task : forks) {
                count = count + task.join();
            }
            return count;
        }
    }
   
    private final ForkJoinPool forkJoinPool = new ForkJoinPool();
    
    // 以Fork/Join框架查找在特定目录下特定单词出现的次数
    Long countOccurrencesInParallel(Folder folder, String searchedWord) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWord));
    }
   
    // 测试客户端
    public static void main(String[] args) throws IOException {
        String dir = ".\\";
        //统计一下有多少个main函数
        String keyword = "main";
        ForkJoinDemoFileSearch wordCounter = new ForkJoinDemoFileSearch();
        Folder folder = Folder.fromDirectory(new File(dir));
        long counts;
        long startTime;
        long stopTime;
        long singleThreadTimes;
        long forkedThreadTimes;
        //执行单线程，并统计耗时
        startTime = System.currentTimeMillis();
        counts = wordCounter.countOccurrencesOnSingleThread(folder, keyword);
        stopTime = System.currentTimeMillis();
        singleThreadTimes = (stopTime - startTime);
        System.out.println("出现"+counts + "次, 单线程耗时: "
                + singleThreadTimes + " ms");
        //Fork/Join执行，并统计耗时
        startTime = System.currentTimeMillis();
        counts = wordCounter.countOccurrencesInParallel(folder, keyword);
        stopTime = System.currentTimeMillis();
        forkedThreadTimes = (stopTime - startTime);
        System.out.println("出现"+counts + "次, Fork/Join耗时: "
                + forkedThreadTimes + " ms");
    }
}

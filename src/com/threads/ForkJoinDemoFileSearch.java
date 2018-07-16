package com.threads;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

/*�߼�ȫ��fork/joinӦ��
ģ���ļ�ϵͳ�ؼ��ּ���*/

//�ĵ���
class Document {
    // һ���ĵ������������й���
    private final List<String> lines;
    
    // ���캯��
    Document(List<String> lines) {
        this.lines = lines;
    }
    
    // ��ȡ�ĵ����������ݣ�����Ϊ��λ��
    List<String> getLines() {
        return this.lines;
    }
    
    // ��һ���ļ��ж�ȡȫ������
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

// �ļ�����
class Folder {
    // һ���ļ������������ļ��к������ļ�����
    private final List<Folder> subFolders;
    private final List<Document> documents;
   
    // ���캯��
    Folder(List<Folder> subFolders, List<Document> documents) {
        this.subFolders = subFolders;
        this.documents = documents;
    }
    
    // ��ȡ���ļ����б�
    List<Folder> getSubFolders() {
        return this.subFolders;
    }
    
    // ��ȡ�ļ��б�
    List<Document> getDocuments() {
        return this.documents;
    }
    
    // ��һ��Ŀ¼�й����������ļ��к������ļ�
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

// ���ҹؼ�����
public class ForkJoinDemoFileSearch {
    // ����������ʽ����һ�������и�ɵ�����
    // �и�ԭ�򣺵���֮�����Կո�������Ϊ�ָ��
    String[] wordsIn(String line) {
        // \\p{Punct} �����ţ�!"#$%&'()*+,-./:;<=>?@[\]^_`{|}~
        // \\s�ո�
        // ����1�����������ַ�
        return line.trim().split("(\\s|\\p{Punct})+");
    }
    
    // ͳ�Ƶ���searchedWord���ĵ�document�еĳ��ִ���
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
    
    // �Ե��̵߳ķ�ʽ���й���
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
    
    // �����з���ֵ��ForkJoinTask
    // ���ļ�Ϊ����ͳ�ƹؼ������ļ��г��ֵĴ���
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
    
    // �����з���ֵ��ForkJoinTask
    // ���ļ���Ϊ����ͳ�ƹؼ������ļ��г��ֵĴ���
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
    
    // ��Fork/Join��ܲ������ض�Ŀ¼���ض����ʳ��ֵĴ���
    Long countOccurrencesInParallel(Folder folder, String searchedWord) {
        return forkJoinPool.invoke(new FolderSearchTask(folder, searchedWord));
    }
   
    // ���Կͻ���
    public static void main(String[] args) throws IOException {
        String dir = ".\\";
        //ͳ��һ���ж��ٸ�main����
        String keyword = "main";
        ForkJoinDemoFileSearch wordCounter = new ForkJoinDemoFileSearch();
        Folder folder = Folder.fromDirectory(new File(dir));
        long counts;
        long startTime;
        long stopTime;
        long singleThreadTimes;
        long forkedThreadTimes;
        //ִ�е��̣߳���ͳ�ƺ�ʱ
        startTime = System.currentTimeMillis();
        counts = wordCounter.countOccurrencesOnSingleThread(folder, keyword);
        stopTime = System.currentTimeMillis();
        singleThreadTimes = (stopTime - startTime);
        System.out.println("����"+counts + "��, ���̺߳�ʱ: "
                + singleThreadTimes + " ms");
        //Fork/Joinִ�У���ͳ�ƺ�ʱ
        startTime = System.currentTimeMillis();
        counts = wordCounter.countOccurrencesInParallel(folder, keyword);
        stopTime = System.currentTimeMillis();
        forkedThreadTimes = (stopTime - startTime);
        System.out.println("����"+counts + "��, Fork/Join��ʱ: "
                + forkedThreadTimes + " ms");
    }
}

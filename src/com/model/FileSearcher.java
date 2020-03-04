package com.model;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileSearcher {
    String path;
    String regex;
    String zipFileName;
    Pattern pattern;
    List<File> zipFiles;
//  OR List<File> zipfiles = new ArrayList<File>();

    {
        zipFiles = new ArrayList<File>();
    }



/*


    //  Using the classic File class Java 6
        public void walkDirectoryJava6(String path) throws IOException {
            File dir = new File(path);
            File[] files = dir.listFiles();

            for (File file : files) {
                if (file.isDirectory()){
                    //Calling walkDirectoryJava6 recursively
                    walkDirectoryJava6(file.getAbsolutePath());
                }else {
                    processFile(file);
                }

            }

        }

     USING JAVA 7
        public void walkDirectoryJava7(String path) throws IOException{
            Files.walkFileTree(Paths.get(path),
                    new SimpleFileVisitor<Path>() {
                        @Override
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            processFile(file.toFile());
                            return FileVisitResult.CONTINUE;
                        }
                    });

        }
    */

    public void walkDirectoryJava8(String path)throws IOException{
        Files.walk(Paths.get(path))
                .forEach(f -> processFile(f.toFile()));
        zipFilesJava7();
    }

    private void processFile(File file) {
        try {
            if (searchFile(file)){
                addFileToZip(file);
            }
        } catch (IOException|UncheckedIOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error processing file: " + file);
        }
    }


    private boolean searchFile(File file) throws IOException {
        return Files.lines(file.toPath()).anyMatch(this::searchText);
    }

    private boolean searchText(String text) {
        return (this.getRegex() == null) || this.pattern.matcher(text).matches();

    }
    public void addFileToZip(File file) {
        System.out.println("addFileToZip: " + file);
    }

    public void zipFilesJava7() throws IOException {
        try (ZipOutputStream out =
                     new ZipOutputStream(new FileOutputStream(getZipFileName())) ) {
            File baseDir = new File(getPath());

            for (File file : zipFiles) {
                // fileName must be a relative path, not an absolute one.
                String fileName = getRelativeFilename(file, baseDir);

                ZipEntry zipEntry = new ZipEntry(fileName);
                zipEntry.setTime(file.lastModified());
                out.putNextEntry(zipEntry);

                Files.copy(file.toPath(), out);

                out.closeEntry();
            }
        }
    }


    public String getRelativeFilename(File file, File baseDir) {
        String fileName = file.getAbsolutePath().substring(
                baseDir.getAbsolutePath().length());

        // IMPORTANT: the ZipEntry file name must use "/", not "\".
        fileName = fileName.replace('\\', '/');

        while (fileName.startsWith("/")) {
            fileName = fileName.substring(1);
        }

        return fileName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public void setZipFileName(String zipFileName) {
        this.zipFileName = zipFileName;
    }


}

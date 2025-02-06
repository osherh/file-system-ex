package com.ex.file_system.bussiness_logic;

public interface IFileSystemManager
{
    void addFile(String parentDirName, String fileName, long fileSize);
    void addDir(String parentDirName, String dirName);
    long getFileSize(String fileName);
    String getBiggestFile();
    void showFileSystem();
    void delete(String name);
}
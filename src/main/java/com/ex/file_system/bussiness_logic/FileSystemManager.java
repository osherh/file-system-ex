package com.ex.file_system.bussiness_logic;

import com.ex.file_system.entities.DirectoryEntity;
import com.ex.file_system.entities.FileEntity;
import com.ex.file_system.entities.FileSystemEntity;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.PriorityBlockingQueue;

public class FileSystemManager implements IFileSystemManager
{
    private static volatile FileSystemManager instance;
    private DirectoryEntity root;
    private ConcurrentMap<String, FileEntity> fileMap;
    private ConcurrentMap<String, DirectoryEntity> dirMap;
    private PriorityBlockingQueue<FileEntity> fileMaxHeap;

    private FileSystemManager()
    {
        root = new DirectoryEntity("root");
        fileMap = new ConcurrentHashMap<>();
        dirMap = new ConcurrentHashMap<>();
        dirMap.put("root", root);
        fileMaxHeap = new PriorityBlockingQueue<FileEntity>(11, Comparator.comparingLong(FileEntity::getSize).reversed());
    }

    public static FileSystemManager getInstance()
    {
        if (instance == null)
		{
            synchronized (FileSystemManager.class)
			{
                if (instance == null) {
                    instance = new FileSystemManager();
                }
            }
        }
        return instance;
    }

    @Override
    public void addFile(String parentDirName, String fileName, long fileSize)
    {
        DirectoryEntity parentDir = dirMap.get(parentDirName);
        if (parentDir == null)
        {
            System.err.println("Error: Parent directory '" + parentDirName + "' not found");
            return;
        }

        FileEntity newFile = new FileEntity(fileName, fileSize);
        parentDir.addFile(newFile);
        fileMap.put(fileName, newFile);
        fileMaxHeap.add(newFile);
    }

    @Override
    public void addDir(String parentDirName, String dirName)
    {
        DirectoryEntity parentDir = dirMap.get(parentDirName);
        if (parentDir == null)
        {
            System.err.println("Error: Parent directory '" + parentDirName + "' not found");
            return;
        }

        DirectoryEntity newDir = new DirectoryEntity(dirName);
        parentDir.addDirectory(newDir);
        dirMap.put(dirName, newDir);
    }

    @Override
    public long getFileSize(String fileName)
    {
        FileEntity file = fileMap.get(fileName);
        if (file == null)
        {
            System.err.println("Error: File '" + fileName + "' not found");
            return -1;
        }
        return file.getSize();
    }

    @Override
    public String getBiggestFile()
    {
        if (fileMaxHeap.isEmpty())
        {
            return "No files available.";
        }
        return fileMaxHeap.peek().getName();
    }

    @Override
    public void showFileSystem()
    {
        root.showHierarchy(0);
    }

    private void deleteFile(String name)
    {
        FileEntity file = fileMap.remove(name);
        fileMaxHeap.remove(file);
        removeFileOrDirectoryFromItsParentChildren(name);
    }
    
    private void deleteDirectoryRecursively(DirectoryEntity dir)
    {
        List<FileSystemEntity> toRemove = new ArrayList<>();
        Iterator<FileSystemEntity> it = dir.getChildren().iterator();
        while (it.hasNext())
        {
            FileSystemEntity entity = it.next();
            if (entity instanceof DirectoryEntity)
            {
                deleteDirectoryRecursively((DirectoryEntity) entity);
                dirMap.remove(entity.getName());
            }
            else
            {
                fileMap.remove(entity.getName());
                fileMaxHeap.remove(entity);                
            }
            toRemove.add(entity);
        }
        dir.getChildren().removeAll(toRemove);
    }

    private void removeFileOrDirectoryFromItsParentChildren(String name)
    {
        FileSystemEntity entityToRemove = null;
        DirectoryEntity parentToRemoveFrom = null;
        boolean entityFound = false;
        for (DirectoryEntity parent : dirMap.values())
        {
            if(entityFound) break;
            for (FileSystemEntity entity : parent.getChildren())
            {
                if (entity.getName().equals(name))
                {
                    entityToRemove = entity;
                    parentToRemoveFrom = parent;
                    entityFound = true;
                    break;
                }
            }
        }
        parentToRemoveFrom.removeEntity(entityToRemove.getName());
    }

    private void deleteDirectory(String name)
    {
        DirectoryEntity dir = dirMap.get(name);
        if (dir == null) return;
        deleteDirectoryRecursively(dir);
        removeFileOrDirectoryFromItsParentChildren(name);
        dirMap.remove(name);
    }
    
    @Override
    public void delete(String name)
    {
        if (fileMap.containsKey(name))
        {
            deleteFile(name);
        }
        else if (dirMap.containsKey(name))
        {
            deleteDirectory(name);
        }
        else
        {
            System.err.println("Error: No file or directory named " + name + " found");
        }
    }
}

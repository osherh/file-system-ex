package com.ex.file_system.entities;

import java.util.concurrent.CopyOnWriteArraySet;
import lombok.Getter;

@Getter
public class DirectoryEntity extends FileSystemEntity
{
    private CopyOnWriteArraySet<FileSystemEntity> children;

    public DirectoryEntity(String name)
    {
        super(name);
        this.children = new CopyOnWriteArraySet<FileSystemEntity>();
    }

    public void addFile(FileEntity file)
    {
        children.add(file);
    }

    public void addDirectory(DirectoryEntity dir)
    {
        children.add(dir);
    }

    public void removeEntity(String name)
    {
        children.removeIf(entity -> entity.getName().equals(name));
    }
    
    @Override
    public void showHierarchy(int level)
    {
        System.out.println("  ".repeat(level) + "|-- Dir: " + name + " (Created: " + creationDate + ")");
        for (FileSystemEntity entity : children)
        {
            entity.showHierarchy(level + 1);
        }
    }
}
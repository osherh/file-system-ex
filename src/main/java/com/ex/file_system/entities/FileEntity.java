package com.ex.file_system.entities;

import lombok.Getter;

@Getter
public class FileEntity extends FileSystemEntity
{
    private long size;

    public FileEntity(String name, long size)
    {
        super(name);
        this.size = size;
    }
    
    @Override
    public void showHierarchy(int level)
    {
        System.out.println("  ".repeat(level) + "|-- File: " + name + " (Size: " + size + " bytes, Created: " + creationDate + ")");
    }
}
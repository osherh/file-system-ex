package com.ex.file_system.entities;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public abstract class FileSystemEntity
{
    protected String name;
    protected LocalDate creationDate;

    public FileSystemEntity(String name)
    {
        this.name = name;
        this.creationDate = LocalDate.now();
    }
    
    public abstract void showHierarchy(int level);
}
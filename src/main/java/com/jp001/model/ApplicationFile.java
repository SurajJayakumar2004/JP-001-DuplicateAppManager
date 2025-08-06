package com.jp001.model;

public class ApplicationFile {
    private String name;
    private String path;
    private String hash;
    private long size;
    private String category;

    public ApplicationFile(String name, String path, String hash, long size) {
        this.name = name;
        this.path = path;
        this.hash = hash;
        this.size = size;
        this.category = "Uncategorized";
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getHash() {
        return hash;
    }

    public long getSize() {
        return size;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Optional: toString() for printing
    @Override
    public String toString() {
        return "ApplicationFile{" +
                "name='" + name + '\'' +
                ", path='" + path + '\'' +
                ", hash='" + hash + '\'' +
                ", size=" + size +
                ", category='" + category + '\'' +
                '}';
    }
}
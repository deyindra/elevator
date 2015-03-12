package com.intuit.elevator.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Concurrent
 * @param <T>
 */
public class ConcurrentList<T> {

    /**
     * use this to lock for write operations like add/remove
     */
    private final Lock readLock;
    /**
     * use this to lock for read operations like get/iterator/contains..
     */
    private final Lock writeLock;
    /**
     * the underlying list
     */
    private final List<T> list = new ArrayList<>();

    public ConcurrentList(){
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    public boolean add(T e) {
        writeLock.lock();
        try {
            return list.add(e);
        } finally {
            writeLock.unlock();
        }
    }

    public boolean isEmpty(){
        return size()==0;
    }

    public boolean remove(T e){
        writeLock.lock();
        try {
            return list.remove(e);
        } finally {
            writeLock.unlock();
        }
    }

    public T get(int index) {
        readLock.lock();
        try {
            return list.get(index);
        } finally {
            readLock.unlock();
        }
    }

    public int size(){
        readLock.lock();
        try {
            return list.size();
        } finally {
            readLock.unlock();
        }
    }

    public Iterator<T> iterator(){
        readLock.lock();
        try {
            return new ArrayList<>(list).iterator();
            //^ we iterate over an snapshot of our list
        } finally {
            readLock.unlock();
        }
    }


    @Override
    public String toString() {
        readLock.lock();
        try {
            StringBuilder builder = new StringBuilder("");
            String separator ="";
            for(T obj:list){
                if(obj!=null){
                    builder.append(separator);
                    builder.append(obj.toString());
                    separator=",";
                }
            }
            return builder.toString();
        } finally {
            readLock.unlock();
        }
    }
}
package com.poc.java.buffer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CircularBuffer<T> {

    private final Object[] buffer;
    private final int capacity;

    private int writePos = 0;
    private int readPos = 0;
    private boolean bufferFull = false;

    public CircularBuffer(int size) {
        this.capacity = size;
        this.buffer = new Object[size];
    }

    public CircularBuffer() {
        this(1_000);
    }

    /** Adds an element to the buffer, overwriting oldest if full */
    public synchronized void add(T element) {
        buffer[writePos] = element;

        writePos = (writePos + 1) % capacity;

        if (bufferFull) {
            // Overwrite oldest: move readPos forward too
            readPos = (readPos + 1) % capacity;
        }

        if (writePos == readPos) {
            bufferFull = true;
        } else {
            bufferFull = false;
        }
    }

    /** Retrieves and removes the next element, or returns null if empty */
    @SuppressWarnings("unchecked")
    public synchronized T get() {
        if (isEmpty()) {
            return null;
        }

        T element = (T) buffer[readPos];
        buffer[readPos] = null; // help GC

        readPos = (readPos + 1) % capacity;

        bufferFull = false;

        return element;
    }

    public synchronized boolean isEmpty() {
        return (!bufferFull && writePos == readPos);
    }

    public synchronized boolean isFull() {
        return bufferFull;
    }

    public synchronized int size() {
        if (bufferFull) {
            return capacity;
        } else if (writePos >= readPos) {
            return writePos - readPos;
        } else {
            return capacity - readPos + writePos;
        }
    }
}

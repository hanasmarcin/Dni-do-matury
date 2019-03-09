package hanas.dnidomatury.model.fileSupport;

import java.util.List;

public interface FileSupportedList<T> extends FileSupport<FileSupportedList<T>>, Iterable<T>{

    int size();
    boolean isEmpty();
    boolean add(T newElement);
    void add(int position, T newElement);
    boolean remove(Object oldElement);
    void sort();
    void clear();
    T get(int element);
    T remove(int index);
}

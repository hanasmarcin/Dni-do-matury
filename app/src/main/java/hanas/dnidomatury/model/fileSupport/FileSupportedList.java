package hanas.dnidomatury.model.fileSupport;

public interface FileSupportedList<T> extends FileSupported<FileSupportedList<T>>, Iterable<T>{

    int size();
    boolean isEmpty();
    void add(int position, T newElement);
    T remove(int index);
    boolean remove(Object oldElement);
    void sort();
    void clear();
    T get(int element);
}

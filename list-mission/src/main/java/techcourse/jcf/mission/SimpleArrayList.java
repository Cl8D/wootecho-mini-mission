package techcourse.jcf.mission;

import java.util.Arrays;

public class SimpleArrayList<E> implements SimpleList<E> {

    private static final int MIN_INDEX = 0;
    private static final int MIN_CAPACITY = 10;
    private static final Object[] EMPTY_STORAGE = {};
    private Object[] storage;
    private int size;

    public SimpleArrayList() {
        storage = EMPTY_STORAGE;
        this.size = 0;
    }

    @Override
    public boolean add(final E value) {
        addLast(value);
        return true;
    }

    @Override
    public void add(final int index, final E value) {
        if (index < MIN_INDEX || index > size) {
            throw new IndexOutOfBoundsException();
        }

        if (index == size) {
            addLast(value);
            return;
        }

        if (size == storage.length) {
            resize();
        }

        // 요소들을 한 칸씩 뒤로
        for (int i = size; i > index; i--) {
            storage[i] = storage[i - 1];
        }

        storage[index] = value;
        size++;
    }

    @Override
    public E set(final int index, final E value) {
        validateIndexRange(index);
        Object item = storage[index];
        storage[index] = value;
        return (E) item;
    }

    @Override
    public E get(final int index) {
        validateIndexRange(index);
        return (E) storage[index];
    }

    @Override
    public boolean contains(final E value) {
        return indexOf(value) >= 0;
    }

    @Override
    public int indexOf(final E value) {
        for (int i = 0; i < size; i++) {
            if (storage[i].equals(value)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public boolean remove(final E value) {
        int targetIndex = indexOf(value);
        if (targetIndex == -1) {
            return false;
        }
        remove(targetIndex);
        return true;
    }

    @Override
    public E remove(final int index) {
        validateIndexRange(index);

        E removedElement = (E) storage[index];
        // GC에서 메모리 수거를 하도록 하기 위해서 null로 명시적 선언을 해준다.
        storage[index] = null;

        // 삭제할 요소 뒤에 있는 요소들을 한 칸씩 앞으로 당겨온다.
        for (int i = index; i < size; i++) {
            storage[i] = storage[i + 1];
            storage[i + 1] = null;
        }

        size--;
        resize();

        return removedElement;
    }

    @Override
    public void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
        // 여기서 resize를 하게 되면, 현재 capacity의 절반으로 줄인다.
        resize();
    }

    private void validateIndexRange(final int index) {
        if (index < MIN_INDEX || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void addLast(final E value) {
        if (size == storage.length) {
            resize();
        }
        storage[size++] = value;
    }

    private void resize() {
        int capacity = storage.length;

        // 비어 있을 경우
        if (capacity == 0) {
            storage = new Object[MIN_CAPACITY];
            return;
        }

        // 가득찼을 경우
        if (size == capacity) {
            int newCapacity = capacity * 2;
            storage = Arrays.copyOf(storage, newCapacity);
            return;
        }

        // size에 비해 capacity가 큰 경우 (메모리 낭비를 막기 위해서)
        if (size < (capacity / 2)) {
            int newCapacity = Math.max(MIN_CAPACITY, capacity / 2);
            storage = Arrays.copyOf(storage, newCapacity);
        }
    }
}

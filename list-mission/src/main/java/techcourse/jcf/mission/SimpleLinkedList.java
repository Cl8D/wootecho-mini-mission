package techcourse.jcf.mission;

import java.util.NoSuchElementException;
import java.util.Objects;

public class SimpleLinkedList<E> implements SimpleList<E> {

    private static final int MIN_INDEX = 0;

    private Node<E> first;
    private Node<E> last;
    private int size;

    public SimpleLinkedList() {
        this.first = null;
        this.last = null;
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

        if (index == 0) {
            addFirst(value);
            return;
        }

        if (index == size) {
            addLast(value);
            return;
        }

        // 추가하려는 노드
        Node<E> node = new Node(value);

        // 추가하려던 위치의 이전 노드
        Node<E> prevNode = getNodeByIndex(index - 1);
        prevNode.next = node;

        // 추가하려는 위치의 노드
        Node<E> nextNode = prevNode.next;
        nextNode.prev = node;

        node.prev = prevNode;
        node.next = nextNode;
        size++;
    }

    @Override
    public E set(final int index, final E value) {
        Node<E> targetNode = getNodeByIndex(index);
        E targetItem = (E) targetNode.item;
        targetNode.item = value;
        return targetItem;
    }

    @Override
    public E get(final int index) {
        return (E) getNodeByIndex(index).item;
    }

    @Override
    public boolean contains(final E value) {
        return indexOf(value) != -1;
    }

    @Override
    public int indexOf(final E value) {
        int index = 0;

        Node<E> target = first;
        while (target != null) {
            if (target.item.equals(value)) {
                return index;
            }
            index++;
            target = target.next;
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
        Node<E> target = first;
        Node<E> prevNode = first;

        // 해당 value와 일치하는 노드 찾기
        while (target != null) {
            if (target.item.equals(value)) {
                break;
            }
            target = target.next;
            prevNode = target;
        }

        if (target == null) {
            return false;
        }

        if (target.equals(first)) {
            removeFirst();
            return true;
        }

        // 제거 대상의 다음 노드
        Node<E> nextNode = target.next;
        if (nextNode != null) {
            nextNode.prev = prevNode;
            prevNode.next = null;
        } else {
            last = prevNode;
        }

        size--;
        return true;
    }

    @Override
    public E remove(final int index) {
        validateIndexRange(index);

        if (index == 0) {
            E firstItem = (E) first.item;
            removeFirst();
            return firstItem;
        }

        // 제거 대상의 이전 노드
        Node<E> prevNode = getNodeByIndex(index - 1);
        // 제거 대상 노드
        Node<E> targetNode = prevNode.next;
        // 제거 대상의 다음 노드
        Node<E> nextNode = targetNode.next;

        // 제거 전에 미리 저장해둔 제거 대상 아이템
        E targetItem = (E) targetNode.item;

        prevNode.next = null;
        targetNode.prev = null;
        targetNode.next = null;
        targetNode.item = null;

        // 다음 노드가 존재한다면, 이전 노드와 연결 다시하기
        if (nextNode != null) {
            nextNode.prev = prevNode;
            prevNode.next = nextNode;
        } else {
            // 존재하지 않는다면 마지막 노드 정보 갱신하기
            last = prevNode;
        }

        size--;
        return targetItem;
    }

    @Override
    public void clear() {
        for (Node<E> target = first; target != null; ) {
            Node<E> nextNode = target.next;
            target.item = null;
            target.prev = null;
            target.next = null;
            target = nextNode;
        }
        first = last = null;
        size = 0;
    }

    private Node<E> getNodeByIndex(final int index) {
        validateIndexRange(index);

        // 그 다음 것으로 이동하면서 해당 노드 발견하기
        Node<E> target = first;
        for (int i = 0; i < index; i++) {
            target = target.next;
        }

        return target;
    }

    private void validateIndexRange(final int index) {
        if (index < MIN_INDEX || index >= size) {
            throw new IndexOutOfBoundsException();
        }
    }

    private void addLast(final E value) {
        Node<E> newNode = new Node(value);

        // 사이즈가 0이면 가장 처음에 넣어주기
        if (size == 0) {
            addFirst(value);
            return;
        }

        // 기존에 있는 마지막 노드의 다음 것을 현재 노드로 설정한다.
        last.next = newNode;
        newNode.prev = last;
        // 마지막 노드를 새로운 노드로 갱신
        last = newNode;
        size++;
    }

    private void addFirst(final E value) {
        Node<E> current = new Node(value);
        // 현재 노드의 다음 상태에 대해서 새로 들어온 노드 연결해주기.
        current.next = first;

        // 기존 노드의 이전에 새로 들어온 노드 연결해주기
        if (first != null) {
            first.prev = current;
        }

        // 첫 위치를 갱신해주기
        first = current;
        size++;

        // 다음 노드가 없으면, 마지막으로 설정
        if (first.next == null) {
            last = first;
        }
    }

    private void removeFirst() {
        if (first == null) {
            throw new NoSuchElementException();
        }

        // 헤드 위치에 있는 노드의 다음 노드 임시 저장
        Node<E> nextNode = first.next;

        first.item = null;
        first.next = null;

        // 헤더 다음의 노드가 없었다면 그냥 하나였던 거니까 이전 노드를 초기화해줄 필요가 없다.
        if (nextNode != null) {
            // 다음 노드의 이전 노드 정보 제거
            nextNode.prev = null;
        }

        // 다음 노드를 헤드로 설정
        first = nextNode;
        size--;

        // 제거 후 리스트가 비었다면, last에 대해 null 처리
        if (size == 0) {
            last = null;
        }
    }

    private static class Node<E> {
        E item;
        Node<E> prev;
        Node<E> next;

        public Node(final E item, final Node<E> prev, final Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }

        public Node(final E item) {
            this.item = item;
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Node<E> node = (Node<E>) o;
            return Objects.equals(item, node.item) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(item, prev, next);
        }
    }
}

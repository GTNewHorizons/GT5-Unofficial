/*
 * Copyright (c) 2018-2019 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util.accessprioritylist;

import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.Spliterator;

import org.apache.commons.lang3.NotImplementedException;

public class AccessPriorityList<E> implements List<E>, Deque<E>, Set<E> {

    transient int size = 0;
    transient AccessPriorityListNode<E> head;
    transient AccessPriorityListNode<E> tail;

    public static <E> AccessPriorityList<E> create() {
        return new AccessPriorityList<>();
    }

    public AccessPriorityList() {}

    @Override
    public void addFirst(E t) {
        final AccessPriorityListNode<E> first = this.head;
        final AccessPriorityListNode<E> newNode = new AccessPriorityListNode<>(null, t, first);
        this.head = newNode;
        if (first == null) this.tail = newNode;
        else first.setBefore(newNode);
        this.size++;
    }

    @Override
    public void addLast(E t) {
        final AccessPriorityListNode<E> last = this.tail;
        final AccessPriorityListNode<E> newNode = new AccessPriorityListNode<>(last, t, null);
        this.tail = newNode;
        if (last == null) this.head = newNode;
        else last.setNext(newNode);
        this.size++;
    }

    @Override
    public boolean offerFirst(E e) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean offerLast(E e) {
        throw new NotImplementedException("");
    }

    @Override
    public E removeFirst() {
        E first = this.head.getELEMENT();
        AccessPriorityListNode<E> node = this.head;
        this.head = node.getNext();
        this.head.setBefore(null);
        node.destroy();
        node = null;
        this.size--;
        return first;
    }

    @Override
    public E removeLast() {
        E last = this.tail.getELEMENT();
        AccessPriorityListNode<E> node = this.tail;
        this.tail = node.getBefore();
        this.tail.setNext(null);
        node.destroy();
        node = null;
        this.size--;
        return last;
    }

    @Override
    public E pollFirst() {
        throw new NotImplementedException("");
    }

    @Override
    public E pollLast() {
        throw new NotImplementedException("");
    }

    @Override
    public E getFirst() {
        return this.peekFirst();
    }

    @Override
    public E getLast() {
        return this.peekLast();
    }

    @Override
    public E peekFirst() {
        return this.head != null ? this.head.getELEMENT() : null;
    }

    @Override
    public E peekLast() {
        return this.tail != null ? this.tail.getELEMENT() : null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        throw new NotImplementedException("");
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public boolean contains(Object o) {
        throw new NotImplementedException("");
    }

    @Override
    public Iterator<E> iterator() {
        return new AccessPriorityListIterators.AccessPriorityListIterator<>(this.head);
    }

    public Iterator<AccessPriorityListNode<E>> nodeIterator() {
        return new AccessPriorityListIterators.AccessPriorityListNodeIterator<>(this.head);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new AccessPriorityListIterators.AccessPriorityListReverseIterator<>(this.tail);
    }

    @Override
    public Object[] toArray() {
        Object[] ret = new Object[this.size];
        int index = 0;
        for (Iterator<E> it = this.iterator(); it.hasNext(); index++) ret[index] = it.next();
        return ret;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] ret = (T[]) new Object[this.size];
        int index = 0;
        for (Iterator<T> it = (Iterator<T>) this.iterator(); it.hasNext(); index++) ret[index] = it.next();
        return ret;
    }

    @Override
    public boolean add(E e) {
        this.addLast(e);
        return true;
    }

    private void moveNodeUp(AccessPriorityListNode<E> node) {
        if (node == this.head || node.getBefore() == null) return;
        final AccessPriorityListNode<E> before = node.getBefore();
        final AccessPriorityListNode<E> beforeBefore = before.getBefore();
        final AccessPriorityListNode<E> next = node.getNext();

        // <0,1,2> <1,2,3> N<2,3,4> <3,4,5>

        node.setBefore(beforeBefore);
        // <0,1,2> <1,2,3> N<0,3,4> <3,4,5>

        if (beforeBefore != null) beforeBefore.setNext(node);
        else this.head = node;
        // <0,1,3> <1,2,3> N<0,3,4> <3,4,5>

        before.setBefore(node);
        // <0,1,3> <3,2,3> N<0,3,4> <3,4,5>

        before.setNext(next);
        // <0,1,3> <3,2,4> N<0,3,4> <3,4,5>

        if (next != null) next.setBefore(before);
        else this.tail = before;
        // <0,1,3> N<0,3,4> <3,2,4> <2,4,5>

        node.setNext(before);
        // <0,1,3> N<0,3,2> <3,2,4> <2,4,5>
    }

    AccessPriorityListNode<E> getNode(int index) {
        if (index <= this.size / 2) {
            AccessPriorityListNode<E> x = this.head;
            for (int i = 0; i < index; i++) x = x.getNext();
            return x;
        }
        AccessPriorityListNode<E> x = this.tail;
        for (int i = this.size - 1; i > index; i--) x = x.getBefore();
        return x;
    }

    @Override
    public boolean offer(E e) {
        throw new NotImplementedException("");
    }

    private boolean isValidIndex(int index) {
        if (index >= 0 && index < this.size) return true;
        throw new ArrayIndexOutOfBoundsException("NOT A VAILD INDEX!");
    }

    @Override
    public E remove() {
        throw new NotImplementedException("");
    }

    @Override
    public E poll() {
        return this.removeFirst();
    }

    @Override
    public E element() {
        throw new NotImplementedException("");
    }

    @Override
    public E peek() {
        return this.getFirst();
    }

    @Override
    public void push(E e) {
        this.addFirst(e);
    }

    @Override
    public E pop() {
        return this.removeFirst();
    }

    @Override
    public boolean remove(Object o) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (c == null) return false;
        c.forEach(this::addLast);
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new NotImplementedException("");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new NotImplementedException("");
    }

    @Override
    public void clear() {
        if (this.tail != null) {
            AccessPriorityListNode<E> node = this.tail;
            while (node.getBefore() != null) {
                node.setNext(null);
                node.setPriority(0L);
                node = node.getBefore();
                node.getNext()
                    .setBefore(null);
            }
            this.size = 0;
            this.head = null;
            this.tail = null;
        }
    }

    public void addPrioToNode(AccessPriorityListNode<E> node) {
        this.addPrioToNode(node, 1L);
    }

    public void addPrioToNode(AccessPriorityListNode<E> node, long prio) {
        long current = node.getPriority();
        if (current == Long.MAX_VALUE || current > 0 && prio > 0 && prio + current < 0)
            node.setPriority(Long.MAX_VALUE);
        else node.setPriority(current + prio);
        while (node.getBefore() != null && node.getPriority() >= node.getBefore()
            .getPriority()) {
            this.moveNodeUp(node);
        }
    }

    public void addPrioToNode(int index, long prio) {
        if (!this.isValidIndex(index)) return;
        AccessPriorityListNode<E> node = this.getNode(index);
        this.addPrioToNode(node, prio);
    }

    public void addPrioToNode(int index) {
        this.addPrioToNode(index, 1L);
    }

    @Override
    public E get(int index) {
        if (!this.isValidIndex(index)) return null;
        AccessPriorityListNode<E> node = this.getNode(index);
        return node.getELEMENT();
    }

    @Override
    public E set(int index, E element) {
        throw new NotImplementedException("");
    }

    @Override
    public void add(int index, E element) {
        throw new NotImplementedException("");
    }

    @Override
    public E remove(int index) {
        throw new NotImplementedException("");
    }

    @Override
    public int indexOf(Object o) {
        throw new NotImplementedException("");
    }

    @Override
    public int lastIndexOf(Object o) {
        throw new NotImplementedException("");
    }

    @Override
    public ListIterator<E> listIterator() {
        return new AccessPriorityListIterators.AccessPriorityListListIterator<>(this.head, this.tail, false);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new AccessPriorityListIterators.AccessPriorityListListIterator<>(this, index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        throw new NotImplementedException("");
    }

    @Override
    public Spliterator<E> spliterator() {
        throw new NotImplementedException("");
    }
}

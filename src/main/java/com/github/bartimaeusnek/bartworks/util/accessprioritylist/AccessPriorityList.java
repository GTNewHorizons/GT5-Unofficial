/*
 * Copyright (c) 2018-2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util.accessprioritylist;

import java.util.*;

public class AccessPriorityList<E> implements List<E>, Deque<E>, Set<E> {

    transient int size = 0;
    transient AccessPriorityListNode<E> head;
    transient AccessPriorityListNode<E> tail;

    public static AccessPriorityList create(){
        return new AccessPriorityList();
    }

    public AccessPriorityList() {}

    @Override
    public void addFirst(E t) {
        final AccessPriorityListNode<E> first = head;
        final AccessPriorityListNode<E> newNode = new AccessPriorityListNode<>(null, t, first);
        head = newNode;
        if (first == null)
            tail = newNode;
        else
            first.setBefore(newNode);
        size++;
    }

    @Override
    public void addLast(E t) {
        final AccessPriorityListNode<E> last = tail;
        final AccessPriorityListNode<E> newNode = new AccessPriorityListNode<>(last, t, null);
        tail = newNode;
        if (last == null)
            head = newNode;
        else
            last.setNext(newNode);
        size++;
    }

    @Override
    public boolean offerFirst(E e) {
        return false;
    }

    @Override
    public boolean offerLast(E e) {
        return false;
    }

    @Override
    public E removeFirst() {
        return null;
    }

    @Override
    public E removeLast() {
        return null;
    }

    @Override
    public E pollFirst() {
        return null;
    }

    @Override
    public E pollLast() {
        return null;
    }

    @Override
    public E getFirst() {
        return peekFirst();
    }

    @Override
    public E getLast() {
        return peekLast();
    }

    @Override
    public E peekFirst() {
        return head != null ? head.getELEMENT() : null;
    }

    @Override
    public E peekLast() {
        return tail != null ? tail.getELEMENT() : null;
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return false;
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return false;
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
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator<E> iterator() {
        return new AccessPriorityListIterators.AccessPriorityListIterator<>(head);
    }

    @Override
    public Iterator<E> descendingIterator() {
        return new AccessPriorityListIterators.AccessPriorityListReverseIterator<>(tail);
    }

    @Override
    public Object[] toArray() {
        Object[] ret = new Object[size];
        while (listIterator().hasNext())
            ret[listIterator().nextIndex()-1] = listIterator().next();
        return ret;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        T[] ret = (T[]) new Object[size];
        while (listIterator().hasNext())
            ret[listIterator().nextIndex()-1] = (T) listIterator().next();
        return ret;
    }

    @Override
    public boolean add(E e) {
        addLast(e);
        return true;
    }

    private void moveNodeUp(AccessPriorityListNode<E> node){
        if (node == head || node.getBefore() == null)
            return;
        final AccessPriorityListNode<E> before = node.getBefore();
        final AccessPriorityListNode<E> beforeBefore = before.getBefore();
        final AccessPriorityListNode<E> next = node.getNext();

        // <0,1,2> <1,2,3> N<2,3,4> <3,4,5>

        node.setBefore(beforeBefore);
        // <0,1,2> <1,2,3> N<0,3,4> <3,4,5>

        if (beforeBefore != null)
            beforeBefore.setNext(node);
        // <0,1,3> <1,2,3> N<0,3,4> <3,4,5>

        before.setBefore(node);
        // <0,1,3> <3,2,3> N<0,3,4> <3,4,5>

        before.setNext(next);
        // <0,1,3> <3,2,4> N<0,3,4> <3,4,5>

        if (next != null)
            next.setBefore(before);
        // <0,1,3> N<0,3,4> <3,2,4> <2,4,5>

        node.setNext(before);
        // <0,1,3> N<0,3,2> <3,2,4> <2,4,5>
    }

    AccessPriorityListNode<E> getNode(int index) {
        if (index <= (size / 2)) {
            AccessPriorityListNode<E> x = head;
            for (int i = 0; i < index; i++)
                x = x.getNext();
            return x;
        } else {
            AccessPriorityListNode<E> x = tail;
            for (int i = size - 1; i > index; i--)
                x = x.getBefore();
            return x;
        }
    }

    @Override
    public boolean offer(E e) {
        return false;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }

    @Override
    public E remove() {
        return null;
    }

    @Override
    public E poll() {
        return null;
    }

    @Override
    public E element() {
        return null;
    }

    @Override
    public E peek() {
        return getFirst();
    }

    @Override
    public void push(E e) {
        addFirst(e);
    }

    @Override
    public E pop() {
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        for (E e : c) {
            this.addLast(e);
        }
        return true;
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return false;
    }

    @Override
    public void clear() {
        if (tail != null) {
            AccessPriorityListNode<E> node = tail;
            while (node.getBefore() != null) {
                node.setNext(null);
                node.setPriority(0L);
                node = node.getBefore();
                node.getNext().setBefore(null);
            }
            this.size = 0;
            this.head = null;
            this.tail = null;
        }
    }

    public void addPrioToNode(int index, long prio){
        if (!isValidIndex(index))
            return;
        AccessPriorityListNode<E> node = getNode(index);
        node.setPriority(node.getPriority()+prio);
        while (node.getBefore() != null && node.getPriority() >= node.getBefore().getPriority()){
                moveNodeUp(node);
        }
    }

    public void addPrioToNode(int index){
        addPrioToNode(index,1L);
    }

    @Override
    public E get(int index) {
        if (!isValidIndex(index))
            return null;
        AccessPriorityListNode<E> node = getNode(index);
        return node.getELEMENT();
    }

    @Override
    public E set(int index, E element) {
        return null;
    }

    @Override
    public void add(int index, E element) {

    }

    @Override
    public E remove(int index) {
        return null;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator<E> listIterator() {
        return new AccessPriorityListIterators.AccessPriorityListListIterator<>(head,tail,false);
    }

    @Override
    public ListIterator<E> listIterator(int index) {
        return new AccessPriorityListIterators.AccessPriorityListListIterator<>(this,index);
    }

    @Override
    public List<E> subList(int fromIndex, int toIndex) {
        return null;
    }

    @Override
    public Spliterator<E> spliterator() {
        return null;
    }
}

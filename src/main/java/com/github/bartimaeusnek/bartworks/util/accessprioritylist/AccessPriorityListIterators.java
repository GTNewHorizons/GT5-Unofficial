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

import java.util.Iterator;
import java.util.ListIterator;
import org.apache.commons.lang3.NotImplementedException;

public class AccessPriorityListIterators {

    public static class AccessPriorityListListIterator<E> implements ListIterator<E> {
        final AccessPriorityListNode<E> head;
        final AccessPriorityListNode<E> tail;
        AccessPriorityListNode<E> current;
        int counter = 0;
        boolean reverse;

        public AccessPriorityListListIterator(
                AccessPriorityListNode<E> head, AccessPriorityListNode<E> tail, boolean reverse) {
            this.head = head;
            this.tail = tail;
            current = reverse ? tail : head;
            this.reverse = reverse;
        }

        public AccessPriorityListListIterator(AccessPriorityList<E> list, int index) {
            this.head = list.head;
            this.tail = list.tail;
            current = list.getNode(index);
            counter = index;
        }

        @Override
        public boolean hasNext() {
            return reverse ? head != current : tail != current;
        }

        @Override
        public E next() {
            counter++;
            E ret = current.getELEMENT();
            current = current.getNext();
            return ret;
        }

        @Override
        public boolean hasPrevious() {
            return !reverse ? head != current : tail != current;
        }

        @Override
        public E previous() {
            counter--;
            E ret = current.getELEMENT();
            current = current.getBefore();
            return ret;
        }

        @Override
        public int nextIndex() {
            return counter + 1;
        }

        @Override
        public int previousIndex() {
            return counter - 1;
        }

        @Override
        public void remove() {
            throw new NotImplementedException("Not Implemented");
        }

        @Override
        public void set(E e) {
            throw new NotImplementedException("Not Implemented");
        }

        @Override
        public void add(E e) {
            throw new NotImplementedException("Not Implemented");
        }
    }

    public static class AccessPriorityListIterator<E> implements Iterator<E> {
        final AccessPriorityListNode<E> head;
        AccessPriorityListNode<E> current;

        public AccessPriorityListIterator(AccessPriorityListNode<E> head) {
            this.head = this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E ret = current.getELEMENT();
            current = current.getNext();
            return ret;
        }
    }

    public static class AccessPriorityListReverseIterator<E> implements Iterator<E> {
        final AccessPriorityListNode<E> tail;
        AccessPriorityListNode<E> current;

        public AccessPriorityListReverseIterator(AccessPriorityListNode<E> tail) {
            this.tail = this.current = tail;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public E next() {
            E ret = current.getELEMENT();
            current = current.getBefore();
            return ret;
        }
    }

    public static class AccessPriorityListNodeIterator<E> implements Iterator<AccessPriorityListNode<E>> {
        final AccessPriorityListNode<E> head;
        AccessPriorityListNode<E> current;

        public AccessPriorityListNodeIterator(AccessPriorityListNode<E> head) {
            this.head = this.current = head;
        }

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public AccessPriorityListNode<E> next() {
            AccessPriorityListNode<E> ret = current;
            current = current.getNext();
            return ret;
        }
    }
}

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

public class AccessPriorityListNode<E> {

    public static final AccessPriorityListNode EMPTY_NODE = new AccessPriorityListNode(null);

    private final E ELEMENT;
    private long priority = Long.MIN_VALUE;
    private AccessPriorityListNode<E> next;
    private AccessPriorityListNode<E> before;

    public AccessPriorityListNode(E element) {
        ELEMENT = element;
    }

    public AccessPriorityListNode(AccessPriorityListNode<E> before, E element, AccessPriorityListNode<E> next) {
        this.ELEMENT = element;
        connect(next, before);
    }

    public void connect(AccessPriorityListNode<E> next, AccessPriorityListNode<E> before) {
        this.setNext(next);
        this.setBefore(before);
    }

    public E getELEMENT() {
        return ELEMENT;
    }

    public long getPriority() {
        return priority;
    }

    public void setPriority(long priority) {
        this.priority = priority;
    }

    public AccessPriorityListNode<E> getNext() {
        return next;
    }

    public void setNext(AccessPriorityListNode<E> next) {
        this.next = next;
    }

    public AccessPriorityListNode<E> getBefore() {
        return before;
    }

    public void setBefore(AccessPriorityListNode<E> before) {
        this.before = before;
    }

    void destroy() {
        this.before = null;
        this.next = null;
        this.priority = 0L;
    }
}

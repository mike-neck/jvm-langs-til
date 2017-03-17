/*
 * Copyright 2017 Shinya Mochida
 * 
 * Licensed under the Apache License,Version2.0(the"License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing,software
 * Distributed under the License is distributed on an"AS IS"BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.overtime;

import com.example.Tuple;
import lombok.RequiredArgsConstructor;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public interface Queue<E> extends Iterable<E> {

    int size();

    default int maxSize() {
        return 12;
    }

    E head();

    E last();

    default Stream<E> stream() {
        return StreamSupport.stream(this.spliterator(), false);
    }

    Tuple<Queue<E>, Optional<E>> enqueue(E elem);

    static <E> Queue<E> empty() {
        return new Empty<>();
    }

    @SafeVarargs
    static <E> Queue<E> of(E... es) {
        if (es == null || es.length == 0) {
            return empty();
        }
        if (es.length <= 12) {
            final Iterable<E> ite = new Iterable<E>() {
                @Override
                public Iterator<E> iterator() {
                    return new Iterator<E>() {

                        int current = es.length - 1;

                        @Override
                        public boolean hasNext() {
                            return current >= 0;
                        }

                        @Override
                        public E next() {
                            final int i = this.current;
                            current = current - 1;
                            return es[i];
                        }
                    };
                }
            };
            return StreamSupport.stream(ite.spliterator(), false)
                    .<SideEffect<E>>collect(SideEffect::new, SideEffect::append, (l, r) -> {
                    })
                    .getQueue();
        } else {
            throw new IllegalArgumentException("queue.size > 12");
        }
    }
}

class SideEffect<E> {
    private Queue<E> queue = new Empty<>();

    void append(E elem) {
        queue = queue.enqueue(elem).getLeft();
    }

    boolean isNotEmpty() {
        return !isEmpty();
    }

    boolean isEmpty() {
        return queue instanceof Empty;
    }

    Queue<E> getQueue() {
        return queue;
    }
}

@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
@RequiredArgsConstructor
class Element<E> {

    private final E value;

    private Optional<Element<E>> next = Optional.empty();
    private Optional<Element<E>> previous = Optional.empty();

    void setNext(Element<E> next) {
        this.next = Optional.of(next);
        next.previous = Optional.of(this);
    }

    void removePrevious() {
        this.previous = Optional.empty();
    }

    boolean hasNext() {
        return next.isPresent();
    }

    Optional<Element<E>> nextAsOptional() {
        return next;
    }

    E get() {
        return value;
    }

    Element<E> getNext() {
        if (next.isPresent()) {
            return next.get();
        } else {
            throw new UnsupportedOperationException("element::next::get");
        }
    }

    Element<E> getPrevious() {
        if (previous.isPresent()) {
            return previous.get();
        } else {
            throw new UnsupportedOperationException("element::previous::get");
        }
    }
}

class Empty<E> implements Queue<E> {

    @Override
    public int size() {
        return 0;
    }

    @Override
    public E head() {
        throw new UnsupportedOperationException("empty::head");
    }

    @Override
    public E last() {
        throw new UnsupportedOperationException("empty::last");
    }

    @Override
    public Tuple<Queue<E>, Optional<E>> enqueue(E elem) {
        final Element<E> e = new Element<>(elem);
        final Mid<E> mid = new Mid<>(1, e, e);
        return new Tuple<>(mid, Optional.empty());
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public E next() {
                throw new UnsupportedOperationException("empty::iterator::next");
            }
        };
    }
}

class Mid<E> implements Queue<E> {

    private final int size;
    private final Element<E> head;
    private final Element<E> last;

    Mid(int size, Element<E> head, Element<E> last) {
        this.size = size;
        this.head = head;
        this.last = last;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public E head() {
        return head.get();
    }

    @Override
    public E last() {
        return last.get();
    }

    public Element<E> getHead() {
        return head;
    }

    private final Function<Element<E>, Queue<E>> mid = e -> new Mid<>(size() + 1, getHead(), e);

    private final Function<Element<E>, Queue<E>> full = e -> new Full<>(getHead(), e);

    @Override
    public Tuple<Queue<E>, Optional<E>> enqueue(E elem) {
        final Element<E> e = new Element<>(elem);
        last.setNext(e);

        final Function<Element<E>, Queue<E>> constructor = this.size + 1 < maxSize() ? mid : full;
        return new Tuple<>(constructor.apply(e), Optional.empty());
    }

    @Override
    public Iterator<E> iterator() {
        return new QueIterator<>(head);
    }
}

class Full<E> implements Queue<E> {

    private final Element<E> head;
    private final Element<E> last;

    Full(Element<E> head, Element<E> last) {
        this.head = head;
        this.last = last;
    }

    @Override
    public int size() {
        return maxSize();
    }

    @Override
    public E head() {
        return head.get();
    }

    @Override
    public E last() {
        return last.get();
    }

    @Override
    public Tuple<Queue<E>, Optional<E>> enqueue(E elem) {
        final Element<E> e = new Element<>(elem);
        last.setNext(e);

        final Element<E> next = head.getNext();
        next.removePrevious();

        return new Tuple<>(new Full<>(next, e), Optional.of(head.get()));
    }

    @Override
    public Iterator<E> iterator() {
        return new QueIterator<>(head);
    }
}

class QueIterator<E> implements Iterator<E> {

    QueIterator(Element<E> head) {
        current = Optional.of(head);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<Element<E>> current;

    @Override
    public boolean hasNext() {
        return current.isPresent();
    }

    @Override
    public E next() {
        if (current.isPresent()) {
            final Element<E> element = current.get();
            current = element.nextAsOptional();
            return element.get();
        } else {
            throw new UnsupportedOperationException("queue::iterator::next");
        }
    }
}

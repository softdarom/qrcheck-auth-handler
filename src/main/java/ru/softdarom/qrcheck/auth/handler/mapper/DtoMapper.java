package ru.softdarom.qrcheck.auth.handler.mapper;

import java.util.Collection;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Predicate;

public interface DtoMapper<S, D> {

    S convertToSource(D destination);

    D convertToDestination(S source);

    Collection<S> convertToSources(Collection<D> collection);

    Collection<D> convertToDestinations(Collection<S> collection);

    @FunctionalInterface
    interface Action {
        void execute();
    }

    default <T> void whenNotNull(T o, Consumer<T> c) {
        whenNotNull(o, c, null);
    }

    default <T> void whenNotNull(T o, Consumer<T> c, Action nullAction) {
        when(o, Objects::nonNull, c, nullAction);
    }

    default <T> void whenNot(T o, Predicate<T> p, Consumer<T> c) {
        when(o, p.negate(), c, null);
    }

    default <T> void when(T o, Predicate<T> p, Consumer<T> c) {
        when(o, p, c, null);
    }

    default <T> void when(T o, Predicate<T> p, Consumer<T> c, Action nullAction) {
        if (p.test(o)) {
            c.accept(o);
        } else {
            if (nullAction != null) {
                nullAction.execute();
            }
        }
    }
}
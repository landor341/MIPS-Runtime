package mips;

import java.util.Objects;
import java.util.function.Function;

/*
    This code was sourced from stackOverflow since java only provides a BiFunction natively and the code was relative simple to implement
    but uses notation I'm not super familiar with
    SOURCE: https://stackoverflow.com/questions/18400210/java-8-where-is-trifunction-and-kin-in-java-util-function-or-what-is-the-alt

 */

@FunctionalInterface
public interface PentaFunction<A,B,C,D,E,R> {

    R apply(A a, B b, C c, D d, E e);

    default <V> PentaFunction<A, B, C, D, E, V> andThen(
            Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c, D d, E e) -> after.apply(apply(a, b, c, d, e));
    }
}
package com.unrealdinnerbone.javd.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ListUtil {
    public static <T> Optional<T> getFirstValue(List<T> tList) {
        return tList.size() > 1 ? Optional.ofNullable(tList.get(0)) : Optional.empty();
    }

    public static <T> Optional<T> next(List<T> tList, String value) {
        boolean next = false;
        for (T t : tList) {
            if(next) {
                return Optional.of(t);
            }
            if(t.equals(value)) {
                next = true;
            }
        }
        return getFirstValue(tList);
    }

    public static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

}

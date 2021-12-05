package com.unrealdinnerbone.javd.util;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class ListUtil {

    public static <E> Optional<E> getRandom(Collection<E> e) {
        return e.stream().skip((int) (e.size() * Math.random())).findFirst();
    }

}

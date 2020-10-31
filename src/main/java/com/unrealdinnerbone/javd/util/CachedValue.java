package com.unrealdinnerbone.javd.util;

import java.util.Optional;
import java.util.function.Supplier;

public class CachedValue<T>
{
    private final Supplier<T> tSupplier;
    private T tValue;

    public CachedValue(Supplier<T> tSupplier) {
        this.tSupplier = tSupplier;
    }

    public T get() {
        if (tValue == null || (tValue instanceof Optional<?> && !((Optional<?>) tValue).isPresent())) {
            return tValue = tSupplier.get();
        }
        return tValue;
    }

    public void reset() {
        tValue = null;
    }
}

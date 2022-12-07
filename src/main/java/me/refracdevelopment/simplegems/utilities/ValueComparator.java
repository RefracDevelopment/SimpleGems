package me.refracdevelopment.simplegems.utilities;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<T> implements Comparator<T> {

    Map<T, Double> base;

    public ValueComparator(Map<T, Double> base) {
        this.base = base;
    }

    @Override
    public int compare(T a, T b) {
        if (this.base.get(a) >= this.base.get(b)) {
            return -1;
        }
        return 1;
    }
}

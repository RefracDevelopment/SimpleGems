package me.refracdevelopment.simplegems.leaderboard;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<T> implements Comparator<T> {

    Map<T, Long> base;

    public ValueComparator(Map<T, Long> base) {
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
package me.refracdevelopment.simplegems.leaderboard;

import java.util.Comparator;
import java.util.Map;

public class ValueComparator<T> implements Comparator<T> {

    Map<String, TopGems> base;

    public ValueComparator(Map<String, TopGems> base) {
        this.base = base;
    }

    @Override
    public int compare(T a, T b) {
        if (this.base.get(a).getGems() >= this.base.get(b).getGems()) {
            return -1;
        }
        return 1;
    }
}
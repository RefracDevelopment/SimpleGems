package me.refracdevelopment.simplegems.leaderboard;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TopGems {

    private final String playerName;
    private final long gems;

    public TopGems(String playerName, long gems) {
        this.playerName = playerName;
        this.gems = gems;
    }
}
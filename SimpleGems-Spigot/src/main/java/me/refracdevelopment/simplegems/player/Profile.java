package me.refracdevelopment.simplegems.player;

import lombok.Data;
import me.refracdevelopment.simplegems.player.data.ProfileData;

import java.util.UUID;

@Data
public class Profile {

    private ProfileData data;
    private UUID UUID;
    private String playerName;

    public Profile(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
        this.data = new ProfileData(uuid, name);
    }
}
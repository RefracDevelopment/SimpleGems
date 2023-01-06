package me.refracdevelopment.simplegems.data;

import lombok.Getter;
import lombok.Setter;
import me.refracdevelopment.simplegems.SimpleGems;

import java.util.UUID;

@Getter
@Setter
public class Profile {

    private SimpleGems plugin = SimpleGems.getInstance();

    private ProfileData data;
    private UUID UUID;
    private String playerName;

    public Profile(UUID uuid, String name) {
        this.UUID = uuid;
        this.playerName = name;
        this.data = new ProfileData(uuid, name);
    }
}
package me.refracdevelopment.simplegems.hooks;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import lombok.Getter;
import me.refracdevelopment.simplegems.SimpleGems;
import me.refracdevelopment.simplegems.menu.GemShop;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@Getter
public class ItemsAdderListener implements Listener {

    @EventHandler
    public void onItemsAdder(ItemsAdderLoadDataEvent event) {
        SimpleGems.getInstance().setGemShop(new GemShop());
    }
}

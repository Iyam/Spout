package net.glowstone.msg.handler;

import net.glowstone.block.BlockProperties;
import net.glowstone.entity.GlowPlayer;
import net.glowstone.inventory.GlowInventory;
import net.glowstone.inventory.GlowItemStack;
import net.glowstone.item.ItemProperties;
import net.glowstone.msg.QuickBarMessage;
import net.glowstone.net.Session;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class QuickBarMessageHandler extends MessageHandler<QuickBarMessage> {

    @Override
    public void handle(Session session, GlowPlayer player, QuickBarMessage message) {
        if (player.getGameMode() != GameMode.CREATIVE) {
            player.kickPlayer("Now now, don't try that here. Won't work.");
            return;
        }
        GlowInventory inv = player.getInventory();
        int slot = inv.getItemSlot(message.getSlot());

        if (slot < 0 || slot > 8
                || !checkValidId(message.getSlot())) {
            player.onSlotSet(inv, slot, inv.getItem(slot));
        }
        GlowItemStack newItem = new GlowItemStack(message.getId(), message.getAmount(), message.getDamage(), message.getNbtData());
        GlowItemStack currentItem = inv.getItem(slot);

        inv.setItem(slot, newItem);
        if (currentItem != null) {
            player.setItemOnCursor(currentItem);
        } else {
            player.setItemOnCursor(null);
        }
    }

    public boolean checkValidId(int id) {
        return BlockProperties.get(id) == null && ItemProperties.get(id) == null;
    }
    
}
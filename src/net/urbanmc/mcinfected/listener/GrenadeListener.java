package net.urbanmc.mcinfected.listener;

import net.urbanmc.mcinfected.MCInfected;
import net.urbanmc.mcinfected.manager.GameManager;
import net.urbanmc.mcinfected.manager.GamePlayerManager;
import net.urbanmc.mcinfected.manager.PacketManager;
import net.urbanmc.mcinfected.object.GamePlayer;
import net.urbanmc.mcinfected.object.grenade.Grenade;
import net.urbanmc.mcinfected.runnable.ItemThrown;
import net.urbanmc.mcinfected.util.ItemUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class GrenadeListener implements Listener {

    private MCInfected plugin;
    public static List<Grenade> grenadeDuds = new ArrayList<>();

    public GrenadeListener(MCInfected plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onGrenadeThrow(PlayerInteractEvent e) {
        if (!e.getAction().equals(Action.RIGHT_CLICK_AIR) && !e.getAction().equals(Action.RIGHT_CLICK_BLOCK))
            return;

        ItemStack is = e.getItem();

        if (is == null) {
            return;
        }

        if (!Grenade.isGrenade(is)) {
            return;
        }

        if(!GameManager.getInstance().getGameState().equals(GameManager.GameState.RUNNING))
            return;


        GamePlayer p = GamePlayerManager.getInstance().getGamePlayer(e.getPlayer());
        e.setCancelled(true);
        ItemUtil.throwItem(p, is, plugin);
    }




    @EventHandler
    public void onGrenadePickup(PlayerPickupItemEvent e) {
        //System.out.print("Grenade Pickup Called");
        //System.out.print(ChatColor.stripColor(e.getItem().getItemStack().getItemMeta().getDisplayName()));
        //String itemname = ChatColor.stripColor(e.getItem().getItemStack().getItemMeta().getDisplayName());


        if (Grenade.isGrenade(e.getItem().getItemStack())/*itemname.equalsIgnoreCase("Sticky Grenade") || itemname.equalsIgnoreCase("Flash Grenade")*/) {
            grenadeDuds.forEach(Grenade::activate);
            grenadeDuds.forEach(grenade -> {
                grenade.getItem().remove();
            });
            System.out.print("Grenade Removed");
            e.setCancelled(true);
        }


        if(e.getItem().getCustomName() == null) return;

        if(e.getItem().getCustomName().equalsIgnoreCase("ThrowingKnife")) {
            e.getItem().remove();
            ItemStack item = ItemUtil.getItem("tripwire_hook name:Throwing_Knife");
            e.getPlayer().getInventory().addItem(item);
            e.setCancelled(true);
        }
    }


    @EventHandler
    public void onKnifed(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player)) return;

        if(!(e.getDamager().getType().equals(EntityType.ARROW))) return;

        System.out.print(e.getDamager().getCustomName());
        System.out.print(e.isCancelled());

        if(e.getDamager().getCustomName() == null) return;
        if(!e.getDamager().getCustomName().equalsIgnoreCase("ThrowingKnife")) return;

        Projectile proj = (Projectile) e.getDamager();
        System.out.print(proj.getLocation().getY() - e.getEntity().getLocation().getY());
        //Originally the headshot distance was 1.35D but after some testing, I decided it would be a bit more accurate using 1.43D
        int damage = (proj.getLocation().getY() - e.getEntity().getLocation().getY()) > 1.43D ? 20 : 16;
        System.out.print(damage);
        e.setDamage(damage);

        PacketManager.getInstance().sendActionBar((Player) e.getEntity(),"You have been hit with a throwing knife!" , "dark_red");
    }
}

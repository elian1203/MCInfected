package net.urbanmc.mcinfected.object.grenade;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class FragGrenade extends Grenade {

    public FragGrenade(Item item) {
        super(item);
    }

    @Override
    public void activate() {
        List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3, 3);

        for (Entity entity : nearbyEntities) {

            if (entity instanceof Player)
                ((Player) entity).damage(10);

        }

    }
}

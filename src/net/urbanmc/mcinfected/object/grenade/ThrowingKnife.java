package net.urbanmc.mcinfected.object.grenade;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import java.util.List;

public class ThrowingKnife extends Grenade {

    public ThrowingKnife(Item item) {
        super(item);
    }

    @Override
    public void activate() {
        Location loc = getItem().getLocation();
        List<Entity> nearbyEntities = this.getItem().getNearbyEntities(1, 1, 1);

        for (Entity entity : nearbyEntities) {

            if (entity instanceof Player)
                ((Player) entity).damage(16);

        }
    }
}

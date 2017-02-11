package net.urbanmc.mcinfected.object.grenade;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;

import java.util.List;

public class ThrowingKnife extends Grenade {

    public ThrowingKnife(Item item) {
        super(item);
    }

    @Override
    public void activate() {

        List<Entity> nearbyEntities = this.getItem().getNearbyEntities(3, 3, 3);

        for(Entity entity : nearbyEntities) {



        }
    }
}

package dev.emortal.minestom.lazertag.gun;

import dev.emortal.minestom.lazertag.game.LazerTagGame;
import dev.emortal.minestom.lazertag.gun.guns.AssaultRifle;
import dev.emortal.minestom.lazertag.gun.guns.BeeBlaster;
import dev.emortal.minestom.lazertag.gun.guns.LazerMinigun;
import dev.emortal.minestom.lazertag.gun.guns.Shotgun;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public final class GunRegistry {

    private final @NotNull LazerTagGame game;
    private final @NotNull List<Gun> guns;

    private final Map<String, Gun> registry = new HashMap<>();

    public GunRegistry(@NotNull LazerTagGame game) {
        this.game = game;

        this.registerGuns();
        this.guns = List.copyOf(this.registry.values());
    }

    public @Nullable Gun getByName(@NotNull String name) {
        return this.registry.get(name);
    }

    public @NotNull Gun getRandomGun() {
        int totalWeight = 0;
        for (Gun value : this.guns) {
            totalWeight += value.getItemInfo().rarity().getWeight();
        }

        int index = 0;
        int randomIndex = ThreadLocalRandom.current().nextInt(totalWeight + 1);
        while (index < this.guns.size() - 1) {
            randomIndex -= this.guns.get(index).getItemInfo().rarity().getWeight();
            if (randomIndex <= 0) break;
            index++;
        }

        return this.guns.get(index);
    }

    public void register(@NotNull Gun powerUp) {
        String name = powerUp.getName();
        if (this.registry.containsKey(name)) {
            throw new IllegalArgumentException("Power up with name " + name + " already exists!");
        }
        this.registry.put(name, powerUp);
    }

    public void registerGuns() {
        this.register(new AssaultRifle(this.game));
        this.register(new BeeBlaster(this.game));
        this.register(new LazerMinigun(this.game));
        this.register(new Shotgun(this.game));
    }
}

package com.github.technus.tectech.mechanics.enderStorage;

import com.google.common.base.Objects;

import java.awt.*;
import java.io.Serializable;
import java.util.UUID;

public class EnderLinkTag implements Serializable {
    private final Color color;
    private final UUID player;

    public EnderLinkTag(Color color, UUID player) {
        this.color = color;
        this.player = player;
    }

    public int getColorInt() {
        return color.getRGB();
    }

    public UUID getUUID() {
        return player;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EnderLinkTag that = (EnderLinkTag) o;
        return Objects.equal(color, that.color) &&
                Objects.equal(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(color, player);
    }
}

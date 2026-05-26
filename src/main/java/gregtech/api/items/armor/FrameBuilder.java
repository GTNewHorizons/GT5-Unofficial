package gregtech.api.items.armor;

import net.minecraft.item.EnumRarity;

public class FrameBuilder extends ArmorPartBuilder<FrameBuilder> {

    private int protectionSlots;
    private int movementSlots;
    private int utilitySlots;
    private int prismaticSlots;

    private short[] color = { 255, 255, 255, 255 };

    private int protection;
    private EnumRarity rarity = EnumRarity.common;

    public FrameBuilder setSlotCounts(int protectionSlots, int movementSlots, int utilitySlots, int prismaticSlots) {
        onMutated();
        this.protectionSlots = protectionSlots;
        this.movementSlots = movementSlots;
        this.utilitySlots = utilitySlots;
        this.prismaticSlots = prismaticSlots;
        return this;
    }

    public FrameBuilder setColor(short[] color) {
        onMutated();
        this.color = color;
        return this;
    }

    public FrameBuilder setProtection(int protection) {
        onMutated();
        this.protection = protection;
        return this;
    }

    public FrameBuilder setRarity(EnumRarity rarity) {
        onMutated();
        this.rarity = rarity;
        return this;
    }

    public int getProtectionSlots() {
        return protectionSlots;
    }

    public int getMovementSlots() {
        return movementSlots;
    }

    public int getUtilitySlots() {
        return utilitySlots;
    }

    public int getPrismaticSlots() {
        return prismaticSlots;
    }

    public short[] getColor() {
        return color;
    }

    public int getProtection() {
        return protection;
    }

    public EnumRarity getRarity() {
        return rarity;
    }
}

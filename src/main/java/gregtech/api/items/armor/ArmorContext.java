package gregtech.api.items.armor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.items.armor.behaviors.BehaviorName;

/// An armor context wraps several contextual objects into one parameter. It also exposes several utility methods. Its
/// main purpose is to make it trivial to add new fields to the various [IArmorBehavior] methods without changing their
/// signature.
public interface ArmorContext {

    default boolean isRemote() {
        if (getWorld() != null) {
            return getWorld().isRemote;
        } else {
            return FMLCommonHandler.instance()
                .getEffectiveSide()
                .isClient();
        }
    }

    World getWorld();

    EntityPlayer getPlayer();

    default boolean isWornByPlayer() {
        return getPlayer() != null;
    }

    ItemStack getArmorStack();

    ArmorState getArmorState();

    void setArmorState(ArmorState state);

    default boolean drainEnergy(double amount) {
        if (getArmorState().drainEnergy(amount)) {
            save();
            return true;
        } else {
            return false;
        }
    }

    default boolean hasBehavior(BehaviorName name) {
        return getArmorState().hasBehavior(name);
    }

    default boolean isBehaviorActive(BehaviorName name) {
        return getArmorState().isActive(name);
    }

    default boolean toggleBehavior(BehaviorName name) {
        boolean enabled = getArmorState().toggle(this, name);

        save();

        return enabled;
    }

    default void save() {
        ArmorState.save(this);
    }

    class ArmorContextImpl implements ArmorContext {

        public World world;
        public EntityPlayer player;
        public ItemStack armorStack;
        public ArmorState armorState;

        public ArmorContextImpl(World world, EntityPlayer player, ItemStack armorStack, ArmorState armorState) {
            this.world = world;
            this.player = player;
            this.armorStack = armorStack;
            this.armorState = armorState;
        }

        public ArmorContextImpl(EntityPlayer player, ItemStack armorStack, ArmorState armorState) {
            this.world = player.getEntityWorld();
            this.player = player;
            this.armorStack = armorStack;
            this.armorState = armorState;
        }

        public ArmorContextImpl(ItemStack armorStack, ArmorState armorState) {
            this.armorStack = armorStack;
            this.armorState = armorState;
        }

        @Override
        public World getWorld() {
            return world;
        }

        @Override
        public EntityPlayer getPlayer() {
            return player;
        }

        @Override
        public ItemStack getArmorStack() {
            return armorStack;
        }

        @Override
        public ArmorState getArmorState() {
            return armorState;
        }

        @Override
        public void setArmorState(ArmorState armorState) {
            this.armorState = armorState;
        }
    }
}

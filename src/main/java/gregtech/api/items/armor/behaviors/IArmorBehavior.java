package gregtech.api.items.armor.behaviors;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

public interface IArmorBehavior {
    /*
     * Armor behavior system adapted from GTCEU
     */

    /**
     * Used in renderItem to get the texture that will be drawn onto modular armor. Ignore if this behavior is not
     * intended to dynamically change a texture.
     */
    default IIcon getModularArmorTexture() {
        return null;
    }

    /**
     * Called every tick that this behavior's armor item is equipped.
     */
    default void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {}

    /** Called when this behavior's armor item is unequipped. */
    default void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {}

    /**
     * Called when this behavior's armor item is equipped, as well as when the player logs into the world or is cloned.
     */
    default void onArmorEquip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {}

    /** Add to this behavior's armor item tooltip. */
    default void addInformation(@NotNull ItemStack stack, @NotNull List<String> tooltip) {
        if (!getOrCreateNbtCompound(stack).hasKey(getMainNBTTag())) return;
        tooltip.add(StatCollector.translateToLocalFormatted("GT5U.armor.message.installed", getBehaviorName()));
    }

    default void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {}

    default void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {}

    default String getMainNBTTag() {
        return "";
    }

    default String getBehaviorName() {return "";}

    default Set<SyncedKeybind> getListenedKeys() {
        return Collections.emptySet();
    }

    /** Get the equipment slot for this behavior's armor item. Provided since the method call is somewhat obscure. */
    default int getEquipmentSlot(@NotNull ItemStack stack) {
        return EntityLiving.getArmorPosition(stack);
    }
}

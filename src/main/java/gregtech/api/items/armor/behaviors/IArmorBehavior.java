package gregtech.api.items.armor.behaviors;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import ic2.api.item.IElectricItem;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public interface IArmorBehavior {
    /*
     * Armor behavior system adapted from GTCEU
     */


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
    default void addInformation(@NotNull ItemStack stack, @Nullable World world, @NotNull List<String> tooltip) {}

    default void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player) {}

    default void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {}

    default Set<SyncedKeybind> getListenedKeys() {
        return Collections.emptySet();
    }

    /** Get the equipment slot for this behavior's armor item. Provided since the method call is somewhat obscure. */
    default int getEquipmentSlot(@NotNull ItemStack stack) {
        return EntityLiving.getArmorPosition(stack);
    }
}

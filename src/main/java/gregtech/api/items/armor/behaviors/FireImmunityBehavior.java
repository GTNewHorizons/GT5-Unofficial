package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class FireImmunityBehavior implements IArmorBehavior {

    public static FireImmunityBehavior INSTANCE = new FireImmunityBehavior();

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (!getOrCreateNbtCompound(stack).hasKey(ArmorHelper.FIRE_IMMUNITY_KEY)) return;
        player.isImmuneToFire = true;
        if (player.isBurning()) {
            player.extinguish();
        }
    }

    @Override
    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (!getOrCreateNbtCompound(stack).hasKey(ArmorHelper.FIRE_IMMUNITY_KEY)) return;
        player.isImmuneToFire = false;
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.FIRE_IMMUNITY_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.FIRE_IMMUNITY_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.fireimmunity");
    }
}

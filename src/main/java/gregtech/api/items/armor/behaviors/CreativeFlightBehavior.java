package gregtech.api.items.armor.behaviors;

import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorHelper;

public class CreativeFlightBehavior implements IArmorBehavior {

    public static final CreativeFlightBehavior INSTANCE = new CreativeFlightBehavior();

    protected CreativeFlightBehavior() {/**/}

    // TODO: we should have our own electric item wrapper
    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (world.isRemote) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.CREATIVE_FLIGHT_KEY) && player.capabilities.isFlying) {
            // TODO: discharge while flying
            // ElectricItem.manager.discharge(stack, 5, 1, true, true, false);
        }
    }

    @Override
    public void onArmorEquip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.CREATIVE_FLIGHT_KEY)) {
            player.capabilities.allowFlying = true;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.CREATIVE_FLIGHT_KEY) && !player.capabilities.isCreativeMode) {
            player.capabilities.allowFlying = false;
            player.capabilities.isFlying = false;
            player.sendPlayerAbilities();
        }
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.CREATIVE_FLIGHT_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.CREATIVE_FLIGHT_KEY;
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @NotNull List<String> tooltip) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.hasKey(ArmorHelper.CREATIVE_FLIGHT_KEY))
            tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.creativeflight"));
    }
}

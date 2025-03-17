package gregtech.api.items.armor.behaviors;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import gregtech.api.items.armor.ArmorHelper;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static gregtech.api.items.armor.ArmorKeybinds.INERTIA_CANCELING_KEYBIND;
import static gregtech.api.items.armor.ArmorKeybinds.OMNI_MOVEMENT_KEYBIND;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

public class OmniMovementBehavior implements IArmorBehavior {

    public static OmniMovementBehavior INSTANCE = new OmniMovementBehavior();

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.OMNI_MOVEMENT_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.OMNI_MOVEMENT_KEY;
    }

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.OMNI_MOVEMENT_KEY)) return;

        boolean wasEnabled = tag.getBoolean(ArmorHelper.OMNI_MOVEMENT_KEY);
        tag.setBoolean(ArmorHelper.OMNI_MOVEMENT_KEY, !wasEnabled);

        if (wasEnabled) {
            PlayerUtils
                .messagePlayer(player, StatCollector.translateToLocal("GT5U.armor.message.omnimovement.disabled"));
        } else {
            PlayerUtils.messagePlayer(player, StatCollector.translateToLocal("GT5U.armor.message.omnimovement.enabled"));
        }
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return Collections.singleton(OMNI_MOVEMENT_KEYBIND);
    }

    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (world.isRemote) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.OMNI_MOVEMENT_KEY)) {

        }
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @NotNull List<String> tooltip) {
        if (!getOrCreateNbtCompound(stack).hasKey(ArmorHelper.OMNI_MOVEMENT_KEY)) return;
        tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.omnimovement"));
    }
}

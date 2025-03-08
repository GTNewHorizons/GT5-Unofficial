package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.GOGGLES_OF_REVEALING_KEYBIND;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorHelper;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class GogglesOfRevealingBehavior implements IArmorBehavior {

    public static final GogglesOfRevealingBehavior INSTANCE = new GogglesOfRevealingBehavior();

    protected GogglesOfRevealingBehavior() {/**/}

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.GOGGLES_OF_REVEALING_KEY)) return;

        boolean wasEnabled = tag.getBoolean(ArmorHelper.GOGGLES_OF_REVEALING_KEY);
        tag.setBoolean(ArmorHelper.GOGGLES_OF_REVEALING_KEY, !wasEnabled);

        if (wasEnabled) {
            PlayerUtils.messagePlayer(
                player,
                StatCollector.translateToLocal("GT5U.armor.message.gogglesofrevealing.disabled"));
        } else {
            PlayerUtils
                .messagePlayer(player, StatCollector.translateToLocal("GT5U.armor.message.gogglesofrevealing.enabled"));
        }
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return Collections.singleton(GOGGLES_OF_REVEALING_KEYBIND);
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.GOGGLES_OF_REVEALING_KEY, true);
        tag.setInteger(ArmorHelper.VIS_DISCOUNT_KEY, tag.getInteger(ArmorHelper.VIS_DISCOUNT_KEY) + 10);
    }

    @Override
    public void addInformation(@NotNull ItemStack stack, @NotNull List<String> tooltip) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.GOGGLES_OF_REVEALING_KEY)) return;
        if (tag.getBoolean(ArmorHelper.GOGGLES_OF_REVEALING_KEY)) {
            tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.gogglesofrevealing.enabled"));
        } else {
            tooltip.add(StatCollector.translateToLocal("GT5U.armor.message.gogglesofrevealing.disabled"));
        }
    }
}

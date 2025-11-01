package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.FORCE_FIELD_KEYBIND;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;
import static gregtech.loaders.ExtraIcons.forceFieldAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorHelper;
import gregtech.api.util.GTUtility;

public class ForceFieldBehavior implements IArmorBehavior {

    public static ForceFieldBehavior INSTANCE = new ForceFieldBehavior();

    @Override
    public IIcon getModularArmorTexture() {
        return forceFieldAugment;
    }

    @Override
    public void addBehaviorNBT(@NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.FORCE_FIELD_KEY, false);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.FORCE_FIELD_KEY;
    }

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.FORCE_FIELD_KEY)) return;

        boolean wasEnabled = tag.getBoolean(ArmorHelper.FORCE_FIELD_KEY);
        tag.setBoolean(ArmorHelper.FORCE_FIELD_KEY, !wasEnabled);

        if (wasEnabled) {
            GTUtility.sendChatToPlayer(
                player,
                StatCollector.translateToLocalFormatted("GT5U.armor.message.disabled", getBehaviorName()));
        } else {
            GTUtility.sendChatToPlayer(
                player,
                StatCollector.translateToLocalFormatted("GT5U.armor.message.enabled", getBehaviorName()));
        }
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return Collections.singleton(FORCE_FIELD_KEYBIND);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.forcefield");
    }
}

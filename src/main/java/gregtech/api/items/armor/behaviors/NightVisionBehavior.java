package gregtech.api.items.armor.behaviors;

import static gregtech.api.items.armor.ArmorKeybinds.NIGHT_VISION_KEYBIND;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;
import static gregtech.loaders.ExtraIcons.nightVisionAugment;

import java.util.Collections;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.ArmorHelper;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class NightVisionBehavior implements IArmorBehavior {

    public static final NightVisionBehavior INSTANCE = new NightVisionBehavior();

    @Override
    public IIcon getModularArmorTexture() {
        return nightVisionAugment;
    }

    protected NightVisionBehavior() {/**/}

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.NIGHT_VISION_KEY;
    }

    @Override
    public void onKeyPressed(@NotNull ItemStack stack, @NotNull EntityPlayer player, SyncedKeybind keyPressed) {
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (!tag.hasKey(ArmorHelper.NIGHT_VISION_KEY)) return;

        boolean wasEnabled = tag.getBoolean(ArmorHelper.NIGHT_VISION_KEY);
        tag.setBoolean(ArmorHelper.NIGHT_VISION_KEY, !wasEnabled);

        if (wasEnabled) {
            player.removePotionEffect(Potion.nightVision.id);
            PlayerUtils.messagePlayer(
                player,
                StatCollector.translateToLocalFormatted("GT5U.armor.message.disabled", getBehaviorName()));
        } else {
            PlayerUtils.messagePlayer(
                player,
                StatCollector.translateToLocalFormatted("GT5U.armor.message.enabled", getBehaviorName()));
        }
    }

    @Override
    public Set<SyncedKeybind> getListenedKeys() {
        return Collections.singleton(NIGHT_VISION_KEYBIND);
    }

    // TODO: we should have our own electric item wrapper
    @Override
    public void onArmorTick(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        if (world.isRemote) return;
        NBTTagCompound tag = getOrCreateNbtCompound(stack);
        if (tag.getBoolean(ArmorHelper.NIGHT_VISION_KEY)) {
            // ElectricItem.manager.discharge(stack, 5, 1, true, true, false);
            player.removePotionEffect(Potion.blindness.id);
            player.addPotionEffect(new PotionEffect(Potion.nightVision.id, 999999, 0, true));
        }
    }

    @Override
    public void onArmorUnequip(@NotNull World world, @NotNull EntityPlayer player, @NotNull ItemStack stack) {
        player.removePotionEffect(Potion.nightVision.id);
    }

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.NIGHT_VISION_KEY, true);
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.nightvision");
    }
}

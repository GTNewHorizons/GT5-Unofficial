package gregtech.api.items.armor.behaviors;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import gregtech.api.items.armor.ArmorHelper;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.Set;

import static gregtech.api.items.armor.ArmorKeybinds.GOGGLES_OF_REVEALING_KEYBIND;
import static gregtech.api.util.GTUtility.getOrCreateNbtCompound;

public class HazmatBehavior implements IArmorBehavior {

    public static final HazmatBehavior INSTANCE = new HazmatBehavior();

    protected HazmatBehavior() {/**/}

    @Override
    public void addBehaviorNBT(@NotNull ItemStack stack, @NotNull NBTTagCompound tag) {
        tag.setBoolean(ArmorHelper.HAZMAT_PROTECTION_KEY, true);
    }

    @Override
    public String getMainNBTTag() {
        return ArmorHelper.HAZMAT_PROTECTION_KEY;
    }

    @Override
    public String getBehaviorName() {
        return StatCollector.translateToLocal("GT5U.armor.behavior.hazmat");
    }
}

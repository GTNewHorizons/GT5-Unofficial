package gregtech.api.items.armor.behaviors;

import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;
import gregtech.api.util.GTUtility;

public class VisDiscountBehavior implements IArmorBehavior {

    public final int visDiscount;

    public VisDiscountBehavior(int visDiscount) {
        this.visDiscount = visDiscount;
    }

    @Override
    public BehaviorName getName() {
        return BehaviorName.VisDiscount;
    }

    @Override
    public void configureArmorState(@NotNull ArmorContext context, @NotNull NBTTagCompound stackTag) {
        context.getArmorState().visDiscount += visDiscount;
    }

    @Override
    public @NotNull IArmorBehavior merge(@NotNull IArmorBehavior other) {
        if (!(other instanceof VisDiscountBehavior discount)) return this;

        return new VisDiscountBehavior(this.visDiscount + discount.visDiscount);
    }

    @Override
    public void addArmorInformation(@NotNull ArmorContext context, @NotNull List<String> tooltip) {
        if (Thaumcraft.isModLoaded()) {
            tooltip
                .add(EnumChatFormatting.DARK_PURPLE + GTUtility.translate("tc.visdiscount") + ": " + visDiscount + "%");
        }
    }

    @Override
    public void addPartInformation(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        if (Thaumcraft.isModLoaded()) {
            desc.add(EnumChatFormatting.DARK_PURPLE + GTUtility.translate("tc.visdiscount") + ": " + visDiscount + "%");
        }
    }
}

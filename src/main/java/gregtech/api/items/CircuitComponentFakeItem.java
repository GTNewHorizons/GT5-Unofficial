package gregtech.api.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class CircuitComponentFakeItem extends GTGenericItem {

    public static CircuitComponentFakeItem INSTANCE = null;

    public CircuitComponentFakeItem() {
        super("gt.fakecircuitcomponent", "Fake Circuit Component Item", null);
        setMaxDamage(0);
        setHasSubtypes(true);
        INSTANCE = this;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(aStack);
        return component.unlocalizedName;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(stack);
        return component.getLocalizedName();
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        aList.add("Item in the Nanochip Assembly Complex vacuum pipe system");
        super.addInformation(aStack, aPlayer, aList, aF3_H);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        // If the component stores an icon, use that
        CircuitComponent component = CircuitComponent.getFromMetaDataUnsafe(meta);
        if (component.icon != null) return component.icon;
        // Else just use the texture that should be assigned to it
        return super.getIconFromDamage(meta);
    }

}

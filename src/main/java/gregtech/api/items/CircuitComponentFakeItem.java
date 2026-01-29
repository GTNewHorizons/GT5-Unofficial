package gregtech.api.items;

import static gregtech.api.enums.Mods.GregTech;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class CircuitComponentFakeItem extends GTGenericItem {

    public final String TextureLocation = ":gt.circuitcomponent/";
    public static CircuitComponentFakeItem INSTANCE = null;
    private final Map<Integer, IIcon> iconMap = new HashMap<>();

    public CircuitComponentFakeItem() {
        super("gt.fakecircuitcomponent", "Fake Circuit Component Item", null);
        setMaxStackSize(Integer.MAX_VALUE);
        setMaxDamage(0);
        setHasSubtypes(true);
        INSTANCE = this;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(aStack);
        return component.nameKey;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(stack);
        if (!component.isProcessed) return translateToLocalFormatted(
            "gt.circuitcomponent.base",
            component.realComponent.get()
                .getDisplayName());
        return component.getLocalizedName();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> textList, boolean advanced) {
        CircuitComponent component = CircuitComponent.getFromFakeStackUnsafe(stack);
        textList.add(translateToLocal("gt.circuitcomponent.tooltip.base"));
        if (!component.isProcessed) textList.add(translateToLocal("gt.circuitcomponent.tooltip.cc.base"));
        else textList.add(translateToLocal("gt.circuitcomponent.tooltip.pc.base"));
        super.addInformation(stack, player, textList, advanced);
    }

    @Override
    public IIcon getIconFromDamage(int meta) {
        IIcon icon = iconMap.get(meta);
        if (icon != null) return icon;
        else return iconMap.get(-1);
        /*
         * // If the component stores an icon, use that
         * CircuitComponent component = CircuitComponent.getFromMetaDataUnsafe(meta);
         * if (component.hasIcon()) return component.getIcon();
         * // Else just use the texture that should be assigned to it
         * return super.getIconFromDamage(meta);
         */
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        for (CircuitComponent component : CircuitComponent.VALUES) {
            if (component.iconString != null) iconMap
                .put(component.metaId, iconRegister.registerIcon(GregTech.ID + TextureLocation + component.iconString));
        }
        iconMap.put(-1, iconRegister.registerIcon(GregTech.ID + TextureLocation + "circuitcomponent_default"));
    }
}

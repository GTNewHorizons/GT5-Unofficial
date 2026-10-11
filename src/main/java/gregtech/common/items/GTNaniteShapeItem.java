package gregtech.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.ruling_0.materiallib.api.ShapeItem;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.NaniteTier;

/// The `nanite` MaterialLib shape. Adds the stack's [NaniteTier] to its tooltip.
public class GTNaniteShapeItem extends ShapeItem {

    public GTNaniteShapeItem(String modid, String name, String... oreDicts) {
        super(modid, name, oreDicts);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> lines, boolean advanced) {
        super.addInformation(stack, player, lines, advanced);
        NaniteTier tier = NaniteTier.fromStack(stack);
        if (tier != null) {
            lines.add(StatCollector.translateToLocalFormatted("gt.tooltip.nanite-tier", tier.tier));
        }
    }
}

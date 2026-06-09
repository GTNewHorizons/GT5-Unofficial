package gregtech.common.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 * This class is for registration. For use inside MTE's, use {@link gregtech.api.casing.Casings#asElement()}
 * Make sure to also register each new Casing inside of {@link gregtech.api.casing.Casings}
 */
public class BlockCasings14 extends BlockCasingsAbstract {

    public BlockCasings14() {
        super(ItemCasings.class, "gt.blockcasings14", MaterialCasings.INSTANCE, 16);
        // IDs 0 - 2 are currently reserved for the CGC
        register(3, ItemList.CasingHearth);
        register(4, ItemList.CasingFridge);
        register(5, ItemList.CasingNaquadahReinforcedDistillation);
        register(6, ItemList.Casing_Vat_T1);
        register(7, ItemList.Casing_Vat_T2);
        register(8, ItemList.Casing_Vat_T3);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 3 -> {
                if (ordinalSide == 0) yield Textures.BlockIcons.MACHINE_CASING_HEARTH_BOTTOM.getIcon();
                if (ordinalSide == 1) yield Textures.BlockIcons.MACHINE_CASING_HEARTH_TOP.getIcon();
                yield Textures.BlockIcons.MACHINE_CASING_HEARTH_SIDE.getIcon();
            }
            case 4 -> {
                if (ordinalSide == 0) yield Textures.BlockIcons.MACHINE_CASING_FRIDGE_BOTTOM.getIcon();
                if (ordinalSide == 1) yield Textures.BlockIcons.MACHINE_CASING_FRIDGE_TOP.getIcon();
                yield Textures.BlockIcons.MACHINE_CASING_FRIDGE_SIDE.getIcon();
            }
            case 5 -> Textures.BlockIcons.MACHINE_CASING_NAQUADAH_REINFORCED_DISTILLATION.getIcon();
            case 6 -> Textures.BlockIcons.STERILE_VAT_CASING_T1.getIcon();
            case 7 -> Textures.BlockIcons.STERILE_VAT_CASING_T2.getIcon();
            case 8 -> Textures.BlockIcons.STERILE_VAT_CASING_T3.getIcon();
            default -> Textures.BlockIcons.MACHINE_CASING_ROBUST_TUNGSTENSTEEL.getIcon();
        };
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> tooltip, boolean advancedTooltips) {
        super.addInformation(stack, player, tooltip, advancedTooltips);

        switch (stack.getItemDamage()) {
            case 6 -> tooltip.add(StatCollector.translateToLocal("gt.casing.hazard-shielded"));
            case 7 -> tooltip.add(StatCollector.translateToLocal("gt.casing.dynamic-bio-regulation"));
            case 8 -> tooltip.add(StatCollector.translateToLocal("gt.casing.symbiotic-vat"));
        }
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 112);
    }
}

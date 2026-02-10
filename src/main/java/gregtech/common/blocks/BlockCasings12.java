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
import gregtech.common.misc.GTStructureChannels;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class BlockCasings12 extends BlockCasingsAbstract {

    public BlockCasings12() {
        super(ItemCasings.class, "gt.blockcasings12", MaterialCasings.INSTANCE, 16);

        register(0, ItemList.CokeOvenCasing, "Coke Oven Bricks");
        register(6, ItemList.Casing_Vat_T1, "Hazard-Shielded Casing");
        register(7, ItemList.Casing_Vat_T2, "Dynamic Bio-Regulation Casing");
        register(8, ItemList.Casing_Vat_T3, "Symbiotic Vat Casing");

        register(9, ItemList.Spinmatron_Casing, "Vibration-Safe Casing");
        register(10, ItemList.CasingThaumium, "Alchemically Resistant Thaumium Casing");
        register(11, ItemList.CasingVoid, "Alchemically Inert Void Casing");
        register(12, ItemList.CasingIchorium, "Alchemically Immune Ichorium Casing");
        for (int i = 0; i < 3; i++) {
            GTStructureChannels.METAL_MACHINE_CASING.registerAsIndicator(new ItemStack(this, 1, i + 10), i + 1);
        }
    }

    @Override
    public String getHarvestTool(int aMeta) {
        // Coke Oven Bricks can be harvested with a pickaxe.
        if (aMeta == 0) return "pickaxe";
        return super.getHarvestTool(aMeta);
    }

    @Override
    public int getHarvestLevel(int aMeta) {
        // Coke Oven Bricks have Harvest Level 0.
        if (aMeta == 0) return 0;
        return super.getHarvestLevel(aMeta);
    }

    @Override
    public int getTextureIndex(int aMeta) {
        return (16 << 7) | (aMeta + 80);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int aMeta) {
        return switch (aMeta) {
            case 0 -> Textures.BlockIcons.COKE_OVEN_CASING.getIcon();
            case 6 -> Textures.BlockIcons.STERILE_VAT_CASING_T1.getIcon();
            case 7 -> Textures.BlockIcons.STERILE_VAT_CASING_T2.getIcon();
            case 8 -> Textures.BlockIcons.STERILE_VAT_CASING_T3.getIcon();
            case 9 -> Textures.BlockIcons.SPINMATRON_CASING.getIcon();
            case 10 -> Textures.BlockIcons.MACHINE_CASING_THAUMIUM.getIcon();
            case 11 -> Textures.BlockIcons.MACHINE_CASING_VOID.getIcon();
            case 12 -> Textures.BlockIcons.MACHINE_CASING_ICHORIUM.getIcon();
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
}

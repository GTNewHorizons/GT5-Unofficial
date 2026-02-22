package bartworks.system.material;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.common.config.Client;

public class BWItemMetaGeneratedOre extends ItemBlock {

    public final BWMetaGeneratedOres blockOre;

    public BWItemMetaGeneratedOre(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);

        blockOre = (BWMetaGeneratedOres) block;
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    private OrePrefixes getOrePrefix() {
        return blockOre.isSmall ? OrePrefixes.oreSmall : OrePrefixes.ore;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "bw.blocktype." + getOrePrefix().getName();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        int meta = stack.getItemDamage();

        Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) meta);

        if (werkstoff == null) {
            return blockOre.blockTypeLocalizedName.replace("%material", "Empty");
        }

        return blockOre.blockTypeLocalizedName.replace("%material", werkstoff.getLocalizedName());
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> desc, boolean advancedTooltips) {
        if (Client.tooltip.showFormula) {
            int meta = stack.getItemDamage();

            Werkstoff werkstoff = Werkstoff.werkstoffHashMap.get((short) meta);

            if (werkstoff != null) {
                String tooltip = werkstoff.getLocalizedToolTip();
                if (!tooltip.isEmpty()) {
                    desc.add(tooltip);
                }
            }
        }
    }
}

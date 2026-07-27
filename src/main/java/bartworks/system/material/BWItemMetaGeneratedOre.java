package bartworks.system.material;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2WerkstoffIndex;
import gregtech.api.material.MU;
import gregtech.api.material.MaterialFormulas;
import gregtech.api.util.GTUtility;
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

        Material material = Materials2WerkstoffIndex.get(meta);

        if (material == null) {
            return blockOre.getPrefix()
                .getLocalizedNameForItem(Materials._NULL.getInternalName());
        }

        return blockOre.getPrefix()
            .getLocalizedNameForItem(MU.internalName(material));
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> desc, boolean advancedTooltips) {
        if (!Client.tooltip.showFormula) {
            return;
        }
        int meta = stack.getItemDamage();

        Material material = Materials2WerkstoffIndex.get(meta);

        if (material != null) {
            String formula = MaterialFormulas.forSearch(material);
            if (GTUtility.isStringValid(formula)) desc.add(formula);
        }
    }
}

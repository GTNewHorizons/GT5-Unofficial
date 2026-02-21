package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.Optional;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTDataUtils;
import gregtech.common.config.Client;
import mods.railcraft.common.items.firestone.IItemFirestoneBurning;

@Optional.Interface(
    iface = "mods.railcraft.common.items.firestone.IItemFirestoneBurning",
    modid = Mods.ModIDs.RAILCRAFT)
public class GTItemOre extends ItemBlock implements IItemFirestoneBurning {

    public final GTBlockOre blockOre;

    public GTItemOre(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTechAPI.TAB_GREGTECH_MATERIALS);

        blockOre = (GTBlockOre) block;
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        int metadata = stack.getItemDamage();

        int matId = blockOre.getMaterialIndex(metadata);
        boolean small = blockOre.isSmallOre(metadata);

        return this.field_150939_a.getUnlocalizedName() + "."
            + (matId + (small ? GTBlockOre.SMALL_ORE_META_OFFSET : 0));
    }

    @Override
    public int getMetadata(int meta) {
        return meta;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        Materials mat = GTDataUtils
            .getIndexSafe(GregTechAPI.sGeneratedMaterials, blockOre.getMaterialIndex(stack.getItemDamage()));

        if (mat == null) mat = Materials._NULL;

        boolean small = blockOre.isSmallOre(stack.getItemDamage());

        return (small ? OrePrefixes.oreSmall : OrePrefixes.ore).getDefaultLocalNameForItem(mat);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> desc, boolean advancedTooltips) {
        if (Client.tooltip.showFormula) {
            Materials mat = GTDataUtils
                .getIndexSafe(GregTechAPI.sGeneratedMaterials, blockOre.getMaterialIndex(stack.getItemDamage()));

            if (mat == null) mat = Materials._NULL;

            if (mat.mChemicalFormula != null && !mat.mChemicalFormula.isEmpty()) {
                desc.add(mat.mChemicalFormula);
            }
        }
    }

    @Override
    @Optional.Method(modid = Mods.ModIDs.RAILCRAFT)
    public boolean shouldBurn(ItemStack itemStack) {
        int metadata = itemStack.getItemDamage();
        int matId = blockOre.getMaterialIndex(metadata);

        return matId == Materials.Firestone.mMetaItemSubID;
    }
}

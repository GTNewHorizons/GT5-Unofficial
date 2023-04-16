package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.apache.commons.lang3.StringUtils;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;

public class GT_Item_Ores extends ItemBlock {

    public GT_Item_Ores(Block block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        return this.field_150939_a.getUnlocalizedName() + "." + getDamage(aStack);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String aName = super.getItemStackDisplayName(aStack);
        if (this.field_150939_a instanceof GT_Block_Ores_Abstract) {
            aName = Materials.getLocalizedNameForItem(aName, aStack.getItemDamage() % 1000);
        }
        return aName;
    }

    @Override
    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int ordinalSide, float hitX, float hitY, float hitZ, int aMeta) {
        short tDamage = (short) getDamage(aStack);
        if (tDamage > 0) {
            if (!aWorld.setBlock(
                aX,
                aY,
                aZ,
                this.field_150939_a,
                GT_TileEntity_Ores.getHarvestData(
                    tDamage,
                    ((GT_Block_Ores_Abstract) field_150939_a).getBaseBlockHarvestLevel(aMeta % 16000 / 1000)),
                3)) {
                return false;
            }
            GT_TileEntity_Ores tTileEntity = (GT_TileEntity_Ores) aWorld.getTileEntity(aX, aY, aZ);
            tTileEntity.mMetaData = tDamage;
            tTileEntity.mNatural = false;
        } else if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, 0, 3)) {
            return false;
        }
        if (aWorld.getBlock(aX, aY, aZ) == this.field_150939_a) {
            this.field_150939_a.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
            this.field_150939_a.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
        }
        return true;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        String formula = StatCollector
            .translateToLocal(field_150939_a.getUnlocalizedName() + '.' + getDamage(aStack) + ".tooltip");
        if (!StringUtils.isBlank(formula)) aList.add(formula);
    }
}

package gregtech.common.blocks;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ExtendedMaterials;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class GT_Item_OresExtended extends ItemBlock {
    public GT_Item_OresExtended(Block par1) {
        super(par1);
        setMaxDamage(0);
        setHasSubtypes(true);
        setCreativeTab(GregTech_API.TAB_GREGTECH_MATERIALS);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
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
    		aName = ExtendedMaterials.getLocalizedNameForItem(aName, aStack.getItemDamage());
    	}
    	return aName;
    }

    @Override
    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta) {
        short tDamage = (short) getDamage(aStack);
        if (tDamage > 0) {
            if (!aWorld.setBlock(aX, aY, aZ, this.field_150939_a, GT_TileEntity_OresExtended.getHarvestData(tDamage, ((GT_Block_Ores_Abstract) field_150939_a).getBaseBlockHarvestLevel(aMeta % 16000 / 1000)), 3)) {
                return false;
            }
            GT_TileEntity_OresExtended tTileEntity = (GT_TileEntity_OresExtended) aWorld.getTileEntity(aX, aY, aZ);
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
}

package gtPlusPlus.core.item.general.matterManipulator;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockAnalyzer {
    
    static interface BlockAction {
        boolean apply(World world, int x, int y, int z, EntityPlayer player);

        ItemStack[] getRequiredItems();

        default double getEUMultiplier() {
            return 1.0;
        }
    }

    public static BlockAction[] getActions(World world, int x, int y, int z, EntityPlayer player) {
        TileEntity te = world.getTileEntity(x, y, z);

        if(te == null) {
            return null;
        }

        if (te instanceof IGregTechTileEntity gte) {
            IMetaTileEntity imte = gte.getMetaTileEntity();

            if (imte instanceof MetaTileEntity mte) {
                
            } else if (imte instanceof MetaPipeEntity mpe) {

            }
        }

        return null;
    }

}

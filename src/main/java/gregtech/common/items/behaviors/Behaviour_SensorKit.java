package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;

public class Behaviour_SensorKit extends Behaviour_None {

    private final String mTooltip = GT_LanguageManager.addStringLocalization(
        "gt.behaviour.sensorkit.tooltip",
        "Used to display Information using the Mod Nuclear Control");

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if ((aPlayer instanceof EntityPlayerMP)) {
            TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
            if (((tTileEntity instanceof IInventory)) && (!((IInventory) tTileEntity).isUseableByPlayer(aPlayer))) {
                return false;
            }
            if (((tTileEntity instanceof IGregTechDeviceInformation))
                && (((IGregTechDeviceInformation) tTileEntity).isGivingInformation())) {
                GT_Utility.setStack(aStack, ItemList.NC_SensorCard.get(aStack.stackSize));
                NBTTagCompound tNBT = aStack.getTagCompound();
                if (tNBT == null) {
                    tNBT = new NBTTagCompound();
                }
                tNBT.setInteger("x", aX);
                tNBT.setInteger("y", aY);
                tNBT.setInteger("z", aZ);
                aStack.setTagCompound(tNBT);
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}

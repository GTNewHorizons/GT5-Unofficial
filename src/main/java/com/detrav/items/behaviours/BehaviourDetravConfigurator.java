package com.detrav.items.behaviours;

import com.detrav.enums.DetravItemList;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.common.items.behaviors.Behaviour_None;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Created by Detrav on 29.04.2017.
 */
public class BehaviourDetravConfigurator  extends Behaviour_None {
    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {

        InventoryPlayer inv = aPlayer.inventory;
        if (inv != null) {
            for (int i = 0; i < inv.mainInventory.length; i++) {
                if (inv.mainInventory[i].getUnlocalizedName().startsWith("gt.integrated_circuit")
                        && inv.mainInventory[i].stackSize == 1) {

                    int circuit_config = inv.mainInventory[i].getItemDamage();
                    if(circuit_config > 5)
                        circuit_config = 5;
                    circuit_config *= 2;
                    circuit_config++;
                    inv.mainInventory[i] = DetravItemList.ConfiguredCircuit.get(1);
                    ItemStack aCircuit = inv.mainInventory[i];

                    //in here if circuit is empty set data to chunk


                    NBTTagCompound aNBT = aCircuit.getTagCompound();
                    if (aNBT == null) {
                        aNBT = new NBTTagCompound();
                        NBTTagCompound detravPosition = new NBTTagCompound();
                        aNBT.setTag("DetravPosition", detravPosition);
                        aCircuit.setTagCompound(aNBT);
                    }

                    NBTTagCompound detravPosition = aNBT.getCompoundTag("DetravPosition");
                    if (detravPosition == null) {
                        detravPosition = new NBTTagCompound();
                        aNBT.setTag("DetravPosition", detravPosition);
                    }


                    int x_from = ((((int) aPlayer.posX) >> 4) - circuit_config + 1) * 16;
                    int x_to = ((((int) aPlayer.posX) >> 4) + circuit_config) * 16;
                    int x_current = x_from;
                    int z_from = ((((int) aPlayer.posZ) >> 4) - circuit_config + 1) * 16;
                    int z_to = ((((int) aPlayer.posZ) >> 4) + circuit_config) * 16;
                    int z_current = z_from;


                    int y_from = (int) aPlayer.posY + 1;
                    int y_to = (int) aPlayer.posY - 10;

                    detravPosition.setInteger("XFrom", x_from);
                    detravPosition.setInteger("XTo", x_to);
                    detravPosition.setInteger("XCurrent", x_current);
                    detravPosition.setInteger("ZFrom", z_from);
                    detravPosition.setInteger("ZTo", z_to);
                    detravPosition.setInteger("ZCurrent", z_current);
                    detravPosition.setInteger("YFrom", y_from);
                    detravPosition.setInteger("YTo", y_to);
                    return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);
                }
            }

        }
        return super.onItemRightClick(aItem, aStack, aWorld, aPlayer);

    }
}

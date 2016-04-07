package com.detrav.proxies;

import com.detrav.DetravScannerMod;
import com.detrav.enums.DetravSimpleItems;
import com.detrav.gui.DetravGuiProPick;
import com.detrav.gui.containers.DetravPortableChargerContainer;
import com.detrav.gui.DetravPortableChargerGui;
import cpw.mods.fml.common.network.IGuiHandler;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class CommonProxy implements IGuiHandler {

    public void onLoad() {

    }

    public void onPostLoad() {
        long tBits = GT_ModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS | GT_ModHandler.RecipeBits.BUFFERED
                | GT_ModHandler.RecipeBits.ONLY_ADD_IF_RESULT_IS_NOT_NULL | GT_ModHandler.RecipeBits.NOT_REMOVABLE;
        for (Materials aMaterial : Materials.VALUES) {
            if ((aMaterial.mUnificatable) && (aMaterial.mMaterialInto == aMaterial)) {
                if (!aMaterial.contains(SubTag.NO_SMASHING)) {
                    GT_ModHandler.addCraftingRecipe(GT_OreDictUnificator.get(DetravSimpleItems.toolHeadProPick.get(aMaterial), null, 1L), tBits, new Object[]{"PI", "fh",
                            Character.valueOf('P'), OrePrefixes.plate.get(aMaterial), Character.valueOf('I'), OrePrefixes.ingot.get(aMaterial)});
                }
            }
        }
    }

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravGuiProPick.GUI_ID:
                return null;
            case DetravPortableChargerGui.GUI_ID:
                return new DetravPortableChargerContainer(player.inventory,world);
            default:
                return null;
        }
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        switch (ID) {
            case DetravGuiProPick.GUI_ID:
                return new DetravGuiProPick();
            case DetravPortableChargerGui.GUI_ID:
                return new DetravPortableChargerGui(player.inventory,world);
            default:
                return null;
        }
    }

    public void openProPickGui()
    {

    }

    public void openPortableChargerGui(EntityPlayer player)
    {
        player.openGui(DetravScannerMod.instance, DetravPortableChargerGui.GUI_ID,player.worldObj,(int)player.posX,(int)player.posY,(int)player.posZ);
    }
}

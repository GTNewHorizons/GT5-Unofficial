package com.github.technus.tectech.thing.item;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.elementalMatter.classes.cElementalInstanceStackTree;
import com.github.technus.tectech.elementalMatter.classes.tElementalException;
import com.github.technus.tectech.elementalMatter.commonValues;
import com.github.technus.tectech.elementalMatter.interfaces.iElementalInstanceContainer;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import java.util.Collections;
import java.util.List;

import static com.github.technus.tectech.auxiliary.Reference.MODID;

/**
 * Created by Tec on 15.03.2017.
 */
public class debug_container_EM extends Item {
    public static debug_container_EM INSTANCE;

    debug_container_EM(){
        super();
        setMaxStackSize(1);
        setUnlocalizedName("em.debugContainer");
        setTextureName(MODID + ":itemDebugContainer");
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT;
        //if(aStack.getTagCompound()==null){
        //    aStack.setTagCompound(new NBTTagCompound());
        //}
        tNBT = aStack.getTagCompound();
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity != null && tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE != null && metaTE instanceof iElementalInstanceContainer) {
                    cElementalInstanceStackTree content = ((iElementalInstanceContainer) metaTE).getContainerHandler();
                    if (tNBT.hasKey("content")) {
                        try {
                            content.putUnifyAll(cElementalInstanceStackTree.fromNBT(tNBT.getCompoundTag("content")));
                        } catch (tElementalException e) {
                            if (TecTech.ModConfig.DEBUG_MODE) e.printStackTrace();
                            return true;
                        }
                        ((iElementalInstanceContainer) metaTE).purgeOverflow();
                        tNBT.removeTag("content");
                        tNBT.removeTag("info");
                    } else if (content.hasStacks()) {
                        ((iElementalInstanceContainer) metaTE).purgeOverflow();
                        tNBT.setTag("info", content.getInfoNBT());
                        tNBT.setTag("content", content.toNBT());
                        content.clear();
                    }
                }
                return true;
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List aList, boolean boo) {
        aList.add(commonValues.tecMark);
        aList.add("Container for elemental matter");
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT!=null && tNBT.hasKey("info")) {
                aList.add("Contains:");
                Collections.addAll(aList, cElementalInstanceStackTree.infoFromNBT(tNBT.getCompoundTag("info")));
            }
        } catch (Exception e) {
            aList.add("---Unexpected Termination---");
        }
    }

    public static void run(){
        INSTANCE=new debug_container_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list) {
        ItemStack that=new ItemStack(this,1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
    }
}

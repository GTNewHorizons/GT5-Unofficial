package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.creativeTabTecTech;
import static com.github.technus.tectech.thing.CustomItemList.parametrizerMemory;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Param;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_ParamText;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by Tec on 15.03.2017.
 */
public final class ParametrizerMemoryCard extends Item {

    public static ParametrizerMemoryCard INSTANCE;
    private static IIcon locked, unlocked;

    private ParametrizerMemoryCard() {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName("em.parametrizerMemoryCard");
        setTextureName(MODID + ":itemParametrizerMemoryCardUnlocked");
        setCreativeTab(creativeTabTecTech);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
            int aSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE != null) {
                    if (metaTE instanceof GT_MetaTileEntity_Hatch_Param) {
                        GT_MetaTileEntity_Hatch_Param parametrizer = (GT_MetaTileEntity_Hatch_Param) metaTE;
                        if (aStack.getTagCompound() == null) {
                            aStack.setTagCompound(new NBTTagCompound());
                        }
                        NBTTagCompound tNBT = aStack.getTagCompound();
                        if (aStack.getItemDamage() == 1) {
                            // write to parametrizer
                            parametrizer.param = tNBT.getInteger("param");
                            parametrizer.value0D = tNBT.getDouble("value0D");
                            parametrizer.value1D = tNBT.getDouble("value1D");
                        } else {
                            // read from parametrizer
                            tNBT.setInteger("param", parametrizer.param);
                            tNBT.setDouble("value0D", parametrizer.value0D);
                            tNBT.setDouble("value1D", parametrizer.value1D);
                            tNBT.removeTag("value0s");
                            tNBT.removeTag("value1s");
                        }
                        return true;
                    } else if (metaTE instanceof GT_MetaTileEntity_MultiblockBase_EM) {
                        GT_MetaTileEntity_MultiblockBase_EM base = (GT_MetaTileEntity_MultiblockBase_EM) metaTE;
                        if (aStack.getTagCompound() == null) {
                            aStack.setTagCompound(new NBTTagCompound());
                        }
                        NBTTagCompound tNBT = aStack.getTagCompound();
                        if (aStack.getItemDamage() == 1) {
                            base.parametrization.trySetParameters(
                                    tNBT.getInteger("param"),
                                    tNBT.getDouble("value0D"),
                                    tNBT.getDouble("value1D"));
                            return true;
                        }
                    } else if (metaTE instanceof GT_MetaTileEntity_Hatch_ParamText) {
                        GT_MetaTileEntity_Hatch_ParamText parametrizer = (GT_MetaTileEntity_Hatch_ParamText) metaTE;
                        if (aStack.getTagCompound() == null) {
                            aStack.setTagCompound(new NBTTagCompound());
                        }
                        NBTTagCompound tNBT = aStack.getTagCompound();
                        if (aStack.getItemDamage() == 1) {
                            // write to parametrizer
                            parametrizer.param = tNBT.getInteger("param");
                            parametrizer.value0D = tNBT.getDouble("value0D");
                            parametrizer.value1D = tNBT.getDouble("value1D");
                            parametrizer.value0s = tNBT.getString("value0s");
                            parametrizer.value1s = tNBT.getString("value1s");
                        } else {
                            // read from parametrizer
                            tNBT.setInteger("param", parametrizer.param);
                            tNBT.setDouble("value0D", parametrizer.value0D);
                            tNBT.setDouble("value1D", parametrizer.value1D);
                            tNBT.setString("value0s", parametrizer.value0s);
                            tNBT.setString("value1s", parametrizer.value1s);
                        }
                        return true;
                    }
                }
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aPlayer instanceof EntityPlayerMP && aPlayer.isSneaking()) {
            aStack.stackSize = 1;
            if (aStack.getItemDamage() == 1) {
                aStack.setItemDamage(0);
            } else {
                aStack.setItemDamage(1);
            }
            return aStack;
        }
        return aStack;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.em.parametrizerMemoryCard.desc.0")); // Stores Parameters

        if (aStack.getItemDamage() == 1) {
            aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.parametrizerMemoryCard.desc.1")); // Use on
                                                                                                            // Parametrizer/Controller
                                                                                                            // to
                                                                                                            // configure
                                                                                                            // it
        } else {
            aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.parametrizerMemoryCard.desc.2")); // Use on
                                                                                                            // Parametrizer
                                                                                                            // to store
                                                                                                            // parameters
        }
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.parametrizerMemoryCard.desc.3")); // Sneak right
                                                                                                        // click to
                                                                                                        // lock/unlock

        double temp;
        if (tNBT != null && tNBT.hasKey("param")) {
            aList.add("Hatch ID: " + EnumChatFormatting.AQUA + tNBT.getInteger("param"));
            temp = tNBT.getInteger("value0D");
            aList.add("Value 0D: " + EnumChatFormatting.AQUA + temp);
            aList.add(
                    "Value 0B: " + EnumChatFormatting.AQUA
                            + TT_Utility.longBitsToShortString(Double.doubleToLongBits(temp)));
            aList.add("Value 0s: " + EnumChatFormatting.AQUA + tNBT.getString("value0s"));
            temp = tNBT.getInteger("value1D");
            aList.add("Value 1D: " + EnumChatFormatting.AQUA + temp);
            aList.add(
                    "Value 1B: " + EnumChatFormatting.AQUA
                            + TT_Utility.longBitsToShortString(Double.doubleToLongBits(temp)));
            aList.add("Value 1s: " + EnumChatFormatting.AQUA + tNBT.getString("value1s"));
        }
    }

    public static void run() {
        INSTANCE = new ParametrizerMemoryCard();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        parametrizerMemory.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        locked = iconRegister.registerIcon(MODID + ":itemParametrizerMemoryCardLocked");
        unlocked = itemIcon = iconRegister.registerIcon(getIconString());
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage == 1) {
            return locked;
        }
        return unlocked;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
    }
}

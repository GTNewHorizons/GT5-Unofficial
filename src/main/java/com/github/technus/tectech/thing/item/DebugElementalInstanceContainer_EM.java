package com.github.technus.tectech.thing.item;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.TecTech.creativeTabEM;
import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_1k;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_ITEM;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
import static cpw.mods.fml.relauncher.Side.CLIENT;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.font.TecTechFontRender;
import com.github.technus.tectech.mechanics.elementalMatter.core.EMException;
import com.github.technus.tectech.mechanics.elementalMatter.core.IEMContainer;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.IEMDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.thing.item.renderElemental.IElementalItem;
import com.github.technus.tectech.util.CommonValues;
import com.github.technus.tectech.util.TT_Utility;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by Tec on 15.03.2017.
 */
public final class DebugElementalInstanceContainer_EM extends Item implements IElementalItem {

    public static DebugElementalInstanceContainer_EM INSTANCE;

    private DebugElementalInstanceContainer_EM() {
        setMaxStackSize(1);
        setUnlocalizedName("em.debugContainer");
        setTextureName(MODID + ":itemDebugContainer");
        setCreativeTab(creativeTabEM);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
            int aSide, float hitX, float hitY, float hitZ) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (aPlayer instanceof EntityPlayerMP) {
            aStack.stackSize = 1;
            if (tTileEntity instanceof IGregTechTileEntity) {
                IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();
                if (metaTE instanceof IEMContainer) {
                    EMInstanceStackMap content = ((IEMContainer) metaTE).getContentHandler();
                    if (tNBT.hasKey("content")) {
                        try {
                            content.putUnifyAll(
                                    EMInstanceStackMap
                                            .fromNBT(TecTech.definitionsRegistry, tNBT.getCompoundTag("content")));
                        } catch (EMException e) {
                            if (DEBUG_MODE) {
                                e.printStackTrace();
                            }
                            return true;
                        }
                        ((IEMContainer) metaTE).purgeOverflow();
                        tNBT.removeTag("content");
                        tNBT.removeTag("symbols");
                    } else if (content.hasStacks()) {
                        ((IEMContainer) metaTE).purgeOverflow();
                        tNBT.setTag("content", content.toNBT(TecTech.definitionsRegistry));
                        tNBT.setTag("symbols", TT_Utility.packStrings(content.getShortSymbolsInfo()));
                        content.clear();
                    }
                    return true;
                }
            }
        }
        return aPlayer instanceof EntityPlayerMP;
    }

    public ItemStack setContent(ItemStack aStack, EMInstanceStackMap content) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setTag("content", content.toNBT(TecTech.definitionsRegistry));
        tNBT.setTag("symbols", TT_Utility.packStrings(content.getShortSymbolsInfo()));
        return aStack;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        aList.add(CommonValues.TEC_MARK_EM);
        try {
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (tNBT != null && tNBT.hasKey("content")) {
                aList.add(translateToLocal("item.em.debugContainer.desc.0") + ": "); // Contains
                EMInstanceStackMap content = EMInstanceStackMap
                        .fromNBT(TecTech.definitionsRegistry, tNBT.getCompoundTag("content"));
                Collections.addAll(aList, content.getElementalInfo());
            } else {
                aList.add(translateToLocal("item.em.debugContainer.desc.1")); // Container for elemental matter
            }
            aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.debugContainer.desc.2")); // Right click on
                                                                                                    // elemental hatches
        } catch (Exception e) {
            if (DEBUG_MODE) {
                e.printStackTrace();
            }
            aList.add(translateToLocal("item.em.debugContainer.desc.3")); // ---Unexpected Termination---
        }
    }

    public static void run() {
        INSTANCE = new DebugElementalInstanceContainer_EM();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
        for (IEMDefinition definition : TecTech.definitionsRegistry.getStacksRegisteredForDisplay()) {
            list.add(
                    setContent(
                            new ItemStack(this).setStackDisplayName(
                                    definition.getLocalizedName() + " "
                                            + 1
                                            + " "
                                            + translateToLocal("tt.keyword.unit.mbMols")),
                            new EMInstanceStackMap(new EMInstanceStack(definition, EM_COUNT_PER_MATERIAL_AMOUNT))));
            list.add(
                    setContent(
                            new ItemStack(this).setStackDisplayName(
                                    definition.getLocalizedName() + " "
                                            + 1
                                            + " "
                                            + translateToLocal("tt.keyword.unit.itemMols")),
                            new EMInstanceStackMap(new EMInstanceStack(definition, EM_COUNT_PER_ITEM))));
            list.add(
                    setContent(
                            new ItemStack(this).setStackDisplayName(
                                    definition.getLocalizedName() + " "
                                            + 1000
                                            + " "
                                            + translateToLocal("tt.keyword.unit.mbMols")),
                            new EMInstanceStackMap(new EMInstanceStack(definition, EM_COUNT_PER_1k))));
        }
    }

    @Override
    @SideOnly(CLIENT)
    public FontRenderer getFontRenderer(ItemStack stack) {
        return TecTechFontRender.INSTANCE;
    }
}

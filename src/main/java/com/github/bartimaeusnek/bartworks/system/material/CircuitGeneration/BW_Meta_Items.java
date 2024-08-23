/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material.CircuitGeneration;

import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.MainMod;
import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Util;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class BW_Meta_Items {

    public static BW_Meta_Items.BW_GT_MetaGenCircuits getCircuitParts() {
        return BW_Meta_Items.NEW_CIRCUIT_PARTS;
    }

    private static final BW_Meta_Items.BW_GT_MetaGenCircuits NEW_CIRCUIT_PARTS = new BW_Meta_Items.BW_GT_MetaGenCircuits();

    static {
        BW_Meta_Items.NEW_CIRCUIT_PARTS.addItem(0, "Circuit Imprint", "", SubTag.NO_UNIFICATION, SubTag.NO_RECYCLING);
        BW_Meta_Items.NEW_CIRCUIT_PARTS.addItem(1, "Sliced Circuit", "", SubTag.NO_UNIFICATION, SubTag.NO_RECYCLING);
        BW_Meta_Items.NEW_CIRCUIT_PARTS
            .addItem(2, "Raw Imprint supporting Board", "A Raw Board needed for Circuit Imprints");
        BW_Meta_Items.NEW_CIRCUIT_PARTS.addItem(3, "Imprint supporting Board", "A Board needed for Circuit Imprints");

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.dust, 1),
                WerkstoffLoader.ArInGaPhoBiBoTe.get(OrePrefixes.dust, 4))
            .itemOutputs(BW_Meta_Items.NEW_CIRCUIT_PARTS.getStack(2))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(formingPressRecipes);

        RecipeMaps.autoclaveRecipes.add(
            new GT_Recipe(
                false,
                new ItemStack[] { BW_Meta_Items.NEW_CIRCUIT_PARTS.getStack(2) },
                new ItemStack[] { BW_Meta_Items.NEW_CIRCUIT_PARTS.getStack(3) },
                null,
                new int[] { 7500 },
                new FluidStack[] { Materials.SolderingAlloy.getMolten(576) },
                null,
                300,
                (int) TierEU.RECIPE_EV,
                BW_Util.CLEANROOM));
    }

    public static class BW_GT_MetaGenCircuits extends BW_Meta_Items.BW_GT_MetaGen_Item_Hook {

        public BW_GT_MetaGenCircuits() {
            super("bwMetaGeneratedItem0");
        }

        public final ItemStack getStack(int meta) {
            return getStack(meta, 1);
        }

        public final ItemStack getStack(int meta, int stackSize) {
            return new ItemStack(this, stackSize, meta);
        }

        public final ItemStack getStackWithNBT(NBTTagCompound tag, int meta, int stackSize) {
            ItemStack itemStack = getStack(meta, stackSize);
            itemStack.setTagCompound(tag);
            return itemStack;
        }

        @Override
        public void getSubItems(Item var1, CreativeTabs aCreativeTab, List<ItemStack> aList) {
            if (aCreativeTab == this.getCreativeTab())
                for (NBTTagCompound tag : CircuitImprintLoader.recipeTagMap.keySet()) {
                    ItemStack stack = new ItemStack(BW_Meta_Items.NEW_CIRCUIT_PARTS, 1, 0);
                    stack.setTagCompound(tag);
                    aList.add(stack);
                }
            super.getSubItems(var1, aCreativeTab, aList);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public final void registerIcons(IIconRegister aIconRegister) {

            for (short i = 0; i < CircuitImprintLoader.reverseIDs; ++i) {
                if (this.mEnabledItems.get(i)) {
                    BW_Util.set2DCoordTo1DArray(
                        i,
                        0,
                        2,
                        aIconRegister.registerIcon("gregtech:" + this.getUnlocalizedName() + "/" + i),
                        this.mIconList);
                }
            }

            for (short i = CircuitImprintLoader.reverseIDs; i < Short.MAX_VALUE; i++) {
                if (this.mEnabledItems.get(i)) {
                    BW_Util.set2DCoordTo1DArray(
                        i,
                        0,
                        2,
                        Objects.requireNonNull(CircuitImprintLoader.circuitIIconRefs.get(i))
                            .get(1)
                            .getIconIndex(),
                        this.mIconList);
                    BW_Util.set2DCoordTo1DArray(
                        i,
                        1,
                        2,
                        aIconRegister.registerIcon(MainMod.MOD_ID + ":WrapOverlay"),
                        this.mIconList);
                }
            }
        }

        @Override
        protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
            if (aStack.getTagCompound() != null) {
                ItemStack tagStack = CircuitImprintLoader.getStackFromTag(aStack.getTagCompound());
                String itemName = tagStack != null
                    ? GT_LanguageManager.getTranslation(GT_LanguageManager.getTranslateableItemStackName(tagStack))
                    : "a circuit";

                if (aStack.getItemDamage() == 0) {
                    aList.add("An imprint for: " + itemName);
                } else if (aStack.getItemDamage() == 1) {
                    aList.add("A sliced " + itemName);
                }
            } else {
                if (aStack.getItemDamage() == 0) {
                    aList.add("An imprint for a Circuit");
                } else if (aStack.getItemDamage() == 1) {
                    aList.add("A sliced Circuit");
                }
            }

            super.addAdditionalToolTips(aList, aStack, aPlayer);
        }

    }

    public static class BW_GT_MetaGen_Item_Hook extends GT_MetaBase_Item {

        public static final HashSet<BW_Meta_Items.BW_GT_MetaGen_Item_Hook> sInstances = new HashSet<>();
        public final IIcon[] mIconList;
        public final BitSet mEnabledItems;

        {
            this.mIconList = new IIcon[Short.MAX_VALUE * 2];
            this.mEnabledItems = new BitSet(Short.MAX_VALUE);
        }

        private BW_GT_MetaGen_Item_Hook(String aUnlocalized) {
            super(aUnlocalized);

            this.setCreativeTab(new CreativeTabs("bw.MetaItems.0") {

                @Override
                public Item getTabIconItem() {
                    return ItemRegistry.TAB;
                }
            });
            this.setHasSubtypes(true);
            this.setMaxDamage(0);
            BW_Meta_Items.BW_GT_MetaGen_Item_Hook.sInstances.add(this);
        }

        @Override
        public Long[] getElectricStats(ItemStack itemStack) {
            return null;
        }

        @Override
        public Long[] getFluidContainerStats(ItemStack itemStack) {
            return null;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aRandomData) {
            if (aToolTip == null) {
                aToolTip = "";
            }
            ItemStack rStack = new ItemStack(this, 1, aID);
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(rStack) + ".name", aEnglish);
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(rStack) + ".tooltip", aToolTip);
            List<TC_Aspects.TC_AspectStack> tAspects = new ArrayList<>();
            this.mEnabledItems.set(aID);
            Object[] var7 = aRandomData;
            int var8 = aRandomData.length;

            int var9;
            Object tRandomData;
            for (var9 = 0; var9 < var8; ++var9) {
                tRandomData = var7[var9];
                if (tRandomData instanceof SubTag && tRandomData == SubTag.NO_UNIFICATION) {
                    GT_OreDictUnificator.addToBlacklist(rStack);
                }
            }

            var7 = aRandomData;
            var8 = aRandomData.length;

            for (var9 = 0; var9 < var8; ++var9) {
                tRandomData = var7[var9];
                if (tRandomData != null) {
                    boolean tUseOreDict = true;

                    if (tRandomData instanceof IItemBehaviour) {
                        this.addItemBehavior(aID, (IItemBehaviour) tRandomData);
                        tUseOreDict = false;
                    }

                    if (tRandomData instanceof IItemContainer) {
                        ((IItemContainer) tRandomData).set(rStack);
                        tUseOreDict = false;
                    }

                    if (!(tRandomData instanceof SubTag)) {
                        if (tRandomData instanceof TC_Aspects.TC_AspectStack) {
                            ((TC_Aspects.TC_AspectStack) tRandomData).addToAspectList(tAspects);
                        } else if (tRandomData instanceof ItemData) {
                            if (GT_Utility.isStringValid(tRandomData)) {
                                GT_OreDictUnificator.registerOre(tRandomData, rStack);
                            } else {
                                GT_OreDictUnificator.addItemData(rStack, (ItemData) tRandomData);
                            }
                        } else if (tUseOreDict) {
                            GT_OreDictUnificator.registerOre(tRandomData, rStack);
                        }
                    }
                }
            }

            if (GregTech_API.sThaumcraftCompat != null) {
                GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
            }

            return rStack;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public void getSubItems(Item var1, CreativeTabs aCreativeTab, List<ItemStack> aList) {
            int j = this.mEnabledItems.length();

            for (int i = 0; i < j; ++i) {
                if (this.mEnabledItems.get(i)) {
                    ItemStack tStack = new ItemStack(this, 1, i);
                    this.isItemStackUsable(tStack);
                    aList.add(tStack);
                }
            }
        }

        @Override
        protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
            super.addAdditionalToolTips(aList, aStack, aPlayer);
        }

        @Override
        public String getUnlocalizedName(ItemStack aStack) {
            return this.getUnlocalizedName() + "." + aStack.getItemDamage();
        }

        @Override
        public IIcon getIconFromDamage(int i) {
            if (this.mEnabledItems.get(i)) return (IIcon) BW_Util.get2DCoordFrom1DArray(i, 0, 2, this.mIconList);
            return null;
        }

        @Override
        public IIcon getIcon(ItemStack stack, int renderPass, EntityPlayer player, ItemStack usingItem,
            int useRemaining) {
            return this.getIconFromDamage(stack.getItemDamage());
        }

        @Override
        public IIcon getIcon(ItemStack stack, int pass) {
            return this.getIconFromDamage(stack.getItemDamage());
        }
    }
}

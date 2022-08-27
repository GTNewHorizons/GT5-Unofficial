/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement;

import static com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.GTMetaItemEnhancer.NoMetaValue;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Items;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GT_OreDictUnificator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

public class BWGTMetaItems extends BW_MetaGenerated_Items {

    private boolean hasList;
    private final Set<Integer> hiddenThings = new HashSet<>();

    public BWGTMetaItems(OrePrefixes orePrefixes, List<Materials> noSubIDMaterials) {
        super(orePrefixes, null);
        // materialloop:
        for (int i = 0; i < Materials.values().length; i++) {
            ItemStack tStack = new ItemStack(this, 1, i);
            Materials material = Materials.values()[i];
            if (((material.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten)
                    || ((material.getFluid(1) == null && material.getGas(1) == null)
                            && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle)))) continue;
            // for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet)
            //    if (material.mDefaultLocalName.equalsIgnoreCase(werkstoff.getDefaultName()))
            //        continue materialloop;
            if (OreDictionary.doesOreNameExist(
                    this.orePrefixes.name() + material.mDefaultLocalName.replaceAll(" ", ""))) {
                hiddenThings.add(i);
                continue;
            }
            GT_OreDictUnificator.registerOre(
                    this.orePrefixes.name() + material.mDefaultLocalName.replaceAll(" ", ""), tStack);
        }

        if (noSubIDMaterials != null) {
            hasList = true;
            // materialloop:
            for (int i = 0; i < noSubIDMaterials.size(); i++) {
                ItemStack tStack = new ItemStack(this, 1, i + 1001);
                Materials w = noSubIDMaterials.get(i);
                if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten)
                        || ((w.getFluid(1) == null && w.getGas(1) == null)
                                && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))))
                    continue;
                // for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet)
                //    if (w.mDefaultLocalName.equalsIgnoreCase(werkstoff.getDefaultName()))
                //        continue materialloop;
                if (OreDictionary.doesOreNameExist(this.orePrefixes.name() + w.mDefaultLocalName.replaceAll(" ", ""))) {
                    hiddenThings.add(i);
                    continue;
                }
                GT_OreDictUnificator.registerOre(
                        this.orePrefixes.name() + w.mDefaultLocalName.replaceAll(" ", ""), tStack);
            }
        }
    }

    private Materials getMaterial(ItemStack is) {
        if (is == null || is.getItem() != this) return null;
        final int meta = is.getItemDamage();
        Materials material;
        if (meta > 1000 && hasList) material = NoMetaValue.get(meta - 1001);
        else material = Materials.values()[meta];
        return material;
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        Materials material = getMaterial(aStack);
        if (material == null) material = Materials._NULL;
        return material.getLocalizedNameForItem(itemTypeLocalizedName);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        Materials material = getMaterial(aStack);
        if (material != null) {
            String tooltip = material.getToolTip();
            if (tooltip != null && !tooltip.isEmpty()) {
                aList.add(tooltip);
            }
        }
        aList.add(BW_Tooltip_Reference.ADDED_BY_BARTWORKS.get());
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        if (this.orePrefixes.mTextureIndex == -1) return getIconContainerBartWorks(aMetaData);
        if (aMetaData > 1000 && hasList)
            return NoMetaValue.get(aMetaData - 1001).mIconSet.mTextures[this.orePrefixes.mTextureIndex];
        if (aMetaData < 0 || aMetaData > Materials.values().length || Materials.values()[(short) aMetaData] == null)
            return null;
        return Materials.values()[(short) aMetaData].mIconSet.mTextures[this.orePrefixes.mTextureIndex];
    }

    @Override
    protected IIconContainer getIconContainerBartWorks(int aMetaData) {
        if (SideReference.Side.Server || PrefixTextureLinker.texMap == null) return null;

        HashMap<TextureSet, Textures.ItemIcons.CustomIcon> iconLink = PrefixTextureLinker.texMap.get(this.orePrefixes);

        if (iconLink == null) return null;

        Materials material;

        if (aMetaData > 1000 && hasList) material = NoMetaValue.get(aMetaData - 1001);
        else material = Materials.values()[aMetaData];

        if (material == null || material.mIconSet == null) return null;

        return iconLink.get(material.mIconSet);
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < Materials.values().length; i++) {
            Materials w = Materials.values()[i];
            if ((w == null)
                    || (w.mTypes & Werkstoff.GenerationFeatures.getPrefixDataRaw(this.orePrefixes)) == 0
                            && Werkstoff.GenerationFeatures.getPrefixDataRaw(this.orePrefixes) != 0) continue;
            else if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten)
                    || ((w.getFluid(1) == null && w.getGas(1) == null)
                            && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle)))) continue;
            else if (hiddenThings.contains(i)) continue;
            aList.add(new ItemStack(this, 1, i));
        }
        if (hasList)
            for (int i = 0; i < NoMetaValue.size(); i++) {
                Materials w = NoMetaValue.get(i);
                if ((w == null)
                        || (w.mTypes & Werkstoff.GenerationFeatures.getPrefixDataRaw(this.orePrefixes)) == 0
                                && Werkstoff.GenerationFeatures.getPrefixDataRaw(this.orePrefixes) != 0) continue;
                else if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten)
                        || ((w.getFluid(1) == null && w.getGas(1) == null)
                                && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))))
                    continue;
                else if (hiddenThings.contains(i)) continue;
                aList.add(new ItemStack(this, 1, i + 1001));
            }
    }

    @Override
    public short[] getColorForGUI(ItemStack aStack) {
        if (aStack.getItemDamage() > 1000 && hasList) return NoMetaValue.get(aStack.getItemDamage() - 1001).mRGBa;
        return Materials.values()[aStack.getItemDamage()].mRGBa;
    }

    @Override
    public String getNameForGUI(ItemStack aStack) {
        if (aStack.getItemDamage() > 1000 && hasList)
            return NoMetaValue.get(aStack.getItemDamage() - 1001).mDefaultLocalName;
        return Materials.values()[aStack.getItemDamage()].mDefaultLocalName;
    }

    @Override
    public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {}

    @Override
    public short[] getRGBa(ItemStack aStack) {
        if (aStack.getItemDamage() > 1000 && hasList) return NoMetaValue.get(aStack.getItemDamage() - 1001).mRGBa;
        return Materials.values()[aStack.getItemDamage()].mRGBa;
    }

    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        return false;
    }
}

/*
 * Copyright (c) 2018-2019 bartimaeusnek
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

import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import com.github.bartimaeusnek.bartworks.common.configs.ConfigHandler;
import com.github.bartimaeusnek.bartworks.system.material.BW_MetaGenerated_Items;
import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.bartimaeusnek.bartworks.system.oredict.OreDictAdder;
import com.github.bartimaeusnek.bartworks.util.Pair;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

import static com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.GTMetaItemEnhancer.NoMetaValue;

public class BWGTMetaItems extends BW_MetaGenerated_Items {

    private boolean hasList;

    public BWGTMetaItems(OrePrefixes orePrefixes, List<Materials> noSubIDMaterials) {
        super(orePrefixes,null);
        materialloop:
        for (int i = 0; i < Materials.values().length; i++) {
            ItemStack tStack = new ItemStack(this, 1, i);
            Materials w = Materials.values()[i];
            if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten) || ((w.getFluid(1) == null && w.getGas(1) == null) && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))))
                continue;
            for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet)
                if (w.mDefaultLocalName.equalsIgnoreCase(werkstoff.getDefaultName()))
                    continue materialloop;
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".name", getDefaultLocalization(w));
            GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".tooltip", w.getToolTip());
            if (ConfigHandler.experimentalThreadedLoader)
                OreDictAdder.addToMap(new Pair<>(this.orePrefixes.name() + w.mDefaultLocalName.replaceAll(" ",""), tStack));
            else
                GT_OreDictUnificator.registerOre(this.orePrefixes.name() + w.mDefaultLocalName.replaceAll(" ",""), tStack);
        }
        if (noSubIDMaterials != null){
            hasList = true;
            materialloop:
            for (int i = 0; i < noSubIDMaterials.size(); i++) {
                ItemStack tStack = new ItemStack(this, 1, i+1001);
                Materials w = noSubIDMaterials.get(i);
                if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten) || ((w.getFluid(1) == null && w.getGas(1) == null) && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))))
                    continue;
                for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet)
                    if (w.mDefaultLocalName.equalsIgnoreCase(werkstoff.getDefaultName()))
                        continue materialloop;
                GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".name", getDefaultLocalization(w));
                GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(tStack) + ".tooltip", w.getToolTip());
                if (ConfigHandler.experimentalThreadedLoader)
                    OreDictAdder.addToMap(new Pair<>(this.orePrefixes.name() + w.mDefaultLocalName.replaceAll(" ",""), tStack));
                else
                    GT_OreDictUnificator.registerOre(this.orePrefixes.name() + w.mDefaultLocalName.replaceAll(" ",""), tStack);
            }
        }
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        if (this.orePrefixes.mTextureIndex == -1)
            return getIconContainerBartWorks(aMetaData);
        if (aMetaData > 1000 && hasList)
            return NoMetaValue.get(aMetaData-1001).mIconSet.mTextures[this.orePrefixes.mTextureIndex];
        if (aMetaData < 0 || aMetaData > Materials.values().length || Materials.values()[(short) aMetaData] == null)
            return null;
        return Materials.values()[(short) aMetaData].mIconSet.mTextures[this.orePrefixes.mTextureIndex];
    }

    public String getDefaultLocalization(Materials werkstoff) {
        return werkstoff != null ? this.orePrefixes.mLocalizedMaterialPre + werkstoff.mDefaultLocalName + this.orePrefixes.mLocalizedMaterialPost : Materials._NULL.mDefaultLocalName;
    }

    protected IIconContainer getIconContainerBartWorks(int aMetaData) {
        if (FMLCommonHandler.instance().getSide().isClient()) {
            if (aMetaData > 1000 && hasList)
                return PrefixTextureLinker.texMap.get(this.orePrefixes).get(NoMetaValue.get(aMetaData-1001).mIconSet);
            return PrefixTextureLinker.texMap.get(this.orePrefixes).get(Materials.values()[(short) aMetaData].mIconSet);
        }
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings("unchecked")
    public void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < Materials.values().length; i++) {
            Materials w = Materials.values()[i];
            if ((w == null) || (w.mTypes & Werkstoff.GenerationFeatures.prefixLogic.get(this.orePrefixes)) == 0 && Werkstoff.GenerationFeatures.prefixLogic.get(this.orePrefixes) != 0)
                continue;
            else if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten) || ((w.getFluid(1) == null && w.getGas(1) == null) && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))))
                continue;
            aList.add(new ItemStack(this, 1, i));
        }
        if (hasList)
            for (int i = 0; i < NoMetaValue.size(); i++) {
                Materials w = NoMetaValue.get(i);
                if ((w == null) || (w.mTypes & Werkstoff.GenerationFeatures.prefixLogic.get(this.orePrefixes)) == 0 && Werkstoff.GenerationFeatures.prefixLogic.get(this.orePrefixes) != 0)
                    continue;
                else if (((w.getMolten(1) == null && orePrefixes == WerkstoffLoader.capsuleMolten) || ((w.getFluid(1) == null && w.getGas(1) == null) && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))))
                    continue;
                aList.add(new ItemStack(this, 1, i + 1001));
            }
    }

    @Override
    public short[] getColorForGUI(ItemStack aStack) {
        if (aStack.getItemDamage() > 1000 && hasList)
            return NoMetaValue.get(aStack.getItemDamage()-1001).mRGBa;
        return Materials.values()[aStack.getItemDamage()].mRGBa;
    }

    @Override
    public String getNameForGUI(ItemStack aStack) {
        if (aStack.getItemDamage() > 1000 && hasList)
            return NoMetaValue.get(aStack.getItemDamage()-1001).mDefaultLocalName;
        return Materials.values()[aStack.getItemDamage()].mDefaultLocalName;
    }

    @Override
    public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {

    }

    @Override
    public short[] getRGBa(ItemStack aStack) {
        if (aStack.getItemDamage() > 1000 && hasList)
            return NoMetaValue.get(aStack.getItemDamage()-1001).mRGBa;
        return Materials.values()[aStack.getItemDamage()].mRGBa;
    }

    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        return false;
    }
}

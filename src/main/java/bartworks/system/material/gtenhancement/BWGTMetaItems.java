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

package bartworks.system.material.gtenhancement;

import java.util.List;
import java.util.Map;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;

import bartworks.API.SideReference;
import bartworks.client.textures.PrefixTextureLinker;
import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.render.items.GeneratedMaterialRenderer;
import it.unimi.dsi.fastutil.ints.IntOpenHashSet;

public class BWGTMetaItems extends BWMetaGeneratedItems {

    private boolean hasList;
    private final IntOpenHashSet hiddenThings = new IntOpenHashSet();

    public BWGTMetaItems(OrePrefixes orePrefixes, List<Materials> noSubIDMaterials) {
        super(orePrefixes, null);
        // materialloop:
        for (int i = 0; i < Materials.values().length; i++) {
            ItemStack tStack = new ItemStack(this, 1, i);
            Materials material = Materials.values()[i];
            if (material.getMolten(1) == null && orePrefixes == OrePrefixes.capsuleMolten
                || material.getFluid(1) == null && material.getGas(1) == null
                    && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))
                continue;

            if (OreDictionary
                .doesOreNameExist(this.orePrefixes.getName() + material.mDefaultLocalName.replace(" ", ""))) {
                this.hiddenThings.add(i);
                continue;
            }
            GTOreDictUnificator
                .registerOre(this.orePrefixes.getName() + material.mDefaultLocalName.replace(" ", ""), tStack);
        }

        if (noSubIDMaterials != null) {
            this.hasList = true;
            // materialloop:
            for (int i = 0; i < noSubIDMaterials.size(); i++) {
                ItemStack tStack = new ItemStack(this, 1, i + 1001);
                Materials w = noSubIDMaterials.get(i);
                if (w.getMolten(1) == null && orePrefixes == OrePrefixes.capsuleMolten
                    || w.getFluid(1) == null && w.getGas(1) == null
                        && (orePrefixes == OrePrefixes.capsule || orePrefixes == OrePrefixes.bottle))
                    continue;

                if (OreDictionary.doesOreNameExist(this.orePrefixes.getName() + w.mDefaultLocalName.replace(" ", ""))) {
                    this.hiddenThings.add(i);
                    continue;
                }
                GTOreDictUnificator
                    .registerOre(this.orePrefixes.getName() + w.mDefaultLocalName.replace(" ", ""), tStack);
            }
        }
    }

    private Materials getMaterial(ItemStack is) {
        if (is == null || is.getItem() != this) return null;
        final int meta = is.getItemDamage();
        return this.getMaterialFromMeta(meta);
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        Materials material = this.getMaterial(aStack);
        if (material == null) material = Materials._NULL;
        return material.getLocalizedNameForItem(this.itemTypeLocalizedName);
    }

    @Override
    protected void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        Materials material = this.getMaterial(aStack);
        if (material != null) {
            String tooltip = material.getChemicalTooltip();
            if (tooltip != null && !tooltip.isEmpty()) {
                aList.add(tooltip);
            }
        }
    }

    @Override
    public IIconContainer getIconContainer(int aMetaData) {
        if (this.orePrefixes.getTextureIndex() == -1) return this.getIconContainerBartWorks(aMetaData);
        Materials material = this.getMaterialFromMeta(aMetaData);
        if (material != null) {
            return material.mIconSet.mTextures[this.orePrefixes.getTextureIndex()];
        }
        return null;
    }

    @Override
    protected IIconContainer getIconContainerBartWorks(int aMetaData) {
        if (SideReference.Side.Server || PrefixTextureLinker.texMap == null) return null;

        Map<TextureSet, Textures.ItemIcons.CustomIcon> iconLink = PrefixTextureLinker.texMap.get(this.orePrefixes);

        if (iconLink == null) return null;

        Materials material = this.getMaterialFromMeta(aMetaData);

        if (material == null || material.mIconSet == null) return null;

        return iconLink.getOrDefault(material.mIconSet, iconLink.get(TextureSet.SET_DULL));
    }

    @Override
    public IIcon getIconFromDamage(int aMetaData) {
        return this.getIcon(aMetaData, 0);
    }

    @Override
    public GeneratedMaterialRenderer getMaterialRenderer(int aMetaData) {
        return this.getMaterialFromMeta(aMetaData).renderer;
    }

    public Materials getMaterialFromMeta(int aMetaData) {
        if (aMetaData > 1000 && this.hasList) {
            return GTMetaItemEnhancer.NoMetaValue.get(aMetaData - 1001);
        }
        if (aMetaData < 0 || aMetaData >= Materials.values().length) {
            return null;
        }
        return Materials.values()[aMetaData];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item var1, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        int materialGenerationBits = Werkstoff.GenerationFeatures.getPrefixDataRaw(this.orePrefixes);

        for (int i = 0; i < Materials.values().length; i++) {
            Materials material = Materials.values()[i];
            if (material == null) continue;

            // This only falls through when the material has no overlap with `materialGenerationBits`.
            // spotless:off
            if ((materialGenerationBits & 1) == 0 || !material.hasDustItems())
                if ((materialGenerationBits & 2) == 0 || !material.hasMetalItems())
                    if ((materialGenerationBits & 4) == 0 || !material.hasGemItems())
                        if ((materialGenerationBits & 8) == 0 || !material.hasOresItems())
                            if ((materialGenerationBits & 16) == 0 || !material.hasCell())
                                if ((materialGenerationBits & 32) == 0 || !material.hasPlasma())
                                    if ((materialGenerationBits & 64) == 0 || !material.hasToolHeadItems())
                                        if ((materialGenerationBits & 128) == 0 || !material.hasGearItems())
                                            if ((materialGenerationBits & 256) == 0 || !material.hasEmpty())
                                                continue;
            // spotless:on

            if (material.getMolten(1) == null) {
                if (this.orePrefixes == OrePrefixes.capsuleMolten) continue;
            }

            if (material.getFluid(1) == null && material.getGas(1) == null) {
                if (this.orePrefixes == OrePrefixes.capsule) continue;
                if (this.orePrefixes == OrePrefixes.bottle) continue;
            }

            if (this.hiddenThings.contains(i)) continue;
            aList.add(new ItemStack(this, 1, i));
        }

        if (this.hasList) {
            for (int i = 0; i < GTMetaItemEnhancer.NoMetaValue.size(); i++) {
                Materials material = GTMetaItemEnhancer.NoMetaValue.get(i);
                if (material == null) continue;

                // This only falls through when the material has no overlap with `materialGenerationBits`.
                // spotless:off
                if ((materialGenerationBits & 1) == 0 || !material.hasDustItems())
                    if ((materialGenerationBits & 2) == 0 || !material.hasMetalItems())
                        if ((materialGenerationBits & 4) == 0 || !material.hasGemItems())
                            if ((materialGenerationBits & 8) == 0 || !material.hasOresItems())
                                if ((materialGenerationBits & 16) == 0 || !material.hasCell())
                                    if ((materialGenerationBits & 32) == 0 || !material.hasPlasma())
                                        if ((materialGenerationBits & 64) == 0 || !material.hasToolHeadItems())
                                            if ((materialGenerationBits & 128) == 0 || !material.hasGearItems())
                                                if ((materialGenerationBits & 256) == 0 || !material.hasEmpty())
                                                    continue;
                // spotless:on

                if (material.getMolten(1) == null) {
                    if (this.orePrefixes == OrePrefixes.capsuleMolten) continue;
                }

                if (material.getFluid(1) == null && material.getGas(1) == null) {
                    if (this.orePrefixes == OrePrefixes.capsule) continue;
                    if (this.orePrefixes == OrePrefixes.bottle) continue;
                }

                if (this.hiddenThings.contains(i)) continue;
                aList.add(new ItemStack(this, 1, i + 1001));
            }
        }
    }

    @Override
    public short[] getColorForGUI(ItemStack aStack) {
        return this.getMaterial(aStack).mRGBa;
    }

    @Override
    public String getNameForGUI(ItemStack aStack) {
        return this.getMaterial(aStack).mDefaultLocalName;
    }

    @Override
    public void onUpdate(ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {}

    @Override
    public short[] getRGBa(ItemStack aStack) {
        return this.getMaterial(aStack).mRGBa;
    }

    @Override
    public boolean onEntityItemUpdate(EntityItem aItemEntity) {
        return false;
    }
}

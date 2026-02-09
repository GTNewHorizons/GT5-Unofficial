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

package bartworks.system.material.CircuitGeneration;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.formingPressRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import bartworks.API.enums.CircuitImprint;
import bartworks.MainMod;
import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import codechicken.nei.api.API;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTOreDictUnificator;

/**
 * This class is used for all items to circuit generation. Such as: Circuit Imprints, Sliced Circuits and the wraps.
 */
public class CircuitPartsItem extends Item {

    private static final int CIRCUIT_SLICED_OFFSET = 1000;

    public static CircuitPartsItem getCircuitParts() {
        return CircuitPartsItem.INSTANCE;
    }

    private static CircuitPartsItem INSTANCE;

    public static void init() {
        INSTANCE = new CircuitPartsItem();

        GTOreDictUnificator.addToBlacklist(INSTANCE.getStack(0));
        GTOreDictUnificator.addToBlacklist(INSTANCE.getStack(1));
        ItemList.RawImprintBoard.set(INSTANCE.getStack(2));
        ItemList.ImprintBoard.set(INSTANCE.getStack(3));

        GTValues.RA.stdBuilder()
            .itemInputs(
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.dust, 1),
                WerkstoffLoader.ArInGaPhoBiBoTe.get(OrePrefixes.dust, 4))
            .itemOutputs(ItemList.RawImprintBoard.get(1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(formingPressRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.RawImprintBoard.get(1))
            .itemOutputs(ItemList.ImprintBoard.get(1))
            .outputChances(75_00)
            .fluidInputs(Materials.SolderingAlloy.getMolten(4 * INGOTS))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .requiresCleanRoom()
            .addTo(autoclaveRecipes);

        GameRegistry.addRecipe(new DeprecatedPartsMigrationRecipe());
        API.hideItem(INSTANCE.getStack(0));
        API.hideItem(INSTANCE.getStack(1));
    }

    private IIcon iconImprint;
    private IIcon iconSliced;
    private IIcon iconRawImprintBoard;
    private IIcon iconImprintBoard;
    private IIcon iconImprintOverlay;

    private CircuitPartsItem() {
        setUnlocalizedName("gt.bwMetaGeneratedItem0");
        GameRegistry.registerItem(this, "gt.bwMetaGeneratedItem0", GregTech.ID);

        this.setCreativeTab(new CreativeTabs("bw.MetaItems.0") {

            @Override
            public Item getTabIconItem() {
                return ItemRegistry.TAB;
            }
        });
        this.setHasSubtypes(true);
        this.setMaxDamage(0);
    }

    public final ItemStack getImprintStack(int id) {
        return getStack(id);
    }

    public final ItemStack getSlicedStack(int id) {
        return getStack(id + CIRCUIT_SLICED_OFFSET);
    }

    public final ItemStack getStack(int meta) {
        return getStack(meta, 1);
    }

    public final ItemStack getStack(int meta, int stackSize) {
        return new ItemStack(this, stackSize, meta);
    }

    @Deprecated
    public final ItemStack getStackWithNBT(NBTTagCompound tag, int meta, int stackSize) {
        ItemStack itemStack = getStack(meta, stackSize);
        itemStack.setTagCompound(tag);
        return itemStack;
    }

    public boolean isWrap(int id) {
        return id >= CircuitWraps.getMinimalID();
    }

    public boolean isImprint(int id) {
        return id < CIRCUIT_SLICED_OFFSET;
    }

    public boolean isSliced(int id) {
        return id >= CIRCUIT_SLICED_OFFSET && id < CircuitWraps.getMinimalID();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IIconRegister aIconRegister) {
        iconImprint = aIconRegister.registerIcon("gregtech:" + this.getUnlocalizedName() + "/0");
        iconSliced = aIconRegister.registerIcon("gregtech:" + this.getUnlocalizedName() + "/1");
        iconRawImprintBoard = aIconRegister.registerIcon("gregtech:" + this.getUnlocalizedName() + "/2");
        iconImprintBoard = aIconRegister.registerIcon("gregtech:" + this.getUnlocalizedName() + "/3");
        iconImprintOverlay = aIconRegister.registerIcon(MainMod.MOD_ID + ":WrapOverlay");
    }

    @Override
    public IIcon getIconFromDamage(int i) {
        switch (i) {
            case 0:
                return iconImprint;
            case 1:
                return iconSliced;
            case 2:
                return iconRawImprintBoard;
            case 3:
                return iconImprintBoard;
        }

        if (isImprint(i)) return iconImprint;
        if (isSliced(i)) return iconSliced;
        if (isWrap(i)) {
            CircuitWraps wrap = CircuitWraps.getByID(i);
            if (wrap != null && wrap.itemSingle.hasBeenSet()) return wrap.itemSingle.get(1)
                .getIconIndex();
        }
        return null;
    }

    public IIcon getOverlayIcon(int id) {
        if (isWrap(id)) return iconImprintOverlay;
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item var1, CreativeTabs aCreativeTab, List<ItemStack> aList) {
        aList.add(getStack(0));
        aList.add(getStack(1));
        aList.add(getStack(2));
        aList.add(getStack(3));
        for (CircuitImprint imprint : CircuitImprint.values()) {
            if (!imprint.sourceMod.isModLoaded()) continue;
            ItemStack imprintStack = new ItemStack(this, 1, imprint.id);
            aList.add(imprintStack);
            ItemStack slicedStack = new ItemStack(this, 1, imprint.id + CIRCUIT_SLICED_OFFSET);
            aList.add(slicedStack);
        }
        for (CircuitWraps wrap : CircuitWraps.values()) {
            ItemStack wrapStack = new ItemStack(this, 1, wrap.id);
            aList.add(wrapStack);
        }
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean advanced) {
        final int id = aStack.getItemDamage();
        switch (id) {
            case 0, 1:
                aList.add(
                    EnumChatFormatting.DARK_RED
                        + "This item is deprecated, place it in the crafting grid to get the new one!");
                return;
            case 2:
                aList.add(StatCollector.translateToLocal("tooltip.item.CircuitParts.RawImprintBoard"));
                return;
            case 3:
                aList.add(StatCollector.translateToLocal("tooltip.item.CircuitParts.ImprintBoard"));
                return;
        }

        if (isImprint(id)) {
            CircuitImprint imprint = CircuitImprint.IMPRINT_LOOKUPS_BY_IDS.get(id);
            if (imprint != null) {
                aList.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.bw.item.circuit.tagged.imprint",
                        imprint.circuit.get(1)
                            .getDisplayName()));
            }
        }
        if (isSliced(id)) {
            CircuitImprint imprint = CircuitImprint.IMPRINT_LOOKUPS_BY_IDS.get(id - CIRCUIT_SLICED_OFFSET);
            if (imprint != null) {
                aList.add(
                    StatCollector.translateToLocalFormatted(
                        "tooltip.bw.item.circuit.tagged.sliced",
                        imprint.circuit.get(1)
                            .getDisplayName()));
            }
        }
        if (isWrap(id)) {
            CircuitWraps wrap = CircuitWraps.getByID(id);
            if (wrap != null && wrap.itemSingle.hasBeenSet()) {
                ItemStack itemSingle = wrap.itemSingle.get(1);
                itemSingle.getItem()
                    .addInformation(itemSingle, aPlayer, aList, false);
            }
        }
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        final int id = stack.getItemDamage();
        if (isWrap(id)) {
            CircuitWraps wrap = CircuitWraps.getByID(id);
            if (wrap != null && wrap.itemSingle.hasBeenSet()) {
                return StatCollector.translateToLocalFormatted(
                    "item.CircuitParts.Wrap.name",
                    wrap.itemSingle.get(1)
                        .getDisplayName());
            }
        }
        return super.getItemStackDisplayName(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack aStack) {
        final int id = aStack.getItemDamage();
        switch (id) {
            case 0:
                return "item.CircuitParts.Imprint";
            case 1:
                return "item.CircuitParts.Sliced";
            case 2:
                return "item.CircuitParts.RawImprintBoard";
            case 3:
                return "item.CircuitParts.ImprintBoard";
        }

        if (isImprint(id)) return "item.CircuitParts.Imprint";
        if (isSliced(id)) return "item.CircuitParts.Sliced";

        return this.getUnlocalizedName() + "." + aStack.getItemDamage();
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.bwMetaGeneratedItem0";
    }
}

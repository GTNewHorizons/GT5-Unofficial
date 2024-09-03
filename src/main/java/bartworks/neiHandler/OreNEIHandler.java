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

package bartworks.neiHandler;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import bartworks.MainMod;
import bartworks.system.material.BWMetaGeneratedOres;
import bartworks.system.material.BWMetaGeneratedSmallOres;
import bartworks.system.material.Werkstoff;
import bartworks.system.oregen.BWOreLayer;
import bartworks.system.oregen.BWWorldGenRoss128b;
import bartworks.system.oregen.BWWorldGenRoss128ba;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.OrePrefixes;

public class OreNEIHandler extends TemplateRecipeHandler {

    public OreNEIHandler() {
        if (!BWNEIConfig.sIsAdded) {
            FMLInterModComms.sendRuntimeMessage(
                MainMod.MOD_ID,
                "NEIPlugins",
                "register-crafting-handler",
                MainMod.MOD_ID + "@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
        }
    }

    @Override
    public void drawBackground(int recipe) {
        GuiDraw.drawRect(0, 0, 166, 65, 0x888888);
    }

    @Override
    public void loadTransferRects() {
        this.transferRects.add(
            new TemplateRecipeHandler.RecipeTransferRect(new Rectangle(0, 40, 40, 120), "quickanddirtyneihandler"));
    }

    @Override
    public int recipiesPerPage() {
        return 1;
    }

    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if ("quickanddirtyneihandler".equalsIgnoreCase(outputId)) {
            HashSet<ItemStack> result = new HashSet<>();
            Werkstoff.werkstoffHashSet.stream()
                .filter(w -> w.hasGenerationFeature(OrePrefixes.ore))
                .forEach(w -> result.add(w.get(OrePrefixes.ore)));
            result.forEach(this::loadCraftingRecipes);
            result.clear();
            Werkstoff.werkstoffHashSet.stream()
                .filter(w -> w.hasGenerationFeature(OrePrefixes.ore))
                .forEach(w -> result.add(w.get(OrePrefixes.oreSmall)));
            result.forEach(this::loadCraftingRecipes);
            result.clear();
            HashSet<TemplateRecipeHandler.CachedRecipe> hashSet = new HashSet<>(this.arecipes);
            this.arecipes.clear();
            this.arecipes.addAll(hashSet);
        }
        if ("item".equals(outputId)) {
            this.loadCraftingRecipes((ItemStack) results[0]);
            HashSet<TemplateRecipeHandler.CachedRecipe> hashSet = new HashSet<>(this.arecipes);
            this.arecipes.clear();
            this.arecipes.addAll(hashSet);
        }
    }

    @Override
    public void drawExtras(int recipe) {
        if (recipe < this.arecipes.size() && this.arecipes.get(recipe) instanceof CachedOreRecipe) {
            CachedOreRecipe cachedOreRecipe = (CachedOreRecipe) this.arecipes.get(recipe);

            if (cachedOreRecipe == null || cachedOreRecipe.getOtherStacks() == null
                || cachedOreRecipe.getOtherStacks()
                    .size() == 0)
                return;

            if (!cachedOreRecipe.small) {
                if (cachedOreRecipe.getOtherStacks()
                    .get(0) == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(0).item == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(1) == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(2) == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(3) == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(1).item == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(2).item == null
                    || cachedOreRecipe.getOtherStacks()
                        .get(3).item == null)
                    return;
            } else if (cachedOreRecipe.getOtherStacks()
                .get(0) == null
                || cachedOreRecipe.getOtherStacks()
                    .get(0).item == null)
                return;

            if (cachedOreRecipe.worldGen != null) GuiDraw.drawString(
                EnumChatFormatting.BOLD + "DIM: " + EnumChatFormatting.RESET + cachedOreRecipe.worldGen.getDimName(),
                0,
                40,
                0,
                false);

            GuiDraw.drawString(EnumChatFormatting.BOLD + "Primary:", 0, 50, 0, false);
            GuiDraw.drawString(
                cachedOreRecipe.getOtherStacks()
                    .get(0).item.getDisplayName(),
                0,
                60,
                0,
                false);

            if (!cachedOreRecipe.small) {
                GuiDraw.drawString(EnumChatFormatting.BOLD + "Secondary:", 0, 70, 0, false);
                GuiDraw.drawString(
                    cachedOreRecipe.getOtherStacks()
                        .get(1).item.getDisplayName(),
                    0,
                    80,
                    0,
                    false);
                GuiDraw.drawString(EnumChatFormatting.BOLD + "InBetween:", 0, 90, 0, false);
                GuiDraw.drawString(
                    cachedOreRecipe.getOtherStacks()
                        .get(2).item.getDisplayName(),
                    0,
                    100,
                    0,
                    false);
                GuiDraw.drawString(EnumChatFormatting.BOLD + "Sporadic:", 0, 110, 0, false);
                GuiDraw.drawString(
                    cachedOreRecipe.getOtherStacks()
                        .get(3).item.getDisplayName(),
                    0,
                    120,
                    0,
                    false);
            } else if (cachedOreRecipe.worldGen != null) {
                GuiDraw.drawString(EnumChatFormatting.BOLD + "Amount per Chunk:", 0, 70, 0, false);
                GuiDraw.drawString(cachedOreRecipe.worldGen.mDensity + "", 0, 80, 0, false);
            }
        }
        super.drawExtras(recipe);
    }

    @Override
    public void loadCraftingRecipes(ItemStack result) {
        Block ore = Block.getBlockFromItem(result.getItem());
        if (ore instanceof BWMetaGeneratedOres) {
            BWOreLayer.NEIMAP.get((short) result.getItemDamage())
                .stream()
                .filter(
                    l -> !(ore instanceof BWMetaGeneratedSmallOres) || !l.getClass()
                        .equals(BWWorldGenRoss128b.class)
                        && !l.getClass()
                            .equals(BWWorldGenRoss128ba.class))
                .forEach(
                    l -> this.arecipes.add(new CachedOreRecipe(l, result, ore instanceof BWMetaGeneratedSmallOres)));
        }
    }

    @Override
    public String getGuiTexture() {
        return "textures/gui/container/brewing_stand.png";
    }

    @Override
    public String getRecipeName() {
        return "BartWorks Ores";
    }

    class CachedOreRecipe extends TemplateRecipeHandler.CachedRecipe {

        public CachedOreRecipe(BWOreLayer worldGen, ItemStack result, boolean smallOres) {
            this.worldGen = worldGen;
            this.stack = new PositionedStack(result, 0, 0);
            this.small = smallOres;
        }

        boolean small;
        BWOreLayer worldGen;
        PositionedStack stack;

        @Override
        public PositionedStack getResult() {
            return this.stack;
        }

        @Override
        public List<PositionedStack> getOtherStacks() {
            List<PositionedStack> ret = new ArrayList<>();
            int x = 0;
            for (int i = 0; i < (this.small ? 1 : 4); i++) {
                x += 20;
                ret.add(
                    new PositionedStack(
                        this.worldGen.getStacks()
                            .get(i),
                        x,
                        12));
            }
            return ret;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof CachedOreRecipe that)) return false;
            return Objects.equals(this.worldGen, that.worldGen);
        }

        @Override
        public int hashCode() {
            return this.worldGen.hashCode();
        }
    }
}

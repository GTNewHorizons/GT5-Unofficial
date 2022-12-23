package com.github.technus.tectech.nei;

import static com.github.technus.tectech.Reference.MODID;
import static com.github.technus.tectech.util.CommonValues.EOH_TIER_FANCY_NAMES;
import static gregtech.api.util.GT_Utility.formatNumbers;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.github.technus.tectech.recipe.EyeOfHarmonyRecipe;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

public class TT_NEI_EyeOfHarmonyHandler extends GT_NEI_DefaultHandler {

    public TT_NEI_EyeOfHarmonyHandler(final GT_Recipe.GT_Recipe_Map tMap) {
        super(tMap);
        if (!NEI_TT_Config.sIsAdded) {

            FMLInterModComms.sendRuntimeMessage(
                    GT_Values.GT,
                    "NEIPlugins",
                    "register-crafting-handler",
                    MODID + "@" + this.getRecipeName() + "@" + this.getOverlayIdentifier());
            GuiCraftingRecipe.craftinghandlers.add(this);
            GuiUsageRecipe.usagehandlers.add(this);
        }
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new TT_NEI_EyeOfHarmonyHandler(this.mRecipeMap);
    }

    @Override
    public List<String> handleItemTooltip(
            GuiRecipe<?> gui, ItemStack aStack, List<String> currentToolTip, int aRecipeIndex) {

        if (aStack == null) {
            return currentToolTip;
        }

        CachedRecipe tObject = this.arecipes.get(aRecipeIndex);
        if (tObject instanceof CachedDefaultRecipe) {

            CachedDefaultRecipe tRecipe = (CachedDefaultRecipe) tObject;
            EyeOfHarmonyRecipe currentRecipe = (EyeOfHarmonyRecipe) tRecipe.mRecipe.mSpecialItems;

            double percentage = currentRecipe.getItemStackToProbabilityMap().getOrDefault(aStack, -1.0);

            if (percentage != -1.0) {
                currentToolTip.add(EnumChatFormatting.GRAY + "Percentage of Solid Mass: " + percentage + "%");
                currentToolTip.add(EnumChatFormatting.GRAY + "Item Count: "
                        + formatNumbers(
                                currentRecipe.getItemStackToTrueStackSizeMap().get(aStack)));
            }
        }

        // So elements are displayed at bottom, call super method after.
        super.handleItemTooltip(gui, aStack, currentToolTip, aRecipeIndex);
        return currentToolTip;
    }

    @Override
    protected void drawOverlays(CachedDefaultRecipe recipe) {
        for (PositionedStack stack : recipe.mOutputs) {
            if (!(stack instanceof FixedPositionedStack)) continue;

            EyeOfHarmonyRecipe EOHRecipe = (EyeOfHarmonyRecipe) recipe.mRecipe.mSpecialItems;

            if (EOHRecipe.getItemStackToTrueStackSizeMap().containsKey(stack.item)) {
                drawOverlayForStack(
                        (FixedPositionedStack) stack,
                        EOHRecipe.getItemStackToTrueStackSizeMap().get(stack.item));
            }
        }
    }

    protected void drawOverlayForStack(FixedPositionedStack stack, long stackSize) {

        FontRenderer fontRender = Minecraft.getMinecraft().fontRenderer;
        float smallTextScale = 0.5f;

        GL11.glPushMatrix();
        GL11.glTranslatef(stack.relx, stack.rely, 300);
        GL11.glScalef(smallTextScale, smallTextScale, 1.0f);

        String displayString;

        if (stackSize > 9999) {
            displayString = ReadableNumberConverter.INSTANCE.toWideReadableForm(stackSize);
        } else {
            displayString = String.valueOf(stackSize);
        }

        fontRender.drawString(
                displayString, 0, (int) (16 / smallTextScale) - fontRender.FONT_HEIGHT + 1, 0xFFFFFF, true);

        GL11.glPopMatrix();

        GL11.glDisable(GL11.GL_ALPHA_TEST);

        super.drawOverlayForStack(stack);
    }

    @Override
    public void drawExtras(int aRecipeIndex) {
        GT_NEI_DefaultHandler.CachedDefaultRecipe cachedRecipe =
                ((GT_NEI_DefaultHandler.CachedDefaultRecipe) this.arecipes.get(aRecipeIndex));
        EyeOfHarmonyRecipe recipe = ((EyeOfHarmonyRecipe) cachedRecipe.mRecipe.mSpecialItems);

        int index = 0;
        drawLine(index++, "Time: " + formatNumbers(recipe.getRecipeTimeInTicks() / 20) + " secs");
        drawLine(index++, "Hydrogen: " + formatNumbers(recipe.getHydrogenRequirement()) + " L");
        drawLine(index++, "Helium: " + formatNumbers(recipe.getHydrogenRequirement()) + " L");
        drawLine(
                index++,
                "Spacetime Tier: "
                        + EOH_TIER_FANCY_NAMES[(int) recipe.getSpacetimeCasingTierRequired()]);
        drawLine(index++, "EU Output: " + formatNumbers(recipe.getEUOutput()) + " EU");
        drawLine(index++, "EU Input: " + formatNumbers(recipe.getEUStartCost()) + " EU");
        drawLine(index++, "Base Recipe Chance: " + formatNumbers(100 * recipe.getBaseRecipeSuccessChance()) + "%");
        drawLine(index, "Recipe Energy Efficiency: " + formatNumbers(100 * recipe.getRecipeEnergyEfficiency()) + "%");
        drawOverlays(cachedRecipe);
    }

    @Override
    protected void drawLine(int lineNumber, String line) {
        drawText(4, getDescriptionYOffset() + 30 + lineNumber * 9, line, 0xFF000000);
    }
}

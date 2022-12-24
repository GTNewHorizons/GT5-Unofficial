package com.github.technus.tectech.nei;

import static com.github.technus.tectech.Reference.MODID;
import static net.minecraft.util.EnumChatFormatting.*;

import appeng.util.ReadableNumberConverter;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.GuiCraftingRecipe;
import codechicken.nei.recipe.GuiUsageRecipe;
import codechicken.nei.recipe.TemplateRecipeHandler;
import com.github.technus.tectech.recipe.EyeOfHarmonyRecipe;
import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Recipe;
import gregtech.nei.GT_NEI_DefaultHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
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
}

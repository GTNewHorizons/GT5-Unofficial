package pers.gwyog.gtneioreplugin.plugin;

import java.util.ArrayList;
import java.util.List;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.TemplateRecipeHandler.CachedRecipe;
import gregtech.api.GregTech_API;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.plugin.PluginAsteroidStat.CachedAsteroidStatRecipe;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GTOreLayerHelper.OreLayerWrapper;

public class PluginIEVeinStat extends PluginBase {
    
    public class CachedIEVeinStatRecipe extends CachedRecipe {
        public String veinName;
        public PositionedStack positionedStackPrimary;
        public PositionedStack positionedStackSecondary;
        public PositionedStack positionedStackBetween;
        public PositionedStack positionedStackSporadic;
            
        public CachedIEVeinStatRecipe(String veinName, ItemStack stackListPrimary, ItemStack stackListSecondary,
                ItemStack stackListBetween, ItemStack stackListSporadic) {
            this.veinName = veinName;
            positionedStackPrimary = new PositionedStack(stackListPrimary, 2, 0);
            positionedStackSecondary = new PositionedStack(stackListSecondary, 22, 0);
            positionedStackBetween = new PositionedStack(stackListBetween, 42, 0);
            positionedStackSporadic = new PositionedStack(stackListSporadic, 62, 0);
        }
        
        @Override
        public List<PositionedStack> getIngredients() {
            List<PositionedStack> ingredientsList = new ArrayList<PositionedStack>();
            ingredientsList.add(positionedStackPrimary);
            ingredientsList.add(positionedStackSecondary);
            ingredientsList.add(positionedStackBetween);
            ingredientsList.add(positionedStackSporadic);
            return ingredientsList;
        }
        
        @Override
        public PositionedStack getResult() {
            return null;
        }
        
    }
    
    @Override
    public void drawExtras(int recipe) {
        CachedIEVeinStatRecipe crecipe = (CachedIEVeinStatRecipe) this.arecipes.get(recipe);
        OreLayerWrapper oreLayer = GTOreLayerHelper.mapOreLayerWrapper.get(crecipe.veinName);
        int stringLength1 = GuiDraw.getStringWidth(I18n.format("gtnop.gui.nei.weightedChance") + ": ");
        int stringLength2 = GuiDraw.getStringWidth("40%");
        int beginXCoord = (stringLength1-stringLength2)/2;
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.veinName") + ": " + getLocalizedVeinName(oreLayer.veinName), 2, 18, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.ieVeinComponent") + ": ", 2, 31, 0x404040, false);
        GuiDraw.drawString("40%", beginXCoord, 44, 0x404040, false);
        GuiDraw.drawString(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.primaryMeta)), 2+stringLength1, 44, 0x404040, false);
        GuiDraw.drawString("40%", beginXCoord, 57, 0x404040, false);
        GuiDraw.drawString(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.secondaryMeta)), 2+stringLength1, 57, 0x404040, false);
        GuiDraw.drawString("15%", beginXCoord, 70, 0x404040, false);
        GuiDraw.drawString(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.betweenMeta)), 2+stringLength1, 70, 0x404040, false);
        GuiDraw.drawString("5% ", beginXCoord, 83, 0x404040, false);
        GuiDraw.drawString(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(oreLayer.sporadicMeta)), 2+stringLength1, 83, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.weightedChance") + ": " + oreLayer.weightedIEChance, 2, 96, 0x404040, false);
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.fromMod") + ": " + "Immersive Engineering", 2, 109, 0x404040, false);
        GuiDraw.drawStringR(EnumChatFormatting.BOLD + I18n.format("gtnop.gui.nei.seeAll"), getGuiWidth()-3, 5, 0x404040, false);
    }   
    
    public String getLocalizedVeinName(String unlocalizedName) {
        if (unlocalizedName.startsWith("ore.mix.custom."))
            return I18n.format("gtnop.ore.custom.name") + I18n.format("gtnop.ore.vein.name") + unlocalizedName.substring(15);
        else
            return I18n.format("gtnop." + unlocalizedName) + I18n.format("gtnop.ore.vein.name");
    }
    
    @Override
    public void loadCraftingRecipes(String outputId, Object... results) {
        if (outputId.equals(getOutputId())) {
            OreLayerWrapper oreLayerWrapper;
            for (String veinName: GTOreLayerHelper.mapOreLayerWrapper.keySet()) {
                oreLayerWrapper = GTOreLayerHelper.mapOreLayerWrapper.get(veinName);
                if (oreLayerWrapper.genIEVein) {
                    ItemStack stackPrimary = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.primaryMeta);
                    ItemStack stackSecondary = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.secondaryMeta);
                    ItemStack stackBetween = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.betweenMeta);
                    ItemStack stackSporadic = new ItemStack(GregTech_API.sBlockOres1, 1, oreLayerWrapper.sporadicMeta);
                    this.arecipes.add(new CachedIEVeinStatRecipe(veinName, stackPrimary, stackSecondary, stackBetween, stackSporadic));
                }
            }
        }
        else
            super.loadCraftingRecipes(outputId, results);
    }
    
    @Override
    public void loadCraftingRecipes(ItemStack stack) {
        if (stack.getUnlocalizedName().startsWith("gt.blockores")) {
            if (stack.getItemDamage()>16000) {
                super.loadCraftingRecipes(stack);
                return;
            }
            short baseMeta = (short)(stack.getItemDamage() % 1000);
            for (OreLayerWrapper worldGen: GTOreLayerHelper.mapOreLayerWrapper.values()) {
                if (worldGen.primaryMeta == baseMeta || worldGen.secondaryMeta == baseMeta || worldGen.betweenMeta == baseMeta || worldGen.sporadicMeta == baseMeta) {
                    if (worldGen.genIEVein) {
                        ItemStack stackPrimary = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.primaryMeta);
                        ItemStack stackSecondary = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.secondaryMeta);
                        ItemStack stackBetween = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.betweenMeta);
                        ItemStack stackSporadic = new ItemStack(GregTech_API.sBlockOres1, 1, worldGen.sporadicMeta);
                        this.arecipes.add(new CachedIEVeinStatRecipe(worldGen.veinName, stackPrimary, stackSecondary, stackBetween, stackSporadic));
                    }
                }
            }
        }
        else
            super.loadCraftingRecipes(stack);
    }
    
    @Override
    public String getOutputId() {
        return "GTOrePluginIEVeinCompat";
    }
    
    @Override
    public String getRecipeName() {
        return I18n.format("gtnop.gui.ieVeinStat.name");
    }
}

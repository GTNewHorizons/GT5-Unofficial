package pers.gwyog.gtneioreplugin.util;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.Level;

import codechicken.nei.recipe.GuiRecipe;
import cpw.mods.fml.common.FMLLog;

public class GuiRecipeHelper {

    private static final String INIT_ERROR = "pers.gwyog.gtneioreplugin.util.GuiRecipeHelper failed setting up reflection : ";
    private static final int DEFAULT_XSIZE = 176;

    public static Field xSizeField;
    public static Field guiLeftField;
    public static Field guiTopField;

    /**
     * Access the xSize field of a GuiRecipe instance, or use a fallback hardcoded value if that fails
     *
     * @param gui GuiRecipe object
     * @return Integer value of the xSize field of that object
     */
    public static int getXSize(GuiRecipe<?> gui) {
        if (xSizeField != null) {
            try {
                return (int) xSizeField.get(gui);
            } catch (IllegalAccessException e) {
                // Fail silently, hoping that it it fails it will be during initialization
            }
        }

        // Fallback should work unless codechicken.nei.recipe.GuiRecipe implementation changes
        return DEFAULT_XSIZE;
    }

    /**
     * Access the guiLeft field of a GuiRecipe instance, or use a fallback hardcoded value if that fails
     *
     * @param gui GuiRecipe object
     * @return Integer value of the guiLeft field of that object
     */
    public static int getGuiLeft(GuiRecipe<?> gui) {
        if (guiLeftField != null) {
            try {
                return (int) guiLeftField.get(gui);
            } catch (IllegalAccessException e) {
                // Fail silently, hoping that it it fails it will be during initialization
            }
        }

        // Fallback should work unless codechicken.nei.recipe.GuiRecipe implementation changes
        return (Minecraft.getMinecraft().currentScreen.width - DEFAULT_XSIZE) / 2;
    }

    /**
     * Access the guiTop field of a GuiRecipe instance, or use a fallback hardcoded value if that fails
     *
     * @param gui GuiRecipe object
     * @return Integer value of the guiTop field of that object
     */
    public static int getGuiTop(GuiRecipe<?> gui) {
        if (guiTopField != null) {
            try {
                return (int) guiTopField.get(gui);
            } catch (IllegalAccessException e) {
                // Fail silently, hoping that it it fails it will be during initialization
            }
        }

        // Fallback should work unless codechicken.nei.recipe.GuiRecipe implementation changes
        int height = Minecraft.getMinecraft().currentScreen.height;
        int ySize = Math.min(Math.max(height - 68, 166), 370);
        return (height - ySize) / 2 + 10;
    }

    /**
     * Initialize the GuiRecipe Field accessors through reflection
     */
    public GuiRecipeHelper() {
        @SuppressWarnings("rawtypes")
        Class<GuiRecipe> guiRecipeClass = GuiRecipe.class;
        try {
            guiLeftField = guiRecipeClass.getField("guiLeft");
            guiLeftField.setAccessible(true);
            guiTopField = guiRecipeClass.getField("guiTop");
            guiTopField.setAccessible(true);
            xSizeField = guiRecipeClass.getField("xSize");
            xSizeField.setAccessible(true);
        } catch (NoSuchFieldException | SecurityException e) {
            FMLLog.log(Level.ERROR, INIT_ERROR + e.getMessage());
        }
    }
}

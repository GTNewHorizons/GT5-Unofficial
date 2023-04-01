package gregtech.api.util;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.google.common.collect.Lists;
import cpw.mods.fml.relauncher.ReflectionHelper;

public class GT_UtilityClient {

    private static final Field isDrawingField = ReflectionHelper.findField(
            Tessellator.class,
            "isDrawing",
            "field_78415_z");

    public static boolean isDrawing(Tessellator tess) {
        try {
            return isDrawingField.getBoolean(tess);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static List<String> getTooltip(ItemStack aStack, boolean aGuiStyle) {
        try {
            List<String> tooltip = aStack.getTooltip(
                    Minecraft.getMinecraft().thePlayer,
                    Minecraft.getMinecraft().gameSettings.advancedItemTooltips);
            if (aGuiStyle) {
                tooltip.set(
                        0,
                        (aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor
                                + tooltip.get(0));
                for (int i = 1; i < tooltip.size(); i++) {
                    tooltip.set(i, EnumChatFormatting.GRAY + tooltip.get(i));
                }
            }
            return tooltip;
        } catch (RuntimeException e) {
            // Collections.singletonList() can not be added to. we don't want that
            if (aGuiStyle) return Lists.newArrayList(
                    (aStack.getRarity() == null ? EnumRarity.common : aStack.getRarity()).rarityColor
                            + aStack.getDisplayName());
            return Lists.newArrayList(aStack.getDisplayName());
        }
    }
}

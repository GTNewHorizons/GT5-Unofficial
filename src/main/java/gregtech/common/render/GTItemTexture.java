package gregtech.common.render;

import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;

import gregtech.api.interfaces.IItemTexture;

public class GTItemTexture implements IItemTexture {

    public final Function<ItemStack, IIcon> icon;
    public final Function<ItemStack, Color> colour;

    public GTItemTexture(Function<ItemStack, IIcon> icon, Function<ItemStack, Color> colour) {
        this.icon = icon;
        this.colour = colour;
    }

    @Override
    public void render(IItemRenderer.ItemRenderType type, ItemStack stack) {
        Color colour = this.colour.apply(stack);
        IIcon icon = this.icon.apply(stack);

        if (colour == null || icon == null) return;

        GL11.glColor3f(Byte.toUnsignedInt(colour.getRedByte()) / 256f, Byte.toUnsignedInt(colour.getGreenByte()) / 256f, Byte.toUnsignedInt(colour.getBlueByte()) / 256f);
        GTRenderUtil.renderItem(type, icon);
    }
}

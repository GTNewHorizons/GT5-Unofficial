package gtPlusPlus.core.item.base.cell;

import java.awt.Color;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

public class BaseItemCell extends BaseItemComponent {

    public BaseItemCell(final Material material) {
        super(material, BaseItemComponent.ComponentTypes.CELL);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass == 1) {
            return Utils.rgbtoHexValue(255, 255, 255);
        }
        if (this.componentMaterial == null) {
            if (extraData != null) {
                return Utils.rgbtoHexValue(extraData[0], extraData[1], extraData[2]);
            }
            return this.componentColour;
        }

        if (this.componentMaterial.getRGBA()[3] <= 1) {
            return this.componentColour;
        } else {
            // Mild Glow Effect
            if (this.componentMaterial.getRGBA()[3] == 2) {
                // 4 sec cycle, 200 control point. 20ms interval.
                int currentFrame = (int) ((System.nanoTime() % 4_000_000_000L) / 20_000_000L);
                int value = currentFrame < 50 ? currentFrame + 1
                    : currentFrame < 100 ? 50 : currentFrame < 150 ? 149 - currentFrame : 0;
                return Utils.rgbtoHexValue(
                    Math.min(255, Math.max(componentMaterial.getRGBA()[0] + value, 0)),
                    Math.min(255, Math.max(componentMaterial.getRGBA()[1] + value, 0)),
                    Math.min(255, Math.max(componentMaterial.getRGBA()[2] + value, 0)));
            }

            // Rainbow Hue Cycle
            else if (this.componentMaterial.getRGBA()[3] == 3) {
                return Color.HSBtoRGB((float) (System.nanoTime() % 8_000_000_000L) / 8_000_000_000f, 1, 1);
            }
        }
        return this.componentColour;
    }
}

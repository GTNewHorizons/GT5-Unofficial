package gtPlusPlus.core.item.base.cell;

import java.awt.Color;

import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

public class BaseItemPlasmaCell extends BaseItemComponent {

    private int tickCounter = 0;

    public BaseItemPlasmaCell(final Material material) {
        super(material, ComponentTypes.PLASMACELL);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
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

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.componentMaterial != null) {
            if (!world.isRemote) {
                final int tickCounterMax = 200;
                if (this.tickCounter < tickCounterMax) {
                    this.tickCounter++;
                } else {
                    entityHolding.attackEntityFrom(DamageSource.onFire, 2);
                    this.tickCounter = 0;
                }
            }
        }
        super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
    }
}

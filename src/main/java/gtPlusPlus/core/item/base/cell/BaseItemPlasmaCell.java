package gtPlusPlus.core.item.base.cell;

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
            return getMaterialCustomColor(this.componentMaterial);
        }
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

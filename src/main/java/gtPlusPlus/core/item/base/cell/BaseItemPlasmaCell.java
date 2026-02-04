package gtPlusPlus.core.item.base.cell;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

public class BaseItemPlasmaCell extends BaseItemComponent {

    private IIcon base;
    private IIcon overlay;
    ComponentTypes PlasmaCell = ComponentTypes.PLASMACELL;
    private int tickCounter = 0;
    private final int tickCounterMax = 200;

    public BaseItemPlasmaCell(final Material material) {
        super(material, ComponentTypes.PLASMACELL);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        this.base = i.registerIcon(GTPlusPlus.ID + ":" + "item" + this.PlasmaCell.getComponent());
        this.overlay = i.registerIcon(GTPlusPlus.ID + ":" + "item" + this.PlasmaCell.getComponent() + "_Overlay");
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
        if (renderPass == 0) {
            return Utils.rgbtoHexValue(255, 255, 255);
        }
        return this.componentColour;
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (pass == 0) {
            return this.base;
        }
        return this.overlay;
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.componentMaterial != null) {
            if (!world.isRemote) {
                if (this.tickCounter < this.tickCounterMax) {
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

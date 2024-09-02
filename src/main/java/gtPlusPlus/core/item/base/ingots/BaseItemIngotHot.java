package gtPlusPlus.core.item.base.ingots;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemIngotHot extends BaseItemIngot {

    private final ItemStack outputIngot;
    private int tickCounter = 0;
    private final int tickCounterMax = 200;
    private final int mTier;

    private IIcon base;
    private IIcon overlay;

    public BaseItemIngotHot(final Material material) {
        super(material, ComponentTypes.HOTINGOT);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemIngotHot");
        this.outputIngot = material.getIngot(1);
        this.mTier = material.vTier;
        this.generateRecipe();
    }

    @Override
    public String getItemStackDisplayName(final ItemStack p_77653_1_) {
        return super.getItemStackDisplayName(p_77653_1_);
        // return ("Hot "+this.materialName+ " Ingot");
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        return Utils.rgbtoHexValue(225, 225, 225);
    }

    private void generateRecipe() {
        Logger.WARNING("Adding Vacuum Freezer recipe for a Hot Ingot of " + this.materialName + ".");
        GTValues.RA.stdBuilder()
            .itemInputs(ItemUtils.getSimpleStack(this))
            .itemOutputs(this.outputIngot.copy())
            .duration(Math.max(this.componentMaterial.getMass() * 3L, 1L) * TICKS)
            .eut(this.componentMaterial.vVoltageMultiplier)
            .addTo(vacuumFreezerRecipes);
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.componentMaterial != null) {
            if (entityHolding != null && entityHolding instanceof EntityPlayer) {
                if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                    EntityUtils.applyHeatDamageToEntity(1, world, entityHolding);
                }
            }
        }
        super.onUpdate(iStack, world, entityHolding, p_77663_4_, p_77663_5_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        if (GTPPCore.ConfigSwitches.useGregtechTextures) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void registerIcons(final IIconRegister i) {

        if (GTPPCore.ConfigSwitches.useGregtechTextures) {
            this.base = i.registerIcon(GregTech.ID + ":" + "materialicons/METALLIC/" + "ingotHot");
            this.overlay = i.registerIcon(GregTech.ID + ":" + "materialicons/METALLIC/" + "ingotHot_OVERLAY");
        } else {
            this.base = i
                .registerIcon(GTPlusPlus.ID + ":" + "item" + BaseItemComponent.ComponentTypes.HOTINGOT.getComponent());
        }
        // this.overlay = cellMaterial.getFluid(1000).getFluid().get
    }

    @Override
    public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
        if (pass == 0 && GTPPCore.ConfigSwitches.useGregtechTextures) {
            return this.base;
        } else if (pass == 1 && GTPPCore.ConfigSwitches.useGregtechTextures) {
            return this.overlay;
        } else {
            return this.overlay;
        }
    }
}

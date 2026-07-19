package gtPlusPlus.core.item.base.ingots;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;

public class BaseItemIngotHot extends BaseItemIngot {

    private final ItemStack outputIngot;

    public BaseItemIngotHot(final Material material) {
        super(material, ComponentTypes.HOTINGOT);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemIngotHot");
        this.outputIngot = material.getIngot(1);
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
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(this))
            .itemOutputs(this.outputIngot.copy())
            .duration(Math.max(this.componentMaterial.getMass() * 3L, 1L) * TICKS)
            .eut(this.componentMaterial.voltageMultiplier)
            .addTo(vacuumFreezerRecipes);
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entity, final int p_77663_4_,
        final boolean p_77663_5_) {
        if (this.componentMaterial != null && entity instanceof EntityPlayer player
            && !player.capabilities.isCreativeMode) {
            GTUtility.applyHeatDamageFromItem(player, 1, stack);
        }
        super.onUpdate(stack, world, entity, p_77663_4_, p_77663_5_);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(final IIconRegister i) {
        IIconContainer container = Textures.ItemIcons
            .textureSetWithRegister("METALLIC", "/" + OrePrefixes.ingotHot.getName(), i);
        iconBase = container.getIcon();
        iconOverlay = container.getOverlayIcon();
    }
}

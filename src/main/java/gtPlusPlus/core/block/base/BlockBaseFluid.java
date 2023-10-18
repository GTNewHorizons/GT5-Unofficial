package gtPlusPlus.core.block.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.client.renderer.particle.EntityDropParticleFX;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.itemblock.ItemBlockMeta;
import gtPlusPlus.core.util.Utils;

public class BlockBaseFluid extends BlockFluidClassic {

    private final String name;
    private final IIcon[] textureArray = new IIcon[6];

    protected float particleRed = 1.0F;
    protected float particleGreen = 1.0F;
    protected float particleBlue = 1.0F;

    public BlockBaseFluid(String materialName, Fluid fluid, Material material) {
        super(fluid, material);
        this.setLightOpacity(2);
        this.name = Utils.sanitizeString(materialName);
        this.setBlockName("fluid" + this.name);
        this.setCreativeTab(AddToCreativeTab.tabBlock);
        GameRegistry.registerBlock(this, ItemBlockMeta.class, "fluid" + this.name);
    }

    @Override
    public boolean canCreatureSpawn(EnumCreatureType arg0, IBlockAccess arg1, int arg2, int arg3, int arg4) {
        return false;
    }

    public boolean preInit() {
        return true;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int ordinalSide, int meta) {
        return ordinalSide <= 1 ? this.textureArray[0] : this.textureArray[1];
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister icon) {
        this.textureArray[0] = icon.registerIcon(GTPlusPlus.ID + ":" + "fluid/" + "Fluid_" + this.name + "_Still");
        this.textureArray[1] = icon.registerIcon(GTPlusPlus.ID + ":" + "fluid/" + "Fluid_" + this.name + "_Flow");
    }

    @Override
    @Optional.Method(modid = Mods.Names.C_O_F_H_CORE)
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(World arg0, int arg1, int arg2, int arg3, Random arg4) {
        super.randomDisplayTick(arg0, arg1, arg2, arg3, arg4);
        double arg5 = arg1 + arg4.nextFloat();
        double arg7 = arg2 - 1.05D;
        double arg9 = arg3 + arg4.nextFloat();
        if (super.density < 0) {
            arg7 = arg2 + 2.1D;
        }

        if (arg4.nextInt(20) == 0
                && arg0.isSideSolid(
                        arg1,
                        arg2 + super.densityDir,
                        arg3,
                        super.densityDir == -1 ? ForgeDirection.UP : ForgeDirection.DOWN)
                && !arg0.getBlock(arg1, arg2 + 2 * super.densityDir, arg3).getMaterial().blocksMovement()) {
            EntityDropParticleFX arg11 = new EntityDropParticleFX(
                    arg0,
                    arg5,
                    arg7,
                    arg9,
                    this.particleRed,
                    this.particleGreen,
                    this.particleBlue,
                    super.densityDir);
            FMLClientHandler.instance().getClient().effectRenderer.addEffect(arg11);
        }
    }
}

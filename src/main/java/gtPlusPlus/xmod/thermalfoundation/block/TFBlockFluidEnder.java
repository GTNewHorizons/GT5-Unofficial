package gtPlusPlus.xmod.thermalfoundation.block;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import cofh.core.fluid.BlockFluidCoFHBase;
import cofh.core.util.CoreUtils;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;

public class TFBlockFluidEnder extends BlockFluidCoFHBase {

    public static final Material materialFluidEnder = new MaterialLiquid(MapColor.greenColor);
    private static boolean effect = true;

    public TFBlockFluidEnder() {
        super(GTPlusPlus.ID, TFFluids.fluidEnder, materialFluidEnder, "ender");
        setQuantaPerBlock(4);
        setTickRate(20);

        setHardness(2000.0F);
        setLightOpacity(7);
        setParticleColor(0.05F, 0.2F, 0.2F);
    }

    @Override
    public boolean preInit() {
        GameRegistry.registerBlock(this, "FluidEnder");

        return true;
    }

    @Override
    public void onEntityCollidedWithBlock(World paramWorld, int paramInt1, int paramInt2, int paramInt3,
        Entity paramEntity) {
        if ((!effect) || (paramWorld.isRemote)) {
            return;
        }
        if (paramWorld.getTotalWorldTime() % 8L == 0L) {
            int i = paramInt1 - 8 + paramWorld.rand.nextInt(17);
            int j = paramInt2 + paramWorld.rand.nextInt(8);
            int k = paramInt3 - 8 + paramWorld.rand.nextInt(17);
            if (!paramWorld.getBlock(i, j, k)
                .getMaterial()
                .isSolid()) {
                CoreUtils.teleportEntityTo(paramEntity, i, j, k);
            }
        }
    }

    @Override
    public int getLightValue(IBlockAccess paramIBlockAccess, int paramInt1, int paramInt2, int paramInt3) {
        return TFFluids.fluidEnder.getLuminosity();
    }
}

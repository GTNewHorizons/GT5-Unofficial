package gtPlusPlus.xmod.thermalfoundation.block;

import java.util.Random;

import cofh.core.fluid.BlockFluidInteractive;
import cofh.lib.util.BlockWrapper;
import cofh.lib.util.helpers.ServerHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.thermalfoundation.fluid.TF_Fluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TF_Block_Fluid_Pyrotheum extends BlockFluidInteractive {
	public static final int			LEVELS					= 5;
	public static final Material	materialFluidPyrotheum	= new MaterialLiquid(MapColor.tntColor);
	private static boolean			effect					= true;
	private static boolean			enableSourceFall		= true;
	Random							random					= new Random();

	public TF_Block_Fluid_Pyrotheum() {
		super(CORE.MODID, TF_Fluids.fluidPyrotheum, Material.lava, "pyrotheum");
		this.setQuantaPerBlock(5);
		this.setTickRate(10);

		this.setHardness(1000.0F);
		this.setLightOpacity(1);
		this.setParticleColor(1.0F, 0.7F, 0.15F);
	}

	protected void checkForInteraction(final World paramWorld, final int paramInt1, final int paramInt2,
			final int paramInt3) {
		if (paramWorld.getBlock(paramInt1, paramInt2, paramInt3) != this) {
			return;
		}
		int i = paramInt1;
		int j = paramInt2;
		int k = paramInt3;
		for (int m = 0; m < 6; m++) {
			i = paramInt1 + cofh.lib.util.helpers.BlockHelper.SIDE_COORD_MOD[m][0];
			j = paramInt2 + cofh.lib.util.helpers.BlockHelper.SIDE_COORD_MOD[m][1];
			k = paramInt3 + cofh.lib.util.helpers.BlockHelper.SIDE_COORD_MOD[m][2];

			this.interactWithBlock(paramWorld, i, j, k);
		}
		this.interactWithBlock(paramWorld, paramInt1 - 1, paramInt2, paramInt3 - 1);
		this.interactWithBlock(paramWorld, paramInt1 - 1, paramInt2, paramInt3 + 1);
		this.interactWithBlock(paramWorld, paramInt1 + 1, paramInt2, paramInt3 - 1);
		this.interactWithBlock(paramWorld, paramInt1 + 1, paramInt2, paramInt3 + 1);
	}

	@Override
	public int getFireSpreadSpeed(final IBlockAccess paramIBlockAccess, final int paramInt1, final int paramInt2,
			final int paramInt3, final ForgeDirection paramForgeDirection) {
		return TF_Block_Fluid_Pyrotheum.effect ? 800 : 0;
	}

	@Override
	public int getFlammability(final IBlockAccess paramIBlockAccess, final int paramInt1, final int paramInt2,
			final int paramInt3, final ForgeDirection paramForgeDirection) {
		return 0;
	}

	@Override
	public int getLightValue(final IBlockAccess paramIBlockAccess, final int paramInt1, final int paramInt2,
			final int paramInt3) {
		return TF_Fluids.fluidPyrotheum.getLuminosity();
	}

	protected void interactWithBlock(final World paramWorld, final int paramInt1, final int paramInt2,
			final int paramInt3) {
		final Block localBlock = paramWorld.getBlock(paramInt1, paramInt2, paramInt3);
		if (localBlock == Blocks.air || localBlock == this) {
			return;
		}
		final int i = paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3);
		if (this.hasInteraction(localBlock, i)) {
			final BlockWrapper localBlockWrapper = this.getInteraction(localBlock, i);
			paramWorld.setBlock(paramInt1, paramInt2, paramInt3, localBlockWrapper.block, localBlockWrapper.metadata,
					3);
			this.triggerInteractionEffects(paramWorld, paramInt1, paramInt2, paramInt3);
		}
		else if (localBlock.isFlammable(paramWorld, paramInt1, paramInt2, paramInt3, ForgeDirection.UP)) {
			paramWorld.setBlock(paramInt1, paramInt2, paramInt3, Blocks.fire);
		}
		else if (paramWorld.isSideSolid(paramInt1, paramInt2, paramInt3, ForgeDirection.UP)
				&& paramWorld.isAirBlock(paramInt1, paramInt2 + 1, paramInt3)) {
			paramWorld.setBlock(paramInt1, paramInt2 + 1, paramInt3, Blocks.fire, 0, 3);
		}
	}

	@Override
	public boolean isFireSource(final World paramWorld, final int paramInt1, final int paramInt2, final int paramInt3,
			final ForgeDirection paramForgeDirection) {
		return TF_Block_Fluid_Pyrotheum.effect;
	}

	@Override
	public boolean isFlammable(final IBlockAccess paramIBlockAccess, final int paramInt1, final int paramInt2,
			final int paramInt3, final ForgeDirection paramForgeDirection) {
		return TF_Block_Fluid_Pyrotheum.effect && paramForgeDirection.ordinal() > ForgeDirection.UP.ordinal()
				&& paramIBlockAccess.getBlock(paramInt1, paramInt2 - 1, paramInt3) != this;
	}

	@Override
	public void onEntityCollidedWithBlock(final World paramWorld, final int paramInt1, final int paramInt2,
			final int paramInt3, final Entity paramEntity) {
		if (!TF_Block_Fluid_Pyrotheum.effect) {
			return;
		}
		if (ServerHelper.isClientWorld(paramWorld)) {
			return;
		}
		if (!(paramEntity instanceof EntityPlayer)) {
			if (paramEntity instanceof EntityCreeper) {
				paramWorld.createExplosion(paramEntity, paramEntity.posX, paramEntity.posY, paramEntity.posZ, 6.0F,
						paramEntity.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing"));
				paramEntity.setDead();
			}
		}
	}

	@Override
	public boolean preInit() {
		GameRegistry.registerBlock(this, "FluidPyrotheum");

		this.addInteraction(Blocks.cobblestone, Blocks.stone);
		this.addInteraction(Blocks.grass, Blocks.dirt);
		this.addInteraction(Blocks.sand, Blocks.glass);
		this.addInteraction(Blocks.water, Blocks.stone);
		this.addInteraction(Blocks.flowing_water, Blocks.stone);
		this.addInteraction(Blocks.clay, Blocks.hardened_clay);
		this.addInteraction(Blocks.ice, Blocks.stone);
		this.addInteraction(Blocks.snow, Blocks.air);
		this.addInteraction(Blocks.snow_layer, Blocks.air);
		for (int i = 0; i < 8; i++) {
			this.addInteraction(Blocks.stone_stairs, i, Blocks.stone_brick_stairs, i);
		}
		final String str1 = "Fluid.Pyrotheum";
		String str2 = "Enable this for Fluid Pyrotheum to be worse than lava.";
		TF_Block_Fluid_Pyrotheum.effect = true;

		str2 = "Enable this for Fluid Pyrotheum Source blocks to gradually fall downwards.";
		TF_Block_Fluid_Pyrotheum.enableSourceFall = true;

		return true;
	}

	protected void triggerInteractionEffects(final World paramWorld, final int paramInt1, final int paramInt2,
			final int paramInt3) {
		if (this.random.nextInt(16) == 0) {
			paramWorld.playSoundEffect(paramInt1 + 0.5F, paramInt2 + 0.5F, paramInt3 + 0.5F, "random.fizz", 0.5F,
					2.2F + (paramWorld.rand.nextFloat() - paramWorld.rand.nextFloat()) * 0.8F);
		}
	}

	@Override
	public void updateTick(final World paramWorld, final int paramInt1, final int paramInt2, final int paramInt3,
			final Random paramRandom) {
		if (TF_Block_Fluid_Pyrotheum.effect) {
			this.checkForInteraction(paramWorld, paramInt1, paramInt2, paramInt3);
		}
		if (TF_Block_Fluid_Pyrotheum.enableSourceFall
				&& paramWorld.getBlockMetadata(paramInt1, paramInt2, paramInt3) == 0) {
			final Block localBlock = paramWorld.getBlock(paramInt1, paramInt2 + this.densityDir, paramInt3);
			final int i = paramWorld.getBlockMetadata(paramInt1, paramInt2 + this.densityDir, paramInt3);
			if (localBlock == this && i != 0 || localBlock.isFlammable(paramWorld, paramInt1,
					paramInt2 + this.densityDir, paramInt3, ForgeDirection.UP)) {
				paramWorld.setBlock(paramInt1, paramInt2 + this.densityDir, paramInt3, this, 0, 3);
				paramWorld.setBlockToAir(paramInt1, paramInt2, paramInt3);
				return;
			}
		}
		super.updateTick(paramWorld, paramInt1, paramInt2, paramInt3, paramRandom);
	}
}

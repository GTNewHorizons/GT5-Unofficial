package gtPlusPlus.xmod.ic2.block.kieticgenerator.tileentity;

import java.util.List;
import java.util.Vector;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCache;

import ic2.api.energy.tile.IKineticSource;
import ic2.api.item.IKineticRotor;
import ic2.api.item.IKineticRotor.GearboxType;
import ic2.core.*;
import ic2.core.block.invslot.InvSlotConsumableKineticRotor;
import ic2.core.block.kineticgenerator.container.ContainerWindKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.util.Util;
import net.minecraftforge.common.util.ForgeDirection;

public class TileEntityKineticWindGenerator
extends TileEntityWindKineticGenerator
implements IKineticSource, IHasGui
{
	public final InvSlotConsumableKineticRotor rotorSlot;
	private double windStrength;
	private int obstructedCrossSection;
	private int crossSection;
	private int updateTicker;
	private float rotationSpeed;
	private static final double efficiencyRollOffExponent = 2.0D;
	private static final int nominalRotationPeriod = 500;

	public TileEntityKineticWindGenerator()
	{
		this.updateTicker = IC2.random.nextInt(this.getTickRate());
		this.rotorSlot = new InvSlotConsumableKineticRotor(this, "rotorslot", 0, null, 1, null, GearboxType.WIND);
	}


	public void update2Entity()
	{
		super.updateEntity();

		assert (IC2.platform.isSimulating());
		if ((this.updateTicker++ % this.getTickRate()) != 0) {
			return;
		}
		boolean needsInvUpdate = false;
		if (!this.rotorSlot.isEmpty())
		{
			if (this.checkSpace(1, true) == 0)
			{
				if (this.getActive() != true) {
					this.setActive(true);
				}
				needsInvUpdate = true;
			}
			else
			{
				if (this.getActive()) {
					this.setActive(false);
				}
				needsInvUpdate = true;
			}
		}
		else
		{
			if (this.getActive()) {
				this.setActive(false);
			}
			needsInvUpdate = true;
		}
		if (this.getActive())
		{
			this.crossSection = (((this.getRotorDiameter() / 2) * 2 * 2) + 1);

			this.crossSection *= this.crossSection;
			this.obstructedCrossSection = this.checkSpace(this.getRotorDiameter() * 3, false);
			if ((this.obstructedCrossSection > 0) && (this.obstructedCrossSection <= ((this.getRotorDiameter() + 1) / 2))) {
				this.obstructedCrossSection = 0;
			} else if (this.obstructedCrossSection < 0) {
				this.obstructedCrossSection = this.crossSection;
			}
			this.windStrength = this.calcWindStrength();

			final float speed = (float)Util.limit((this.windStrength - this.getMinWindStrength()) / this.getMaxWindStrength(), 0.0D, 2.0D);


			this.setRotationSpeed(speed*2);
			if (this.windStrength >= this.getMinWindStrength()) {
				if (this.windStrength <= this.getMaxWindStrength()) {
					this.rotorSlot.damage(1, false);
				} else {
					this.rotorSlot.damage(4, false);
				}
			}
		}
	}



	@Override
	public List<String> getNetworkedFields()
	{
		final List<String> ret = new Vector<>(1);

		ret.add("rotationSpeed");
		ret.add("rotorSlot");
		ret.addAll(super.getNetworkedFields());

		return ret;
	}

	@Override
	public ContainerBase<TileEntityWindKineticGenerator> getGuiContainer(final EntityPlayer entityPlayer)
	{
		return new ContainerWindKineticGenerator(entityPlayer, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(final EntityPlayer entityPlayer, final boolean isAdmin)
	{
		return new GuiWindKineticGenerator(new ContainerWindKineticGenerator(entityPlayer, this));
	}

	@Override
	public boolean facingMatchesDirection(final ForgeDirection direction)
	{
		return direction.ordinal() == this.getFacing();
	}

	@Override
	public boolean wrenchCanSetFacing(final EntityPlayer entityPlayer, final int side)
	{
		if ((side == 0) || (side == 1)) {
			return false;
		}
		return this.getFacing() != side;
	}

	@Override
	public void setFacing(final short side)
	{
		super.setFacing(side);
	}

	public boolean enableUpdateEntity()
	{
		return IC2.platform.isSimulating();
	}

	@Override
	public String getRotorhealth()
	{
		if (!this.rotorSlot.isEmpty()) {
			return StatCollector.translateToLocalFormatted("ic2.WindKineticGenerator.gui.rotorhealth", new Object[] { Integer.valueOf((int)(100.0F - ((this.rotorSlot.get().getItemDamage() / this.rotorSlot.get().getMaxDamage()) * 100.0F))) });
		}
		return "";
	}

	@Override
	public int maxrequestkineticenergyTick(final ForgeDirection directionFrom)
	{
		return this.getKuOutput();
	}

	@Override
	public int requestkineticenergy(final ForgeDirection directionFrom, final int requestkineticenergy)
	{
		if (this.facingMatchesDirection(directionFrom.getOpposite())) {
			return Math.min(requestkineticenergy, this.getKuOutput());
		}
		return 0;
	}

	@Override
	public String getInventoryName()
	{
		return "Advanced Kinetic Wind Generator";
	}

	@Override
	public void onGuiClosed(final EntityPlayer entityPlayer) {}

	@Override
	public boolean shouldRenderInPass(final int pass)
	{
		return pass == 0;
	}

	@Override
	public int checkSpace(int length, final boolean onlyrotor)
	{
		int box = this.getRotorDiameter() / 2;
		int lentemp = 0;
		if (onlyrotor)
		{
			length = 1;
			lentemp = length + 1;
		}
		if (!onlyrotor) {
			box *= 2;
		}
		final ForgeDirection fwdDir = ForgeDirection.VALID_DIRECTIONS[this.getFacing()];
		final ForgeDirection rightDir = fwdDir.getRotation(ForgeDirection.DOWN);

		final int xMaxDist = Math.abs((length * fwdDir.offsetX) + (box * rightDir.offsetX));

		final int zMaxDist = Math.abs((length * fwdDir.offsetZ) + (box * rightDir.offsetZ));


		final ChunkCache chunkCache = new ChunkCache(this.worldObj, this.xCoord - xMaxDist, this.yCoord - box, this.zCoord - zMaxDist, this.xCoord + xMaxDist, this.yCoord + box, this.zCoord + zMaxDist, 0);



		int ret = 0;
		for (int up = -box; up <= box; up++)
		{
			final int y = this.yCoord + up;
			for (int right = -box; right <= box; right++)
			{
				boolean occupied = false;
				for (int fwd = lentemp - length; fwd <= length; fwd++)
				{
					final int x = this.xCoord + (fwd * fwdDir.offsetX) + (right * rightDir.offsetX);

					final int z = this.zCoord + (fwd * fwdDir.offsetZ) + (right * rightDir.offsetZ);


					assert (Math.abs(x - this.xCoord) <= xMaxDist);
					assert (Math.abs(z - this.zCoord) <= zMaxDist);

					final Block block = chunkCache.getBlock(x, y, z);
					if (!block.isAir(chunkCache, x, y, z))
					{
						occupied = true;
						if (((up != 0) || (right != 0) || (fwd != 0)) && ((chunkCache.getTileEntity(x, y, z) instanceof TileEntityKineticWindGenerator)) && (!onlyrotor)) {
							return -1;
						}
					}
				}
				if (occupied) {
					ret++;
				}
			}
		}
		return ret;
	}

	@Override
	public boolean checkrotor()
	{
		return !this.rotorSlot.isEmpty();
	}

	@Override
	public boolean rotorspace()
	{
		return this.checkSpace(1, true) == 0;
	}

	private void setRotationSpeed(final float speed)
	{
		if (this.rotationSpeed != speed)
		{
			this.rotationSpeed = speed;
			IC2.network.get().updateTileEntityField(this, "rotationSpeed");
		}
	}

	@Override
	public int getTickRate()
	{
		return 32;
	}

	@Override
	public double calcWindStrength()
	{
		double windStr = WorldData.get(this.worldObj).windSim.getWindAt(this.yCoord);

		windStr *= (1.0D - Math.pow(this.obstructedCrossSection / this.crossSection, 2.0D));


		return Math.max(0.0D, windStr);
	}

	@Override
	public float getAngle()
	{
		if (this.rotationSpeed > 0.0F)
		{
			final long period = (long) (5.0E+008F / this.rotationSpeed);


			return ((float)(System.nanoTime() % period) / (float)period) * 360.0F;
		}
		return 0.0F;
	}

	@Override
	public float getefficiency()
	{
		final ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticRotor))) {
			return (float) (((IKineticRotor)stack.getItem()).getEfficiency(stack)*1.5);
		}
		return 0.0F;
	}

	@Override
	public int getMinWindStrength()
	{
		final ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticRotor))) {
			return ((IKineticRotor)stack.getItem()).getMinWindStrength(stack)/2;
		}
		return 0;
	}

	@Override
	public int getMaxWindStrength()
	{
		final ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticRotor))) {
			return ((IKineticRotor)stack.getItem()).getMaxWindStrength(stack)*2;
		}
		return 0;
	}

	@Override
	public int getRotorDiameter()
	{
		final ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticRotor))) {
			return ((IKineticRotor)stack.getItem()).getDiameter(stack)/2;
		}
		return 0;
	}

	@Override
	public ResourceLocation getRotorRenderTexture()
	{
		final ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticRotor))) {
			return ((IKineticRotor)stack.getItem()).getRotorRenderTexture(stack);
		}
		return new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorWoodmodel.png");
	}

	@Override
	public boolean guiisoverload()
	{
		if (this.windStrength > this.getMaxWindStrength()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean guiisminWindStrength()
	{
		return this.windStrength >= this.getMinWindStrength();
	}

	@Override
	public int getKuOutput()
	{
		if ((this.windStrength >= this.getMinWindStrength()) && (this.getActive())) {
			return (int)(this.windStrength * 50.0D * this.getefficiency());
		}
		return 0;
	}

	@Override
	public int getWindStrength()
	{
		return (int)this.windStrength;
	}
}

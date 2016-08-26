package miscutil.xmod.ic2.block.kieticgenerator.tileentity;

import ic2.api.energy.tile.IKineticSource;
import ic2.api.item.IKineticWindRotor;
import ic2.core.ContainerBase;
import ic2.core.IC2;
import ic2.core.IHasGui;
import ic2.core.WorldData;
import ic2.core.block.invslot.InvSlotConsumableIKineticWindRotor;
import ic2.core.block.kineticgenerator.container.ContainerWindKineticGenerator;
import ic2.core.block.kineticgenerator.gui.GuiWindKineticGenerator;
import ic2.core.block.kineticgenerator.tileentity.TileEntityWindKineticGenerator;
import ic2.core.network.NetworkManager;
import ic2.core.util.Util;

import java.util.List;
import java.util.Vector;

import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkCache;
import net.minecraftforge.common.util.ForgeDirection;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityKineticWindGenerator
extends TileEntityWindKineticGenerator
implements IKineticSource, IHasGui
{
	public final InvSlotConsumableIKineticWindRotor rotorSlot;
	private double windStrength;
	private int obstructedCrossSection;
	private int crossSection;
	private int updateTicker;
	private float rotationSpeed;
	private static final double efficiencyRollOffExponent = 2.0D;
	private static final int nominalRotationPeriod = 500;

	public TileEntityKineticWindGenerator()
	{
		this.updateTicker = IC2.random.nextInt(getTickRate());
		this.rotorSlot = new InvSlotConsumableIKineticWindRotor(this, "rotorslot", 0, 1);
	}

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		assert (IC2.platform.isSimulating());
		if (this.updateTicker++ % getTickRate() != 0) {
			return;
		}
		boolean needsInvUpdate = false;
		if (!this.rotorSlot.isEmpty())
		{
			if (checkSpace(1, true) == 0)
			{
				if (getActive() != true) {
					setActive(true);
				}
				needsInvUpdate = true;
			}
			else
			{
				if (getActive()) {
					setActive(false);
				}
				needsInvUpdate = true;
			}
		}
		else
		{
			if (getActive()) {
				setActive(false);
			}
			needsInvUpdate = true;
		}
		if (getActive())
		{
			this.crossSection = (getRotorDiameter() / 2 * 2 * 2 + 1);

			this.crossSection *= this.crossSection;
			this.obstructedCrossSection = checkSpace(getRotorDiameter() * 3, false);
			if ((this.obstructedCrossSection > 0) && (this.obstructedCrossSection <= (getRotorDiameter() + 1) / 2)) {
				this.obstructedCrossSection = 0;
			} else if (this.obstructedCrossSection < 0) {
				this.obstructedCrossSection = this.crossSection;
			}
			this.windStrength = calcWindStrength();

			float speed = (float)Util.limit((this.windStrength - getMinWindStrength()) / getMaxWindStrength(), 0.0D, 2.0D);


			setRotationSpeed(speed*2);
			if (this.windStrength >= getMinWindStrength()) {
				if (this.windStrength <= getMaxWindStrength()) {
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
		List<String> ret = new Vector<String>(1);

		ret.add("rotationSpeed");
		ret.add("rotorSlot");
		ret.addAll(super.getNetworkedFields());

		return ret;
	}

	@Override
	public ContainerBase<TileEntityWindKineticGenerator> getGuiContainer(EntityPlayer entityPlayer)
	{
		return new ContainerWindKineticGenerator(entityPlayer, this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public GuiScreen getGui(EntityPlayer entityPlayer, boolean isAdmin)
	{
		return new GuiWindKineticGenerator(new ContainerWindKineticGenerator(entityPlayer, this));
	}

	@Override
	public boolean facingMatchesDirection(ForgeDirection direction)
	{
		return direction.ordinal() == getFacing();
	}

	@Override
	public boolean wrenchCanSetFacing(EntityPlayer entityPlayer, int side)
	{
		if ((side == 0) || (side == 1)) {
			return false;
		}
		return getFacing() != side;
	}

	@Override
	public void setFacing(short side)
	{
		super.setFacing(side);
	}

	@Override
	public boolean enableUpdateEntity()
	{
		return IC2.platform.isSimulating();
	}

	@Override
	public String getRotorhealth()
	{
		if (!this.rotorSlot.isEmpty()) {
			return StatCollector.translateToLocalFormatted("ic2.WindKineticGenerator.gui.rotorhealth", new Object[] { Integer.valueOf((int)(100.0F - this.rotorSlot.get().getItemDamage() / this.rotorSlot.get().getMaxDamage() * 100.0F)) });
		}
		return "";
	}

	@Override
	public int maxrequestkineticenergyTick(ForgeDirection directionFrom)
	{
		return getKuOutput();
	}

	@Override
	public int requestkineticenergy(ForgeDirection directionFrom, int requestkineticenergy)
	{
		if (facingMatchesDirection(directionFrom.getOpposite())) {
			return Math.min(requestkineticenergy, getKuOutput());
		}
		return 0;
	}

	@Override
	public String getInventoryName()
	{
		return "Advanced Kinetic Wind Generator";
	}

	@Override
	public void onGuiClosed(EntityPlayer entityPlayer) {}

	@Override
	public boolean shouldRenderInPass(int pass)
	{
		return pass == 0;
	}

	@Override
	public int checkSpace(int length, boolean onlyrotor)
	{
		int box = getRotorDiameter() / 2;
		int lentemp = 0;
		if (onlyrotor)
		{
			length = 1;
			lentemp = length + 1;
		}
		if (!onlyrotor) {
			box *= 2;
		}
		ForgeDirection fwdDir = ForgeDirection.VALID_DIRECTIONS[getFacing()];
		ForgeDirection rightDir = fwdDir.getRotation(ForgeDirection.DOWN);

		int xMaxDist = Math.abs(length * fwdDir.offsetX + box * rightDir.offsetX);

		int zMaxDist = Math.abs(length * fwdDir.offsetZ + box * rightDir.offsetZ);


		ChunkCache chunkCache = new ChunkCache(this.worldObj, this.xCoord - xMaxDist, this.yCoord - box, this.zCoord - zMaxDist, this.xCoord + xMaxDist, this.yCoord + box, this.zCoord + zMaxDist, 0);



		int ret = 0;
		for (int up = -box; up <= box; up++)
		{
			int y = this.yCoord + up;
			for (int right = -box; right <= box; right++)
			{
				boolean occupied = false;
				for (int fwd = lentemp - length; fwd <= length; fwd++)
				{
					int x = this.xCoord + fwd * fwdDir.offsetX + right * rightDir.offsetX;

					int z = this.zCoord + fwd * fwdDir.offsetZ + right * rightDir.offsetZ;


					assert (Math.abs(x - this.xCoord) <= xMaxDist);
					assert (Math.abs(z - this.zCoord) <= zMaxDist);

					Block block = chunkCache.getBlock(x, y, z);
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
		return checkSpace(1, true) == 0;
	}

	private void setRotationSpeed(float speed)
	{
		if (this.rotationSpeed != speed)
		{
			this.rotationSpeed = speed;
			((NetworkManager)IC2.network.get()).updateTileEntityField(this, "rotationSpeed");
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
			long period = (long) (5.0E+008F / this.rotationSpeed);


			return (float)(System.nanoTime() % period) / (float)period * 360.0F;
		}
		return 0.0F;
	}

	@Override
	public float getefficiency()
	{
		ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticWindRotor))) {
			return (float) (((IKineticWindRotor)stack.getItem()).getEfficiency(stack)*1.5);
		}
		return 0.0F;
	}

	@Override
	public int getMinWindStrength()
	{
		ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticWindRotor))) {
			return ((IKineticWindRotor)stack.getItem()).getMinWindStrength(stack)/2;
		}
		return 0;
	}

	@Override
	public int getMaxWindStrength()
	{
		ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticWindRotor))) {
			return ((IKineticWindRotor)stack.getItem()).getMaxWindStrength(stack)*2;
		}
		return 0;
	}

	@Override
	public int getRotorDiameter()
	{
		ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticWindRotor))) {
			return ((IKineticWindRotor)stack.getItem()).getDiameter(stack)/2;
		}
		return 0;
	}

	@Override
	public ResourceLocation getRotorRenderTexture()
	{
		ItemStack stack = this.rotorSlot.get();
		if ((stack != null) && ((stack.getItem() instanceof IKineticWindRotor))) {
			return ((IKineticWindRotor)stack.getItem()).getRotorRenderTexture(stack);
		}
		return new ResourceLocation(IC2.textureDomain, "textures/items/rotors/rotorWoodmodel.png");
	}

	@Override
	public boolean guiisoverload()
	{
		if (this.windStrength > getMaxWindStrength()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean guiisminWindStrength()
	{
		return this.windStrength >= getMinWindStrength();
	}

	@Override
	public int getKuOutput()
	{
		if ((this.windStrength >= getMinWindStrength()) && (getActive())) {
			return (int)(this.windStrength * 50.0D * getefficiency());
		}
		return 0;
	}

	@Override
	public int getWindStrength()
	{
		return (int)this.windStrength;
	}
}

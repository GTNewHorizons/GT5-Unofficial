package gtPlusPlus.xmod.gregtech.common.tileentities.generators.creative;

import static gregtech.api.enums.GT_Values.V;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.apache.commons.lang3.reflect.FieldUtils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMetaTileEntity;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class GregtechMetaCreativeEnergyBuffer extends GregtechMetaEnergyBuffer {


	public GregtechMetaCreativeEnergyBuffer(final String aName, final int aTier,
			final String aDescription, final ITexture[][][] aTextures, final int aSlotCount) {
		super(aName, aTier, aDescription, aTextures, aSlotCount);
		// TODO Auto-generated constructor stub
	}

	public GregtechMetaCreativeEnergyBuffer(final int aID, final String aName,
			final String aNameRegional, final int aTier, final String aDescription, final int aSlotCount) {
		super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
	}

	@Override
	public String[] getDescription() {
		return new String[] {this.mDescription, "Use Screwdriver to change voltage", CORE.GT_Tooltip};
	}

	/*
	 * MACHINE_STEEL_SIDE
	 */
	@Override
	public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
		CustomIcon g = TexturesGtBlock.Casing_Material_RedSteel;
		CustomIcon h = TexturesGtBlock.Casing_Material_Grisium;
		CustomIcon k;
		boolean j = MathUtils.isNumberEven(this.mTier);
		final ITexture[][][] rTextures = new ITexture[2][17][];
		k = j ? g : h;		
		for (byte i = -1; i < 16; i++) {
			rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(k) };
			rTextures[1][i + 1] = new ITexture[] {
					new GT_RenderedTexture(k), this.mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mTier]
							: Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
		}
		return rTextures;
	}

	@Override
	public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity,
			final byte aSide, final byte aFacing, final byte aColorIndex, final boolean aActive,
			final boolean aRedstone) {
		return this.mTextures[aSide == aFacing ? 1 : 0][aColorIndex + 1];
	}

	@Override
	public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
		return new GregtechMetaCreativeEnergyBuffer(this.mName, this.mTier, this.mDescription,
				this.mTextures, this.mInventory.length);
	}

	@Override
	public long getMinimumStoredEU() {
		return 0;
	}

	@Override
	public long maxEUStore() {
		return Long.MAX_VALUE;
	}

	@Override
	public long maxEUInput() {
		return V[this.mTier];
	}

	@Override
	public long maxEUOutput() {
		return V[this.mTier];
	}

	@Override
	public long maxAmperesIn() {
		return 16;
	}

	@Override
	public long maxAmperesOut() {
		return 16;
	}

	@Override public int getProgresstime()							{return Integer.MAX_VALUE;}
	@Override public int maxProgresstime()							{return Integer.MAX_VALUE;}
	@Override public boolean isAccessAllowed(final EntityPlayer aPlayer)	{return true;}

	@Override
	public Object getServerGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public Object getClientGUI(final int aID, final InventoryPlayer aPlayerInventory,
			final IGregTechTileEntity aBaseMetaTileEntity) {
		return null;
	}

	@Override
	public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
		this.getBaseMetaTileEntity().increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		if (aBaseMetaTileEntity.isServerSide()) {
			aBaseMetaTileEntity.increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
		}
	}

	@Override
	public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex, final byte aSide, final ItemStack aStack) {
		return false;
	}

	@Override
	public String[] getInfoData() {
		String[] infoData = super.getInfoData();
		return new String[] {
				infoData[0],
				"THIS IS A CREATIVE ITEM - FOR TESTING | Tier: "+this.mTier,
				infoData[1],
				infoData[2]
		};
	}

	@Override
	public boolean isGivingInformation() {
		return true;
	}

	@Override
	public void saveNBTData(NBTTagCompound aNBT) {
		aNBT.setByte("mTier", this.mTier);
		super.saveNBTData(aNBT);
	}

	@Override
	public void loadNBTData(NBTTagCompound aNBT) {
		super.loadNBTData(aNBT);
		this.mTier = aNBT.getByte("mTier");
	}

	@Override
	public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
		if (this.mTier < 9) {
			this.mTier++;
		}
		else {
			this.mTier = 0;
		}
		this.markDirty();
		try {
			Field field = ReflectionUtils.getField(this.getClass(), "mTextures");				
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			ITexture[][][] V = getTextureSet(null);
			if (V != null) {
				Logger.REFLECTION("Got Valid Textures.");	
				if (this.getBaseMetaTileEntity().isClientSide()) {
					Logger.REFLECTION("Clientside Call.");	
					Logger.REFLECTION("Refreshing Textures on buffer.");
					field.set(this, V);
					Logger.REFLECTION("Refreshed Textures on buffer.");
				}
				else {
					Logger.REFLECTION("Serverside Call.");	
				}
			}
			else {
				Logger.REFLECTION("Bad mTextures setter.");				
			}			
		}
		catch (Throwable t) {
			//Bad refresh.
			t.printStackTrace();
			Logger.REFLECTION("Bad mTextures setter.");
		}
		PlayerUtils.messagePlayer(aPlayer, "Now running at "+GT_Values.VOLTAGE_NAMES[this.mTier]+". ["+MathUtils.isNumberEven(this.mTier)+"]");
		super.onScrewdriverRightClick(aSide, aPlayer, aX, aY, aZ);
	}
}
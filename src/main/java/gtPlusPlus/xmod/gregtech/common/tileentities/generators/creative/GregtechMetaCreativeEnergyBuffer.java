package gtPlusPlus.xmod.gregtech.common.tileentities.generators.creative;

import static gregtech.api.enums.GT_Values.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.reflect.Fields;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;
import gtPlusPlus.xmod.gregtech.common.tileentities.storage.GregtechMetaEnergyBuffer;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 *
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public class GregtechMetaCreativeEnergyBuffer extends GregtechMetaEnergyBuffer {

    private int mVoltageTier = 3;

    public GregtechMetaCreativeEnergyBuffer(final String aName, final int aTier, final String aDescription,
            final ITexture[][][] aTextures, final int aSlotCount) {
        super(aName, aTier, aDescription, aTextures, aSlotCount);
        // TODO Auto-generated constructor stub
    }

    public GregtechMetaCreativeEnergyBuffer(final int aID, final String aName, final String aNameRegional,
            final int aTier, final String aDescription, final int aSlotCount) {
        super(aID, aName, aNameRegional, aTier, aDescription, aSlotCount);
    }

    @Override
    public String[] getDescription() {
        return new String[] { this.mDescription, "Use Screwdriver to change voltage",
                "Hold Shift while using Screwdriver to change amperage", EnumChatFormatting.GREEN + "CREATIVE MACHINE",
                CORE.GT_Tooltip.get() };
    }

    /*
     * MACHINE_STEEL_SIDE
     */
    @Override
    public ITexture[][][] getTextureSet(final ITexture[] aTextures) {
        CustomIcon h = TexturesGtBlock.Casing_Material_RedSteel;
        CustomIcon g = TexturesGtBlock.Casing_Material_Grisium;
        CustomIcon k;
        boolean j = MathUtils.isNumberEven(this.mVoltageTier);
        final ITexture[][][] rTextures = new ITexture[2][17][];
        k = j ? g : h;
        for (byte i = -1; i < 16; i++) {
            rTextures[0][i + 1] = new ITexture[] { new GT_RenderedTexture(k) };
            rTextures[1][i + 1] = new ITexture[] { new GT_RenderedTexture(k),
                    this.mInventory.length > 4 ? Textures.BlockIcons.OVERLAYS_ENERGY_OUT_MULTI[this.mVoltageTier]
                            : Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mVoltageTier] };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
            final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        return this.mTextures[side == facing ? 1 : 0][aColorIndex + 1];
    }

    @Override
    protected void showEnergy(final World worldIn, final EntityPlayer playerIn) {
        final long tempStorage = this.getBaseMetaTileEntity().getStoredEU();
        final double c = ((double) tempStorage / this.maxEUStore()) * 100;
        final double roundOff = Math.round(c * 100.00) / 100.00;
        PlayerUtils.messagePlayer(
                playerIn,
                "Energy: " + GT_Utility
                        .formatNumbers(tempStorage) + " EU at " + V[this.mVoltageTier] + "v (" + roundOff + "%)");
        PlayerUtils.messagePlayer(playerIn, "Amperage: " + GT_Utility.formatNumbers(maxAmperesOut()) + "A");
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaCreativeEnergyBuffer(
                this.mName,
                this.mTier,
                this.mDescription,
                this.mTextures,
                this.mInventory.length);
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
        return V[mVoltageTier];
    }

    @Override
    public long maxEUOutput() {
        return V[mVoltageTier];
    }

    @Override
    public long maxAmperesIn() {
        return aCurrentOutputAmperage;
    }

    @Override
    public long maxAmperesOut() {
        return aCurrentOutputAmperage;
    }

    @Override
    public int getProgresstime() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int maxProgresstime() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isAccessAllowed(final EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void onPostTick(final IGregTechTileEntity aBaseMetaTileEntity, final long aTick) {
        if (aBaseMetaTileEntity.isServerSide()) {
            aBaseMetaTileEntity.increaseStoredEnergyUnits(Integer.MAX_VALUE, true);
        }
    }

    @Override
    public boolean allowPullStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(final IGregTechTileEntity aBaseMetaTileEntity, final int aIndex,
            final ForgeDirection side, final ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getInfoData() {
        String[] infoData = super.getInfoData();
        return new String[] { infoData[0], "THIS IS A CREATIVE ITEM - FOR TESTING | Tier: " + this.mVoltageTier,
                infoData[1], infoData[2] };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mVoltageTier", this.mVoltageTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.mVoltageTier = aNBT.getInteger("mVoltageTier");
        super.loadNBTData(aNBT);
    }

    private static Fields.ClassFields<GregtechMetaCreativeEnergyBuffer>.Field<ITexture[][][]> mTexturesAccessor;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (KeyboardUtils.isShiftKeyDown()) {
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
        } else {
            if (this.mVoltageTier < (GT_Values.VN[9].equals("UHV") ? GT_Values.V.length - 1 : 9)) {
                this.mVoltageTier++;
            } else {
                this.mVoltageTier = 0;
            }
            this.markDirty();
            try {
                if (mTexturesAccessor == null) {
                    mTexturesAccessor = Fields.ofClass(GregtechMetaCreativeEnergyBuffer.class)
                            .getField(Fields.LookupType.PUBLIC, "mTextures", ITexture[][][].class);
                }
                ITexture[][][] V = getTextureSet(null);
                if (V != null) {
                    Logger.REFLECTION("Got Valid Textures.");
                    if (this.getBaseMetaTileEntity().isClientSide()) {
                        Logger.REFLECTION("Clientside Call.");
                        Logger.REFLECTION("Refreshing Textures on buffer.");
                        mTexturesAccessor.setValue(this, V);
                        Logger.REFLECTION("Refreshed Textures on buffer.");
                    } else {
                        Logger.REFLECTION("Serverside Call.");
                    }
                } else {
                    Logger.REFLECTION("Bad mTextures setter.");
                }
            } catch (Throwable t) {
                // Bad refresh.
                t.printStackTrace();
                Logger.REFLECTION("Bad mTextures setter.");
            }
            PlayerUtils.messagePlayer(aPlayer, "Now running at " + GT_Values.VOLTAGE_NAMES[this.mVoltageTier] + ".");
        }
    }
}

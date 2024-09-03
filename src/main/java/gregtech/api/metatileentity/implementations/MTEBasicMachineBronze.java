package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZEBRICKS_TOP;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZE_BOTTOM;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZE_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_BRONZE_TOP;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.Arrays;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.objects.overclockdescriber.SteamOverclockDescriber;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public abstract class MTEBasicMachineBronze extends MTEBasicMachine {

    private static final String TT_machineType = "GT5U.MBTT.MachineType";
    private static final int NEEDS_STEAM_VENTING = 64;
    public boolean mNeedsSteamVenting = false;

    public MTEBasicMachineBronze(int aID, String aName, String aNameRegional, String aDescription, int aInputSlotCount,
        int aOutputSlotCount, boolean aHighPressure) {
        super(aID, aName, aNameRegional, aHighPressure ? 2 : 1, 0, aDescription, aInputSlotCount, aOutputSlotCount);
    }

    public MTEBasicMachineBronze(String aName, String[] aDescription, ITexture[][][] aTextures, int aInputSlotCount,
        int aOutputSlotCount, boolean aHighPressure) {
        super(aName, aHighPressure ? 2 : 1, 0, aDescription, aTextures, aInputSlotCount, aOutputSlotCount);
    }

    protected boolean isBricked() {
        return false;
    }

    @Override
    public OverclockDescriber createOverclockDescriber() {
        return new SteamOverclockDescriber(SteamVariant.BRONZE, 1, 2);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mNeedsSteamVenting", mNeedsSteamVenting);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mNeedsSteamVenting = aNBT.getBoolean("mNeedsSteamVenting");
    }

    @Override
    public boolean isElectric() {
        return false;
    }

    @Override
    public boolean isEnetInput() {
        return false;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return false;
    }

    @Override
    public long maxEUStore() {
        return 0;
    }

    @Override
    public long maxEUInput() {
        return 0;
    }

    @Override
    public int rechargerSlotCount() {
        return 0;
    }

    @Override
    public int dechargerSlotCount() {
        return 0;
    }

    @Override
    public boolean isSteampowered() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return super.isFacingValid(facing) && facing != mMainFacing;
    }

    @Override
    public long getMinimumStoredEU() {
        return 1000;
    }

    @Override
    public long maxSteamStore() {
        return 16000;
    }

    @Override
    public boolean isLiquidInput(ForgeDirection side) {
        return side != mMainFacing;
    }

    @Override
    public boolean isLiquidOutput(ForgeDirection side) {
        return side != mMainFacing;
    }

    @Override
    public boolean doesAutoOutput() {
        return false;
    }

    @Override
    public boolean allowToCheckRecipe() {
        if (mNeedsSteamVenting
            && getBaseMetaTileEntity().getCoverIDAtSide(getBaseMetaTileEntity().getFrontFacing()) == 0
            && !GTUtility.hasBlockHitBox(
                getBaseMetaTileEntity().getWorld(),
                getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1),
                getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1),
                getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1))) {
            sendSound((byte) 9);
            mNeedsSteamVenting = false;
            try {
                for (EntityLivingBase tLiving : getBaseMetaTileEntity().getWorld()
                    .getEntitiesWithinAABB(
                        EntityLivingBase.class,
                        AxisAlignedBB.getBoundingBox(
                            getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1),
                            getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1),
                            getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1),
                            getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 1,
                            getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 1,
                            getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 1))) {
                    GTUtility.applyHeatDamage(tLiving, getSteamDamage());
                }
            } catch (Throwable e) {
                if (D1) e.printStackTrace(GTLog.err);
            }
        }
        return !mNeedsSteamVenting;
    }

    @Override
    public int checkRecipe() {
        GTRecipe tRecipe = getRecipeMap().findRecipe(getBaseMetaTileEntity(), false, TierEU.LV, null, getAllInputs());
        if ((tRecipe != null) && (canOutput(tRecipe.mOutputs))
            && (tRecipe.isRecipeInputEqual(true, null, getAllInputs()))) {
            this.mOutputItems[0] = tRecipe.getOutput(0);
            calculateCustomOverclock(tRecipe);
            return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
        }
        return DID_NOT_FIND_RECIPE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Super already zeroed out setErrorDisplayID, add additional error codes here.
        aBaseMetaTileEntity.setErrorDisplayID(aBaseMetaTileEntity.getErrorDisplayID() | (mNeedsSteamVenting ? 64 : 0));
    }

    @Override
    public void endProcess() {
        if (isSteampowered()) mNeedsSteamVenting = true;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        super.doSound(aIndex, aX, aY, aZ);
        if (aIndex == 9) {
            GTUtility.doSoundAtClient(SoundResource.RANDOM_FIZZ, 5, 1.0F, aX, aY, aZ);

            new ParticleEventBuilder().setIdentifier(ParticleFX.CLOUD)
                .setWorld(getBaseMetaTileEntity().getWorld())
                .setMotion(
                    getBaseMetaTileEntity().getFrontFacing().offsetX / 5.0,
                    getBaseMetaTileEntity().getFrontFacing().offsetY / 5.0,
                    getBaseMetaTileEntity().getFrontFacing().offsetZ / 5.0)
                .<ParticleEventBuilder>times(
                    8,
                    x -> x
                        .setPosition(
                            aX - 0.5 + XSTR_INSTANCE.nextFloat(),
                            aY - 0.5 + XSTR_INSTANCE.nextFloat(),
                            aZ - 0.5 + XSTR_INSTANCE.nextFloat())
                        .run());
        }
    }

    @Override
    public boolean isGivingInformation() {
        return false;
    }

    @Override
    public boolean allowCoverOnSide(ForgeDirection side, GTItemStack aCoverID) {
        return GregTechAPI.getCoverBehaviorNew(aCoverID.toStack())
            .isSimpleCover() && super.allowCoverOnSide(side, aCoverID);
    }

    public float getSteamDamage() {
        return 6.0F * mTier;
    }

    @Override
    public ITexture[] getSideFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getSideFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getFrontFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getFrontFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getTopFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getTopFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getBottomFacingActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getBottomFacingInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)) };
    }

    @Override
    public ITexture[] getBottomFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getBottomFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_BOTTOM : MACHINE_BRONZE_BOTTOM,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTopFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getTopFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_TOP : MACHINE_BRONZE_TOP,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getSideFacingPipeActive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public ITexture[] getSideFacingPipeInactive(byte aColor) {
        return new ITexture[] { TextureFactory.of(
            isBricked() ? MACHINE_BRONZEBRICKS_SIDE : MACHINE_BRONZE_SIDE,
            Dyes.getModulation(aColor, Dyes._NULL.mRGBa)), TextureFactory.of(OVERLAY_PIPE_OUT) };
    }

    @Override
    public SteamVariant getSteamVariant() {
        return SteamVariant.BRONZE;
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.STEAM.apply(getSteamVariant());
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(152, 63));
    }

    @Override
    protected FluidSlotWidget createFluidInputSlot(IDrawable[] backgrounds, Pos2d pos) {
        return null;
    }

    @Override
    protected FluidSlotWidget createFluidOutputSlot(IDrawable[] backgrounds, Pos2d pos) {
        return null;
    }

    @Override
    public String[] getDescription() {
        String[] description = Arrays.copyOf(mDescriptionArray, mDescriptionArray.length + 1);
        description[mDescriptionArray.length] = StatCollector.translateToLocal(TT_machineType) + ": "
            + EnumChatFormatting.YELLOW
            + StatCollector.translateToLocal(this.getRecipeMap().unlocalizedName)
            + EnumChatFormatting.RESET;
        return description;
    }
}

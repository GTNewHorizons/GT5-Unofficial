package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.FLUID_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_COLORS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchInput extends MTEHatch {

    // hatch filter is disabled by default, meaning any fluid can be inserted when in structure.
    public boolean disableFilter = true;
    public RecipeMap<?> mRecipeMap = null;
    private int customCapacity = 0;

    public MTEHatchInput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            4,
            new String[] { GTUtility.nestParams("gt.te.input_hatch.desc", formatNumber(8000L * (1L << aTier))) });
    }

    public MTEHatchInput(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        this(aID, 3, aName, aNameRegional, aTier, aDescription);
    }

    public MTEHatchInput(int aID, int aSlot, String aName, String aNameRegional, int aTier) {
        this(
            aID,
            aSlot,
            aName,
            aNameRegional,
            aTier,
            new String[] { "Fluid Input for Multiblocks", "Can hold " + aSlot + " types of fluid." });
        mDescriptionArray[1] = String.format("Capacity: %sL", formatNumber(getCapacityPerTank(aTier, aSlot)));
    }

    public MTEHatchInput(int aID, int aSlot, String aName, String aNameRegional, int aTier, String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aSlot, aDescription);
    }

    public int getCapacityPerTank(int aTier, int aSlot) {
        return (int) (8000L * (1L << aTier) / aSlot);
    }

    public MTEHatchInput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public MTEHatchInput(String aName, int aSlots, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aSlots, aDescription, aTextures);
    }

    public void setCustomCapacity(int capacity) {
        this.customCapacity = capacity;
        if (mDescriptionArray != null && mDescriptionArray.length > 0)
            mDescriptionArray[mDescriptionArray.length - 1] = String.format("Capacity: %sL", formatNumber(capacity));
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        byte color = getBaseMetaTileEntity().getColorization();
        ITexture coloredPipeOverlay = TextureFactory.of(OVERLAY_PIPE_COLORS[color + 1]);
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay,
                TextureFactory.of(FLUID_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        byte color = getBaseMetaTileEntity().getColorization();
        ITexture coloredPipeOverlay = TextureFactory.of(OVERLAY_PIPE_COLORS[color + 1]);
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay,
                TextureFactory.of(FLUID_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInput(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("disableFilter", disableFilter);
        if (mRecipeMap != null) {
            aNBT.setString("recipeMap", mRecipeMap.unlocalizedName);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        disableFilter = aNBT.getBoolean("disableFilter");
        mRecipeMap = RecipeMap.getFromOldIdentifier(aNBT.getString("recipeMap"));
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        if (!getBaseMetaTileEntity().getWorld().isRemote) {
            GTClientPreference preference = GTMod.proxy.getClientPreference(getBaseMetaTileEntity().getOwnerUuid());
            if (preference != null) {
                disableFilter = !preference.isInputHatchInitialFilterEnabled();
            }
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setByte("color", getBaseMetaTileEntity().getColorization());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        byte color = accessor.getNBTData()
            .getByte("color");
        if (color >= 0 && color < 16) {
            currenttip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.hatch.color_channel",
                    Dyes.VALUES[color].formatting + Dyes.VALUES[color].getLocalizedDyeName()
                        + EnumChatFormatting.GRAY));
        }
    }

    @Override
    public boolean doesEmptyContainers() {
        return true;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    public void updateSlots() {
        if (mInventory[getInputSlot()] != null && mInventory[getInputSlot()].stackSize <= 0)
            mInventory[getInputSlot()] = null;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return mRecipeMap == null || disableFilter || mRecipeMap.containsInput(aFluid);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 1;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == aBaseMetaTileEntity.getFrontFacing() && aIndex == 0
            && (mRecipeMap == null || disableFilter
                || mRecipeMap.containsInput(aStack)
                || mRecipeMap.containsInput(GTUtility.getFluidForFilledItem(aStack, true)));
    }

    @Override
    public int getCapacity() {
        return customCapacity != 0 ? customCapacity : (8000 * (1 << mTier));
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
        disableFilter = !disableFilter;
        GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.disableFilter." + disableFilter);
    }
}

package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.*;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.RenderOverlay;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtilityClient;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.SimpleCuboidMultiblockBase;

public class MTEIndustrialCentrifuge extends SimpleCuboidMultiblockBase implements INEIPreviewModifier {

    private boolean mIsAnimated;

    // client side stuff
    // mMachine got overwritten by StructureLib extended facing query response
    // so we use a separate field for this
    protected final List<RenderOverlay.OverlayTicket> overlayTickets = new ArrayList<>();
    protected boolean mFormed;

    public MTEIndustrialCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        initStructure();
        mIsAnimated = true;
    }

    public MTEIndustrialCentrifuge(final String aName) {
        super(aName);
        initStructure();
        mIsAnimated = true;
    }

    protected void initStructure() {
        setWidth(3);
        setHeight(3);
        setMinLength(3);
        setMaxLength(12);

        setMinCasingsBase(0);
        setMinCasingsPerLayer(2);

        setCasingTextureIndex(64);
        setCasingBlock(ModBlocks.blockCasingsMisc, 0);

        setValidHatches(InputBus, OutputBus, InputHatch, OutputHatch, Energy, Maintenance, Muffler);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCentrifuge(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Centrifuge";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("125% faster than using single block machines of the same voltage")
            .addInfo("Disable animations with a screwdriver")
            .addInfo("Only uses 90% of the EU/t normally required")
            .addInfo("Processes six items per voltage tier")
            .addPollutionAmount(getPollutionPerSecond(null));
        addStructureInfoToTooltip(tt);
        tt.toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        if (usingAnimations()) {
            return LARGETURBINE_NEW_ACTIVE5;
        } else {
            return getInactiveOverlay();
        }
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return LARGETURBINE_NEW5;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        setTurbineOverlay();
    }

    @Override
    public void setExtendedFacing(ExtendedFacing newExtendedFacing) {
        boolean extendedFacingChanged = newExtendedFacing != getExtendedFacing();
        super.setExtendedFacing(newExtendedFacing);
        if (extendedFacingChanged) {
            setTurbineOverlay();
        }
    }

    @Override
    public void onTextureUpdate() {
        setTurbineOverlay();
    }

    protected void setTurbineOverlay() {
        IGregTechTileEntity tile = getBaseMetaTileEntity();
        if (tile.isServerSide()) return;

        IIconContainer[] tTextures;
        if (getBaseMetaTileEntity().isActive() && usingAnimations()) tTextures = TURBINE_NEW_ACTIVE;
        else tTextures = TURBINE_NEW;

        GTUtilityClient.setTurbineOverlay(
            tile.getWorld(),
            tile.getXCoord(),
            tile.getYCoord(),
            tile.getZCoord(),
            getExtendedFacing(),
            tTextures,
            overlayTickets);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.centrifugeNonCellRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setEuModifier(0.9F)
            .setSpeedBonus(1F / 2.25F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (6 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCentrifuge;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mIsAnimated = !mIsAnimated;
        Logger.INFO("Is Centrifuge animated " + this.mIsAnimated);
        if (this.mIsAnimated) {
            GTUtility.sendChatToPlayer(aPlayer, "Using Animated Turbine Texture. ");
        } else {
            GTUtility.sendChatToPlayer(aPlayer, "Using Static Turbine Texture. ");
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mIsAnimated", mIsAnimated);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mIsAnimated")) {
            mIsAnimated = aNBT.getBoolean("mIsAnimated");
        } else {
            mIsAnimated = true;
        }
    }

    public boolean usingAnimations() {
        // Logger.INFO("Is animated? "+this.mIsAnimated);
        return this.mIsAnimated;
    }

    @Override
    public void onValueUpdate(byte aValue) {
        mFormed = (aValue & 0x1) != 0;
        setTurbineOverlay();
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((mMachine ? 1 : 0));
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        if (getBaseMetaTileEntity().isClientSide()) GTUtilityClient.clearTurbineOverlay(overlayTickets);
    }

    @Override
    public void onPreviewStructureComplete(@NotNull ItemStack trigger) {
        mFormed = true;
    }
}

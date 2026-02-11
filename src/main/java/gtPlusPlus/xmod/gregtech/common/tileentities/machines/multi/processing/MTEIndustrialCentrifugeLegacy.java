package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW5;
import static gregtech.api.enums.Textures.BlockIcons.LARGETURBINE_NEW_ACTIVE5;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW;
import static gregtech.api.enums.Textures.BlockIcons.TURBINE_NEW_ACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
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
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class MTEIndustrialCentrifugeLegacy extends GTPPMultiBlockBase<MTEIndustrialCentrifugeLegacy>
    implements ISurvivalConstructable, INEIPreviewModifier {

    private boolean mIsAnimated;
    private int mCasing;
    private static IStructureDefinition<MTEIndustrialCentrifugeLegacy> STRUCTURE_DEFINITION = null;
    // public static double recipesComplete = 0;
    // client side stuff
    // mMachine got overwritten by StructureLib extended facing query response
    // so we use a separate field for this
    protected final List<RenderOverlay.OverlayTicket> overlayTickets = new ArrayList<>();
    protected boolean mFormed;

    public MTEIndustrialCentrifugeLegacy(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        mIsAnimated = true;
    }

    public MTEIndustrialCentrifugeLegacy(final String aName) {
        super(aName);
        mIsAnimated = true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialCentrifugeLegacy(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Centrifuge";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addDeprecatedLine("CHECK NEI TO FIND NEW CONTROLLER")
            .addBulkMachineInfo(6, 2.25F, 0.9F)
            .addInfo("Disable animations with a screwdriver")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Centrifuge Casings", 6, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialCentrifugeLegacy> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialCentrifugeLegacy>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialCentrifugeLegacy.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(getCasingTextureIndex())
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasingsMisc, 0))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch();
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
    protected int getCasingTextureId() {
        return TAE.GTPP_INDEX(0);
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

    public Block getCasingBlock() {
        return ModBlocks.blockCasingsMisc;
    }

    public byte getCasingMeta() {
        return 0;
    }

    public byte getCasingTextureIndex() {
        return (byte) TAE.GTPP_INDEX(0);
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

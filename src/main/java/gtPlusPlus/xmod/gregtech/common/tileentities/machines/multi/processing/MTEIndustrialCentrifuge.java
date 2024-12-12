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
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtilityClient;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock.CustomIcon;

public class MTEIndustrialCentrifuge extends GTPPMultiBlockBase<MTEIndustrialCentrifuge>
    implements ISurvivalConstructable {

    private boolean mIsAnimated;
    private static final CustomIcon frontFaceActive = new CustomIcon("iconsets/LARGECENTRIFUGE_ACTIVE5");
    private static final CustomIcon frontFace = new CustomIcon("iconsets/LARGECENTRIFUGE5");
    private int mCasing;
    private static IStructureDefinition<MTEIndustrialCentrifuge> STRUCTURE_DEFINITION = null;
    // public static double recipesComplete = 0;
    // client side stuff
    // mMachine got overwritten by StructureLib extended facing query response
    // so we use a separate field for this
    protected boolean mFormed;

    public MTEIndustrialCentrifuge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        mIsAnimated = true;
    }

    public MTEIndustrialCentrifuge(final String aName) {
        super(aName);
        mIsAnimated = true;
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
    public IStructureDefinition<MTEIndustrialCentrifuge> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialCentrifuge>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialCentrifuge.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(getCasingTextureIndex())
                        .dot(1)
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
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkStructure(boolean aForceReset, IGregTechTileEntity aBaseMetaTileEntity) {
        boolean f = super.checkStructure(aForceReset, aBaseMetaTileEntity);
        if (f && getBaseMetaTileEntity().isServerSide()) {
            // while is this a client side field, blockrenderer will reuse the server world for client side rendering
            // so we must set it as well...
            mFormed = true;
            return true;
        }
        return f;
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
    public boolean renderInWorld(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
        if (!mFormed) return false;

        IIconContainer[] tTextures;
        if (getBaseMetaTileEntity().isActive() && usingAnimations()) tTextures = TURBINE_NEW_ACTIVE;
        else tTextures = TURBINE_NEW;
        GTUtilityClient
            .renderTurbineOverlay(aWorld, aX, aY, aZ, aRenderer, getExtendedFacing(), getCasingBlock(), tTextures);
        return false;
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
        return new ProcessingLogic().setEuModifier(0.9F)
            .setSpeedBonus(1F / 2.25F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
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
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialCentrifuge;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        this.mIsAnimated = !mIsAnimated;
        Logger.INFO("Is Centrifuge animated " + this.mIsAnimated);
        if (this.mIsAnimated) {
            PlayerUtils.messagePlayer(aPlayer, "Using Animated Turbine Texture. ");
        } else {
            PlayerUtils.messagePlayer(aPlayer, "Using Static Turbine Texture. ");
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

    }

    @Override
    public byte getUpdateData() {
        return (byte) ((mMachine ? 1 : 0));
    }
}

package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.misc.GTStructureChannels;

public class MTEMegaChemicalReactor extends MTEExtendedPowerMultiBlockBase<MTEMegaChemicalReactor>
    implements ISurvivalConstructable {

    private static final int OFFSET_X = 2;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;

    private int glassTier = -1;

    private final static int PARALLELS = 256;

    public MTEMegaChemicalReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaChemicalReactor(String aName) {
        super(aName);
    }

    private static final int CASING_INDEX = Casings.ChemicallyInertMachineCasing.textureId;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEMegaChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaChemicalReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "AAAAA", "ABABA", "ABABA", "ABABA", "ABABA", "ABABA", "ABABA", "ABABA", "AAAAA" },
                    { "ADDDA", " DDD ", " DDD ", " DDD ", " DDD ", " DDD ", " DDD ", " DDD ", "AAAAA" },
                    { "AD~DA", " DCD ", " DCD ", " DCD ", " DCD ", " DCD ", " DCD ", " DCD ", "AAAAA" },
                    { "ADDDA", " DDD ", " DDD ", " DDD ", " DDD ", " DDD ", " DDD ", " DDD ", "AAAAA" },
                    { "AAAAA", "ABABA", "ABABA", "ABABA", "ABABA", "ABABA", "ABABA", "ABABA", "AAAAA" }, }))
        .addElement(
            'A',
            buildHatchAdder(MTEMegaChemicalReactor.class)
                .atLeast(Energy.or(ExoticEnergy), Maintenance, InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(Casings.ChemicallyInertMachineCasing.asElement()))
        .addElement('B', Casings.PTFEPipeCasing.asElement())
        .addElement(
            'C',
            ofChain(
                activeCoils(Casings.FusionCoilBlock.asElement()),
                activeCoils(Casings.EternalCoilBlock.asElement())))
        .addElement('D', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .build();

    @Override
    public IStructureDefinition<MTEMegaChemicalReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Chemical Reactor, MCR")
            .addInfo(
                TooltipHelper
                    .coloredText(TooltipHelper.italicText("Infinite Potential!"), EnumChatFormatting.DARK_GRAY))
            .addStaticParallelInfo(PARALLELS)
            .addPerfectOCInfo()
            .addSeparator()
            .addTecTechHatchInfo()
            .addInfo(
                "Accepts " + EnumChatFormatting.BOLD
                    + "one"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " energy hatch at most")
            .addMinGlassForLaser(VoltageIndex.UV)
            .addGlassEnergyLimitInfo()
            .addUnlimitedTierSkips()
            .beginStructureBlock(5, 5, 9, false)
            .addController("Front center")
            .addCasingInfoMin("Chemically Inert Machine Casing", 46, false)
            .addCasingInfoExactly("Fusion Coil Block", 7, false)
            .addCasingInfoExactly("PTFE Pipe Casing", 28, false)
            .addCasingInfoExactly("Any Tiered Glass", 64, true)
            .addEnergyHatch("Hint block ", 1)
            .addMaintenanceHatch("Hint block ", 1)
            .addInputHatch("Hint block ", 1)
            .addInputBus("Hint block ", 1)
            .addOutputBus("Hint block ", 1)
            .addOutputHatch("Hint block ", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaChemicalReactor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        inputSeparation = !inputSeparation;
        GTUtility.sendChatTrans(
            aPlayer,
            inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            this.batchMode = !this.batchMode;
            if (this.batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.multiblockChemicalReactorRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock()
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return PARALLELS;
    }

    @Override
    public void construct(ItemStack aStack, boolean aHintsOnly) {
        this.buildPiece(STRUCTURE_PIECE_MAIN, aStack, aHintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return this.survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            realBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.glassTier = -1;

        if (!this.checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z)) return false;

        // ensures at most one energy hatch
        if (this.mExoticEnergyHatches.size() + this.mEnergyHatches.size() > 1) return false;

        if (this.glassTier < VoltageIndex.UV) {
            for (MTEHatch hatch : this.mExoticEnergyHatches) {
                if (hatch.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
                if (this.glassTier < hatch.mTier) {
                    return false;
                }
            }
            for (MTEHatchEnergy mEnergyHatch : this.mEnergyHatches) {
                if (this.glassTier < mEnergyHatch.mTier) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

}

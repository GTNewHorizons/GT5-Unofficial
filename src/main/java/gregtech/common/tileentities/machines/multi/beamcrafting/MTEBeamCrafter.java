package gregtech.common.tileentities.machines.multi.beamcrafting;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.register.LanthItemList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

public class MTEBeamCrafter extends MTEExtendedPowerMultiBlockBase<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.
    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();



    private static final IStructureDefinition<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter> STRUCTURE_DEFINITION = StructureDefinition
        .<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "                 ",
                " BBB         BBB ",
                " BCB         BCB ",
                " BBB         BBB ",
                "                 "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   B       B   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   A       A   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   B       B   B",
                "B   A       A   B",
                "B   B       B   B",
                " BBB         BBB "
            },{
                " BBB         BBB ",
                "B   BB     BB   B",
                "B   AA     AA   B",
                "B   BB     BB   B",
                " BBB         BBB "
            },{
                "  BBB       BBB  ",
                " B   BB   BB   B ",
                " A   AA   AA   A ",
                " B   BB   BB   B ",
                "  BBB       BBB  "
            },{
                "  BBBB     BBBB  ",
                " B    BBBBB    B ",
                " A    AB~BA    A ",
                " B    BBBBB    B ",
                "  BBBB     BBBB  "
            },{
                "   BBBBBBBBBBB   ",
                "  B           B  ",
                "  A           A  ",
                "  B           B  ",
                "   BBBBBBBBBBB   "
            },{
                "    BBBBBBBBB    ",
                "   B         B   ",
                "   A         A   ",
                "   B         B   ",
                "    BBBBBBBBB    "
            },{
                "      BBBBB      ",
                "    BB     BB    ",
                "    AA     AA    ",
                "    BB     BB    ",
                "      BBBBB      "
            },{
                "                 ",
                "      BBBBB      ",
                "      BBBBB      ",
                "      BBBBB      ",
                "                 "
            }})
        //spotless:on
        .addElement('B', // collider casing
            buildHatchAdder(MTEBeamCrafter.class).atLeast(Energy, ExoticEnergy, Maintenance, InputBus, InputHatch, OutputBus, OutputHatch)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(10))
                .dot(1)
                .buildAndChain(GregTechAPI.sBlockCasings13, 10))
        .addElement('A', chainAllGlasses()) // new glass type todo: (?)
        .addElement(
            'C',
            buildHatchAdder(MTEBeamCrafter.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .dot(2)
                .adder(MTEBeamCrafter::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .build();

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    public MTEBeamCrafter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamCrafter(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new gregtech.common.tileentities.machines.multi.beamcrafting.MTEBeamCrafter(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
                                 int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Beam Crafter, Beam Assembler")
            //.addBulkMachineInfo(4, 1.5F, 1F)
            .beginStructureBlock(17, 5, 11, false)
            .addController("Front Center")
            .addCasingInfoMin("Collider Casing", 224, false)
            .addCasingInfoExactly("Any Tiered Glass", 26, false)
            .addInputBus("Any Collider Casing", 1)
            .addOutputBus("Any Collider Casing", 1)
            .addInputHatch("Any Collider Casing", 1)
            .addOutputHatch("Any Collider Casing", 1)
            .addEnergyHatch("Any Collider Casing", 1)
            .addMaintenanceHatch("Any Collider Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addTecTechHatchInfo()
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 8, 2, 6);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 8, 2, 6, elementBudget, env, false, true);
    }


    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 8, 2, 6);
    }


    public int craftProgress = 0;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {

        // particleARate - the Rate value of beam packet A. for this multi,
        //                think of it as the number of particles in the packet
        // particleBRate - the Rate value of beam packet B.
        // craftProgressA - part of the progress of the current craft.
        //                 if a new craft starts that is a different recipe than the previous, reset to 0
        //                 if the recipe is the same as the previous recipe, keep track of the current craftProgressA
        // craftProgressB - the other part of the progress of the current craft.
        //                 if a new craft starts that is a different recipe than the previous, reset to 0
        //                 if the recipe is the same as the previous recipe, keep track of the current craftProgressB
        // recipeParticleACount - the total number of required particle A for the ongoing recipe
        // recipeParticleBCount - the total number of required particle B for the ongoing recipe
        //
        // run the following every second
        //
        // if there is no ongoing recipe, check the item/fluid inputs for a valid recipe
        //   if not found, do nothing for this processing cycle, and consume a tiny amount of power
        //   if found, start a craft, and consume the recipe's amount of power until it is done
        //
        // add particleRateA to craftProgressA, add particleRateB to craftProgressB
        // every cycle, for every integer number of completed craftProgressA and B,
        //    deliver output. subtract that much progress from craftProgressA and B such that they are both < recipeParticle(A/B)Count
        //    if all cached inputs are *not* consumed, wait for more particle packets (do not fail the craft!)
        //
        // if all cached inputs are consumed delivered, check for the same recipe again. if present, continue the process without
        //    resetting craftProgressA/B. otherwise, reset craftProgressA/B
        //
        // repeat forever
        //
        // how would batch mode work? how would parallels work? are parallels even needed, since the machine has
        // linear scaling of processing speed with particleRate?


        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    //todo: what is this
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 1.5F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    //todo: think about parallels
    public int getMaxParallelRecipes() {
        return (4 * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    //todo: make new recipemap with particle compatibility
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.beamcrafterRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}

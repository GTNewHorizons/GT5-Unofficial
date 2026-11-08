package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeInputHatch;
import tectech.mechanics.dataTransport.IALRecipeOutputHatch;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessDataItemsOutput;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

@IMetaTileEntity.SkipGenerateDescription
public class MTEDataBank extends TTMultiblockBase implements ISurvivalConstructable {

    private final ArrayList<IALRecipeInputHatch> recipeInputHatches = new ArrayList<>();
    private final ArrayList<IALRecipeOutputHatch> recipeOutputHatches = new ArrayList<>();
    private boolean slave = false;

    private static final IStructureDefinition<MTEDataBank> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEDataBank>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "BCCCB", "BDDDB", "BDDDB" }, { "BC~CB", "BAAAB", "BDDDB" },
                    { "BCCCB", "BDDDB", "BDDDB" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 1))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 2))
        .addElement(
            'C',
            buildHatchAdder(MTEDataBank.class).atLeast(Maintenance, Energy, EnergyMulti, Dynamo, DynamoMulti)
                .casingIndex(BlockGTCasingsTT.textureOffset)
                .hint(1)
                .buildAndChain(ofBlock(TTCasingsContainer.sBlockCasingsTT, 0)))
        .addElement(
            'D',
            buildHatchAdder(MTEDataBank.class)
                .atLeast(
                    DataBankHatches.OutboundConnector,
                    DataBankHatches.InboundConnector,
                    DataBankHatches.WirelessOutboundConnector)
                .casingIndex(BlockGTCasingsTT.textureOffset + 1)
                .hint(2)
                .buildAndChain(
                    DataBankHatches.DataStick
                        .newAnyOrCasing(BlockGTCasingsTT.textureOffset + 1, 2, TTCasingsContainer.sBlockCasingsTT, 1)))
        .build();
    // endregion

    public MTEDataBank(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEDataBank(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEDataBank(mName);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.databank.type"))
            .addMarkdown(new ResourceLocation("gregtech", "data-bank"))
            .addSupportAny()
            .beginStructureBlock(5, 3, 3, false)
            .addController(translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("18", translateToLocal("gt.blockcasingsTT.2.name"), false)
            .addCasing("3-16", translateToLocal("gt.blockcasingsTT.1.name"), false)
            .addCasing("0-6", translateToLocal("gt.blockcasingsTT.0.name"), false)
            .addEnergyHatch("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addMaintenanceHatch("1", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addMiscHatch("1+", translateToLocal("tt.keyword.Structure.DataAccessHatch"), translateToLocal("tt.keyword.Structure.AnyComputerCasing"), 2)
            .addMiscHatch("1+", translateToLocal("gt.blockmachines.hatch.dataoutass.tier.07.name"), translateToLocal("tt.keyword.Structure.AnyComputerCasing"), 2)
            .addMiscHatch("0+", translateToLocal("gt.blockmachines.hatch.datainass.tier.07.name"), translateToLocal("tt.keyword.Structure.AnyComputerCasing"), 2)
            .addStructureInfo("")
            .addStructureFooter(translateToLocal("tt.keyword.Structure.DaisyChainAssemblyLine"))
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public void checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        recipeInputHatches.clear();
        recipeOutputHatches.clear();
        slave = false;
        if (!checkPiece("main", 2, 1, 0, errors)) return;
        if (recipeInputHatches.isEmpty()) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.databank_missing_data_access"));
        }
        if (recipeOutputHatches.isEmpty()) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.databank_missing_data_output"));
        }
        checkHasAnyEnergy(errors);
        checkHasMaintenanceHatch(errors);
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        if (!recipeInputHatches.isEmpty() && !recipeOutputHatches.isEmpty()) {
            mEUt = -(int) V[slave ? 6 : 4];
            eAmpereFlow = 1 + (long) recipeOutputHatches.size() * recipeInputHatches.size();
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            this.lEUt = this.mEUt;
            return SimpleCheckRecipeResult.ofSuccess("providing_data");
        }
        return SimpleCheckRecipeResult.ofFailure("no_data");
    }

    @Override
    public void outputAfterRecipe_EM() {
        LinkedHashSet<RecipeAssemblyLine> availableRecipes = new LinkedHashSet<>();

        for (IALRecipeInputHatch recipeInputHatch : recipeInputHatches) {
            if (!recipeInputHatch.isValid()) continue;

            availableRecipes.addAll(recipeInputHatch.getAssemblyLineRecipes().contents);
        }

        ALRecipeDataPacket packet = ALRecipeDataPacket.EMPTY_PACKET;

        if (!availableRecipes.isEmpty()) {
            packet = new ALRecipeDataPacket(ImmutableList.copyOf(availableRecipes));
        }

        for (IALRecipeOutputHatch recipeOutputHatch : recipeOutputHatches) {
            if (!recipeOutputHatch.isValid()) continue;

            recipeOutputHatch.setAssemblyLineRecipes(packet);
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][1],
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][1] };
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.TECTECH_MACHINES_FX_HIGH_FREQ;
    }

    public final boolean addDataBankHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }

        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        switch (aMetaTileEntity) {
            case null -> {
                return false;
            }
            case MTEHatchWirelessDataItemsOutput mteHatchWirelessDataItemsOutput -> {
                mteHatchWirelessDataItemsOutput.updateTexture(aBaseCasingIndex);
                return recipeOutputHatches.add(mteHatchWirelessDataItemsOutput);
            }
            case MTEHatchDataItemsOutput mteHatchDataItemsOutput -> {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return recipeOutputHatches.add(mteHatchDataItemsOutput);
            }
            case MTEHatchDataAccess hatch when !(aMetaTileEntity instanceof MTEHatchDataItemsInput) -> {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return recipeInputHatches.add(hatch);
            }
            case MTEHatchDataItemsInput hatch -> {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                slave = true;
                return recipeInputHatches.add(hatch);
            }
            default -> {
            }
        }

        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 2, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stackSize, 2, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public IStructureDefinition<MTEDataBank> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    private enum DataBankHatches implements IHatchElement<MTEDataBank> {

        DataStick("GT5U.MBTT.DataAccessHatch", MTEHatchDataAccess.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.recipeInputHatches.size();
            }
        },
        InboundConnector("GT5U.MBTT.AssemblyLineReception", MTEHatchDataItemsInput.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.recipeInputHatches.size();
            }
        },
        OutboundConnector("GT5U.MBTT.DataBankTransmission", MTEHatchDataItemsOutput.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.recipeOutputHatches.size();
            }
        },
        WirelessOutboundConnector("GT5U.MBTT.WirelessDataBankOutput", MTEHatchWirelessDataItemsOutput.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.recipeOutputHatches.size();
            }
        };

        private final String name;
        private final List<? extends Class<? extends IMetaTileEntity>> mteClasses;

        @SafeVarargs
        DataBankHatches(String name, Class<? extends IMetaTileEntity>... mteClasses) {
            this.name = name;
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTEDataBank> adder() {
            return MTEDataBank::addDataBankHatchToMachineList;
        }

        @Override
        public String getDisplayName() {
            return StatCollector.translateToLocal(name);
        }

        @Override
        public String getDescriptionLangKey() {
            return name;
        }
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}

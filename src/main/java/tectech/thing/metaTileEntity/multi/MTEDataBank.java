package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraft.util.StatCollector.translateToLocal;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.EnergyMulti;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

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
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.WirelessComputationPacket;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsInput;
import tectech.thing.metaTileEntity.hatch.MTEHatchDataItemsOutput;
import tectech.thing.metaTileEntity.hatch.MTEHatchWirelessDataItemsOutput;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;

public class MTEDataBank extends TTMultiblockBase implements ISurvivalConstructable {

    // region variables
    private final ArrayList<MTEHatchDataItemsOutput> eStacksDataOutputs = new ArrayList<>();
    private final ArrayList<MTEHatchWirelessDataItemsOutput> eWirelessStacksDataOutputs = new ArrayList<>();
    private final ArrayList<MTEHatchDataAccess> eDataAccessHatches = new ArrayList<>();
    private boolean slave = false;
    private boolean wirelessModeEnabled = false;
    // endregion

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.databank.hint.0"), // 1 - Classic Hatches or high power
                                                                              // casing
        translateToLocal("gt.blockmachines.multimachine.em.databank.hint.1"), // 2 - Data Access/Data Bank Master
                                                                              // Hatches or
        // computer casing
    };

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
                .buildAndChain(TTCasingsContainer.sBlockCasingsTT, 0))
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
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.databank.type")) // Machine Type: Data
                                                                                              // Bank, DB
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.0")) // Controller block of
                                                                                           // the Data Bank
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.1")) // Used to supply
                                                                                           // Assembling Lines
            // with more Data Sticks
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.2")) // and give multiple
                                                                                           // Assembling Lines
                                                                                           // access to
                                                                                           // the same Data
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.3")) // Use screwdriver to
                                                                                           // toggle
                                                                                           // wireless mode
            .addTecTechHatchInfo()
            .beginStructureBlock(5, 3, 3, false)
            .addController("Front center")
            .addCasingInfoExactly("Computer Heat Vent", 18, false)
            .addCasingInfoExactly("High Power Casing", 7, false)
            .addCasingInfoMin("Computer Casing", 3, false)
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataAccessHatch"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasing"),
                2) // Data Access Hatch: Any Computer Casing
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.dataoutass.tier.07.name"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasing"),
                2) // Data Bank Master Connector: Any Computer Casing
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                            // High Power Casing
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                 // Hatch: Any High
                                                                                                 // Power Casing
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        eDataAccessHatches.clear();
        eStacksDataOutputs.clear();
        eWirelessStacksDataOutputs.clear();
        slave = false;
        return structureCheck_EM("main", 2, 1, 0);
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        if (!eDataAccessHatches.isEmpty() && (!eStacksDataOutputs.isEmpty() || !eWirelessStacksDataOutputs.isEmpty())) {
            mEUt = -(int) V[slave ? 6 : 4];
            eAmpereFlow = 1
                + (long) (eStacksDataOutputs.size() + eWirelessStacksDataOutputs.size()) * eDataAccessHatches.size();
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            return SimpleCheckRecipeResult.ofSuccess("providing_data");
        }
        return SimpleCheckRecipeResult.ofFailure("no_data");
    }

    @Override
    public void outputAfterRecipe_EM() {
        HashSet<RecipeAssemblyLine> availableRecipes = new HashSet<>();

        for (MTEHatchDataAccess dataAccess : validMTEList(eDataAccessHatches)) {
            availableRecipes.addAll(dataAccess.getAssemblyLineRecipes());
        }

        if (!availableRecipes.isEmpty()) {
            RecipeAssemblyLine[] recipeArray = availableRecipes.toArray(new RecipeAssemblyLine[0]);

            for (MTEHatchDataItemsOutput hatch : validMTEList(eStacksDataOutputs)) {
                hatch.q = new ALRecipeDataPacket(recipeArray);
            }

            if (wirelessModeEnabled) {
                for (MTEHatchWirelessDataItemsOutput hatch : validMTEList(eWirelessStacksDataOutputs)) {
                    hatch.dataPacket = new ALRecipeDataPacket(recipeArray);
                }
            }
        } else {
            for (MTEHatchDataItemsOutput hatch : validMTEList(eStacksDataOutputs)) {
                hatch.q = null;
            }

            for (MTEHatchWirelessDataItemsOutput hatch : validMTEList(eWirelessStacksDataOutputs)) {
                hatch.dataPacket = null;
            }
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
        if (aMetaTileEntity == null) {
            return false;
        }

        if (aMetaTileEntity instanceof MTEHatchWirelessDataItemsOutput) {
            ((MTEHatchWirelessDataItemsOutput) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eWirelessStacksDataOutputs.add((MTEHatchWirelessDataItemsOutput) aMetaTileEntity);
        }

        if (aMetaTileEntity instanceof MTEHatchDataItemsOutput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eStacksDataOutputs.add((MTEHatchDataItemsOutput) aMetaTileEntity);
        }

        if (aMetaTileEntity instanceof MTEHatchDataAccess hatch
            && !(aMetaTileEntity instanceof MTEHatchDataItemsInput)) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eDataAccessHatches.add(hatch);
        }

        if (aMetaTileEntity instanceof MTEHatchDataItemsInput hatch) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            slave = true;
            return eDataAccessHatches.add(hatch);
        }

        return false;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (getBaseMetaTileEntity().isServerSide()) {
            wirelessModeEnabled = !wirelessModeEnabled;
            if (wirelessModeEnabled) {
                GTUtility.sendChatToPlayer(aPlayer, "Wireless mode enabled");
                WirelessComputationPacket.enableWirelessNetWork(getBaseMetaTileEntity());
            } else {
                GTUtility.sendChatToPlayer(aPlayer, "Wireless mode disabled");
                WirelessComputationPacket.disableWirelessNetWork(getBaseMetaTileEntity());
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("wirelessModeEnabled", wirelessModeEnabled);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("wirelessModeEnabled")) {
            wirelessModeEnabled = aNBT.getBoolean("wirelessModeEnabled");
        } else {
            wirelessModeEnabled = false;
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 1, 0, stackSize, hintsOnly);
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

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum DataBankHatches implements IHatchElement<MTEDataBank> {

        DataStick(MTEHatchDataAccess.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.eDataAccessHatches.size();
            }
        },
        OutboundConnector(MTEHatchDataItemsOutput.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.eStacksDataOutputs.size();
            }
        },
        InboundConnector(MTEHatchDataItemsInput.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.eDataAccessHatches.size();
            }
        },
        WirelessOutboundConnector(MTEHatchWirelessDataItemsOutput.class) {

            @Override
            public long count(MTEDataBank t) {
                return t.eWirelessStacksDataOutputs.size();
            }
        };

        private final List<? extends Class<? extends IMetaTileEntity>> mteClasses;

        @SafeVarargs
        DataBankHatches(Class<? extends IMetaTileEntity>... mteClasses) {
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
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }
}

package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.recipe.TecTechRecipeMaps;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.hatch.MTEHatchEnergyMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchObjectHolder;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 17.12.2016.
 */
@SuppressWarnings("unchecked")
public class MTEResearchStation extends TTMultiblockBase implements ISurvivalConstructable {

    public static final String machine = "EM Machinery";
    public static final String crafter = "EM Crafting";
    // region variables
    private final ArrayList<MTEHatchObjectHolder> eHolders = new ArrayList<>();
    private GTRecipe.RecipeAssemblyLine tRecipe;
    private static final String assembly = "Assembly line";
    private static final String scanner = "Scanner";
    private String machineType = assembly;
    private ItemStack holdItem;
    private long computationRemaining, computationRequired;

    // Used to sync currently researching item to GUI
    private String clientOutputName;

    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.0"), // 1 - Classic/Data Hatches or
                                                                              // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.1"), // 2 - Holder Hatch
    };

    private String clientLocale = "en_US";
    // endregion

    // region structure
    private static final IStructureDefinition<MTEResearchStation> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEResearchStation>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "   ", " A ", " A ", "AAA", "AAA", "AAA", "AAA" },
                    { "AAA", "ACA", "ACA", "ACA", "BCB", "BCB", "BBB" },
                    { "   ", " C ", "   ", "   ", "ACA", "CCC", "DDD" },
                    { "   ", " E ", "   ", "   ", "A~A", "CCC", "DDD" },
                    { "   ", " C ", "   ", "   ", "ACA", "CCC", "DDD" },
                    { "AAA", "ACA", "ACA", "ACA", "BCB", "BCB", "BBB" },
                    { "   ", " A ", " A ", "AAA", "AAA", "AAA", "AAA" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 1))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 2))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsTT, 3))
        .addElement(
            'D',
            buildHatchAdder(MTEResearchStation.class)
                .atLeast(Energy.or(HatchElement.EnergyMulti), Maintenance, HatchElement.InputData)
                .casingIndex(BlockGTCasingsTT.textureOffset + 1)
                .dot(1)
                .buildAndChain(ofBlock(TTCasingsContainer.sBlockCasingsTT, 1)))
        .addElement('E', HolderHatchElement.INSTANCE.newAny(BlockGTCasingsTT.textureOffset + 3, 2))
        .build();
    // endregion

    public MTEResearchStation(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEResearchStation(String aName) {
        super(aName);
    }

    private void makeStick() {
        mInventory[1].setTagCompound(new NBTTagCompound());
        mInventory[1].getTagCompound()
            .setString(
                "author",
                EnumChatFormatting.BLUE + "Tec"
                    + EnumChatFormatting.DARK_BLUE
                    + "Tech"
                    + EnumChatFormatting.WHITE
                    + ' '
                    + machineType
                    + " Recipe Generator");
        AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(mInventory[1], tRecipe);
    }

    private boolean iterateRecipes() {
        for (GTRecipe ttRecipe : TecTechRecipeMaps.researchStationFakeRecipes.getAllRecipes()) {
            if (GTUtility.areStacksEqual(ttRecipe.mInputs[0], holdItem, true)) {
                computationRequired = computationRemaining = ttRecipe.mDuration * 20L;
                mMaxProgresstime = 20;
                mEfficiencyIncrease = 10000;
                eRequiredData = (short) (ttRecipe.mSpecialValue >>> 16);
                eAmpereFlow = (short) (ttRecipe.mSpecialValue & 0xFFFF);
                mEUt = Math.min(ttRecipe.mEUt, -ttRecipe.mEUt);
                eHolders.get(0)
                    .getBaseMetaTileEntity()
                    .setActive(true);
                return true;
            }
        }
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEResearchStation(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (MTEHatchObjectHolder rack : filterValidMTEs(eHolders)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
        eHolders.clear();

        if (!structureCheck_EM("main", 1, 3, 4)) {
            return false;
        }

        for (MTEHatchObjectHolder rack : filterValidMTEs(eHolders)) {
            rack.getBaseMetaTileEntity()
                .setActive(iGregTechTileEntity.isActive());
        }
        return eHolders.size() == 1;
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        ItemStack controllerStack = getControllerSlot();
        tRecipe = null;
        if (!eHolders.isEmpty() && eHolders.get(0).mInventory[0] != null) {
            holdItem = eHolders.get(0).mInventory[0].copy();
            if (ItemList.Tool_DataStick.isStackEqual(controllerStack, false, true)) {
                switch (machineType) {
                    case scanner -> {
                        for (GTRecipe.RecipeAssemblyLine assRecipe : GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes) {
                            if (GTUtility.areStacksEqual(assRecipe.mResearchItem, holdItem, true)) {
                                boolean failScanner = true;
                                for (GTRecipe scannerRecipe : scannerFakeRecipes.getAllRecipes()) {
                                    if (GTUtility.areStacksEqual(scannerRecipe.mInputs[0], holdItem, true)) {
                                        failScanner = false;
                                        break;
                                    }
                                }
                                if (failScanner) {
                                    return SimpleCheckRecipeResult.ofFailure("wrongRequirements");
                                }
                                this.tRecipe = assRecipe;
                                // Set property
                                computationRequired = computationRemaining = assRecipe.mResearchTime;
                                mMaxProgresstime = 20;
                                mEfficiencyIncrease = 10000;
                                eRequiredData = 1;
                                eAmpereFlow = 1;
                                mEUt = (int) -TierEU.RECIPE_UV;
                                eHolders.get(0)
                                    .getBaseMetaTileEntity()
                                    .setActive(true);
                                return SimpleCheckRecipeResult.ofSuccess("scanning");
                            }
                        }
                    }
                    case assembly -> {
                        for (GTRecipe.RecipeAssemblyLine assRecipe : TecTechRecipeMaps.researchableALRecipeList) {
                            if (GTUtility.areStacksEqual(assRecipe.mResearchItem, holdItem, true)) {
                                tRecipe = assRecipe;
                                // if found
                                if (iterateRecipes()) return SimpleCheckRecipeResult.ofSuccess("researching");
                            }
                        }
                    }
                }
            } else {
                return CheckRecipeResultRegistry.NO_DATA_STICKS;
            }
        }
        holdItem = null;
        computationRequired = computationRemaining = 0;
        for (MTEHatchObjectHolder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
        return SimpleCheckRecipeResult.ofFailure("no_research_item");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eHolders.isEmpty()) {
            if (tRecipe != null && ItemList.Tool_DataStick.isStackEqual(mInventory[1], false, true)) {
                eHolders.get(0)
                    .getBaseMetaTileEntity()
                    .setActive(false);
                eHolders.get(0).mInventory[0] = null;
                makeStick();
            }
        }
        computationRequired = computationRemaining = 0;
        tRecipe = null;
        holdItem = null;
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.research.type")) // Machine Type: Research
                                                                                              // Station, Scanner
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.0")) // Controller block of
                                                                                           // the Research Station
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.1")) // Used to scan Data
                                                                                           // Sticks for
            // Assembling Line Recipes
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.2")) // Needs to be fed with
                                                                                           // computation to work
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.3")) // Does not consume the
                                                                                           // item until
            // the Data Stick is written
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.research.desc.4")) // Use screwdriver to change
                                                                                           // mode
            .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
            .addSeparator()
            .beginStructureBlock(3, 7, 7, false)
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.holder.tier.09.name"),
                translateToLocal("tt.keyword.Structure.CenterPillar"),
                2) // Object Holder: Center of the front pillar
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataConnector"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"),
                1) // Optical Connector: Any Computer Casing on the backside of the main body
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"), 1) // Energy Hatch:
                                                                                                   // Any Computer
                                                                                                   // Casing on the
                                                                                                   // backside of
                                                                                                   // the main body
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyComputerCasingBackMain"), 1) // Maintenance
                                                                                                        // Hatch:
                                                                                                        // Any
                                                                                                        // Computer
                                                                                                        // Casing on
                                                                                                        // the
                                                                                                        // backside
                                                                                                        // of the
                                                                                                        // main body
            .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchEnergy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        for (MTEHatchEnergyMulti tHatch : filterValidMTEs(eEnergyMulti)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] { translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": "
                : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ")
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(Math.abs(mEUt))
                + EnumChatFormatting.RESET
                + " EU/t "
                + translateToLocalFormatted("tt.keyword.at", clientLocale)
                + " "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(eAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            translateToLocalFormatted("tt.keyphrase.Tier_Rating", clientLocale) + ": "
                + EnumChatFormatting.YELLOW
                + VN[getMaxEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.GREEN
                + VN[getMinEnergyInputTier_EM()]
                + EnumChatFormatting.RESET
                + " "
                + translateToLocalFormatted("tt.keyphrase.Amp_Rating", clientLocale)
                + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(eMaxAmpereFlow)
                + EnumChatFormatting.RESET
                + " A",
            translateToLocalFormatted("tt.keyword.Problems", clientLocale) + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + translateToLocalFormatted("tt.keyword.Efficiency", clientLocale)
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            translateToLocalFormatted("tt.keyword.PowerPass", clientLocale) + ": "
                + EnumChatFormatting.BLUE
                + ePowerPass
                + EnumChatFormatting.RESET
                + " "
                + translateToLocalFormatted("tt.keyword.SafeVoid", clientLocale)
                + ": "
                + EnumChatFormatting.BLUE
                + eSafeVoid,
            translateToLocalFormatted("tt.keyphrase.Computation_Available", clientLocale) + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(eAvailableData)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(eRequiredData)
                + EnumChatFormatting.RESET,
            translateToLocalFormatted("tt.keyphrase.Computation_Remaining", clientLocale) + ":",
            EnumChatFormatting.GREEN + GTUtility.formatNumbers(computationRemaining / 20L)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getComputationRequired()) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][3],
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][3] };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (MTEHatchObjectHolder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
    }

    @Override
    protected void extraExplosions_EM() {
        for (MetaTileEntity tTileEntity : eHolders) {
            tTileEntity.getBaseMetaTileEntity()
                .doExplosion(V[9]);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("eComputationRemaining", computationRemaining);
        aNBT.setLong("eComputationRequired", computationRequired);
        aNBT.setString("eMachineType", machineType);
        if (holdItem != null) {
            aNBT.setTag("eHold", holdItem.writeToNBT(new NBTTagCompound()));
        } else {
            aNBT.removeTag("eHold");
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        computationRemaining = aNBT.getLong("eComputationRemaining");
        computationRequired = aNBT.getLong("eComputationRequired");
        machineType = aNBT.hasKey("eMachineType") ? aNBT.getString("eMachineType") : assembly;
        if (aNBT.hasKey("eHold")) {
            holdItem = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("eHold"));
        } else {
            holdItem = null;
        }
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        for (MTEHatchObjectHolder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
        computationRequired = computationRemaining = 0;
        tRecipe = null;
        holdItem = null;
    }

    @Override
    protected boolean supportsSlotAutomation(int aSlot) {
        return aSlot == getControllerSlotIndex();
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0) {
                tRecipe = null;
                if (holdItem != null) {
                    if (ItemList.Tool_DataStick.isStackEqual(mInventory[1], false, true)) {
                        for (GTRecipe.RecipeAssemblyLine tRecipe : TecTechRecipeMaps.researchableALRecipeList) {
                            if (GTUtility.areStacksEqual(tRecipe.mResearchItem, holdItem, true)) {
                                this.tRecipe = tRecipe;
                                break;
                            }
                        }
                    }
                }
                if (tRecipe == null) {
                    holdItem = null;
                    computationRequired = computationRemaining = 0;
                    mMaxProgresstime = 0;
                    mEfficiencyIncrease = 0;
                    for (MTEHatchObjectHolder r : eHolders) {
                        r.getBaseMetaTileEntity()
                            .setActive(false);
                    }
                }
            }
        }
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (computationRemaining <= 0) {
            computationRemaining = 0;
            mProgresstime = mMaxProgresstime;
            return true;
        } else {
            computationRemaining -= eAvailableData;
            mProgresstime = 1;
            return super.onRunningTick(aStack);
        }
    }

    public final boolean addHolderToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchObjectHolder) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eHolders.add((MTEHatchObjectHolder) aMetaTileEntity);
        }
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        super.onRightclick(aBaseMetaTileEntity, aPlayer);

        if (!aBaseMetaTileEntity.isClientSide() && aPlayer instanceof EntityPlayerMP) {
            try {
                EntityPlayerMP player = (EntityPlayerMP) aPlayer;
                clientLocale = (String) FieldUtils.readField(player, "translator", true);
            } catch (Exception e) {
                clientLocale = "en_US";
            }
        } else {
            return true;
        }
        return true;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
        switch (machineType) {
            case scanner -> machineType = assembly;
            case assembly -> machineType = scanner;
        }
        aPlayer.addChatComponentMessage(
            new ChatComponentTranslation(
                "gt.blockmachines.multimachine.em.research.mode." + machineType.replace(" ", "_")));
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget().setStringSupplier(
                    () -> StatCollector.translateToLocalFormatted("GT5U.gui.text.researching_item", clientOutputName))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(
                        widget -> computationRequired > 0 && clientOutputName != null && !clientOutputName.isEmpty()))
            .widget(
                new TextWidget()
                    .setStringSupplier(
                        () -> StatCollector.translateToLocalFormatted(
                            "GT5U.gui.text.research_progress",
                            getComputationConsumed(),
                            getComputationRequired(),
                            GTUtility.formatNumbers(getComputationProgress())))
                    .setTextAlignment(Alignment.CenterLeft)
                    .setEnabled(
                        widget -> computationRequired > 0 && clientOutputName != null && !clientOutputName.isEmpty()))
            .widget(new FakeSyncWidget.LongSyncer(() -> computationRequired, aLong -> computationRequired = aLong))
            .widget(new FakeSyncWidget.LongSyncer(() -> computationRemaining, aLong -> computationRemaining = aLong))
            .widget(new FakeSyncWidget.StringSyncer(() -> {
                if (tRecipe != null && tRecipe.mOutput != null) {
                    return tRecipe.mOutput.getDisplayName();
                }
                return "";
            }, aString -> clientOutputName = aString));
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("hasProblems", (getIdealStatus() - getRepairStatus()) > 0);
        tag.setFloat("efficiency", mEfficiency / 100.0F);
        tag.setBoolean("incompleteStructure", (getBaseMetaTileEntity().getErrorDisplayID() & 64) != 0);
        tag.setString("machineType", machineType);
        tag.setLong("computation", getComputationConsumed());
        tag.setLong("computationRequired", getComputationRequired());
    }

    private long getComputationConsumed() {
        return (computationRequired - computationRemaining) / 20L;
    }

    private long getComputationRequired() {
        return computationRequired / 20L;
    }

    private double getComputationProgress() {
        return 100d
            * (getComputationRequired() > 0d ? (double) getComputationConsumed() / getComputationRequired() : 0d);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();

        if (tag.getBoolean("incompleteStructure")) {
            currentTip.add(RED + "** INCOMPLETE STRUCTURE **" + RESET);
        }
        String efficiency = RESET + "  Efficiency: " + tag.getFloat("efficiency") + "%";
        if (tag.getBoolean("hasProblems")) {
            currentTip.add(RED + "** HAS PROBLEMS **" + efficiency);
        } else if (!tag.getBoolean("incompleteStructure")) {
            currentTip.add(GREEN + "Running Fine" + efficiency);
        }
        currentTip.add("Mode: " + tag.getString("machineType"));
        currentTip.add(
            String.format(
                "Computation: %,d / %,d",
                tag.getInteger("computation"),
                tag.getInteger("computationRequired")));
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 3, 4, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 1, 3, 4, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEResearchStation> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum HolderHatchElement implements IHatchElement<MTEResearchStation> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(MTEHatchObjectHolder.class);
        }

        @Override
        public IGTHatchAdder<? super MTEResearchStation> adder() {
            return MTEResearchStation::addHolderToMachineList;
        }

        @Override
        public long count(MTEResearchStation t) {
            return t.eHolders.size();
        }
    }
}

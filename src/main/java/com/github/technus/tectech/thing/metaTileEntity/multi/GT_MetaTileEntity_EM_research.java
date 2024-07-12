package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.util.CommonValues.V;
import static com.github.technus.tectech.util.CommonValues.VN;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_Utility.filterValidMTEs;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.recipe.TecTechRecipeMaps;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_EnergyMulti;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_Holder;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_AssemblyLineUtils;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.api.util.shutdown.ShutDownReason;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Created by danie_000 on 17.12.2016.
 */
@SuppressWarnings("unchecked")
public class GT_MetaTileEntity_EM_research extends GT_MetaTileEntity_MultiblockBase_EM
    implements ISurvivalConstructable {

    public static final String machine = "EM Machinery";
    public static final String crafter = "EM Crafting";
    // region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_Holder> eHolders = new ArrayList<>();
    private GT_Recipe.GT_Recipe_AssemblyLine tRecipe;
    private static final String assembly = "Assembly line";
    private static final String scanner = "Scanner";
    private String machineType = assembly;
    private ItemStack holdItem;
    private long computationRemaining, computationRequired;

    private static LinkedHashMap<String, String> lServerNames;

    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.0"), // 1 - Classic/Data Hatches or
                                                                              // Computer casing
        translateToLocal("gt.blockmachines.multimachine.em.research.hint.1"), // 2 - Holder Hatch
    };

    private String clientLocale = "en_US";
    // endregion

    // region structure
    private static final IStructureDefinition<GT_MetaTileEntity_EM_research> STRUCTURE_DEFINITION = IStructureDefinition
        .<GT_MetaTileEntity_EM_research>builder()
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
        .addElement('A', ofBlock(sBlockCasingsTT, 1))
        .addElement('B', ofBlock(sBlockCasingsTT, 2))
        .addElement('C', ofBlock(sBlockCasingsTT, 3))
        .addElement(
            'D',
            buildHatchAdder(GT_MetaTileEntity_EM_research.class)
                .atLeast(Energy.or(HatchElement.EnergyMulti), Maintenance, HatchElement.InputData)
                .casingIndex(textureOffset + 1)
                .dot(1)
                .buildAndChain(ofBlock(sBlockCasingsTT, 1)))
        .addElement('E', HolderHatchElement.INSTANCE.newAny(textureOffset + 3, 2))
        .build();
    // endregion

    public GT_MetaTileEntity_EM_research(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_research(String aName) {
        super(aName);
    }

    private void makeStick() {
        mInventory[1].setTagCompound(new NBTTagCompound());
        mInventory[1].setStackDisplayName(
            GT_LanguageManager.getTranslation(tRecipe.mOutput.getDisplayName()) + " Construction Data");
        GT_Utility.ItemNBT.setBookTitle(
            mInventory[1],
            GT_LanguageManager.getTranslation(tRecipe.mOutput.getDisplayName()) + " Construction Data");
        NBTTagCompound tNBT = mInventory[1].getTagCompound(); // code above makes it not null

        tNBT.setTag("output", tRecipe.mOutput.writeToNBT(new NBTTagCompound()));
        tNBT.setInteger("time", tRecipe.mDuration);
        tNBT.setInteger("eu", tRecipe.mEUt);
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            tNBT.setTag(String.valueOf(i), tRecipe.mInputs[i].writeToNBT(new NBTTagCompound()));
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            tNBT.setTag("f" + i, tRecipe.mFluidInputs[i].writeToNBT(new NBTTagCompound()));
        }
        tNBT.setString(
            "author",
            EnumChatFormatting.BLUE + "Tec"
                + EnumChatFormatting.DARK_BLUE
                + "Tech"
                + EnumChatFormatting.WHITE
                + " Assembling Line Recipe Generator");
        NBTTagList tNBTList = new NBTTagList();
        tNBTList.appendTag(
            new NBTTagString(
                "Construction plan for " + tRecipe.mOutput.stackSize
                    + ' '
                    + GT_LanguageManager.getTranslation(tRecipe.mOutput.getDisplayName())
                    + ". Needed EU/t: "
                    + tRecipe.mEUt
                    + " Production time: "
                    + tRecipe.mDuration / 20));
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            if (tRecipe.mInputs[i] != null) {
                tNBTList.appendTag(
                    new NBTTagString(
                        "Input Bus " + (i + 1)
                            + ": "
                            + tRecipe.mInputs[i].stackSize
                            + ' '
                            + GT_LanguageManager.getTranslation(tRecipe.mInputs[i].getDisplayName())));
            }
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            if (tRecipe.mFluidInputs[i] != null) {
                tNBTList.appendTag(
                    new NBTTagString(
                        "Input Hatch " + (i + 1)
                            + ": "
                            + tRecipe.mFluidInputs[i].amount
                            + "L "
                            + GT_LanguageManager.getTranslation(tRecipe.mFluidInputs[i].getLocalizedName())));
            }
        }
        tNBT.setTag("pages", tNBTList);
    }

    static {
        try {
            Class<?> GT_Assemblyline_Server = Class.forName("gregtech.api.util.GT_Assemblyline_Server");
            lServerNames = (LinkedHashMap<String, String>) GT_Assemblyline_Server.getField("lServerNames")
                .get(null);
        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException e) {
            lServerNames = null;
        }
    }

    private void makeStick2() {
        String s = tRecipe.mOutput.getDisplayName();
        if (getBaseMetaTileEntity().isServerSide()) {
            if (lServerNames != null) {
                s = lServerNames.get(tRecipe.mOutput.getDisplayName());
                if (s == null) {
                    s = tRecipe.mOutput.getDisplayName();
                }
            } else {
                s = tRecipe.mOutput.getDisplayName();
            }
        }
        mInventory[1].setTagCompound(new NBTTagCompound());
        mInventory[1].setStackDisplayName(s + " Construction Data");
        GT_Utility.ItemNBT.setBookTitle(mInventory[1], s + " Construction Data");

        NBTTagCompound tNBT = mInventory[1].getTagCompound();

        tNBT.setTag("output", tRecipe.mOutput.writeToNBT(new NBTTagCompound()));
        tNBT.setInteger("time", tRecipe.mDuration);
        tNBT.setInteger("eu", tRecipe.mEUt);
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            tNBT.setTag("" + i, tRecipe.mInputs[i].writeToNBT(new NBTTagCompound()));
        }
        for (int i = 0; i < tRecipe.mOreDictAlt.length; i++) {
            if (tRecipe.mOreDictAlt[i] != null && tRecipe.mOreDictAlt[i].length > 0) {
                tNBT.setInteger("a" + i, tRecipe.mOreDictAlt[i].length);
                for (int j = 0; j < tRecipe.mOreDictAlt[i].length; j++) {
                    tNBT.setTag("a" + i + ":" + j, tRecipe.mOreDictAlt[i][j].writeToNBT(new NBTTagCompound()));
                }
            }
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            tNBT.setTag("f" + i, tRecipe.mFluidInputs[i].writeToNBT(new NBTTagCompound()));
        }
        tNBT.setString(
            "author",
            EnumChatFormatting.BLUE + "Tec"
                + EnumChatFormatting.DARK_BLUE
                + "Tech"
                + EnumChatFormatting.WHITE
                + ' '
                + machineType
                + " Recipe Generator");
        NBTTagList tNBTList = new NBTTagList();
        s = tRecipe.mOutput.getDisplayName();
        if (getBaseMetaTileEntity().isServerSide()) {
            s = lServerNames.get(tRecipe.mOutput.getDisplayName());
            if (s == null) {
                s = tRecipe.mOutput.getDisplayName();
            }
        }
        tNBTList.appendTag(
            new NBTTagString(
                "Construction plan for " + tRecipe.mOutput.stackSize
                    + " "
                    + s
                    + ". Needed EU/t: "
                    + tRecipe.mEUt
                    + " Production time: "
                    + (tRecipe.mDuration / 20)));
        for (int i = 0; i < tRecipe.mInputs.length; i++) {
            if (tRecipe.mOreDictAlt[i] != null) {
                int count = 0;
                StringBuilder tBuilder = new StringBuilder("Input Bus " + (i + 1) + ": ");
                for (ItemStack tStack : tRecipe.mOreDictAlt[i]) {
                    if (tStack != null) {
                        s = tStack.getDisplayName();
                        if (getBaseMetaTileEntity().isServerSide()) {
                            s = lServerNames.get(tStack.getDisplayName());
                            if (s == null) s = tStack.getDisplayName();
                        }

                        tBuilder.append(count == 0 ? "" : "\nOr ")
                            .append(tStack.stackSize)
                            .append(" ")
                            .append(s);
                        count++;
                    }
                }
                if (count > 0) tNBTList.appendTag(new NBTTagString(tBuilder.toString()));
            } else if (tRecipe.mInputs[i] != null) {
                s = tRecipe.mInputs[i].getDisplayName();
                if (getBaseMetaTileEntity().isServerSide()) {
                    s = lServerNames.get(tRecipe.mInputs[i].getDisplayName());
                    if (s == null) {
                        s = tRecipe.mInputs[i].getDisplayName();
                    }
                }
                tNBTList.appendTag(
                    new NBTTagString("Input Bus " + (i + 1) + ": " + tRecipe.mInputs[i].stackSize + " " + s));
            }
        }
        for (int i = 0; i < tRecipe.mFluidInputs.length; i++) {
            if (tRecipe.mFluidInputs[i] != null) {
                s = tRecipe.mFluidInputs[i].getLocalizedName();
                if (getBaseMetaTileEntity().isServerSide()) {
                    s = lServerNames.get(tRecipe.mFluidInputs[i].getLocalizedName());
                    if (s == null) {
                        s = tRecipe.mFluidInputs[i].getLocalizedName();
                    }
                }
                tNBTList.appendTag(
                    new NBTTagString("Input Hatch " + (i + 1) + ": " + tRecipe.mFluidInputs[i].amount + "L " + s));
            }
        }
        tNBT.setTag("pages", tNBTList);

        mInventory[1].setTagCompound(tNBT);
    }

    private boolean iterateRecipes() {
        for (GT_Recipe ttRecipe : TecTechRecipeMaps.researchStationFakeRecipes.getAllRecipes()) {
            if (GT_Utility.areStacksEqual(ttRecipe.mInputs[0], holdItem, true)) {
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
        return new GT_MetaTileEntity_EM_research(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        for (GT_MetaTileEntity_Hatch_Holder rack : filterValidMTEs(eHolders)) {
            rack.getBaseMetaTileEntity()
                .setActive(false);
        }
        eHolders.clear();

        if (!structureCheck_EM("main", 1, 3, 4)) {
            return false;
        }

        for (GT_MetaTileEntity_Hatch_Holder rack : filterValidMTEs(eHolders)) {
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
                        for (GT_Recipe.GT_Recipe_AssemblyLine assRecipe : GT_Recipe.GT_Recipe_AssemblyLine.sAssemblylineRecipes) {
                            if (GT_Utility.areStacksEqual(assRecipe.mResearchItem, holdItem, true)) {
                                boolean failScanner = true;
                                for (GT_Recipe scannerRecipe : scannerFakeRecipes.getAllRecipes()) {
                                    if (GT_Utility.areStacksEqual(scannerRecipe.mInputs[0], holdItem, true)) {
                                        failScanner = false;
                                        break;
                                    }
                                }
                                if (failScanner) {
                                    return SimpleCheckRecipeResult.ofFailure("wrongRequirements");
                                }
                                this.tRecipe = assRecipe;
                                // Scanner mode should consume item first
                                eHolders.get(0).mInventory[0] = null;
                                mInventory[1] = null;
                                // Set property
                                computationRequired = computationRemaining = assRecipe.mResearchTime;
                                mMaxProgresstime = 20;
                                mEfficiencyIncrease = 10000;
                                eRequiredData = 1;
                                eAmpereFlow = 1;
                                mEUt = -524288;
                                eHolders.get(0)
                                    .getBaseMetaTileEntity()
                                    .setActive(true);
                                return SimpleCheckRecipeResult.ofSuccess("scanning");
                            }
                        }
                    }
                    case assembly -> {
                        for (GT_Recipe.GT_Recipe_AssemblyLine assRecipe : TecTechRecipeMaps.researchableALRecipeList) {
                            if (GT_Utility.areStacksEqual(assRecipe.mResearchItem, holdItem, true)) {
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
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
        return SimpleCheckRecipeResult.ofFailure("no_research_item");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (!eHolders.isEmpty()) {
            switch (machineType) {
                case assembly -> {
                    if (tRecipe != null && ItemList.Tool_DataStick.isStackEqual(mInventory[1], false, true)) {
                        eHolders.get(0)
                            .getBaseMetaTileEntity()
                            .setActive(false);
                        eHolders.get(0).mInventory[0] = null;
                        if (lServerNames == null) {
                            makeStick();
                        } else {
                            try {
                                makeStick2();
                            } catch (NoSuchFieldError e) {
                                makeStick();
                            }
                        }
                    }
                }
                case scanner -> {
                    mInventory[1] = ItemList.Tool_DataStick.get(1);
                    GT_AssemblyLineUtils.setAssemblyLineRecipeOnDataStick(mInventory[1], tRecipe);
                    eHolders.get(0)
                        .getBaseMetaTileEntity()
                        .setActive(false);
                }
            }
        }
        computationRequired = computationRemaining = 0;
        tRecipe = null;
        holdItem = null;
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.research.name")) // Machine Type: Research
                                                                                              // Station
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
        for (GT_MetaTileEntity_Hatch_Energy tHatch : filterValidMTEs(mEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        for (GT_MetaTileEntity_Hatch_EnergyMulti tHatch : filterValidMTEs(eEnergyMulti)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] { translateToLocalFormatted("tt.keyphrase.Energy_Hatches", clientLocale) + ":",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            (mEUt <= 0 ? translateToLocalFormatted("tt.keyphrase.Probably_uses", clientLocale) + ": "
                : translateToLocalFormatted("tt.keyphrase.Probably_makes", clientLocale) + ": ")
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(Math.abs(mEUt))
                + EnumChatFormatting.RESET
                + " EU/t "
                + translateToLocalFormatted("tt.keyword.at", clientLocale)
                + " "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(eAmpereFlow)
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
                + GT_Utility.formatNumbers(eMaxAmpereFlow)
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
                + GT_Utility.formatNumbers(eAvailableData)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(eRequiredData)
                + EnumChatFormatting.RESET,
            translateToLocalFormatted("tt.keyphrase.Computation_Remaining", clientLocale) + ":",
            EnumChatFormatting.GREEN + GT_Utility.formatNumbers(computationRemaining / 20L)
                + EnumChatFormatting.RESET
                + " / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(computationRequired / 20L) };
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3],
                new TT_RenderedExtendedFacingTexture(
                    aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                        : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][3] };
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
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
        for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
            r.getBaseMetaTileEntity()
                .setActive(false);
        }
        computationRequired = computationRemaining = 0;
        tRecipe = null;
        holdItem = null;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (aBaseMetaTileEntity.isServerSide()) {
            if (computationRemaining > 0) {
                tRecipe = null;
                if (holdItem != null) {
                    if (ItemList.Tool_DataStick.isStackEqual(mInventory[1], false, true)) {
                        for (GT_Recipe.GT_Recipe_AssemblyLine tRecipe : TecTechRecipeMaps.researchableALRecipeList) {
                            if (GT_Utility.areStacksEqual(tRecipe.mResearchItem, holdItem, true)) {
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
                    for (GT_MetaTileEntity_Hatch_Holder r : eHolders) {
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
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Holder) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eHolders.add((GT_MetaTileEntity_Hatch_Holder) aMetaTileEntity);
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
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("hasProblems", (getIdealStatus() - getRepairStatus()) > 0);
        tag.setFloat("efficiency", mEfficiency / 100.0F);
        tag.setBoolean("incompleteStructure", (getBaseMetaTileEntity().getErrorDisplayID() & 64) != 0);
        tag.setString("machineType", machineType);
        tag.setLong("computation", (computationRequired - computationRemaining) / 20L);
        tag.setLong("computationRequired", computationRequired / 20L);
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
    public IStructureDefinition<GT_MetaTileEntity_EM_research> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum HolderHatchElement implements IHatchElement<GT_MetaTileEntity_EM_research> {

        INSTANCE;

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return Collections.singletonList(GT_MetaTileEntity_Hatch_Holder.class);
        }

        @Override
        public IGT_HatchAdder<? super GT_MetaTileEntity_EM_research> adder() {
            return GT_MetaTileEntity_EM_research::addHolderToMachineList;
        }

        @Override
        public long count(GT_MetaTileEntity_EM_research t) {
            return t.eHolders.size();
        }
    }
}

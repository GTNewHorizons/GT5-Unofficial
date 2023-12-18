package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Mods.BiomesOPlenty;
import static gregtech.api.enums.Mods.ForbiddenMagic;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.Natura;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.PamsHarvestTheNether;
import static gregtech.api.enums.Mods.TaintedMagic;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gregtech.api.enums.Mods.TwilightForest;
import static gregtech.api.enums.Mods.Witchery;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase.GTPPHatchElement.TTEnergy;

import java.util.ArrayList;
import java.util.HashMap;

import javax.annotation.Nonnull;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import forestry.api.arboriculture.EnumTreeChromosome;
import forestry.api.arboriculture.ITree;
import forestry.api.arboriculture.TreeManager;
import forestry.api.genetics.IAlleleBoolean;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.VoidProtectionHelper;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_MultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper;
import gtPlusPlus.xmod.gregtech.common.helpers.TreeFarmHelper.SAWTOOL;

public class GregtechMetaTileEntityTreeFarm extends GregtechMeta_MultiBlockBase<GregtechMetaTileEntityTreeFarm>
        implements ISurvivalConstructable {

    public static int CASING_TEXTURE_ID;
    public static String mCasingName = "Sterile Farm Casing";
    public static HashMap<String, ItemStack> sLogCache = new HashMap<>();
    private static final int TICKS_PER_OPERATION = 100;

    private int mCasing;
    private static IStructureDefinition<GregtechMetaTileEntityTreeFarm> STRUCTURE_DEFINITION = null;

    private SAWTOOL mToolType;
    private ItemStack mSapling;
    private ItemStack mWood;
    private float heightModifier = 1.0f;
    private float saplingsModifier = 1.0f;
    private int girthModifier = 1;

    public GregtechMetaTileEntityTreeFarm(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
    }

    public GregtechMetaTileEntityTreeFarm(final String aName) {
        super(aName);
        CASING_TEXTURE_ID = TAE.getIndexFromPage(1, 15);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntityTreeFarm(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Tree Farm";
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType()).addInfo("Converts EU to Logs").addInfo("Eu Usage: 100% | Parallel: 1")
                .addInfo("Requires a Saw or Chainsaw in GUI slot").addInfo("Output multiplier:").addInfo("Saw = 1x")
                .addInfo("Buzzsaw = 2x").addInfo("Chainsaw = 4x")
                .addInfo("Add a sapling in the input bus to select wood type output")
                .addInfo("The sapling is not consumed").addInfo("Tools can also be fed to the controller via input bus")
                .addInfo("The working speed is fixed for 5s")
                .addInfo("Production Formula: (2 * tier^2 - 2 * tier + 5) * 5 * saw boost")
                .addInfo("When fertilizer is supplied, produces saplings instead of logs")
                .addInfo("Forestry saplings can get increased production")
                .addPollutionAmount(getPollutionPerSecond(null)).addSeparator().beginStructureBlock(3, 3, 3, true)
                .addController("Front center").addCasingInfoMin("Sterile Farm Casing", 8, false)
                .addInputBus("Any casing", 1).addOutputBus("Any casing", 1).addEnergyHatch("Any casing", 1)
                .addMaintenanceHatch("Any casing", 1).addMufflerHatch("Any casing", 1)
                .toolTipFinisher(CORE.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.Overlay_Machine_Controller_Advanced;
    }

    @Override
    protected int getCasingTextureId() {
        return CASING_TEXTURE_ID;
    }

    @Override
    public boolean isCorrectMachinePart(final ItemStack aStack) {
        // is correct part && either not powered tool or have enough power
        if (TreeFarmHelper.isValidForGUI(aStack)
                && GT_MetaGenerated_Tool.getToolDamage(aStack) < GT_MetaGenerated_Tool.getToolMaxDamage(aStack)) {
            return GT_ModHandler.isElectricItem(aStack) ? GT_ModHandler.canUseElectricItem(aStack, 32) : true;
        }
        return false;
    }

    /**
     * Method used to get the boost based on the ordinal of the saw
     * 
     * @param sawType type of the saw
     * @return an int corresponding to the boost
     */
    public int getSawBoost(SAWTOOL sawType) {
        return switch (sawType) {
            case SAW -> 1;
            case BUZZSAW -> 2;
            case CHAINSAW -> 4;
            default -> 1;
        };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        // Only for visual
        return GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        final ItemStack controllerStack = getControllerSlot();
        if (!isCorrectMachinePart(controllerStack) && !replaceTool())
            return SimpleCheckRecipeResult.ofFailure("no_saw");
        if (!checkSapling()) return SimpleCheckRecipeResult.ofFailure("no_sapling");

        this.mToolType = TreeFarmHelper.isCorrectMachinePart(controllerStack);

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));

        int aOutputAmount = ((2 * (tTier * tTier)) - (2 * tTier) + 5) * (TICKS_PER_OPERATION / 20)
                * getSawBoost(mToolType);
        int aFert = hasLiquidFert();
        ItemStack[] toOutput;

        if (aFert > 0) { // Sapling
            if (aFert < aOutputAmount) {
                aOutputAmount /= 10;
            }
            int amplifiedOutputAmount = (int) (aOutputAmount * saplingsModifier);
            toOutput = new ItemStack[] { ItemUtils.getSimpleStack(mSapling, amplifiedOutputAmount) };
        } else { // Log
            int amplifiedOutputAmount = (int) (aOutputAmount * heightModifier * girthModifier);
            toOutput = new ItemStack[] { ItemUtils.getSimpleStack(mWood, amplifiedOutputAmount) };
        }

        VoidProtectionHelper voidProtection = new VoidProtectionHelper().setMachine(this).setItemOutputs(toOutput)
                .build();

        if (voidProtection.isItemFull()) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        if (aFert > 0 && aFert >= aOutputAmount) {
            tryConsumeLiquidFert(aOutputAmount);
        }

        this.mOutputItems = toOutput;

        this.mMaxProgresstime = TICKS_PER_OPERATION;
        this.lEUt = MaterialUtils.getVoltageForTier(tTier);

        this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (this.lEUt > 0) {
            this.lEUt = (-this.lEUt);
        }

        this.tryDamageTool();
        this.updateSlots();
        return SimpleCheckRecipeResult.ofSuccess("growing_trees");
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 8 && checkHatch();
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public IStructureDefinition<GregtechMetaTileEntityTreeFarm> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntityTreeFarm>builder()
                    .addShape(
                            mName,
                            transpose(
                                    new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" },
                                            { "CCC", "CCC", "CCC" }, }))
                    .addElement(
                            'C',
                            buildHatchAdder(GregtechMetaTileEntityTreeFarm.class).atLeast(
                                    InputHatch,
                                    OutputHatch,
                                    InputBus,
                                    OutputBus,
                                    Maintenance,
                                    Energy.or(TTEnergy),
                                    Muffler).casingIndex(CASING_TEXTURE_ID).dot(1).buildAndChain(
                                            onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings2Misc, 15))))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return CORE.ConfigSwitches.pollutionPerSecondMultiTreeFarm;
    }

    @Override
    public int getDamageToComponent(final ItemStack aStack) {
        return MathUtils.balance((int) (75 - ((GT_MetaGenerated_Tool.getPrimaryMaterial(aStack).getMass()))), 5, 120);
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    private boolean tryDamageTool() {
        GT_ModHandler.damageOrDechargeItem(this.mInventory[1], 1, 32, null);
        return replaceTool();
    }

    public boolean replaceTool() {
        ItemStack invItem = this.mInventory[1];
        if (isCorrectMachinePart(invItem)) return true;
        else {
            if (invItem != null) {
                this.mInventory[1] = null;
                this.addOutput(invItem);
            }

            for (ItemStack aStack : getStoredInputs()) {
                if (isCorrectMachinePart(aStack)) {
                    this.mInventory[1] = aStack.copy();
                    this.depleteInput(aStack);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean checkSapling() {
        for (ItemStack uStack : this.getStoredInputs()) {

            if (uStack != null) {
                String registryName = Item.itemRegistry.getNameForObject(uStack.getItem());
                ItemStack aWood = sLogCache.get(registryName + ":" + uStack.getItemDamage());

                if (aWood != null) {
                    this.heightModifier = 1.0f;
                    this.saplingsModifier = 1.0f;
                    this.girthModifier = 1;

                    this.mSapling = uStack;
                    this.mWood = aWood;
                    return true;
                } else {
                    if (registryName.equals("Forestry:sapling")) {

                        ITree tree = TreeManager.treeRoot.getMember(uStack);

                        this.heightModifier = Math.max(3 * (tree.getGenome().getHeight() - 1), 0) + 1;
                        this.saplingsModifier = Math.max(tree.getGenome().getFertility() * 20, 1);
                        this.girthModifier = tree.getGenome().getGirth();
                        boolean fireproof = ((IAlleleBoolean) tree.getGenome()
                                .getChromosomes()[EnumTreeChromosome.FIREPROOF.ordinal()].getActiveAllele()).getValue();

                        aWood = sLogCache.get(tree.getIdent() + (fireproof ? "fireproof" : ""));

                        this.mSapling = uStack;
                        this.mWood = aWood;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void loadMapWoodFromSapling() {

        // galaxySpace
        mapSaplingToLog("GalaxySpace:barnardaCsapling:1", GT_ModHandler.getModItem(GalaxySpace.ID, "barnardaClog", 1)); // barnarda
                                                                                                                        // c

        // minecraft
        mapSaplingToLog("minecraft:sapling:0", new ItemStack(Blocks.log, 1, 0)); // oak
        mapSaplingToLog("minecraft:sapling:1", new ItemStack(Blocks.log, 1, 1)); // spruce
        mapSaplingToLog("minecraft:sapling:2", new ItemStack(Blocks.log, 1, 2)); // birch
        mapSaplingToLog("minecraft:sapling:3", new ItemStack(Blocks.log, 1, 3)); // jungle
        mapSaplingToLog("minecraft:sapling:4", new ItemStack(Blocks.log2, 1, 0)); // acacia
        mapSaplingToLog("minecraft:sapling:5", new ItemStack(Blocks.log2, 1, 1)); // dark oak

        // ic2
        mapSaplingToLog("IC2:blockRubSapling:0", GT_ModHandler.getModItem(IndustrialCraft2.ID, "blockRubWood", 1)); // rubber

        // natura
        mapSaplingToLog("Natura:florasapling:0", GT_ModHandler.getModItem(Natura.ID, "redwood", 1, 1)); // redwood
        mapSaplingToLog("Natura:florasapling:1", GT_ModHandler.getModItem(Natura.ID, "tree", 1, 0)); // eucalyptus
        mapSaplingToLog("Natura:florasapling:2", GT_ModHandler.getModItem(Natura.ID, "tree", 1, 3)); // hopseed
        mapSaplingToLog("Natura:florasapling:3", GT_ModHandler.getModItem(Natura.ID, "tree", 1, 1)); // sakura
        mapSaplingToLog("Natura:florasapling:4", GT_ModHandler.getModItem(Natura.ID, "tree", 1, 2)); // ghostwood
        mapSaplingToLog("Natura:florasapling:5", GT_ModHandler.getModItem(Natura.ID, "bloodwood", 1, 0)); // bloodwood
        mapSaplingToLog("Natura:florasapling:6", GT_ModHandler.getModItem(Natura.ID, "Dark Tree", 1, 0)); // darkwood
        mapSaplingToLog("Natura:florasapling:7", GT_ModHandler.getModItem(Natura.ID, "Dark Tree", 1, 1)); // fusewood

        mapSaplingToLog("Natura:Rare Sapling:0", GT_ModHandler.getModItem(Natura.ID, "Rare Tree", 1, 0)); // maple
        mapSaplingToLog("Natura:Rare Sapling:1", GT_ModHandler.getModItem(Natura.ID, "Rare Tree", 1, 1)); // silverbell
        mapSaplingToLog("Natura:Rare Sapling:2", GT_ModHandler.getModItem(Natura.ID, "Rare Tree", 1, 2)); // amaranth
        mapSaplingToLog("Natura:Rare Sapling:3", GT_ModHandler.getModItem(Natura.ID, "Rare Tree", 1, 3)); // tigerwood
        mapSaplingToLog("Natura:Rare Sapling:4", GT_ModHandler.getModItem(Natura.ID, "willow", 1, 0)); // willow

        // BOP
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:0", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs1", 1, 0)); // Sacred
                                                                                                                         // Oak
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:1", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs2", 1, 2)); // Mangrove
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:2", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs2", 1, 3)); // Palm
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:3", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs3", 1, 0)); // Redwood
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:4", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs3", 1, 1)); // Willow
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:5", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs4", 1, 0)); // Pine
        mapSaplingToLog("BiomesOPlenty:colorizedSaplings:6", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs4", 1, 3)); // Mahogany
        mapSaplingToLog("BiomesOPlenty:saplings:2", GT_ModHandler.getModItem(BiomesOPlenty.ID, "bamboo", 1, 0)); // Bamboo
        mapSaplingToLog("BiomesOPlenty:saplings:3", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs2", 1, 1)); // Magic
        mapSaplingToLog("BiomesOPlenty:saplings:4", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs1", 1, 2)); // Dark
        mapSaplingToLog("BiomesOPlenty:saplings:5", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs3", 1, 2)); // Dying/Dead
        mapSaplingToLog("BiomesOPlenty:saplings:6", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs1", 1, 3)); // Fir
        mapSaplingToLog("BiomesOPlenty:saplings:7", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs2", 1, 0)); // Ethereal
        mapSaplingToLog("BiomesOPlenty:saplings:10", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs1", 1, 1)); // Pink
                                                                                                                 // Cherry
        mapSaplingToLog("BiomesOPlenty:saplings:12", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs1", 1, 1)); // White
                                                                                                                 // Cherry
        mapSaplingToLog("BiomesOPlenty:saplings:13", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs4", 1, 1)); // Hellbark
        mapSaplingToLog("BiomesOPlenty:saplings:14", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs4", 1, 2)); // Jacaranda
        mapSaplingToLog("minecraft:yellow_flower:0", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs3", 1, 3)); // Giant
                                                                                                                 // Flower
                                                                                                                 // Stem
        mapSaplingToLog("minecraft:red_flower:0", GT_ModHandler.getModItem(BiomesOPlenty.ID, "logs3", 1, 3)); // Giant
                                                                                                              // Flower
                                                                                                              // Stem

        // Witchery
        mapSaplingToLog("witchery:witchsapling:0", GT_ModHandler.getModItem(Witchery.ID, "witchlog", 1, 0)); // Rowan
        mapSaplingToLog("witchery:witchsapling:1", GT_ModHandler.getModItem(Witchery.ID, "witchlog", 1, 1)); // Alder
        mapSaplingToLog("witchery:witchsapling:2", GT_ModHandler.getModItem(Witchery.ID, "witchlog", 1, 2)); // Hawthorn

        // TConstruct
        mapSaplingToLog("TConstruct:slime.sapling:0", GT_ModHandler.getModItem(TinkerConstruct.ID, "slime.gel", 1)); // green
        // slime
        // blocks

        // TaintedMagic
        mapSaplingToLog(
                "TaintedMagic:BlockWarpwoodSapling:0",
                GT_ModHandler.getModItem(TaintedMagic.ID, "BlockWarpwoodLog", 1)); // warpwood

        // Thaumcraft
        mapSaplingToLog(
                "Thaumcraft:blockCustomPlant:0",
                GT_ModHandler.getModItem(Thaumcraft.ID, "blockMagicalLog", 1, 0)); // greatwood
        mapSaplingToLog(
                "Thaumcraft:blockCustomPlant:1",
                GT_ModHandler.getModItem(Thaumcraft.ID, "blockMagicalLog", 1, 1)); // silverwood

        // gt++
        mapSaplingToLog(
                "miscutils:blockRainforestOakSapling:0",
                GT_ModHandler.getModItem(GTPlusPlus.ID, "blockRainforestOakLog", 1)); // rainforest
        mapSaplingToLog("miscutils:blockPineSapling:0", GT_ModHandler.getModItem(GTPlusPlus.ID, "blockPineLogLog", 1)); // pine

        // Harvestcraft
        mapSaplingToLog("harvestcraft:pampistachioSapling:0", new ItemStack(Blocks.log, 1, 3)); // Pistachio
        mapSaplingToLog("harvestcraft:pampapayaSapling:0", new ItemStack(Blocks.log, 1, 3)); // Papaya
        mapSaplingToLog("harvestcraft:pammapleSapling:0", GT_ModHandler.getModItem(PamsHarvestCraft.ID, "pamMaple", 1)); // Maple
        mapSaplingToLog("harvestcraft:pamappleSapling:0", new ItemStack(Blocks.log, 1, 0)); // Apple
        mapSaplingToLog("harvestcraft:pamdateSapling:0", new ItemStack(Blocks.log, 1, 3)); // Date
        mapSaplingToLog("harvestcraft:pamorangeSapling:0", new ItemStack(Blocks.log, 1, 3)); // Orange
        mapSaplingToLog("harvestcraft:pamdragonfruitSapling:0", new ItemStack(Blocks.log, 1, 3)); // Dragon fruit
        mapSaplingToLog("harvestcraft:pamnutmegSapling:0", new ItemStack(Blocks.log, 1, 0)); // NutMeg
        mapSaplingToLog(
                "harvestcraft:pampaperbarkSapling:0",
                GT_ModHandler.getModItem(PamsHarvestCraft.ID, "pamPaperbark", 1)); // Paperbark
        mapSaplingToLog("harvestcraft:pammangoSapling:0", new ItemStack(Blocks.log, 1, 3)); // Mango
        mapSaplingToLog("harvestcraft:pamavocadoSapling:0", new ItemStack(Blocks.log, 1, 0)); // Avocado
        mapSaplingToLog("harvestcraft:pamchestnutSapling:0", new ItemStack(Blocks.log, 1, 0)); // Chestnut
        mapSaplingToLog("harvestcraft:pampeppercornSapling:0", new ItemStack(Blocks.log, 1, 3)); // Peppercorn
        mapSaplingToLog("harvestcraft:pampecanSapling:0", new ItemStack(Blocks.log, 1, 3)); // Pecan
        mapSaplingToLog("harvestcraft:pamcashewSapling:0", new ItemStack(Blocks.log, 1, 3)); // Cashew
        mapSaplingToLog("harvestcraft:pamfigSapling:0", new ItemStack(Blocks.log, 1, 3)); // Fig
        mapSaplingToLog("harvestcraft:pamoliveSapling:0", new ItemStack(Blocks.log, 1, 3)); // Olive
        mapSaplingToLog(
                "harvestcraft:pamcinnamonSapling:0",
                GT_ModHandler.getModItem(PamsHarvestCraft.ID, "pamCinnamon", 1)); // Cinnamon
        mapSaplingToLog("harvestcraft:pampeachSapling:0", new ItemStack(Blocks.log, 1, 3)); // Peach
        mapSaplingToLog("harvestcraft:pamlemonSapling:0", new ItemStack(Blocks.log, 1, 3)); // Lemon
        mapSaplingToLog("harvestcraft:pamvanillabeanSapling:0", new ItemStack(Blocks.log, 1, 3)); // Vanilla
        mapSaplingToLog("harvestcraft:pamalmondSapling:0", new ItemStack(Blocks.log, 1, 3)); // Almond
        mapSaplingToLog("harvestcraft:pambananaSapling:0", new ItemStack(Blocks.log, 1, 3)); // Banana
        mapSaplingToLog("harvestcraft:pamdurianSapling:0", new ItemStack(Blocks.log, 1, 3)); // Durian
        mapSaplingToLog("harvestcraft:pamplumSapling:0", new ItemStack(Blocks.log, 1, 0)); // Plum
        mapSaplingToLog("harvestcraft:pamlimeSapling:0", new ItemStack(Blocks.log, 1, 3)); // Lime
        mapSaplingToLog("harvestcraft:pampearSapling:0", new ItemStack(Blocks.log, 1, 0)); // Pear
        mapSaplingToLog("harvestcraft:pamgooseberrySapling:0", new ItemStack(Blocks.log, 1, 0)); // Gooseberry
        mapSaplingToLog("harvestcraft:pamcherrySapling:0", new ItemStack(Blocks.log, 1, 0)); // Cherry
        mapSaplingToLog("harvestcraft:pampomegranateSapling:0", new ItemStack(Blocks.log, 1, 3)); // Pomegranate
        mapSaplingToLog("harvestcraft:pamwalnutSapling:0", new ItemStack(Blocks.log, 1, 0)); // Walnut
        mapSaplingToLog("harvestcraft:pampersimmonSapling:0", new ItemStack(Blocks.log, 1, 3)); // Persimmon
        mapSaplingToLog("harvestcraft:pamapricotSapling:0", new ItemStack(Blocks.log, 1, 3)); // Apricot
        mapSaplingToLog("harvestcraft:pamcoconutSapling:0", new ItemStack(Blocks.log, 1, 3)); // Coconut
        mapSaplingToLog("harvestcraft:pamgrapefruitSapling:0", new ItemStack(Blocks.log, 1, 3)); // Grapefruit
        mapSaplingToLog("harvestcraft:pamstarfruitSapling:0", new ItemStack(Blocks.log, 1, 3)); // Starfruit

        // Harvest The Nether
        mapSaplingToLog(
                "harvestthenether:netherSapling:0",
                GT_ModHandler.getModItem(PamsHarvestTheNether.ID, "netherLog", 1)); // Nether

        // The Twilight Forest
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:0",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 0)); // Sickly Twilight Oak
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:1",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 1)); // Canopy Tree
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:2",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 2)); // Twilight Mangrove
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:3",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 3)); // Darkwood
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:4",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 0)); // Robust Twilight Oad
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:5",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFMagicLog", 1, 0)); // Tree of Time
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:6",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFMagicLog", 1, 1)); // Tree of Trasformation
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:7",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFMagicLog", 1, 2)); // Miner's Tree
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:8",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFMagicLog", 1, 3)); // Sorting Tree
        mapSaplingToLog(
                "TwilightForest:tile.TFSapling:9",
                GT_ModHandler.getModItem(TwilightForest.ID, "tile.TFLog", 1, 0)); // Rainbow Oak

        // Thaumic Bases
        mapSaplingToLog("thaumicbases:goldenOakSapling:0", new ItemStack(Blocks.log, 1, 0)); // Golden Oak
        mapSaplingToLog("thaumicbases:goldenOakSapling:1", GT_ModHandler.getModItem(ThaumicBases.ID, "genLogs", 1, 0)); // Peaceful
        mapSaplingToLog("thaumicbases:goldenOakSapling:2", GT_ModHandler.getModItem(ThaumicBases.ID, "genLogs", 1, 1)); // Nether
        mapSaplingToLog("thaumicbases:goldenOakSapling:3", GT_ModHandler.getModItem(ThaumicBases.ID, "genLogs", 1, 2)); // Ender

        // Forbidden Magic
        mapSaplingToLog("ForbiddenMagic:TaintSapling:0", GT_ModHandler.getModItem(ForbiddenMagic.ID, "TaintLog", 1)); // Tainted
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

    public static void mapSaplingToLog(String aSapling, ItemStack aLog) {
        ItemStack aSaplingStack = ItemUtils.getItemStackFromFQRN(aSapling, 1);
        if (aSaplingStack != null && aLog != null) {
            sLogCache.put(aSapling, aLog);
            addFakeRecipeToNEI(aSaplingStack, aLog);
        } else {
            Logger.INFO("Unable to add Tree Growth Simulation for " + aSapling);
        }
    }

    private static int sRecipeID = 0;

    public static boolean addFakeRecipeToNEI(@Nonnull ItemStack aSapling, ItemStack aLog) {
        int aRecipes = GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.getAllRecipes().size();
        Logger.INFO(
                "Adding Tree Growth Simulation for " + aSapling.getDisplayName()
                        + " -> "
                        + (aLog == null ? "NULL" : aLog.getDisplayName()));
        ItemStack[] aOutput = new ItemStack[] { aLog, aSapling };
        String aOutputs = ItemUtils.getArrayStackNames(aOutput);
        Logger.INFO("" + aOutputs);
        ItemStack inputStack = aSapling.copy();
        inputStack.stackSize = 0;
        GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.addFakeRecipe(
                false,
                new ItemStack[] { inputStack },
                aOutput,
                null,
                new int[] { 10000, 1000 },
                new FluidStack[] { FluidUtils.getFluidStack(ModItems.fluidFertBasic, 1) },
                new FluidStack[] {},
                1,
                sRecipeID++,
                0);
        return GTPPRecipeMaps.treeGrowthSimulatorFakeRecipes.getAllRecipes().size() > aRecipes;
    }

    public int hasLiquidFert() {
        ArrayList<FluidStack> aFluids = this.getStoredFluids();
        for (FluidStack aFluid : aFluids) {
            if (aFluid.getFluid().equals(ModItems.fluidFertBasic)) {
                return aFluid.amount;
            }
        }
        return 0;
    }

    public boolean tryConsumeLiquidFert(int aFluidAmount) {
        return this.depleteInput(FluidUtils.getFluidStack(ModItems.fluidFertBasic, aFluidAmount));
    }
}

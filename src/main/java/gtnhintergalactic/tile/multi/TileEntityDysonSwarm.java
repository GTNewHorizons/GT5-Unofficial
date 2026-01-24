package gtnhintergalactic.tile.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.filterValidMTEs;
import static net.minecraft.util.EnumChatFormatting.*;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti;
import static tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.InputData;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import gtnhintergalactic.client.IGTextures;
import gtnhintergalactic.client.TooltipUtil;
import gtnhintergalactic.config.IGConfig;
import micdoodle8.mods.galacticraft.api.world.IOrbitDimension;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;

public class TileEntityDysonSwarm extends TTMultiblockBase implements ISurvivalConstructable {

    private static final Map<Locale, DecimalFormat> DECIMAL_FORMATTERS = new HashMap<>();
    private static final String STRUCTURE_PIECE_MAIN = "main";

    // spotless:off

    private static final IStructureDefinition<TileEntityDysonSwarm> STRUCTURE_DEFINITION = StructureDefinition
        .<TileEntityDysonSwarm>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            StructureUtility.transpose(
                new String[][] {
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----------", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "         ---    ", "  ttt           ",
                        "  ttt        k  ", "  ttt       k k ", "             k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----------", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "  ttt    ---    ", " ttttt          ",
                        " ttttt       k  ", " ttttt      k k ", "  ttt        k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----------", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "  ttt    ---    ", " ttttt          ",
                        " ttttt       k  ", " ttttt      k k ", "  ttt        k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----------", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "  ttt    ---    ", " ttttt          ",
                        " ttttt       k  ", " ttttt      k k ", "  ttt        k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----------", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "         ---    ", "  ttt           ",
                        "  tst        k  ", "  ttt       k k ", "             k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----------", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "         ---    ", "                ",
                        "   s         k  ", "            k k ", "             k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----h-----", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "  ttt    ---    ", " t g t          ",
                        " tgsgt       k  ", " t g t      k k ", "  ttt        k  ", "                " },
                    { "         ---    ", "        -----   ", "       -------  ", "      --------- ",
                        "     -----------", "     -----f-----", "     -----------", "      --------- ",
                        "       -------  ", "        -----   ", "         ---    ", "                ",
                        "   s         k  ", "            k k ", "             k  ", "                " },
                    { "         ddd    ", "        d---d   ", "       d-----d  ", "      d-------d ",
                        "     d---------d", "     d----f----d", "     d---------d", "      d-------d ",
                        "       d-----d  ", "        d---d   ", "  ttt    ddd    ", " t g t          ",
                        " tgsgt      kmk ", " t g t      m m ", "  ttt       kmk ", "                " },
                    { "                ", "         ddd    ", "        ddddd   ", "       dd---dd  ",
                        "      dd-----dd ", "      dd--f--dd ", "      dd-----dd ", "       dd---dd  ",
                        "        ddddd   ", "         ddd    ", "                ", "                ",
                        "   s        kmk ", "            m m ", "            kmk ", "                " },
                    { "                ", "                ", "                ", "         ddd    ",
                        "        ddddd   ", "        ddddd   ", "        ddddd   ", "         ddd    ",
                        "                ", "                ", "  ttt           ", " t g t          ",
                        " tgsgt      kmk ", " t g t      m m ", "  ttt       kmk ", "                " },
                    { "                ", "                ", "                ", "                ",
                        "         f f    ", "                ", "         f f    ", "                ",
                        "                ", "                ", "                ", "                ",
                        "   s        kmk ", "            m m ", "            kmk ", "                " },
                    { "                ", "                ", "                ", "                ",
                        "         f f    ", "                ", "         f f    ", "                ",
                        "                ", "                ", "  ttt           ", " t g t          ",
                        " tgsgt      kmk ", " t g t      m m ", "  ttt       kmk ", "                " },
                    { "                ", "                ", "                ", "                ",
                        "         f f    ", "                ", "         f f    ", "                ",
                        "                ", "                ", "                ", "                ",
                        "   s        kmk ", "            m m ", "            kmk ", "                " },
                    { "                ", "                ", "                ", "                ",
                        "         f f    ", "                ", "         f f    ", "                ",
                        "                ", "                ", "                ", "                ",
                        "   s        kmk ", "            m m ", "            kmk ", "                " },
                    { "                ", "                ", "                ", "                ",
                        "         f f    ", "                ", "         f f    ", "                ",
                        "                ", "                ", "  ppp           ", " p   p          ",
                        " p s p      kmk ", " p   p      m m ", "  ppp       kmk ", "                " },
                    { "                ", "                ", "                ", "        xxxxx   ",
                        "        xxxxx   ", "        xxxxx   ", "        xxxxx   ", "        xxxxx   ",
                        "                ", "yyyyyyy         ", "yyyyyyy         ", "yypppyy    zzzzz",
                        "yypypyy    zzzzz", "yypppyy    zzjzz", "yyyyyyy    zzzzz", "yyyyyyy    zzzzz" },
                    { "                ", "                ", "                ", "        xeeex   ",
                        "        eccce   ", "        eccce   ", "        eccce   ", "        xeeex   ",
                        "                ", "ooooooo         ", "oyyyyyo         ", "oyyyyyo    ziiiz",
                        "oyyyyyo    izzzi", "oyyyyyo    izzzi", "oyyyyyo    izzzi", "ooooooo    ziiiz" },
                    { "                ", "                ", "                ", "        xx~xx   ",
                        "        xxxxx   ", "        xxxxx   ", "        xxxxx   ", "        xxxxx   ",
                        "                ", "yyyyyyy         ", "yyyyyyy         ", "yyyyyyy    zzzzz",
                        "yyyyyyy    zzzzz", "yyyyyyy    zzzzz", "yyyyyyy    zzzzz", "yyyyyyy    zzzzz" },
                    { "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb",
                        "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb",
                        "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb",
                        "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb", "bbbbbbbbbbbbbbbb" } }))
        .addElement('b', ofBlock(GregTechAPI.sBlockCasingsDyson, 9)) // Ultra High Strength Concrete Floor
        .addElement('c', ofBlock(GregTechAPI.sBlockCasings5, 8)) // Awakened Draconium Coil Block
        .addElement('d', ofBlock(GregTechAPI.sBlockCasingsDyson, 1)) // Receiver Dish Block
        .addElement(
            'e',
            buildHatchAdder(TileEntityDysonSwarm.class).atLeast(Dynamo, DynamoMulti)
                .casingIndex(IGTextures.CASING_INDEX_RECEIVER)
                .hint(1)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasingsDyson, 0))) // Receiver Base Casing
        .addElement('f', ofFrame(Materials.HSSS))
        .addElement('g', ofFrame(Materials.Titanium))
        .addElement('h', ofBlock(GregTechAPI.sBlockCasings6, 10)) // Hermetic Casing X
        .addElement(
            'i',
            buildHatchAdder(TileEntityDysonSwarm.class)
                .atLeast(InputBus, InputHatch)
                .casingIndex(IGTextures.CASING_INDEX_LAUNCH)
                .hint(2)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasingsDyson, 2))) // Deployment Unit Base Casing
        .addElement('j', ofBlock(GregTechAPI.sBlockCasingsDyson, 3)) // Deployment Unit Core
        .addElement('k', ofFrame(Materials.SuperconductorUHVBase))
        .addElement('m', ofBlock(GregTechAPI.sBlockCasingsDyson, 4)) // Deployment Unit Superconducting Magnet
        .addElement(
            'o',
            buildHatchAdder(TileEntityDysonSwarm.class)
                .atLeast(InputData)
                .casingIndex(IGTextures.CASING_INDEX_COMMAND)
                .hint(4)
                .buildAndChain(ofBlock(GregTechAPI.sBlockCasingsDyson, 5))) // Command Center Base Casing
        .addElement('p', ofBlock(GregTechAPI.sBlockCasingsDyson, 6)) // Command Center Primary Windings
        .addElement('s', ofBlock(GregTechAPI.sBlockCasingsDyson, 7)) // Command Center Secondary Windings
        .addElement('t', ofBlock(GregTechAPI.sBlockCasingsDyson, 8)) // Command Center Toroid Casing
        .addElement('x', ofBlock(GregTechAPI.sBlockCasingsDyson, 0)) // Receiver Base Casing
        .addElement('y', ofBlock(GregTechAPI.sBlockCasingsDyson, 5)) // Command Center Base Casing
        .addElement('z', ofBlock(GregTechAPI.sBlockCasingsDyson, 2)) // Deployment Unit Base Casing
        .build();

    // spotless:on

    private long euPerTick = 0;
    private double powerFactor = 0.0;
    private int moduleCount = 0;

    public TileEntityDysonSwarm(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected TileEntityDysonSwarm(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TileEntityDysonSwarm(this.mName);
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        powerFactor = getPowerFactor();
    }

    /*************
     * STRUCTURE *
     *************/
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM(STRUCTURE_PIECE_MAIN, 10, 18, 3, stackSize, hintsOnly);
    }

    @Override
    public IStructureDefinition<? extends TTMultiblockBase> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        return this.survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 10, 18, 3, elementBudget, env, true, true);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(STRUCTURE_PIECE_MAIN, 10, 18, 3) && mInputBusses.size() > 0
            && mInputHatches.size() > 0
            && eInputData.size() > 0
            && (mDynamoHatches.size() > 0 || eDynamoMulti.size() > 0);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return true;
    }

    /**********
     * RECIPE *
     **********/
    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        for (MTEHatchInputBus bus : filterValidMTEs(mInputBusses)) {
            for (int i = 0; i < bus.getBaseMetaTileEntity()
                .getSizeInventory(); i++) {
                ItemStack stack = bus.getBaseMetaTileEntity()
                    .getStackInSlot(i);
                if (stack != null && stack.getItem() == ItemList.DysonSwarmModule.getItem()
                    && stack.getItemDamage() == 0
                    && moduleCount < IGConfig.dysonSwarm.maxModules + 1) {
                    int usedStackSize = Math.min(stack.stackSize, IGConfig.dysonSwarm.maxModules - moduleCount);
                    moduleCount += usedStackSize;
                    stack.stackSize -= usedStackSize;
                }
            }
        }

        euPerTick = (long) ((long) moduleCount * IGConfig.dysonSwarm.euPerModule * powerFactor);

        if (moduleCount > 0 && depleteInput(IGConfig.dysonSwarm.getCoolantStack())) {
            // With a certain chance (configurable), the size of the ItemStack(s) is reduced.
            // This has the effect that the player must constantly replace "broken" Modules.
            destroyModules();
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 72000;
            return true;
        }
        mEfficiency = 0;
        return false;
    }

    private void destroyModules() {
        if (IGConfig.dysonSwarm.destroyModuleA <= 0.0f) {
            return;
        }

        moduleCount -= moduleCount * (2 * IGConfig.dysonSwarm.destroyModuleChance)
            / (Math.exp(-IGConfig.dysonSwarm.destroyModuleA * (moduleCount - 1)) + Math.exp(
                IGConfig.dysonSwarm.destroyModuleB
                    * Math.min(eAvailableData, (long) IGConfig.dysonSwarm.destroyModuleMaxCPS)));

        if (moduleCount < 0) {
            moduleCount = 0;
        }
    }

    @Override
    public boolean energyFlowOnRunningTick(ItemStack aStack, boolean allowProduction) {
        if (allowProduction && euPerTick > 0) {
            addEnergyOutput_EM(euPerTick, 1);
        }
        return true;
    }

    @Override
    public boolean addEnergyOutput_EM(long EU, long Amperes) {
        return addEnergyOutput_EM(EU, Amperes, true);
    }

    public boolean addEnergyOutput_EM(long EU, long Amperes, boolean allowMixedVoltages) {
        if (EU < 0) {
            EU = -EU;
        }
        if (Amperes < 0) {
            Amperes = -Amperes;
        }
        long euVar = EU * Amperes;
        long diff;
        for (MTEHatchDynamo tHatch : filterValidMTEs(mDynamoHatches)) {
            if (tHatch.maxEUOutput() < euVar && !allowMixedVoltages) {
                explodeMultiblock();
            }
            diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            if (diff > 0) {
                if (euVar <= diff) {
                    tHatch.setEUVar(
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + euVar);
                    return true;
                }
                tHatch.setEUVar(tHatch.maxEUStore());
                euVar -= diff;
            }
        }
        for (MTEHatchDynamoMulti tHatch : filterValidMTEs(eDynamoMulti)) {
            if (tHatch.maxEUOutput() < euVar && !allowMixedVoltages) {
                explodeMultiblock();
            }
            diff = tHatch.maxEUStore() - tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            if (diff > 0) {
                if (euVar <= diff) {
                    tHatch.setEUVar(
                        tHatch.getBaseMetaTileEntity()
                            .getStoredEU() + euVar);
                    return true;
                }
                tHatch.setEUVar(tHatch.maxEUStore());
                euVar -= diff;
            }
        }
        explodeMultiblock(); // If no energy is left, we would've already exploded. Thus no check is needed here
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        ItemStack heldItem = aPlayer.getHeldItem();

        // Check if the player is holding a plunger
        if (heldItem == null || heldItem.getItem() != MetaGeneratedTool01.INSTANCE
            || heldItem.getItemDamage() != IDMetaTool01.PLUNGER.ID) {
            return super.onRightclick(aBaseMetaTileEntity, aPlayer);
        }

        // Setup
        int prevCount = this.moduleCount;
        int maxReduction = (int) Math.min(
            this.moduleCount,
            MetaGeneratedTool01.getToolMaxDamage(heldItem) - MetaGeneratedTool01.getToolDamage(heldItem));
        ItemStack modules = ItemList.DysonSwarmModule.get(maxReduction);

        // Fill player inventory
        aPlayer.inventory.addItemStackToInventory(modules);

        // If the player's inventory is full and some modules are still left and the player is sneaking, spawn them in
        // front of the controller
        if (modules.stackSize > 0 && aPlayer.isSneaking()) {
            aPlayer.worldObj.spawnEntityInWorld(
                new EntityItem(aPlayer.worldObj, aPlayer.posX, aPlayer.posY + 0.5, aPlayer.posZ, modules));

            // Set moduleCount based on the number of ejected modules and damage the plunger
            this.moduleCount = 0;
            MetaGeneratedTool01.INSTANCE.doDamage(heldItem, maxReduction);
        } else {
            this.moduleCount = prevCount - maxReduction + modules.stackSize;
            MetaGeneratedTool01.INSTANCE.doDamage(heldItem, maxReduction - modules.stackSize);
        }

        return true;
    }

    /****************
     * CLIENT STUFF *
     ****************/
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive)
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_RECEIVER),
                    TextureFactory.of(IGTextures.DYSON_OVERLAY_FRONT_ACTIVE), TextureFactory.builder()
                        .addIcon(IGTextures.DYSON_OVERLAY_FRONT_ACTIVE_GLOW)
                        .glow()
                        .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_RECEIVER),
                TextureFactory.of(IGTextures.DYSON_OVERLAY_FRONT), TextureFactory.builder()
                    .addIcon(IGTextures.DYSON_OVERLAY_FRONT_GLOW)
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(IGTextures.CASING_INDEX_RECEIVER) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.ig.dyson.type"));
        if (TooltipUtil.dysonLoreText != null) tt.addInfo(ITALIC + TooltipUtil.dysonLoreText);

        tt.addInfo(translateToLocal("gt.blockmachines.multimachine.ig.dyson.desc1"))
            .addInfo(
                translateToLocalFormatted(
                    "gt.blockmachines.multimachine.ig.dyson.desc2",
                    getDecimalFormat().format(IGConfig.dysonSwarm.euPerModule)))
            .addInfo(
                translateToLocalFormatted(
                    "gt.blockmachines.multimachine.ig.dyson.desc3",
                    getDecimalFormat().format(IGConfig.dysonSwarm.destroyModuleChance),
                    getDecimalFormat().format(IGConfig.dysonSwarm.destroyModuleA),
                    getDecimalFormat().format(IGConfig.dysonSwarm.destroyModuleB)))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.ig.dyson.desc4"))
            .addInfo(
                translateToLocalFormatted(
                    "gt.blockmachines.multimachine.ig.dyson.desc5",
                    getDecimalFormat().format(IGConfig.dysonSwarm.coolantConsumption),
                    IGConfig.dysonSwarm.getCoolantStack()
                        .getLocalizedName()))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.ig.dyson.desc6"))
            .addInfo(translateToLocal("gt.blockmachines.multimachine.ig.dyson.desc7"))
            .addTecTechHatchInfo()
            .beginStructureBlock(16, 20, 16, false)
            .addDynamoHatch(translateToLocal("ig.dyson.structure.dynamo"), 1)
            .addInputBus("1 - 11", 2)
            .addInputHatch("1 - 11", 2)
            .addOtherStructurePart(translateToLocal("ig.dyson.structure.optical"), "1 - 24", 4)
            .addStructureInfo("")
            .addStructureInfo(ITALIC + translateToLocal("ig.dyson.structure.additionally"))
            .addCasingInfoRange(translateToLocal("ig.dyson.structure.receiver.base"), 53, 64, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.receiver.dish"), 81, false)
            .addCasingInfoRange(translateToLocal("ig.dyson.structure.deployment.base"), 62, 72, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.deployment.core"), 1, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.deployment.magnet"), 32, false)
            .addCasingInfoRange(translateToLocal("ig.dyson.structure.control.base"), 115, 138, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.control.primary"), 20, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.control.secondary"), 12, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.control.toroid"), 128, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.base.floor"), 256, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.base.coil"), 9, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.base.hermetic"), 1, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.base.frameTitanium"), 16, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.base.frameHSSS"), 23, false)
            .addCasingInfoExactly(translateToLocal("ig.dyson.structure.base.frameUHVBase"), 64, false)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        info.add("Modules: " + YELLOW + GTUtility.formatNumbers(moduleCount) + RESET);

        info.add("Power Factor: " + (powerFactor < 1.0f ? RED : GREEN)
            + GTUtility.formatNumbers(powerFactor * 100.0)
            + "%"
            + RESET);

        info.add("Theoretical Output: " + YELLOW
            + GTUtility.formatNumbers((long) moduleCount * IGConfig.dysonSwarm.euPerModule * powerFactor)
            + RESET
            + " EU/t");

        info.add("Current Output: " + YELLOW + GTUtility.formatNumbers(euPerTick) + RESET + " EU/t");
    }

    /******************
     * HELPER METHODS *
     ******************/
    public double getPowerFactor() {
        WorldProvider provider = this.getBaseMetaTileEntity()
            .getWorld().provider;

        if (!Mods.GalacticraftCore.isModLoaded()) {
            return 1.0D;
        }

        if (provider instanceof IOrbitDimension orbitDimension) {
            return IGConfig.dysonSwarm.getPowerFactor(orbitDimension.getPlanetToOrbit());
        }

        String className = provider.getClass()
            .getName();
        return switch (className) {
            case "me.eigenraven.personalspace.world.PersonalWorldProvider" -> IGConfig.dysonSwarm.getPowerFactor("PS");
            default -> IGConfig.dysonSwarm.getPowerFactor(String.valueOf(provider.dimensionId));
        };
    }

    private static DecimalFormat getDecimalFormat() {
        return DECIMAL_FORMATTERS.computeIfAbsent(Locale.getDefault(Locale.Category.FORMAT), locale -> {
            DecimalFormat format = new DecimalFormat();
            format.setGroupingUsed(true);
            format.setMaximumFractionDigits(5);
            format.setRoundingMode(RoundingMode.HALF_UP);
            DecimalFormatSymbols dfs = format.getDecimalFormatSymbols();
            dfs.setGroupingSeparator(',');
            format.setDecimalFormatSymbols(dfs);
            return format;
        });
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        moduleCount = aNBT.getInteger("moduleCount");
        euPerTick = aNBT.getLong("euPerTick");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("moduleCount", moduleCount);
        aNBT.setLong("euPerTick", euPerTick);
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}

/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksMap;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofOreDictBlockMap;
import static gregtech.api.util.tooltip.TooltipHelper.anyCasingText;
import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;
import static kubatech.api.utils.ItemUtils.writeItemStackToNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.EnumBeeType;
import forestry.api.apiculture.FlowerManager;
import forestry.api.apiculture.IAlleleBeeSpecies;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeGenome;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.apiculture.blocks.BlockAlveary;
import forestry.apiculture.blocks.BlockApicultureType;
import forestry.apiculture.genetics.Bee;
import forestry.plugins.PluginApiculture;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.ResultMissingApiaryFlowers;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.client.effect.MegaApiaryBeesRenderer;
import kubatech.gui.modularui2.MTEMegaIndustrialApiaryGui;

@IMetaTileEntity.SkipGenerateDescription
public class MTEMegaIndustrialApiary extends KubaTechGTMultiBlockBase<MTEMegaIndustrialApiary>
    implements ISurvivalConstructable, ICasingTextureProvider {

    protected int glassTier = -1;
    protected int mCasing = 0;
    public int mMaxSlots = 0;
    private boolean needsWaterFill = false;

    public int mPrimaryMode = MODE_PRIMARY_INPUT;
    public int mSecondaryMode = MODE_SECONDARY_NORMAL;

    private final LinkedHashMap<ItemId, BeeGroup> beeGroups = new LinkedHashMap<>();
    private int beeCount = 0;
    private int storageVersion = 0;

    public static final int MODE_PRIMARY_INPUT = 0;
    public static final int MODE_PRIMARY_OUTPUT = 1;
    public static final int MODE_PRIMARY_OPERATING = 2;

    protected static final int MODE_SECONDARY_NORMAL = 0;
    protected static final int MODE_SECONDARY_SWARMER = 1;

    private static final int BEE_ACCELERATION = 64;
    private static final int SWARMER_JELLY_COST = 100;
    private static final int NORMAL_PROCESSING_TICKS = 100;
    private static final int SWARMER_PROCESSING_TICKS = 1200;
    private static final int MAX_JELLY_BONUS_PERCENT = 200;
    private static final int JELLY_BONUS_PER_BEE_PERCENT = 5;

    protected static final ItemStack royalJelly = PluginApiculture.items.royalJelly.getItemStack(1);

    // Only used to check and consume inputs; swarmer timing and outputs are handled in checkProcessing().
    private static final GTRecipe SWARMER_JELLY_RECIPE = GTRecipeBuilder.empty()
        .itemInputsUnsafe(GTUtility.copyAmountUnsafe(SWARMER_JELLY_COST, royalJelly))
        .nbtSensitive()
        .build()
        .orElseThrow();

    protected static final int CASING_INDEX = 10;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final String STRUCTURE_PIECE_FLOWERS = "flowers";
    protected static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "mainsurvival";

    protected static final int CONFIGURATION_WINDOW_ID = 999;
    protected static final int MEGA_APIARY_STORAGE_VERSION = 2;

    private static final String[][] struct = transpose(
        new String[][] { // spotless:off
            {"               ","               ","               ","      HHH      ","    HHAAAHH    ","    HAPLPAH    ","   HAPAAAPAH   ","   HALAAALAH   ","   HAPAAAPAH   ","    HAPLPAH    ","    HHAAAHH    ","      HHH      ","               ","               ","               "},
            {"               ","               ","      GGG      ","    GG   GG    ","   G       G   ","   G       G   ","  G         G  ","  G         G  ","  G         G  ","   G       G   ","   G       G   ","    GG   GG    ","      GGG      ","               ","               "},
            {"               ","      HHH      ","   HHH   HHH   ","  HG       GH  ","  H         H  ","  H         H  "," H           H "," H           H "," H           H ","  H         H  ","  H         H  ","  HG       GH  ","   HHH   HHH   ","      HHH      ","               "},
            {"      GGG      ","   GGG   GGG   ","  G         G  "," G           G "," G           G "," G           G ","G             G","G             G","G             G"," G           G "," G           G "," G           G ","  G         G  ","   GGG   GGG   ","      GGG      "},
            {"      AAA      ","   OLA   ALO   ","  P         P  "," O           O "," L           L "," A           A ","A             A","A             A","A             A"," A           A "," L           L "," O           O ","  P         P  ","   OLA   ALO   ","      AAA      "},
            {"     AAAAA     ","   NA     AO   ","  P         P  "," N           O "," A           A ","A             A","A     III     A","A     III     A","A     III     A","A             A"," A           A "," N           N ","  P         P  ","   NA     AN   ","     AAAAA     "},
            {"     AAAAA     ","   NA FFF AO   ","  PFF     FFP  "," NFF       FFO "," AF         FA ","A             A","AF    JJJ    FA","AF    JKJ    FA","AF    JJJ    FA","A             A"," AF         FA "," NFF       FFN ","  PFF     FFP  ","   NA FFF AN   ","     AAAAA     "},
            {"      AAA      ","   OLAFFFALO   ","  PFFFFFFFFFP  "," OFFFF   FFFFO "," LFF       FFL "," AFF FFFFF  FA ","AFF  FKKKFF FFA","AFF FFKKKFF FFA","AFF FFKKKF  FFA"," AF  FFFFF  FA "," LFF   FF  FFL "," OFFFF    FFFO ","  PFFFFFFFFFP  ","   OLAFFFALO   ","      AAA      "},
            {"      G~G      ","   GGGBBBGGG   ","  GBBFFFFFBBG  "," GBBFF   FFBBG "," GBF       FBG "," GFF FFFFF  FG ","GBF  FKKKFF FBG","GBF FFKJKFF FBG","GBF FFKKKF  FBG"," GF  FFFFF  FG "," GBF   FF  FBG "," GBBFF    FBBG ","  GBBFFFFFBBG  ","   GGGBBBGGG   ","      GGG      "},
            {"      HHH      ","    HHBBBHH    ","  HHBBBBBBBHH  ","  HBBBWWWBBBH  "," HBBWWWWWWWBBH "," HBBWBBBBBWWBH ","HBBWWBBBBBBWBBH","HBBWBBBBBBBWBBH","HBBWBBBBBBWWBBH"," HBWWBBBBBWWBH "," HBBWWWBBWWBBH ","  HBBBWWWWBBH  ","  HHBBBBBBBHH  ","    HHBBBHH    ","      HHH      "},
            {"               ","     GGGGG     ","   GGBBBBBGG   ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","   GGBBBBBGG   ","     GGGGG     ","               "},
            {"               ","      HHH      ","    HHBBBHH    ","   HBBBBBBBH   ","  HBBBBBBBBBH  ","  HBBBBBBBBBH  "," HBBBBBBBBBBBH "," HBBBBBBBBBBBH "," HBBBBBBBBBBBH ","  HBBBBBBBBBH  ","  HBBBBBBBBBH  ","   HBBBBBBBH   ","    HHBBBHH    ","      HHH      ","               "},
            {"               ","               ","      GGG      ","    GGBBBGG    ","   GBBBBBBBG   ","   GBBBBBBBG   ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","   GBBBBBBBG   ","   GBBBBBBBG   ","    GGBBBGG    ","      GGG      ","               ","               "},
            {"               ","               ","       H       ","     HHBHH     ","    HBBBBBH    ","   HBBBBBBBH   ","   HBBBBBBBH   ","  HBBBBBBBBBH  ","   HBBBBBBBH   ","   HBBBBBBBH   ","    HBBBBBH    ","     HHBHH     ","       H       ","               ","               "},
            {"               ","               ","               ","       G       ","     GGBGG     ","    GBBBBBG    ","    GBBBBBG    ","   GBBBBBBBG   ","    GBBBBBG    ","    GBBBBBG    ","     GGBGG     ","       G       ","               ","               ","               "},
            {"               ","               ","               ","               ","      HHH      ","     HHHHH     ","    HHBBBHH    ","    HHBBBHH    ","    HHBBBHH    ","     HHHHH     ","      HHH      ","               ","               ","               ","               "},
            {"               ","               ","               ","               ","               ","               ","      GGG      ","      GHG      ","      GGG      ","               ","               ","               ","               ","               ","               "}
        }); // spotless:on

    private static final IStructureDefinition<MTEMegaIndustrialApiary> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaIndustrialApiary>builder()
        .addShape(STRUCTURE_PIECE_MAIN, struct)
        .addShape(
            STRUCTURE_PIECE_MAIN_SURVIVAL,
            Arrays.stream(struct)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replace("F", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        .addShape(
            STRUCTURE_PIECE_FLOWERS,
            Arrays.stream(struct)
                .map(
                    sa -> Arrays.stream(sa)
                        .map(s -> s.replaceAll("[^F]", " "))
                        .toArray(String[]::new))
                .toArray(String[][]::new))
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('B', ofChain(ofBlockAnyMeta(Blocks.dirt, 0), ofBlock(Blocks.grass, 0)))
        .addElement(
            'G',
            buildHatchAdder(MTEMegaIndustrialApiary.class).atLeast(InputBus, OutputBus, Energy, Maintenance)
                .casingIndex(CASING_INDEX)
                .hint(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, ofBlock(GregTechAPI.sBlockCasings1, 10))))
        .addElement('H', ofBlocksMap(ofOreDictBlockMap("plankWood"), Blocks.planks, 0))
        .addElement('I', ofBlocksMap(ofOreDictBlockMap("slabWood"), Blocks.wooden_slab, 0))
        .addElement('J', ofBlock(PluginApiculture.blocks.apiculture, BlockApicultureType.APIARY.getMeta()))
        .addElement('K', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.PLAIN.ordinal()))
        .addElement('L', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.HYGRO.ordinal()))
        .addElement('N', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.STABILIZER.ordinal()))
        .addElement('O', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.HEATER.ordinal()))
        .addElement('P', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.FAN.ordinal()))
        .addElement('W', ofChain(ofAnyWater(false), isAir()))
        .addElement('F', new IStructureElementNoPlacement<>() {

            @Override
            public boolean check(MTEMegaIndustrialApiary mte, World world, int x, int y, int z) {
                mte.flowerCheck(world, x, y, z);
                return true;
            }

            @Override
            public boolean spawnHint(MTEMegaIndustrialApiary mte, World world, int x, int y, int z, ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), 2 - 1);
                return true;
            }
        })
        .build();

    public MTEMegaIndustrialApiary(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaIndustrialApiary(String aName) {
        super(aName);
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        IGregTechTileEntity bmte = getBaseMetaTileEntity();
        for (BeeGroup group : beeGroups.values()) {
            for (int i = 0; i < group.count; i++) {
                EntityItem item = new EntityItem(
                    bmte.getWorld(),
                    bmte.getXCoord(),
                    bmte.getYCoord(),
                    bmte.getZCoord(),
                    group.copyQueen());
                item.delayBeforeCanPickup = 10;
                bmte.getWorld()
                    .spawnEntityInWorld(item);
            }
        }
    }

    public int getBeeCount() {
        return beeCount;
    }

    public Collection<BeeGroup> getBeeGroups() {
        return Collections.unmodifiableCollection(beeGroups.values());
    }

    public int getStorageVersion() {
        return storageVersion;
    }

    /**
     * Stores one bee of the given queen. The stack itself is not modified.
     *
     * @return {@code false} if the stack is not a valid queen
     */
    public boolean addQueen(ItemStack queen) {
        if (beeRoot.getType(queen) != EnumBeeType.QUEEN) return false;
        BeeGroup group = beeGroups.get(ItemId.createNoCopy(queen));
        if (group == null) {
            group = new BeeGroup(queen);
            if (!group.generate(getBaseMetaTileEntity().getWorld(), (float) getVoltageTierExact())) return false;
            beeGroups.put(ItemId.createNoCopy(group.queen), group);
        }
        group.count++;
        beeCount++;
        storageVersion++;
        return true;
    }

    /**
     * Removes one bee from the group.
     *
     * @return a copy of the removed queen, or {@code null} if the group is no longer stored
     */
    public ItemStack removeQueen(BeeGroup group) {
        if (group.count <= 0) return null;
        if (--group.count == 0) beeGroups.remove(ItemId.createNoCopy(group.queen));
        beeCount--;
        storageVersion++;
        return group.copyQueen();
    }

    private void loadGroup(BeeGroup loaded) {
        BeeGroup existing = beeGroups.get(ItemId.createNoCopy(loaded.queen));
        if (existing == null) beeGroups.put(ItemId.createNoCopy(loaded.queen), loaded);
        else existing.count += loaded.count;
        beeCount += loaded.count;
    }

    /**
     * The map used to store the required flowers in the apiary.
     * <p>
     * The instance itself is updated in {@link #onStorageContentChanged(boolean)}.
     *
     * @see #onStorageContentChanged(boolean)
     */
    @NotNull
    protected Map<String, String> flowerRequiredMap = new HashMap<>();

    /**
     * The map used to check the flowers in the apiary.
     * <p>
     * The instance is updated in {@link #checkMachine(IGregTechTileEntity, ItemStack, List)} and entries will be
     * removed
     * during structural check defined in the structure definition, via {@link #flowerCheck(World, int, int, int)}.
     * After {@code checkMachine}, the remaining entries are the missing flowers, which is shown on the GUI as error
     * message.
     *
     * @see #checkRequiredFlowers()
     */
    @NotNull
    protected Map<String, String> flowerCheckingMap = new HashMap<>();

    /**
     * {@code true} if there is any required flower missing.
     *
     * @see #checkRequiredFlowers()
     */
    private boolean missingFlowers = false;

    private boolean needsTVarUpdate = false;
    private int megaApiaryStorageVersion = 0;

    /**
     * Checks the block in the given world and block position, and remove the entries in the {@link #flowerCheckingMap}
     * if it matches any. This function will be called during the structural check, see structure definition also.
     *
     * @see #flowerCheckingMap
     */
    private void flowerCheck(final World world, final int x, final int y, final int z) {
        if (!flowerCheckingMap.isEmpty() && !world.isAirBlock(x, y, z)) {
            flowerCheckingMap.keySet()
                .removeIf(flowerType -> FlowerManager.flowerRegistry.isAcceptedFlower(flowerType, world, x, y, z));
        }
    }

    /**
     * This should be called when the stored bees are changed. And this will trigger the flower check update.
     * <p>
     * The flower check should be ignored when the storage is updated when loading world (or loadNBTData specifically),
     * which the world itself is not ready yet.
     *
     * @param ignoreFlowerCheck {@code true} to ignore the flower check.
     * @see #flowerRequiredMap
     * @see #flowerCheckingMap
     */
    public void onStorageContentChanged(boolean ignoreFlowerCheck) {
        flowerRequiredMap = new HashMap<>();
        for (BeeGroup group : beeGroups.values()) {
            String type = group.getFlowerType();
            if (!type.isEmpty()) {
                flowerRequiredMap.putIfAbsent(type, group.getFlowerTypeDescription());
            }
        }

        if (!ignoreFlowerCheck) {
            checkRequiredFlowers();
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivalBuildPiece(STRUCTURE_PIECE_MAIN_SURVIVAL, stackSize, 7, 8, 0, elementBudget, env, true);
        if (built == -1) {
            GTUtility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done! Now go place the flowers yourself !");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<MTEMegaIndustrialApiary> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        Map<String, Object> ttVars = new HashMap<>();
        ttVars.put("voltageTier_LuV", voltageTooltipFormatted(6));
        ttVars.put("voltageTier_IV", voltageTooltipFormatted(5));
        ttVars.put("bee_acceleration", BEE_ACCELERATION);
        ttVars.put("swarmer_jelly_cost", SWARMER_JELLY_COST);
        ttVars.put("normal_seconds", NORMAL_PROCESSING_TICKS / 20);
        ttVars.put("swarmer_seconds", SWARMER_PROCESSING_TICKS / 20);
        ttVars.put("max_jelly_bonus", MAX_JELLY_BONUS_PERCENT);
        ttVars.put("jelly_bonus", JELLY_BONUS_PER_BEE_PERCENT);
        tt.addMachineType(StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.machine_type"))
            .addMarkdown(new ResourceLocation("gregtech", "mega-apiary"), ttVars)
            .beginStructureBlock(15, 17, 15, true)
            .addEnergyHatchGlassTier()
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_9th_layer"))
            .addCasing(
                "455",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.dirt_or_grass"),
                false)
            .addCasing("190-200", Casings.BronzePlatedBricks.getLocalizedName(), false)
            .addCasing(
                "185",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.wood_plank"),
                false)
            .addCasing("121", StatCollector.translateToLocal("gt.mbtt.structure.any_tiered_glass"), true)
            .addCasing("34", StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.water"), false)
            .addCasing(
                "24",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.alveary_fan"),
                false)
            .addCasing(
                "20",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.alveary_heater"),
                false)
            .addCasing(
                "20",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.alveary_hygroregulator"),
                false)
            .addCasing("18", StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.alveary"), false)
            .addCasing(
                "12",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.alveary_stabiliser"),
                false)
            .addCasing("9", StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.apiary"), false)
            .addCasing("9", StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.wood_slab"), false)
            .addCasing("0+", StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.flower"), false)
            .addEnergyHatch(
                "1+",
                StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.any_casing_luv"),
                1)
            .addMaintenanceHatch("1", anyCasingText(), 1)
            .addInputBus("0+", anyCasingText(), 1)
            .addOutputBus("1+", anyCasingText(), 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("kubatech.multiblock.MegaIndustrialApiary.footer"))
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(GTAuthors.AuthorKuba, "Runakai");
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mPrimaryMode", mPrimaryMode);
        aNBT.setInteger("mSecondaryMode", mSecondaryMode);
        NBTTagList groups = new NBTTagList();
        for (BeeGroup group : beeGroups.values()) groups.appendTag(group.toNBTTagCompound());
        aNBT.setTag("beeGroups", groups);
        aNBT.setInteger("MEGA_APIARY_STORAGE_VERSION", MEGA_APIARY_STORAGE_VERSION);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mPrimaryMode = aNBT.getInteger("mPrimaryMode");
        mSecondaryMode = aNBT.getInteger("mSecondaryMode");
        if (aNBT.hasKey("beeGroups")) {
            NBTTagList groups = aNBT.getTagList("beeGroups", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < groups.tagCount(); i++) loadGroup(BeeGroup.fromNBT(groups.getCompoundTagAt(i)));
        } else {
            // Storage written before bee grouping holds one entry per bee.
            for (int i = 0, isize = aNBT.getInteger("mStorageSize"); i < isize; i++) {
                NBTTagCompound tag = aNBT.getCompoundTag("mStorage." + i);
                BeeGroup existing = beeGroups
                    .get(ItemId.createNoCopy(readItemStackFromNBT(tag.getCompoundTag("queenStack"))));
                if (existing != null) {
                    existing.count++;
                    beeCount++;
                } else {
                    loadGroup(BeeGroup.fromLegacyNBT(tag));
                }
            }
        }
        megaApiaryStorageVersion = aNBT.getInteger("MEGA_APIARY_STORAGE_VERSION");
        onStorageContentChanged(true);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (this.mMaxProgresstime > 0) {
            GTUtility.sendChatToPlayer(aPlayer, "Can't change mode when running !");
            return;
        }
        if (!aPlayer.isSneaking()) {
            mPrimaryMode++;
            if (mPrimaryMode == 3) mPrimaryMode = 0;
            switch (mPrimaryMode) {
                case 0:
                    GTUtility.sendChatToPlayer(aPlayer, "Changed primary mode to: Input mode");
                    break;
                case 1:
                    GTUtility.sendChatToPlayer(aPlayer, "Changed primary mode to: Output mode");
                    break;
                case 2:
                    GTUtility.sendChatToPlayer(aPlayer, "Changed primary mode to: Operating mode");
                    break;
            }
        } else {
            if (beeCount != 0) {
                GTUtility.sendChatToPlayer(aPlayer, "Can't change operating mode when the multi is not empty !");
                return;
            }
            mSecondaryMode++;
            if (mSecondaryMode == 2) mSecondaryMode = 0;
            switch (mSecondaryMode) {
                case 0:
                    GTUtility.sendChatToPlayer(aPlayer, "Changed secondary mode to: Normal mode");
                    break;
                case 1:
                    GTUtility.sendChatToPlayer(aPlayer, "Changed secondary mode to: Swarmer mode");
                    break;
            }
        }
    }

    private void updateMaxSlots() {
        int mOld = mMaxSlots;
        long v = this.getMaxInputEu();
        if (v < GTValues.V[6]) mMaxSlots = 0;
        else if (mSecondaryMode == 0) mMaxSlots = (int) (v / GTValues.V[6]);
        else mMaxSlots = 1;
        if (mOld != 0 && mOld != mMaxSlots) {
            needsTVarUpdate = true;
        }
    }

    @Override
    public boolean needsClientTick() {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (needsWaterFill && aTick % 20 == 0) {
                if (GTStructureUtility
                    .fillStructureWithWater(aBaseMetaTileEntity, getExtendedFacing(), struct, 7, 8, 0, 'W')) {
                    needsWaterFill = false;
                }
            }
            // TODO: Look for proper fix
            if (mUpdate < -550) mUpdate = 50;
        } else {
            if (aBaseMetaTileEntity.isActive() && aTick % 100 == 0) {
                int[] abc = new int[] { 0, -2, 7 };
                int[] xyz = new int[] { 0, 0, 0 };
                this.getExtendedFacing()
                    .getWorldOffset(abc, xyz);
                xyz[0] += aBaseMetaTileEntity.getXCoord();
                xyz[1] += aBaseMetaTileEntity.getYCoord();
                xyz[2] += aBaseMetaTileEntity.getZCoord();
                showBees(aBaseMetaTileEntity.getWorld(), xyz[0], xyz[1], xyz[2], 100);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    private void showBees(World world, int x, int y, int z, int age) {
        MegaApiaryBeesRenderer bee = new MegaApiaryBeesRenderer(world, x, y, z, age);
        Minecraft.getMinecraft().effectRenderer.addEffect(bee);
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {
        updateMaxSlots();
        if (mPrimaryMode < 2) { // input and output mode
            if (mPrimaryMode == MODE_PRIMARY_INPUT && beeCount < mMaxSlots) {
                ArrayList<ItemStack> inputs = getStoredInputs();
                boolean storageChanged = false;
                for (ItemStack input : inputs) {
                    if (addQueen(input)) {
                        input.stackSize--;
                        storageChanged = true;
                    }
                    if (beeCount >= mMaxSlots) break;
                }
                if (storageChanged) {
                    onStorageContentChanged(false);
                }
                updateSlots();
            } else if (mPrimaryMode == MODE_PRIMARY_OUTPUT && beeCount > 0) { // output mode
                if (outputQueens()) {
                    onStorageContentChanged(false);
                }
            } else return CheckRecipeResultRegistry.NO_RECIPE;
            mMaxProgresstime = 10;
            mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            mEfficiencyIncrease = 10000;
            lEUt = 0;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        } else if (mPrimaryMode == MODE_PRIMARY_OPERATING) {
            if (mMaxSlots > 0 && beeCount > 0) {
                if (mSecondaryMode == MODE_SECONDARY_NORMAL) {
                    if (megaApiaryStorageVersion != MEGA_APIARY_STORAGE_VERSION) {
                        megaApiaryStorageVersion = MEGA_APIARY_STORAGE_VERSION;
                        World w = getBaseMetaTileEntity().getWorld();
                        float t = (float) getVoltageTierExact();
                        beeGroups.values()
                            .forEach(g -> g.generate(w, t));
                    }

                    if (beeCount > mMaxSlots) return SimpleCheckRecipeResult.ofFailure("MegaApiary_slotoverflow");

                    if (missingFlowers) {
                        return ResultMissingApiaryFlowers.newFailure(flowerCheckingMap);
                    }

                    if (needsTVarUpdate) {
                        float t = (float) getVoltageTierExact();
                        needsTVarUpdate = false;
                        World w = getBaseMetaTileEntity().getWorld();
                        beeGroups.values()
                            .forEach(g -> g.updateTVar(w, t));
                    }

                    int maxConsume = beeCount * 40;
                    int toConsume = maxConsume;
                    ArrayList<ItemStack> inputs = getStoredInputs();

                    for (ItemStack input : inputs) {
                        if (!input.isItemEqual(royalJelly)) continue;
                        int consumed = Math.min(input.stackSize, toConsume);
                        toConsume -= consumed;
                        input.stackSize -= consumed;
                        if (toConsume == 0) break;
                    }
                    double boosted = 1d;
                    if (toConsume != maxConsume) {
                        boosted += (((double) maxConsume - (double) toConsume) / (double) maxConsume) * 2d;
                        this.updateSlots();
                    }

                    List<ItemStack> stacks = new ArrayList<>();
                    for (BeeGroup group : beeGroups.values()) {
                        stacks.addAll(group.getDrops(this, 64_00d * boosted * group.count));
                    }

                    this.lEUt = -(int) ((double) GTValues.V[6] * (double) mMaxSlots * 0.99d);
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mMaxProgresstime = NORMAL_PROCESSING_TICKS;
                    this.mOutputItems = mergeOutputStacks(stacks);
                } else { // SWARMER mode
                    boolean check_swarmer_jelly = SWARMER_JELLY_RECIPE
                        .isRecipeInputEqual(true, null, getStoredInputs().toArray(new ItemStack[0]));
                    if (!check_swarmer_jelly) {
                        this.updateSlots();
                        return CheckRecipeResultRegistry.NO_RECIPE;
                    }
                    calculateOverclock(GTValues.V[5] - 2L, SWARMER_PROCESSING_TICKS);
                    if (this.lEUt > 0) this.lEUt = -this.lEUt;
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mOutputItems = new ItemStack[] { beeGroups.values()
                        .iterator()
                        .next()
                        .createIgnobleCopy() };
                    this.updateSlots();
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }

        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    /**
     * Ejects stored queens, one per empty output bus slot, or all if ME Output.
     *
     * @return {@code true} if any queen was ejected
     */
    private boolean outputQueens() {
        int capacity = 0;
        for (MTEHatchOutputBus bus : mOutputBusses) {
            if (bus instanceof MTEHatchOutputBusME) {
                capacity = Integer.MAX_VALUE;
                break;
            }
            for (int j = 0; j < bus.getSizeInventory(); j++)
                if (bus.isValidSlot(j) && bus.getStackInSlot(j) == null) capacity++;
        }
        boolean ejected = false;
        Iterator<BeeGroup> it = beeGroups.values()
            .iterator();
        while (capacity > 0 && it.hasNext()) {
            BeeGroup group = it.next();
            int n = Math.min(capacity, group.count);
            for (int i = 0; i < n; i++) addOutputPartial(group.copyQueen());
            capacity -= n;
            group.count -= n;
            beeCount -= n;
            ejected = true;
            if (group.count == 0) it.remove();
        }
        if (ejected) storageVersion++;
        return ejected;
    }

    private static ItemStack[] mergeOutputStacks(List<ItemStack> stacks) {
        HashMap<ItemId, Integer> countMap = new HashMap<>();
        HashMap<ItemId, ItemStack> stackMap = new HashMap<>();
        for (ItemStack stack : stacks) {
            ItemId id = ItemId.createNoCopyWithStackSize(stack);
            countMap.merge(id, stack.stackSize, Integer::sum);
            stackMap.putIfAbsent(id, stack);
        }
        ItemStack[] result = new ItemStack[countMap.size()];
        int i = 0;
        for (Map.Entry<ItemId, Integer> entry : countMap.entrySet()) {
            ItemStack merged = stackMap.get(entry.getKey())
                .copy();
            merged.stackSize = entry.getValue();
            result[i++] = merged;
        }
        return result;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(
            mPrimaryMode == 0 ? "kubatech.infodata.mia.running_mode.input"
                : (mPrimaryMode == 1 ? "kubatech.infodata.mia.running_mode.output"
                    : (mSecondaryMode == 0 ? "kubatech.infodata.mia.running_mode.operating.normal"
                        : "kubatech.infodata.mia.running_mode.operating.swarmer")));
        info.add(
            IGregTechDeviceInformation.encode(
                "kubatech.infodata.mia.running_mode.bee_storage",
                "" + EnumChatFormatting.GOLD + beeCount + EnumChatFormatting.RESET,
                (beeCount > mMaxSlots ? EnumChatFormatting.DARK_RED.toString() : EnumChatFormatting.GOLD.toString())
                    + mMaxSlots
                    + EnumChatFormatting.RESET));
        HashMap<String, Integer> infos = new HashMap<>();
        for (BeeGroup group : beeGroups.values()) {
            StringBuilder builder = new StringBuilder();
            builder.append(EnumChatFormatting.GOLD);
            builder.append(group.queen.getDisplayName());
            // bee flower info
            String flowerType = group.getFlowerType();
            boolean flowerExists = !missingFlowers || flowerCheckingMap.get(flowerType) == null;
            builder.append(" ")
                .append(flowerExists ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                .append("(")
                .append(flowerType)
                .append(")");
            infos.merge(builder.toString(), group.count, Integer::sum);
        }
        infos.forEach((key, value) -> info.add("x" + value + ": " + key));

        if (mMaxSlots > 0 && beeCount >= mMaxSlots) {
            info.add(
                EnumChatFormatting.YELLOW + StatCollector.translateToLocal("kubatech.infodata.mia.inventory_full"));
        }
        if (mPrimaryMode == MODE_PRIMARY_OPERATING && mMaxProgresstime > 0) {
            info.add(
                EnumChatFormatting.RED
                    + StatCollector.translateToLocal("kubatech.infodata.mia.gui_locked_while_running"));
        }

        return info.toArray(new String[0]);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        needsWaterFill = false;
        glassTier = -1;
        mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 8, 0, errors)) {
            needsWaterFill = GTStructureUtility
                .hasWaterAtStructurePosition(aBaseMetaTileEntity, getExtendedFacing(), struct, 7, 8, 0, 'W');
            return;
        }
        for (MTEHatchEnergy hatchEnergy : this.mEnergyHatches) {
            if (this.glassTier < hatchEnergy.getTierForStructure()) {
                errors.add(StructureErrors.glassTierNotEnough(hatchEnergy.mTier));
                break;
            }
        }
        checkCasingMin(errors, this.mCasing, 190);
        for (MTEHatchEnergy hatchEnergy : this.mEnergyHatches) {
            if (hatchEnergy.mTier < VoltageIndex.LuV) {
                errors.add(StructureErrors.energyHatchTierTooLow(VoltageIndex.LuV));
                break;
            }
        }
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasOutputBus(errors);
        if (errors.isEmpty()) {
            updateMaxSlots();
            needsWaterFill = true;
        }
        checkRequiredFlowers();
    }

    /**
     * Runs the flower checking.
     * <p>
     * You should update the {@link #flowerRequiredMap} before invoking this.
     */
    protected void checkRequiredFlowers() {
        flowerCheckingMap = new HashMap<>(flowerRequiredMap);

        // check the flowers in the machine structure
        // the found flower types are removed from the flowerCheckingMap.
        checkPiece(STRUCTURE_PIECE_FLOWERS, 7, 8, 0, new ArrayList<>());

        missingFlowers = !flowerCheckingMap.isEmpty();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaIndustrialApiary(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_DISTILLATION_TOWER,
            OVERLAY_FRONT_DISTILLATION_TOWER_GLOW,
            OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE,
            OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(CASING_INDEX);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMegaIndustrialApiaryGui(this);
    }

    public final HashMap<ItemId, Double> dropProgress = new HashMap<>();

    /**
     * Identical stored bees, which share one queen stack and one set of drops.
     */
    public static class BeeGroup {

        /** Size 1, never modified or handed out, see {@link #copyQueen()}. */
        public final ItemStack queen;
        private int count;
        final List<BeeDrop> drops = new ArrayList<>();
        final List<BeeDrop> specialDrops = new ArrayList<>();
        float beeSpeed;
        String flowerType;
        String flowerTypeDescription;
        public String speciesKey;
        private static IBeekeepingMode mode;

        private BeeGroup(ItemStack queen) {
            this.queen = queen.copy();
            this.queen.stackSize = 1;
        }

        public int getCount() {
            return count;
        }

        public ItemStack copyQueen() {
            return queen.copy();
        }

        boolean generate(World world, float t) {
            if (mode == null) mode = beeRoot.getBeekeepingMode(world);
            drops.clear();
            specialDrops.clear();
            if (beeRoot.getType(this.queen) != EnumBeeType.QUEEN) return false;
            IBeeGenome genome = beeRoot.getMember(this.queen)
                .getGenome();
            this.flowerType = genome.getFlowerProvider()
                .getFlowerType();
            this.flowerTypeDescription = genome.getFlowerProvider()
                .getDescription();
            IAlleleBeeSpecies primary = genome.getPrimary();
            beeSpeed = genome.getSpeed();
            initSpeciesKey(genome);
            genome.getPrimary()
                .getProductChances()
                .forEach((key, value) -> drops.add(new BeeDrop(key, value, beeSpeed, t)));
            genome.getSecondary()
                .getProductChances()
                .forEach((key, value) -> drops.add(new BeeDrop(key, value / 2.f, beeSpeed, t)));
            primary.getSpecialtyChances()
                .forEach((key, value) -> specialDrops.add(new BeeDrop(key, value, beeSpeed, t)));
            return true;
        }

        private void initSpeciesKey(IBeeGenome genome) {
            speciesKey = genome.getPrimary()
                .getUID() + "\0"
                + genome.getSecondary()
                    .getUID()
                + "\0"
                + beeSpeed;
        }

        static BeeGroup fromNBT(NBTTagCompound tag) {
            BeeGroup group = new BeeGroup(readItemStackFromNBT(tag.getCompoundTag("queen")));
            group.count = tag.getInteger("count");
            group.beeSpeed = tag.getFloat("beeSpeed");
            group.flowerType = tag.getString("flowerType");
            group.flowerTypeDescription = tag.getString("flowerTypeDescription");
            NBTTagList drops = tag.getTagList("drops", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < drops.tagCount(); i++) group.drops.add(new BeeDrop(drops.getCompoundTagAt(i)));
            NBTTagList specialDrops = tag.getTagList("specialDrops", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < specialDrops.tagCount(); i++)
                group.specialDrops.add(new BeeDrop(specialDrops.getCompoundTagAt(i)));
            group.initSpeciesKey(
                beeRoot.getMember(group.queen)
                    .getGenome());
            return group;
        }

        /**
         * Reads one bee stored in the format used before bee grouping.
         */
        static BeeGroup fromLegacyNBT(NBTTagCompound tag) {
            BeeGroup group = new BeeGroup(readItemStackFromNBT(tag.getCompoundTag("queenStack")));
            group.count = 1;
            for (int i = 0, isize = tag.getInteger("dropssize"); i < isize; i++)
                group.drops.add(new BeeDrop(tag.getCompoundTag("drops" + i)));
            for (int i = 0, isize = tag.getInteger("specialDropssize"); i < isize; i++)
                group.specialDrops.add(new BeeDrop(tag.getCompoundTag("specialDrops" + i)));
            group.beeSpeed = tag.getFloat("beeSpeed");
            IBeeGenome genome = beeRoot.getMember(group.queen)
                .getGenome();
            group.initSpeciesKey(genome);
            if (tag.hasKey("flowerType") && tag.hasKey("flowerTypeDescription")) {
                group.flowerType = tag.getString("flowerType");
                group.flowerTypeDescription = tag.getString("flowerTypeDescription");
            } else {
                group.flowerType = genome.getFlowerProvider()
                    .getFlowerType();
                group.flowerTypeDescription = genome.getFlowerProvider()
                    .getDescription();
            }
            return group;
        }

        NBTTagCompound toNBTTagCompound() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("queen", writeItemStackToNBT(queen));
            tag.setInteger("count", count);
            tag.setFloat("beeSpeed", beeSpeed);
            tag.setString("flowerType", flowerType);
            tag.setString("flowerTypeDescription", flowerTypeDescription);
            NBTTagList dropList = new NBTTagList();
            for (BeeDrop drop : drops) dropList.appendTag(drop.toNBTTagCompound());
            tag.setTag("drops", dropList);
            NBTTagList specialDropList = new NBTTagList();
            for (BeeDrop drop : specialDrops) specialDropList.appendTag(drop.toNBTTagCompound());
            tag.setTag("specialDrops", specialDropList);
            return tag;
        }

        static final Map<ItemId, ItemStack> dropstacks = new HashMap<>();

        public List<ItemStack> getDrops(final MTEMegaIndustrialApiary MTE, final double timePassed) {
            drops.forEach(d -> {
                MTE.dropProgress.merge(d.id, d.getAmount(timePassed / 550d), Double::sum);
                if (!dropstacks.containsKey(d.id)) dropstacks.put(d.id, d.stack);
            });
            specialDrops.forEach(d -> {
                MTE.dropProgress.merge(d.id, d.getAmount(timePassed / 550d), Double::sum);
                if (!dropstacks.containsKey(d.id)) dropstacks.put(d.id, d.stack);
            });
            List<ItemStack> currentDrops = new ArrayList<>();
            MTE.dropProgress.entrySet()
                .forEach(e -> {
                    double v = e.getValue();
                    while (v > 1.f) {
                        int size = Math.min((int) v, 64);
                        ItemStack stack = dropstacks.get(e.getKey())
                            .copy();
                        stack.stackSize = size;
                        currentDrops.add(stack);
                        v -= size;
                        e.setValue(v);
                    }
                });
            return currentDrops;
        }

        public ItemStack createIgnobleCopy() {
            IBee princess = beeRoot.getMember(queen);
            princess.setIsNatural(false);
            return beeRoot.getMemberStack(princess, EnumBeeType.PRINCESS.ordinal());
        }

        void updateTVar(World world, float t) {
            if (mode == null) mode = beeRoot.getBeekeepingMode(world);
            drops.forEach(d -> d.updateTVar(t));
            specialDrops.forEach(d -> d.updateTVar(t));
        }

        public String getFlowerType() {
            return flowerType;
        }

        public String getFlowerTypeDescription() {
            return flowerTypeDescription;
        }

        private static class BeeDrop {

            private static final float MAX_PRODUCTION_MODIFIER_FROM_UPGRADES = 17.19926784f; // 4*1.2^8
            final ItemStack stack;
            double amount;
            final ItemId id;

            final float chance;
            final float beeSpeed;
            float t;

            public BeeDrop(ItemStack stack, float chance, float beeSpeed, float t) {
                this.stack = stack;
                this.chance = chance;
                this.beeSpeed = beeSpeed;
                this.t = t;
                id = ItemId.createNoCopy(this.stack);
                evaluate();
            }

            public void updateTVar(float t) {
                if (this.t != t) {
                    this.t = t;
                    evaluate();
                }
            }

            public void evaluate() {
                this.amount = Bee.getFinalChance(
                    chance,
                    beeSpeed,
                    MAX_PRODUCTION_MODIFIER_FROM_UPGRADES + mode.getBeeModifier()
                        .getProductionModifier(null, MAX_PRODUCTION_MODIFIER_FROM_UPGRADES),
                    t);
            }

            public double getAmount(double speedModifier) {
                return amount * speedModifier;
            }

            public ItemStack get(int amount) {
                ItemStack r = stack.copy();
                r.stackSize = amount;
                return r;
            }

            public BeeDrop(NBTTagCompound tag) {
                stack = readItemStackFromNBT(tag.getCompoundTag("stack"));
                chance = tag.getFloat("chance");
                beeSpeed = tag.getFloat("beeSpeed");
                t = tag.getFloat("t");
                amount = tag.getDouble("amount");
                id = ItemId.createNoCopy(stack);
            }

            public NBTTagCompound toNBTTagCompound() {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("stack", writeItemStackToNBT(stack));
                tag.setFloat("chance", chance);
                tag.setFloat("beeSpeed", beeSpeed);
                tag.setFloat("t", t);
                tag.setDouble("amount", amount);
                return tag;
            }

            @Override
            public int hashCode() {
                return id.hashCode();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MEGA_INDUSTRIAL_APIARY_LOOP;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }
}

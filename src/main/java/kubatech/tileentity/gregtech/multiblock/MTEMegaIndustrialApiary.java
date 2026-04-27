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
import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;
import static kubatech.api.utils.ItemUtils.writeItemStackToNBT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
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
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IBeekeepingMode;
import forestry.apiculture.blocks.BlockAlveary;
import forestry.apiculture.blocks.BlockApicultureType;
import forestry.apiculture.genetics.Bee;
import forestry.plugins.PluginApiculture;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.ResultMissingApiaryFlowers;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.client.effect.MegaApiaryBeesRenderer;
import kubatech.gui.modularui2.MTEMegaIndustrialApiaryGui;

public class MTEMegaIndustrialApiary extends KubaTechGTMultiBlockBase<MTEMegaIndustrialApiary>
    implements ISurvivalConstructable {

    protected int glassTier = -1;
    protected int mCasing = 0;
    public int mMaxSlots = 0;

    public int mPrimaryMode = MODE_PRIMARY_INPUT;
    public int mSecondaryMode = MODE_SECONDARY_NORMAL;

    public final ArrayList<BeeSimulator> mStorage = new ArrayList<>();

    protected static final int MODE_PRIMARY_INPUT = 0;
    protected static final int MODE_PRIMARY_OUTPUT = 1;
    protected static final int MODE_PRIMARY_OPERATING = 2;

    protected static final int MODE_SECONDARY_NORMAL = 0;
    protected static final int MODE_SECONDARY_SWARMER = 1;

    protected static final ItemStack royalJelly = PluginApiculture.items.royalJelly.getItemStack(1);

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
                        .map(
                            s -> s.replaceAll("W", " ")
                                .replaceAll("F", " "))
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
        .addElement(
            'H',
            ofBlocksMap(
                Collections.singletonMap(
                    Blocks.planks,
                    IntStream.rangeClosed(0, 5)
                        .boxed()
                        .collect(Collectors.toList())),
                Blocks.planks,
                5))
        .addElement(
            'I',
            ofBlocksMap(
                Collections.singletonMap(
                    Blocks.wooden_slab,
                    IntStream.rangeClosed(0, 5)
                        .boxed()
                        .collect(Collectors.toList())),
                Blocks.wooden_slab,
                5))
        .addElement('J', ofBlock(PluginApiculture.blocks.apiculture, BlockApicultureType.APIARY.getMeta()))
        .addElement('K', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.PLAIN.ordinal()))
        .addElement('L', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.HYGRO.ordinal()))
        .addElement('N', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.STABILIZER.ordinal()))
        .addElement('O', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.HEATER.ordinal()))
        .addElement('P', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.FAN.ordinal()))
        .addElement('W', ofAnyWater())
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
        if (getBaseMetaTileEntity().isServerSide())
            tryOutputAll(mStorage, s -> Collections.singletonList(s.queenStack));
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
     * The instance is updated in {@link #checkMachine(IGregTechTileEntity, ItemStack)} and entries will be removed
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
     * This should be called when {@link #mStorage} is changed. And this will trigger the flower check update.
     * <p>
     * The flower check should be ignored when the storage is updated when loading world (or loadNBTData specifically),
     * which the world itself is not ready yet.
     *
     * @param ignoreFlowerCheck {@code true} to ignore the flower check.
     * @see #flowerRequiredMap
     * @see #flowerCheckingMap
     */
    public void onStorageContentChanged(boolean ignoreFlowerCheck) {
        flowerRequiredMap = mStorage.stream()
            .collect(
                Collectors.toMap(BeeSimulator::getFlowerType, BeeSimulator::getFlowerTypeDescription, (k1, k2) -> k1));
        flowerRequiredMap.remove("");

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
                EnumChatFormatting.GREEN + "Auto placing done ! Now go place the water and flowers yourself !");
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
        tt.addMachineType("Mega Apiary, Mapiary")
            .addInfo("The ideal home for your bees")
            .addInfo("Use screwdriver to change primary mode (INPUT/OUTPUT/OPERATING)")
            .addInfo("Use screwdriver + shift to change operation mode (NORMAL/SWARMER)")
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + "Input Mode:")
            .addInfo("- Does not take power")
            .addInfo("- Put your queens in the input bus to put them in the internal buffer")
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + "Output Mode:")
            .addInfo("- Does not take power")
            .addInfo("- Will give your bees back to output bus")
            .addSeparator()
            .addInfo(EnumChatFormatting.GOLD + "Operating Mode:")
            .addInfo("- NORMAL:")
            .addInfo("  - Processing time: 5 seconds")
            .addInfo("  - Uses 1 " + voltageTooltipFormatted(6) + " amp per queen")
            .addInfo("  - All bees are accelerated 64 times")
            .addInfo("  - 8 production upgrades are applied")
            .addInfo("  - Genetic Stabilizer upgrade applied")
            .addInfo("  - Simulates perfect environment for your bees")
            .addInfo("  - Additionally you can provide royal jelly to increase the outputs:")
            .addInfo("    - 1 royal jelly grants 5% bonus per bee")
            .addInfo("    - They will be consumed on each start of operation")
            .addInfo("    - and be applied to that operation only")
            .addInfo("    - Max bonus: 200%")
            .addInfo("- SWARMER:")
            .addInfo("  - You can only insert 1 queen")
            .addInfo("  - It will slowly produce ignoble princesses")
            .addInfo("  - Consumes 100 royal jelly per operation")
            .addInfo("  - Base processing time: 1 minute")
            .addInfo("  - Uses 1 amp " + voltageTooltipFormatted(5))
            .addInfo("  - Can overclock")
            .beginStructureBlock(15, 17, 15, false)
            .addController("Front center")
            .addCasingInfoMin("Bronze Plated Bricks", 190, false)
            .addCasingInfoExactly("Any Tiered Glass", 121, false)
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addStructureInfo("Regular water and IC2 Distilled Water are accepted")
            .addOtherStructurePart(
                StatCollector.translateToLocal("kubatech.tooltip.structure.flowers"),
                "On dirt/grass",
                2)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .toolTipFinisher(GTAuthors.AuthorKuba, "Runakai");
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mPrimaryMode", mPrimaryMode);
        aNBT.setInteger("mSecondaryMode", mSecondaryMode);
        aNBT.setInteger("mStorageSize", mStorage.size());
        for (int i = 0; i < mStorage.size(); i++) aNBT.setTag(
            "mStorage." + i,
            mStorage.get(i)
                .toNBTTagCompound());
        aNBT.setInteger("MEGA_APIARY_STORAGE_VERSION", MEGA_APIARY_STORAGE_VERSION);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mPrimaryMode = aNBT.getInteger("mPrimaryMode");
        mSecondaryMode = aNBT.getInteger("mSecondaryMode");
        for (int i = 0, isize = aNBT.getInteger("mStorageSize"); i < isize; i++)
            mStorage.add(new BeeSimulator(aNBT.getCompoundTag("mStorage." + i)));
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
            if (!mStorage.isEmpty()) {
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
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
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
            if (mPrimaryMode == MODE_PRIMARY_INPUT && mStorage.size() < mMaxSlots) {
                World w = getBaseMetaTileEntity().getWorld();
                float t = (float) getVoltageTierExact();
                ArrayList<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs) {
                    if (beeRoot.getType(input) == EnumBeeType.QUEEN) {
                        BeeSimulator bs = new BeeSimulator(input, w, t);
                        if (bs.isValid) {
                            mStorage.add(bs);
                            onStorageContentChanged(false);
                        }
                    }
                    if (mStorage.size() >= mMaxSlots) break;
                }
                updateSlots();
            } else if (mPrimaryMode == MODE_PRIMARY_OUTPUT && !mStorage.isEmpty()) { // output mode
                if (tryOutputAll(mStorage, s -> Collections.singletonList(s.queenStack))) {
                    onStorageContentChanged(false);
                }
            } else return CheckRecipeResultRegistry.NO_RECIPE;
            mMaxProgresstime = 10;
            mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            mEfficiencyIncrease = 10000;
            lEUt = 0;
            return CheckRecipeResultRegistry.SUCCESSFUL;
        } else if (mPrimaryMode == MODE_PRIMARY_OPERATING) {
            if (mMaxSlots > 0 && !mStorage.isEmpty()) {
                if (mSecondaryMode == MODE_SECONDARY_NORMAL) {
                    if (megaApiaryStorageVersion != MEGA_APIARY_STORAGE_VERSION) {
                        megaApiaryStorageVersion = MEGA_APIARY_STORAGE_VERSION;
                        World w = getBaseMetaTileEntity().getWorld();
                        float t = (float) getVoltageTierExact();
                        mStorage.forEach(s -> s.generate(w, t));
                    }

                    if (mStorage.size() > mMaxSlots)
                        return SimpleCheckRecipeResult.ofFailure("MegaApiary_slotoverflow");

                    if (missingFlowers) {
                        return ResultMissingApiaryFlowers.newFailure(flowerCheckingMap);
                    }

                    if (needsTVarUpdate) {
                        float t = (float) getVoltageTierExact();
                        needsTVarUpdate = false;
                        World w = getBaseMetaTileEntity().getWorld();
                        mStorage.forEach(s -> s.updateTVar(w, t));
                    }

                    int maxConsume = Math.min(mStorage.size(), mMaxSlots) * 40;
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
                    for (int i = 0, mStorageSize = Math.min(mStorage.size(), mMaxSlots); i < mStorageSize; i++) {
                        BeeSimulator beeSimulator = mStorage.get(i);
                        stacks.addAll(beeSimulator.getDrops(this, 64_00d * boosted));
                    }

                    this.lEUt = -(int) ((double) GTValues.V[6] * (double) mMaxSlots * 0.99d);
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mMaxProgresstime = 100;
                    this.mOutputItems = stacks.toArray(new ItemStack[0]);
                } else { // SWARMER mode
                    if (!depleteInput(PluginApiculture.items.royalJelly.getItemStack(64))
                        || !depleteInput(PluginApiculture.items.royalJelly.getItemStack(36))) {
                        this.updateSlots();
                        return CheckRecipeResultRegistry.NO_RECIPE;
                    }
                    calculateOverclock(GTValues.V[5] - 2L, 1200);
                    if (this.lEUt > 0) this.lEUt = -this.lEUt;
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mOutputItems = new ItemStack[] { this.mStorage.get(0)
                        .createIgnobleCopy() };
                    this.updateSlots();
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }

        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(
            StatCollector.translateToLocal("kubatech.infodata.running_mode") + " "
                + EnumChatFormatting.GOLD
                + (mPrimaryMode == 0 ? StatCollector.translateToLocal("kubatech.infodata.mia.running_mode.input")
                    : (mPrimaryMode == 1 ? StatCollector.translateToLocal("kubatech.infodata.mia.running_mode.output")
                        : (mSecondaryMode == 0
                            ? StatCollector.translateToLocal("kubatech.infodata.mia.running_mode.operating.normal")
                            : StatCollector
                                .translateToLocal("kubatech.infodata.mia.running_mode.operating.swarmer")))));
        info.add(
            StatCollector.translateToLocalFormatted(
                "kubatech.infodata.mia.running_mode.bee_storage",
                "" + EnumChatFormatting.GOLD + mStorage.size() + EnumChatFormatting.RESET,
                (mStorage.size() > mMaxSlots ? EnumChatFormatting.DARK_RED.toString()
                    : EnumChatFormatting.GOLD.toString()) + mMaxSlots + EnumChatFormatting.RESET));
        HashMap<String, Integer> infos = new HashMap<>();
        for (int i = 0; i < mStorage.size(); i++) {
            StringBuilder builder = new StringBuilder();
            if (i > mMaxSlots) builder.append(EnumChatFormatting.DARK_RED);
            builder.append(EnumChatFormatting.GOLD);
            BeeSimulator beeSimulator = mStorage.get(i);
            builder.append(beeSimulator.queenStack.getDisplayName());
            // bee flower info
            String flowerType = beeSimulator.getFlowerType();
            boolean flowerExists = !missingFlowers || flowerCheckingMap.get(flowerType) == null;
            builder.append(" ")
                .append(flowerExists ? EnumChatFormatting.GREEN : EnumChatFormatting.RED)
                .append("(")
                .append(flowerType)
                .append(")");
            infos.merge(builder.toString(), 1, Integer::sum);
        }
        infos.forEach((key, value) -> info.add("x" + value + ": " + key));

        return info.toArray(new String[0]);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        glassTier = -1;
        mCasing = 0;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 8, 0)) return false;
        if (this.glassTier < VoltageIndex.UEV && !this.mEnergyHatches.isEmpty())
            for (MTEHatchEnergy hatchEnergy : this.mEnergyHatches) if (this.glassTier < hatchEnergy.mTier) return false;
        boolean valid = this.mMaintenanceHatches.size() == 1 && !this.mEnergyHatches.isEmpty() && this.mCasing >= 190;
        if (valid) updateMaxSlots();
        checkRequiredFlowers();
        return valid;
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
        checkPiece(STRUCTURE_PIECE_FLOWERS, 7, 8, 0);

        missingFlowers = !flowerCheckingMap.isEmpty();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaIndustrialApiary(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX), TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX) };
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEMegaIndustrialApiaryGui(this);
    }

    public final HashMap<ItemId, Double> dropProgress = new HashMap<>();

    public static class BeeSimulator {

        public final ItemStack queenStack;
        public boolean isValid;
        List<BeeDrop> drops = new ArrayList<>();
        List<BeeDrop> specialDrops = new ArrayList<>();
        float beeSpeed;

        float maxBeeCycles;
        String flowerType;
        String flowerTypeDescription;
        private static IBeekeepingMode mode;

        public BeeSimulator(ItemStack queenStack, World world, float t) {
            isValid = false;
            this.queenStack = queenStack.copy();
            this.queenStack.stackSize = 1;
            generate(world, t);
            isValid = true;
            queenStack.stackSize--;
        }

        public void generate(World world, float t) {
            if (mode == null) mode = beeRoot.getBeekeepingMode(world);
            drops.clear();
            specialDrops.clear();
            if (beeRoot.getType(this.queenStack) != EnumBeeType.QUEEN) return;
            IBee queen = beeRoot.getMember(this.queenStack);
            IBeeModifier beeModifier = mode.getBeeModifier();
            float mod = beeModifier.getLifespanModifier(null, null, 1.f);
            int h = queen.getMaxHealth();
            maxBeeCycles = (float) h / (1.f / mod);
            IBeeGenome genome = queen.getGenome();
            this.flowerType = genome.getFlowerProvider()
                .getFlowerType();
            this.flowerTypeDescription = genome.getFlowerProvider()
                .getDescription();
            IAlleleBeeSpecies primary = genome.getPrimary();
            beeSpeed = genome.getSpeed();
            genome.getPrimary()
                .getProductChances()
                .forEach((key, value) -> drops.add(new BeeDrop(key, value, beeSpeed, t)));
            genome.getSecondary()
                .getProductChances()
                .forEach((key, value) -> drops.add(new BeeDrop(key, value / 2.f, beeSpeed, t)));
            primary.getSpecialtyChances()
                .forEach((key, value) -> specialDrops.add(new BeeDrop(key, value, beeSpeed, t)));
        }

        public BeeSimulator(NBTTagCompound tag) {
            queenStack = readItemStackFromNBT(tag.getCompoundTag("queenStack"));
            isValid = tag.getBoolean("isValid");
            drops = new ArrayList<>();
            specialDrops = new ArrayList<>();
            for (int i = 0, isize = tag.getInteger("dropssize"); i < isize; i++)
                drops.add(new BeeDrop(tag.getCompoundTag("drops" + i)));
            for (int i = 0, isize = tag.getInteger("specialDropssize"); i < isize; i++)
                specialDrops.add(new BeeDrop(tag.getCompoundTag("specialDrops" + i)));
            beeSpeed = tag.getFloat("beeSpeed");
            maxBeeCycles = tag.getFloat("maxBeeCycles");
            if (tag.hasKey("flowerType") && tag.hasKey("flowerTypeDescription")) {
                flowerType = tag.getString("flowerType");
                flowerTypeDescription = tag.getString("flowerTypeDescription");
            } else {
                IBee queen = beeRoot.getMember(this.queenStack);
                IBeeGenome genome = queen.getGenome();
                this.flowerType = genome.getFlowerProvider()
                    .getFlowerType();
                this.flowerTypeDescription = genome.getFlowerProvider()
                    .getDescription();
            }
        }

        public NBTTagCompound toNBTTagCompound() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("queenStack", writeItemStackToNBT(queenStack));
            tag.setBoolean("isValid", isValid);
            tag.setInteger("dropssize", drops.size());
            for (int i = 0; i < drops.size(); i++) tag.setTag(
                "drops" + i,
                drops.get(i)
                    .toNBTTagCompound());
            tag.setInteger("specialDropssize", specialDrops.size());
            for (int i = 0; i < specialDrops.size(); i++) tag.setTag(
                "specialDrops" + i,
                specialDrops.get(i)
                    .toNBTTagCompound());
            tag.setFloat("beeSpeed", beeSpeed);
            tag.setFloat("maxBeeCycles", maxBeeCycles);
            tag.setString("flowerType", flowerType);
            tag.setString("flowerTypeDescription", flowerTypeDescription);
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
            IBee princess = beeRoot.getMember(queenStack);
            princess.setIsNatural(false);
            return beeRoot.getMemberStack(princess, EnumBeeType.PRINCESS.ordinal());
        }

        public void updateTVar(World world, float t) {
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

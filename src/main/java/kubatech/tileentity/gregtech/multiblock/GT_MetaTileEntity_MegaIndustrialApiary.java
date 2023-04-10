/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
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

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static kubatech.api.Variables.*;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.client.effect.MegaApiaryBeesRenderer;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElementNoPlacement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularUIContext;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetParent;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.builder.UIInfo;
import com.gtnewhorizons.modularui.common.internal.wrapper.ModularUIContainer;
import com.gtnewhorizons.modularui.common.widget.*;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.*;
import forestry.apiculture.blocks.BlockAlveary;
import forestry.apiculture.blocks.BlockApicultureType;
import forestry.apiculture.genetics.Bee;
import forestry.plugins.PluginApiculture;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_MegaIndustrialApiary
    extends KubaTechGTMultiBlockBase<GT_MetaTileEntity_MegaIndustrialApiary> implements ISurvivalConstructable {

    private byte mGlassTier = 0;
    private int mCasing = 0;
    private int mMaxSlots = 0;
    private int mPrimaryMode = 0;
    private int mSecondaryMode = 0;
    private final ArrayList<BeeSimulator> mStorage = new ArrayList<>();

    private static final ItemStack royalJelly = PluginApiculture.items.royalJelly.getItemStack(1);
    private static final int CASING_INDEX = 10;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_MAIN_SURVIVAL = "mainsurvival";
    private static final int CONFIGURATION_WINDOW_ID = 999;
    private static final int MEGA_APIARY_STORAGE_VERSION = 2;

    private static final String[][] struct = transpose(
        new String[][] { // spotless:off
        {"               ","               ","               ","      HHH      ","    HHAAAHH    ","    HAPLPAH    ","   HAPAAAPAH   ","   HALAAALAH   ","   HAPAAAPAH   ","    HAPLPAH    ","    HHAAAHH    ","      HHH      ","               ","               ","               "},
        {"               ","               ","      GGG      ","   GGG   GG    ","   G       G   ","   G       G   ","  G         G  ","  G         G  ","  G         G  ","   G       G   ","   G       G   ","    GG   GG    ","      GGG      ","               ","               "},
        {"               ","      HHH      ","   HHH   HHH   ","  H        GH  ","  H         H  ","  H         H  "," H           H "," H           H "," H           H ","  H         H  ","  H         H  ","  HG       GH  ","   HHH   HHH   ","      HHH      ","               "},
        {"      GGG      ","   GGG   GGG   ","  G         G  "," G           G "," G           G "," G           G ","G             G","G             G","G             G"," G           G "," G           G "," G           G ","  G         G  ","   GGG   GGG   ","      GGG      "},
        {"      AAA      ","   OLA   ALO   ","  P         P  "," O           O "," L           L "," A           A ","A             A","A             A","A             A"," A           A "," L           L "," O           O ","  P         P  ","   OLA   ALO   ","      AAA      "},
        {"     AAAAA     ","   NA     AO   ","  P         P  "," N           O "," A           A ","A             A","A     III     A","A     III     A","A     III     A","A             A"," A           A "," N           N ","  P         P  ","   NA     AN   ","     AAAAA     "},
        {"     AAAAA     ","   NA FFF AO   ","  PFF     FFP  "," NF        FFO "," AF         FA ","A             A","AF    JJJ    FA","AF    JKJ    FA","AF    JJJ    FA","A             A"," AF         FA "," NFF       FFN ","  PFF     FFP  ","   NA FFF AN   ","     AAAAA     "},
        {"      AAA      ","   OLAFFFALO   ","  PFFFFFFFFFP  "," OFFFF   FFFFO "," LFF       FFL "," AFF FFFFF  FA ","AFF  FKKKFF FFA","AFF FFKKKFF FFA","AFF FFKKKF  FFA"," AF  FFFFF  FA "," LFF   FF  FFL "," OFFFF    FFFO ","  PFFFFFFFFFP  ","   OLAFFFALO   ","      AAA      "},
        {"      G~G      ","   GGGBBBGGG   ","  GBBFFFFFBBG  "," GBFFF   FFBBG "," GBF       FBG "," GFF FFFFF  FG ","GBF  FKKKFF FBG","GBF FFKJKFF FBG","GBF FFKKKF  FBG"," GF  FFFFF  FG "," GBF   FF  FBG "," GBBFF    FBBG ","  GBBFFFFFBBG  ","   GGGBBBGGG   ","      GGG      "},
        {"      HHH      ","    HHBBBHH    ","  HHBBBBBBBHH  ","  HBBBWWWBBBH  "," HBBWWWWWWWBBH "," HBBWBBBBBWWBH ","HBBWWBBBBBBWBBH","HBBWBBBBBBBWBBH","HBBWBBBBBBWWBBH"," HBWWBBBBBWWBH "," HBBWWWBBWWBBH ","  HBBBWWWWBBH  ","  HHBBBBBBBHH  ","    HHBBBHH    ","      HHH      "},
        {"               ","     GGGGG     ","   GGGBBBBGG   ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","   GGBBBBBGG   ","     GGGGG     ","               "},
        {"               ","      HHH      ","    HHBBBHH    ","   HBBBBBBBH   ","  HBBBBBBBBBH  ","  HBBBBBBBBBH  "," HBBBBBBBBBBBH "," HBBBBBBBBBBBH "," HBBBBBBBBBBBH ","  HBBBBBBBBBH  ","  HBBBBBBBBBH  ","   HBBBBBBBH   ","    HHBBBHH    ","      HHH      ","               "},
        {"               ","               ","      GGG      ","    GGBBBGG    ","   GBBBBBBBG   ","   GBBBBBBBG   ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","   GBBBBBBBG   ","   GBBBBBBBG   ","    GGBBBGG    ","      GGG      ","               ","               "},
        {"               ","               ","       H       ","     HHBHH     ","    HBBBBBH    ","   HBBBBBBBH   ","   HBBBBBBBH   ","  HBBBBBBBBBH  ","   HBBBBBBBH   ","   HBBBBBBBH   ","    HBBBBBH    ","     HHBHH     ","       H       ","               ","               "},
        {"               ","               ","               ","       G       ","     GGBGG     ","    GBBBBBG    ","    GBBBBBG    ","   GBBBBBBBG   ","    GBBBBBG    ","    GBBBBBG    ","     GGBGG     ","       G       ","               ","               ","               "},
        {"               ","               ","               ","               ","      HHH      ","     HHHHH     ","    HHBBBHH    ","    HHBBBHH    ","    HHBBBHH    ","     HHBHH     ","      HHH      ","               ","               ","               ","               "},
        {"               ","               ","               ","               ","               ","               ","      GGG      ","      GHG      ","      GGG      ","               ","               ","               ","               ","               ","               "}
    }); // spotless:on

    private static final IStructureDefinition<GT_MetaTileEntity_MegaIndustrialApiary> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MegaIndustrialApiary>builder()
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
        .addElement(
            'A',
            LoaderReference.Bartworks
                ? BorosilicateGlass.ofBoroGlass((byte) 0, (t, v) -> t.mGlassTier = v, t -> t.mGlassTier)
                : onElementPass(t -> t.mGlassTier = 100, ofBlock(Blocks.glass, 0)))
        .addElement('B', ofChain(ofBlockAnyMeta(Blocks.dirt, 0), ofBlock(Blocks.grass, 0)))
        .addElement(
            'G',
            buildHatchAdder(GT_MetaTileEntity_MegaIndustrialApiary.class)
                .atLeast(InputBus, OutputBus, Energy, Maintenance)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings1, 10))))
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
        .addElement('W', ofBlock(Blocks.water, 0))
        .addElement('F', new IStructureElementNoPlacement<GT_MetaTileEntity_MegaIndustrialApiary>() {

            @Override
            public boolean check(GT_MetaTileEntity_MegaIndustrialApiary mte, World world, int x, int y, int z) {
                mte.flowerCheck(world, x, y, z);
                return true;
            }

            @Override
            public boolean spawnHint(GT_MetaTileEntity_MegaIndustrialApiary mte, World world, int x, int y, int z,
                ItemStack trigger) {
                StructureLibAPI.hintParticle(world, x, y, z, StructureLibAPI.getBlockHint(), 2 - 1);
                return true;
            }
        })
        .build();

    @SuppressWarnings("unused")
    public GT_MetaTileEntity_MegaIndustrialApiary(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MegaIndustrialApiary(String aName) {
        super(aName);
    }

    private boolean isCacheDirty = true;
    private final HashSet<String> flowersCache = new HashSet<>();
    private final HashSet<String> flowersCheck = new HashSet<>();
    private boolean flowersError = false;
    private boolean needsTVarUpdate = false;
    private int megaApiaryStorageVersion = 0;

    private void flowerCheck(final World world, final int x, final int y, final int z) {
        if (!flowersCheck.isEmpty() && !world.isAirBlock(x, y, z))
            flowersCheck.removeIf(s -> FlowerManager.flowerRegistry.isAcceptedFlower(s, world, x, y, z));
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN_SURVIVAL, stackSize, 7, 8, 0, elementBudget, env, true);
        if (built == -1) {
            GT_Utility.sendChatToPlayer(
                env.getActor(),
                EnumChatFormatting.GREEN + "Auto placing done ! Now go place the water and flowers yourself !");
            return 0;
        }
        return built;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MegaIndustrialApiary> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated();
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Apiary")
            .addInfo("Controller block for Industrial Apicultural Acclimatiser and Drone Domestication Station")
            .addInfo(buildAuthorList("kuba6000", "Runakai"))
            .addInfo("The ideal home for your bees")
            .addInfo("AKA. Mega Apiary")
            .addInfo("Use screwdriver to change primary mode (INPUT/OUTPUT/OPERATING)")
            .addInfo("Use screwdriver + shift to change operation mode (NORMAL/SWARMER)")
            .addInfo("--------------------- INPUT MODE ---------------------")
            .addInfo("- Does not take power")
            .addInfo("- Put your queens in the input bus to put them in the internal buffer")
            .addInfo("-------------------- OUTPUT MODE ---------------------")
            .addInfo("- Does not take power")
            .addInfo("- Will give your bees back to output bus")
            .addInfo("------------------- OPERATING MODE -------------------")
            .addInfo("- NORMAL:")
            .addInfo("  - For each " + voltageTooltipFormatted(6) + " amp you can insert 1 bee")
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
            .addInfo(StructureHologram)
            .addSeparator()
            .beginStructureBlock(15, 17, 15, false)
            .addController("Front Bottom Center")
            .addCasingInfoMin("Bronze Plated Bricks", 190, false)
            .addOtherStructurePart("Borosilicate Glass", "Look at the hologram")
            .addStructureInfo("The glass tier limits the Energy Input tier")
            .addOtherStructurePart("Flowers", "On dirt/grass", 2)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .toolTipFinisher(Tags.MODNAME);
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("mGlassTier", mGlassTier);
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
        mGlassTier = aNBT.getByte("mGlassTier");
        mPrimaryMode = aNBT.getInteger("mPrimaryMode");
        mSecondaryMode = aNBT.getInteger("mSecondaryMode");
        for (int i = 0, isize = aNBT.getInteger("mStorageSize"); i < isize; i++)
            mStorage.add(new BeeSimulator(aNBT.getCompoundTag("mStorage." + i)));
        megaApiaryStorageVersion = aNBT.getInteger("MEGA_APIARY_STORAGE_VERSION");
        flowersCache.clear();
        mStorage.forEach(s -> flowersCache.add(s.flowerType));
        flowersCache.remove("");
        isCacheDirty = false;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (this.mMaxProgresstime > 0) {
            GT_Utility.sendChatToPlayer(aPlayer, "Can't change mode when running !");
            return;
        }
        if (!aPlayer.isSneaking()) {
            mPrimaryMode++;
            if (mPrimaryMode == 3) mPrimaryMode = 0;
            switch (mPrimaryMode) {
                case 0:
                    GT_Utility.sendChatToPlayer(aPlayer, "Changed primary mode to: Input mode");
                    break;
                case 1:
                    GT_Utility.sendChatToPlayer(aPlayer, "Changed primary mode to: Output mode");
                    break;
                case 2:
                    GT_Utility.sendChatToPlayer(aPlayer, "Changed primary mode to: Operating mode");
                    break;
            }
        } else {
            if (!mStorage.isEmpty()) {
                GT_Utility.sendChatToPlayer(aPlayer, "Can't change operating mode when the multi is not empty !");
                return;
            }
            mSecondaryMode++;
            if (mSecondaryMode == 2) mSecondaryMode = 0;
            switch (mSecondaryMode) {
                case 0:
                    GT_Utility.sendChatToPlayer(aPlayer, "Changed secondary mode to: Normal mode");
                    break;
                case 1:
                    GT_Utility.sendChatToPlayer(aPlayer, "Changed secondary mode to: Swarmer mode");
                    break;
            }
        }
    }

    private void updateMaxSlots() {
        int mOld = mMaxSlots;
        long v = this.getMaxInputEu();
        if (v < GT_Values.V[6]) mMaxSlots = 0;
        else if (mSecondaryMode == 0) mMaxSlots = (int) (v / GT_Values.V[6]);
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
            if (mUpdate < 0) mUpdate = 600;
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
    public boolean checkRecipe(ItemStack aStack) {
        updateMaxSlots();
        if (mPrimaryMode < 2) {
            if (mPrimaryMode == 0 && mStorage.size() < mMaxSlots) {
                World w = getBaseMetaTileEntity().getWorld();
                float t = (float) getVoltageTierExact();
                ArrayList<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs) {
                    if (beeRoot.getType(input) == EnumBeeType.QUEEN) {
                        BeeSimulator bs = new BeeSimulator(input, w, t);
                        if (bs.isValid) {
                            mStorage.add(bs);
                            isCacheDirty = true;
                        }
                    }
                    if (mStorage.size() >= mMaxSlots) break;
                }
                updateSlots();
            } else if (mPrimaryMode == 1 && mStorage.size() > 0) {
                for (int i = 0, imax = Math.min(10, mStorage.size()); i < imax; i++) {
                    addOutput(mStorage.get(0).queenStack);
                    mStorage.remove(0);
                    isCacheDirty = true;
                }
            } else return false;
            mMaxProgresstime = 10;
            mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            mEfficiencyIncrease = 10000;
            lEUt = 0;
            return true;
        } else if (mPrimaryMode == 2) {
            if (mMaxSlots > 0 && !mStorage.isEmpty()) {
                if (mSecondaryMode == 0) {
                    if (megaApiaryStorageVersion != MEGA_APIARY_STORAGE_VERSION) {
                        megaApiaryStorageVersion = MEGA_APIARY_STORAGE_VERSION;
                        World w = getBaseMetaTileEntity().getWorld();
                        float t = (float) getVoltageTierExact();
                        mStorage.forEach(s -> s.generate(w, t));
                    }

                    if (mStorage.size() > mMaxSlots) return false;

                    if (flowersError) return false;

                    if (needsTVarUpdate) {
                        float t = (float) getVoltageTierExact();
                        needsTVarUpdate = false;
                        mStorage.forEach(s -> s.updateTVar(t));
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
                        stacks.addAll(beeSimulator.getDrops(64_00d * boosted));
                    }

                    this.lEUt = -(int) ((double) GT_Values.V[6] * (double) mMaxSlots * 0.99d);
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mMaxProgresstime = 100;
                    this.mOutputItems = stacks.toArray(new ItemStack[0]);
                } else {
                    if (!depleteInput(PluginApiculture.items.royalJelly.getItemStack(64))
                        || !depleteInput(PluginApiculture.items.royalJelly.getItemStack(36))) {
                        this.updateSlots();
                        return false;
                    }
                    calculateOverclock(GT_Values.V[5] - 2L, 1200);
                    if (this.lEUt > 0) this.lEUt = -this.lEUt;
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mOutputItems = new ItemStack[] { this.mStorage.get(0)
                        .createIgnobleCopy() };
                    this.updateSlots();
                }
                return true;
            }
        }

        return false;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> info = new ArrayList<>(Arrays.asList(super.getInfoData()));
        info.add(
            "Running in mode: " + EnumChatFormatting.GOLD
                + (mPrimaryMode == 0 ? "Input mode"
                    : (mPrimaryMode == 1 ? "Output mode"
                        : (mSecondaryMode == 0 ? "Operating mode (NORMAL)" : "Operating mode (SWARMER)"))));
        info.add(
            "Bee storage (" + EnumChatFormatting.GOLD
                + mStorage.size()
                + EnumChatFormatting.RESET
                + "/"
                + (mStorage.size() > mMaxSlots ? EnumChatFormatting.DARK_RED.toString()
                    : EnumChatFormatting.GOLD.toString())
                + mMaxSlots
                + EnumChatFormatting.RESET
                + "):");
        HashMap<String, Integer> infos = new HashMap<>();
        for (int i = 0; i < mStorage.size(); i++) {
            StringBuilder builder = new StringBuilder();
            if (i > mMaxSlots) builder.append(EnumChatFormatting.DARK_RED);
            builder.append(EnumChatFormatting.GOLD);
            builder.append(mStorage.get(i).queenStack.getDisplayName());
            infos.merge(builder.toString(), 1, Integer::sum);
        }
        infos.forEach((key, value) -> info.add("x" + value + ": " + key));

        return info.toArray(new String[0]);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mGlassTier = 0;
        mCasing = 0;
        if (isCacheDirty) {
            flowersCache.clear();
            mStorage.forEach(s -> flowersCache.add(s.flowerType));
            flowersCache.remove("");
            isCacheDirty = false;
        }
        flowersCheck.addAll(flowersCache);
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 8, 0)) return false;
        if (this.mGlassTier < 10 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.mGlassTier < hatchEnergy.mTier) return false;
        boolean valid = this.mMaintenanceHatches.size() == 1 && this.mEnergyHatches.size() >= 1 && this.mCasing >= 190;
        flowersError = valid && !this.flowersCheck.isEmpty();
        if (valid) updateMaxSlots();
        return valid;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MegaIndustrialApiary(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
        boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
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
    public boolean useModularUI() {
        return true;
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    @Override
    public int getGUIWidth() {
        return 176;
    }

    @Override
    public void bindPlayerInventoryUI(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.bindPlayerInventory(
            buildContext.getPlayer(),
            new Pos2d(7, 83),
            this.getGUITextureSet()
                .getItemSlot());
    }

    private static final UIInfo<?, ?> MegaApiaryUI = createKTMetaTileEntityUI(
        KT_ModulaUIContainer_MegaIndustrialApiary::new);

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        MegaApiaryUI.open(
            aPlayer,
            aBaseMetaTileEntity.getWorld(),
            aBaseMetaTileEntity.getXCoord(),
            aBaseMetaTileEntity.getYCoord(),
            aBaseMetaTileEntity.getZCoord());
        return true;
    }

    private static class KT_ModulaUIContainer_MegaIndustrialApiary extends ModularUIContainer {

        final WeakReference<GT_MetaTileEntity_MegaIndustrialApiary> parent;

        public KT_ModulaUIContainer_MegaIndustrialApiary(ModularUIContext context, ModularWindow mainWindow,
            GT_MetaTileEntity_MegaIndustrialApiary mte) {
            super(context, mainWindow);
            parent = new WeakReference<>(mte);
        }

        @Override
        public ItemStack transferStackInSlot(EntityPlayer aPlayer, int aSlotIndex) {
            if (!(aPlayer instanceof EntityPlayerMP)) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final Slot s = getSlot(aSlotIndex);
            if (s == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (aSlotIndex >= 36) return super.transferStackInSlot(aPlayer, aSlotIndex);
            final ItemStack aStack = s.getStack();
            if (aStack == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            GT_MetaTileEntity_MegaIndustrialApiary mte = parent.get();
            if (mte == null) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (mte.mStorage.size() >= mte.mMaxSlots) return super.transferStackInSlot(aPlayer, aSlotIndex);
            if (beeRoot.getType(aStack) == EnumBeeType.QUEEN) {
                if (mte.mMaxProgresstime > 0) {
                    GT_Utility.sendChatToPlayer(aPlayer, EnumChatFormatting.RED + "Can't insert while running !");
                    return super.transferStackInSlot(aPlayer, aSlotIndex);
                }
                World w = mte.getBaseMetaTileEntity()
                    .getWorld();
                float t = (float) mte.getVoltageTierExact();
                BeeSimulator bs = new BeeSimulator(aStack, w, t);
                if (bs.isValid) {
                    mte.mStorage.add(bs);
                    s.putStack(null);
                    detectAndSendChanges();
                    mte.isCacheDirty = true;
                    return null;
                }
            }
            return super.transferStackInSlot(aPlayer, aSlotIndex);
        }
    }

    private final List<ItemStack> drawables = new ArrayList<>(mMaxSlots);

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 4)
                .setSize(143, 75)
                .setEnabled(widget -> !isFixed.apply(widget)));

        buildContext.addSyncedWindow(CONFIGURATION_WINDOW_ID, this::createConfigurationWindow);
        EntityPlayer player = buildContext.getPlayer();

        // Slot is not needed

        builder.widget(
            new DynamicPositionedColumn().setSynced(false)
                .widget(new CycleButtonWidget().setToggle(() -> getBaseMetaTileEntity().isAllowedToWork(), works -> {
                    if (works) getBaseMetaTileEntity().enableWorking();
                    else getBaseMetaTileEntity().disableWorking();

                    if (!(player instanceof EntityPlayerMP)) return;
                    String tChat = GT_Utility.trans("090", "Machine Processing: ")
                        + (works ? GT_Utility.trans("088", "Enabled") : GT_Utility.trans("087", "Disabled"));
                    if (hasAlternativeModeText()) tChat = getAlternativeModeText();
                    GT_Utility.sendChatToPlayer(player, tChat);
                })
                    .addTooltip(0, new Text("Disabled").color(Color.RED.dark(3)))
                    .addTooltip(1, new Text("Enabled").color(Color.GREEN.dark(3)))
                    .setVariableBackgroundGetter(toggleButtonBackgroundGetter)
                    .setSize(18, 18)
                    .addTooltip("Working status"))
                .widget(
                    new ButtonWidget().setOnClick(
                        (clickData, widget) -> {
                            if (!widget.isClient()) widget.getContext()
                                .openSyncedWindow(CONFIGURATION_WINDOW_ID);
                        })
                        .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                        .addTooltip("Configuration")
                        .setSize(18, 18))
                .setPos(151, 4));

        ChangeableWidget beesContainer = new ChangeableWidget(() -> createBeesContainerWidget(player));

        AtomicInteger lastMaxSlots = new AtomicInteger();
        builder.widget(beesContainer.attachSyncer(new FakeSyncWidget.IntegerSyncer(() -> {
            if (lastMaxSlots.get() != mMaxSlots) {
                lastMaxSlots.set(mMaxSlots);
                beesContainer.notifyChangeNoSync();
            }
            return mMaxSlots;
        }, i -> {
            if (mMaxSlots != i) {
                mMaxSlots = i;
                beesContainer.notifyChangeNoSync();
            }
        }), builder)
            .attachSyncer(
                new FakeSyncWidget.ListSyncer<>(
                    () -> mStorage.stream()
                        .map(s -> s.queenStack)
                        .collect(Collectors.toList()),
                    l -> {
                        drawables.clear();
                        drawables.addAll(l);
                        if (beesContainer.getChildren()
                            .size() > 0)
                            IWidgetParent.forEachByLayer(
                                (IWidgetParent) beesContainer.getChildren()
                                    .get(0),
                                Widget::notifyTooltipChange);
                    },
                    (buffer, i) -> {
                        try {
                            buffer.writeItemStackToBuffer(i);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    },
                    buffer -> {
                        try {
                            return buffer.readItemStackFromBuffer();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }),
                builder));

        final DynamicPositionedColumn screenElements = new DynamicPositionedColumn();
        drawTexts(screenElements, null);
        builder.widget(screenElements);
    }

    private Widget createBeesContainerWidget(EntityPlayer player) {
        Scrollable beesContainer = new Scrollable().setVerticalScroll();
        final int perRow = 7;
        if (mMaxSlots > 0) for (int i = 0, imax = ((mMaxSlots - 1) / perRow); i <= imax; i++) {
            DynamicPositionedRow row = new DynamicPositionedRow().setSynced(false);
            for (int j = 0, jmax = (i == imax ? (mMaxSlots - 1) % perRow : (perRow - 1)); j <= jmax; j++) {
                final int finalI = i * perRow;
                final int finalJ = j;
                final int ID = finalI + finalJ;
                row.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                    if (!(player instanceof EntityPlayerMP)) return;
                    if (!clickData.shift) {
                        ItemStack input = player.inventory.getItemStack();
                        if (input != null) {
                            if (this.mMaxProgresstime > 0) {
                                GT_Utility.sendChatToPlayer(
                                    player,
                                    EnumChatFormatting.RED + "Can't replace/insert while running !");
                                return;
                            }
                            if (beeRoot.getType(input) == EnumBeeType.QUEEN) {
                                World w = getBaseMetaTileEntity().getWorld();
                                float t = (float) getVoltageTierExact();
                                BeeSimulator bs = new BeeSimulator(input, w, t);
                                if (bs.isValid) {
                                    if (mStorage.size() > ID) {
                                        BeeSimulator removed = mStorage.remove(ID);
                                        mStorage.add(ID, bs);
                                        player.inventory.setItemStack(removed.queenStack);

                                    } else {
                                        mStorage.add(bs);
                                        player.inventory.setItemStack(null);
                                    }
                                    ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                                    ((EntityPlayerMP) player).updateHeldItem();

                                    isCacheDirty = true;
                                }
                            }
                            return;
                        }
                    }

                    if (mStorage.size() <= ID) return;
                    if (this.mMaxProgresstime > 0) {
                        GT_Utility.sendChatToPlayer(player, EnumChatFormatting.RED + "Can't eject while running !");
                        return;
                    }
                    BeeSimulator removed = mStorage.remove(ID);
                    isCacheDirty = true;
                    if (clickData.shift) {
                        if (player.inventory.addItemStackToInventory(removed.queenStack)) {
                            player.inventoryContainer.detectAndSendChanges();
                            return;
                        }
                    }
                    if (clickData.mouseButton == 1) {
                        if (player.inventory.getItemStack() == null) {
                            player.inventory.setItemStack(removed.queenStack);
                            ((EntityPlayerMP) player).isChangingQuantityOnly = false;
                            ((EntityPlayerMP) player).updateHeldItem();
                            return;
                        }
                    }

                    addOutput(removed.queenStack);
                    GT_Utility.sendChatToPlayer(player, "Queen ejected !");
                })
                    .setBackground(
                        () -> new IDrawable[] { getBaseMetaTileEntity().getGUITextureSet()
                            .getItemSlot(), GT_UITextures.OVERLAY_SLOT_BEE_QUEEN,
                            new ItemDrawable(drawables.size() > ID ? drawables.get(ID) : null)
                                .withFixedSize(16, 16, 1, 1) })
                    .dynamicTooltip(() -> {
                        if (drawables.size() > ID) return Arrays.asList(
                            drawables.get(ID)
                                .getDisplayName(),
                            EnumChatFormatting.GRAY + "Left click to eject into input bus",
                            EnumChatFormatting.GRAY + "Right click to get into mouse",
                            EnumChatFormatting.GRAY + "Shift click to get into inventory",
                            EnumChatFormatting.GRAY + "Click with other queen in mouse to replace");
                        return Collections
                            .singletonList(EnumChatFormatting.GRAY + "Click with queen in mouse to insert");
                    })
                    .setSize(18, 18));
            }
            beesContainer.widget(row.setPos(0, i * 18));
        }
        beesContainer.setPos(10, 16)
            .setSize(128, 60);
        return beesContainer;
    }

    protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 100);
        builder.setBackground(ModularUITextures.VANILLA_BACKGROUND);
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget("Configuration").setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(185, 3))
            .widget(
                new Column().widget(
                    new CycleButtonWidget().setLength(3)
                        .setGetter(() -> mPrimaryMode)
                        .setSetter(val -> {
                            if (this.mMaxProgresstime > 0) {
                                GT_Utility.sendChatToPlayer(player, "Can't change mode when running !");
                                return;
                            }
                            mPrimaryMode = val;

                            if (!(player instanceof EntityPlayerMP)) return;
                            switch (mPrimaryMode) {
                                case 0:
                                    GT_Utility.sendChatToPlayer(player, "Changed primary mode to: Input mode");
                                    break;
                                case 1:
                                    GT_Utility.sendChatToPlayer(player, "Changed primary mode to: Output mode");
                                    break;
                                case 2:
                                    GT_Utility.sendChatToPlayer(player, "Changed primary mode to: Operating mode");
                                    break;
                            }
                        })
                        .addTooltip(0, new Text("Input").color(Color.YELLOW.dark(3)))
                        .addTooltip(1, new Text("Output").color(Color.YELLOW.dark(3)))
                        .addTooltip(2, new Text("Operating").color(Color.GREEN.dark(3)))
                        .setVariableBackgroundGetter(
                            i -> new IDrawable[] { ModularUITextures.VANILLA_BACKGROUND,
                                GT_UITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18), i
                                    == 0 ? new Text("Input").color(Color.YELLOW.dark(3))
                                        .withFixedSize(70 - 18, 18, 15, 0)
                                        : i == 1 ? new Text("Output").color(Color.YELLOW.dark(3))
                                            .withFixedSize(70 - 18, 18, 15, 0)
                                            : new Text("Operating").color(Color.GREEN.dark(3))
                                                .withFixedSize(70 - 18, 18, 15, 0) })
                        .setSize(70, 18)
                        .addTooltip("Primary mode"))
                    .widget(
                        new CycleButtonWidget().setLength(2)
                            .setGetter(() -> mSecondaryMode)
                            .setSetter(val -> {
                                if (this.mMaxProgresstime > 0) {
                                    GT_Utility.sendChatToPlayer(player, "Can't change mode when running !");
                                    return;
                                }

                                mSecondaryMode = val;

                                if (!(player instanceof EntityPlayerMP)) return;
                                switch (mSecondaryMode) {
                                    case 0:
                                        GT_Utility.sendChatToPlayer(player, "Changed secondary mode to: Normal mode");
                                        break;
                                    case 1:
                                        GT_Utility.sendChatToPlayer(player, "Changed secondary mode to: Swarmer mode");
                                        break;
                                }
                            })
                            .addTooltip(0, new Text("Normal").color(Color.GREEN.dark(3)))
                            .addTooltip(1, new Text("Swarmer").color(Color.YELLOW.dark(3)))
                            .setVariableBackgroundGetter(
                                i -> new IDrawable[] { ModularUITextures.VANILLA_BACKGROUND,
                                    GT_UITextures.OVERLAY_BUTTON_CYCLIC.withFixedSize(18, 18), i
                                        == 0 ? new Text("Normal").color(Color.GREEN.dark(3))
                                            .withFixedSize(70 - 18, 18, 15, 0)
                                            : new Text("Swarmer").color(Color.YELLOW.dark(3))
                                                .withFixedSize(70 - 18, 18, 15, 0) })
                            .setSize(70, 18)
                            .addTooltip("Secondary mode"))
                    .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                    .setPos(10, 30))
            .widget(
                new Column().widget(new TextWidget("Primary mode").setSize(100, 18))
                    .widget(new TextWidget("Secondary mode").setSize(100, 18))
                    .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                    .setPos(80, 30))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CROSS)
                    .setSize(18, 18)
                    .setPos(10, 30)
                    .addTooltip(new Text("Can't change configuration when running !").color(Color.RED.dark(3)))
                    .setEnabled(widget -> getBaseMetaTileEntity().isActive()));
        return builder.build();
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        screenElements.setSynced(false)
            .setSpace(0)
            .setPos(10, 7);

        screenElements.widget(
            new DynamicPositionedRow().setSynced(false)
                .widget(new TextWidget("Status: ").setDefaultColor(COLOR_TEXT_GRAY.get()))
                .widget(new DynamicTextWidget(() -> {
                    if (flowersError) return new Text("No flowers !").color(Color.RED.dark(3));
                    if (getBaseMetaTileEntity().isActive()) return new Text("Working !").color(Color.GREEN.dark(3));
                    else if (getBaseMetaTileEntity().isAllowedToWork())
                        return new Text("Enabled").color(Color.GREEN.dark(3));
                    else if (getBaseMetaTileEntity().wasShutdown())
                        return new Text("Shutdown (CRITICAL)").color(Color.RED.dark(3));
                    else return new Text("Disabled").color(Color.RED.dark(3));
                }))
                .setEnabled(isFixed));

        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("132", "Pipe is loose.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mWrench))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mWrench, val -> mWrench = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("133", "Screws are loose.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mScrewdriver))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mScrewdriver, val -> mScrewdriver = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("134", "Something is stuck.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSoftHammer))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSoftHammer, val -> mSoftHammer = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("135", "Platings are dented.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mHardHammer))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mHardHammer, val -> mHardHammer = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("136", "Circuitry burned out.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mSolderingTool))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mSolderingTool, val -> mSolderingTool = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("137", "That doesn't belong there."))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mCrowbar))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mCrowbar, val -> mCrowbar = val));
        screenElements
            .widget(
                new TextWidget(GT_Utility.trans("138", "Incomplete Structure.")).setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> !mMachine))
            .widget(new FakeSyncWidget.BooleanSyncer(() -> mMachine, val -> mMachine = val));
    }

    private static class BeeSimulator {

        final ItemStack queenStack;
        boolean isValid;
        List<BeeDrop> drops = new ArrayList<>();
        List<BeeDrop> specialDrops = new ArrayList<>();
        float beeSpeed;

        float maxBeeCycles;
        String flowerType;

        public BeeSimulator(ItemStack queenStack, World world, float t) {
            isValid = false;
            this.queenStack = queenStack.copy();
            generate(world, t);
            isValid = true;
            queenStack.stackSize--;
        }

        public void generate(World world, float t) {
            drops.clear();
            specialDrops.clear();
            if (beeRoot.getType(this.queenStack) != EnumBeeType.QUEEN) return;
            IBee queen = beeRoot.getMember(this.queenStack);
            IBeekeepingMode mode = beeRoot.getBeekeepingMode(world);
            IBeeModifier beeModifier = mode.getBeeModifier();
            float mod = beeModifier.getLifespanModifier(null, null, 1.f);
            int h = queen.getMaxHealth();
            maxBeeCycles = (float) h / (1.f / mod);
            IBeeGenome genome = queen.getGenome();
            this.flowerType = genome.getFlowerProvider()
                .getFlowerType();
            IAlleleBeeSpecies primary = genome.getPrimary();
            beeSpeed = genome.getSpeed() * beeModifier.getProductionModifier(null, 1.f);
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
            queenStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("queenStack"));
            isValid = tag.getBoolean("isValid");
            drops = new ArrayList<>();
            specialDrops = new ArrayList<>();
            for (int i = 0, isize = tag.getInteger("dropssize"); i < isize; i++)
                drops.add(new BeeDrop(tag.getCompoundTag("drops" + i)));
            for (int i = 0, isize = tag.getInteger("specialDropssize"); i < isize; i++)
                specialDrops.add(new BeeDrop(tag.getCompoundTag("specialDrops" + i)));
            beeSpeed = tag.getFloat("beeSpeed");
            maxBeeCycles = tag.getFloat("maxBeeCycles");
            if (tag.hasKey("flowerType")) flowerType = tag.getString("flowerType");
            else {
                IBee queen = beeRoot.getMember(this.queenStack);
                IBeeGenome genome = queen.getGenome();
                this.flowerType = genome.getFlowerProvider()
                    .getFlowerType();
            }
        }

        public NBTTagCompound toNBTTagCompound() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("queenStack", queenStack.writeToNBT(new NBTTagCompound()));
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
            return tag;
        }

        final HashMap<BeeDrop, Double> dropProgress = new HashMap<>();

        public List<ItemStack> getDrops(final double timePassed) {
            drops.forEach(d -> dropProgress.merge(d, d.getAmount(timePassed / 550d), Double::sum));
            specialDrops.forEach(d -> dropProgress.merge(d, d.getAmount(timePassed / 550d), Double::sum));
            List<ItemStack> currentDrops = new ArrayList<>();
            dropProgress.entrySet()
                .forEach(e -> {
                    double v = e.getValue();
                    while (v > 1.f) {
                        int size = Math.min((int) v, 64);
                        currentDrops.add(
                            e.getKey()
                                .get(size));
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

        public void updateTVar(float t) {
            drops.forEach(d -> d.updateTVar(t));
            specialDrops.forEach(d -> d.updateTVar(t));
        }

        private static class BeeDrop {

            private static final float MAX_PRODUCTION_MODIFIER_FROM_UPGRADES = 17.19926784f; // 4*1.2^8
            final ItemStack stack;
            double amount;
            final GT_Utility.ItemId id;

            final float chance;
            final float beeSpeed;
            float t;

            public BeeDrop(ItemStack stack, float chance, float beeSpeed, float t) {
                this.stack = stack;
                this.chance = chance;
                this.beeSpeed = beeSpeed;
                this.t = t;
                id = GT_Utility.ItemId.createNoCopy(stack);
                evaluate();
            }

            public void updateTVar(float t) {
                if (this.t != t) {
                    this.t = t;
                    evaluate();
                }
            }

            public void evaluate() {
                this.amount = Bee.getFinalChance(chance, beeSpeed, MAX_PRODUCTION_MODIFIER_FROM_UPGRADES, t);
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
                stack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("stack"));
                chance = tag.getFloat("chance");
                beeSpeed = tag.getFloat("beeSpeed");
                t = tag.getFloat("t");
                amount = tag.getDouble("amount");
                id = GT_Utility.ItemId.createNoCopy(stack);
            }

            public NBTTagCompound toNBTTagCompound() {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
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

}

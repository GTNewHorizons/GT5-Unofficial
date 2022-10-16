/*
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022  kuba6000
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
 *
 */

package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static forestry.api.apiculture.BeeManager.beeRoot;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_DISTILLATION_TOWER_GLOW;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdder;
import static kubatech.api.Variables.*;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import forestry.api.apiculture.*;
import forestry.apiculture.blocks.BlockAlveary;
import forestry.apiculture.blocks.BlockApicultureType;
import forestry.apiculture.genetics.Bee;
import forestry.plugins.PluginApiculture;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Energy;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import kubatech.Tags;
import kubatech.api.LoaderReference;
import kubatech.api.helpers.GTHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class GT_MetaTileEntity_MegaIndustrialApiary
        extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_MegaIndustrialApiary> {

    private byte mGlassTier = 0;
    private int mCasing = 0;
    private int mMaxSlots = 0;
    private int mPrimaryMode = 0;
    private int mSecondaryMode = 0;
    private final ArrayList<BeeSimulator> mStorage = new ArrayList<>();

    private static final ItemStack royalJelly = PluginApiculture.items.royalJelly.getItemStack(1);
    private static final int CASING_INDEX = 10;
    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final IStructureDefinition<GT_MetaTileEntity_MegaIndustrialApiary> STRUCTURE_DEFINITION =
            StructureDefinition.<GT_MetaTileEntity_MegaIndustrialApiary>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, transpose(new String[][] { // spotless:off
                        {"               ","               ","               ","      HHH      ","    HHAAAHH    ","    HAPLPAH    ","   HAPAAAPAH   ","   HALAAALAH   ","   HAPAAAPAH   ","    HAPLPAH    ","    HHAAAHH    ","      HHH      ","               ","               ","               "},
                        {"               ","               ","      GGG      ","   GGG   GG    ","   G       G   ","   G       G   ","  G         G  ","  G         G  ","  G         G  ","   G       G   ","   G       G   ","    GG   GG    ","      GGG      ","               ","               "},
                        {"               ","      HHH      ","   HHH   HHH   ","  H        GH  ","  H         H  ","  H         H  "," H           H "," H           H "," H           H ","  H         H  ","  H         H  ","  HG       GH  ","   HHH   HHH   ","      HHH      ","               "},
                        {"      GGG      ","   GGG   GGG   ","  G         G  "," G           G "," G           G "," G           G ","G             G","G             G","G             G"," G           G "," G           G "," G           G ","  G         G  ","   GGG   GGG   ","      GGG      "},
                        {"      AAA      ","   OLA   ALO   ","  P         P  "," O           O "," L           L "," A           A ","A             A","A             A","A             A"," A           A "," L           L "," O           O ","  P         P  ","   OLA   ALO   ","      AAA      "},
                        {"     AAAAA     ","   NA     AO   ","  P         P  "," N           O "," A           A ","A             A","A     III     A","A     III     A","A     III     A","A             A"," A           A "," N           N ","  P         P  ","   NA     AN   ","     AAAAA     "},
                        {"     AAAAA     ","   NA     AO   ","  P         P  "," N           O "," A           A ","A             A","A     JJJ     A","A     JKJ     A","A     JJJ     A","A             A"," A           A "," N           N ","  P         P  ","   NA     AN   ","     AAAAA     "},
                        {"      AAA      ","   OLA   ALO   ","  P         P  "," O           O "," L           L "," A           A ","A     KKK     A","A     KKK     A","A     KKK     A"," A           A "," L           L "," O           O ","  P         P  ","   OLA   ALO   ","      AAA      "},
                        {"      G~G      ","   GGGBBBGGG   ","  GBB     BBG  "," GB        BBG "," GB         BG "," G           G ","GB    KKK    BG","GB    KJK    BG","GB    KKK    BG"," G           G "," GB         BG "," GBB       BBG ","  GBB     BBG  ","   GGGBBBGGG   ","      GGG      "},
                        {"      HHH      ","    HHBBBHH    ","  HHBBBBBBBHH  ","  HBBBWWWBBBH  "," HBBWWWWWWWBBH "," HBBWBBBBBWWBH ","HBBWWBBBBBBWBBH","HBBWBBBBBBBWBBH","HBBWBBBBBBWWBBH"," HBWWBBBBBWWBH "," HBBWWWBBWWBBH ","  HBBBWWWWBBH  ","  HHBBBBBBBHH  ","    HHBBBHH    ","      HHH      "},
                        {"               ","     GGGGG     ","   GGGBBBBGG   ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG "," GBBBBBBBBBBBG ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","   GGBBBBBGG   ","     GGGGG     ","               "},
                        {"               ","      HHH      ","    HHBBBHH    ","   HBBBBBBBH   ","  HBBBBBBBBBH  ","  HBBBBBBBBBH  "," HBBBBBBBBBBBH "," HBBBBBBBBBBBH "," HBBBBBBBBBBBH ","  HBBBBBBBBBH  ","  HBBBBBBBBBH  ","   HBBBBBBBH   ","    HHBBBHH    ","      HHH      ","               "},
                        {"               ","               ","      GGG      ","    GGBBBGG    ","   GBBBBBBBG   ","   GBBBBBBBG   ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","  GBBBBBBBBBG  ","   GBBBBBBBG   ","   GBBBBBBBG   ","    GGBBBGG    ","      GGG      ","               ","               "},
                        {"               ","               ","       H       ","     HHBHH     ","    HBBBBBH    ","   HBBBBBBBH   ","   HBBBBBBBH   ","  HBBBBBBBBBH  ","   HBBBBBBBH   ","   HBBBBBBBH   ","    HBBBBBH    ","     HHBHH     ","       H       ","               ","               "},
                        {"               ","               ","               ","       G       ","     GGBGG     ","    GBBBBBG    ","    GBBBBBG    ","   GBBBBBBBG   ","    GBBBBBG    ","    GBBBBBG    ","     GGBGG     ","       G       ","               ","               ","               "},
                        {"               ","               ","               ","               ","      HHH      ","     HHHHH     ","    HHBBBHH    ","    HHBBBHH    ","    HHBBBHH    ","     HHBHH     ","      HHH      ","               ","               ","               ","               "},
                        {"               ","               ","               ","               ","               ","               ","      GGG      ","      GHG      ","      GGG      ","               ","               ","               ","               ","               ","               "}
                    })) // spotless:on
                    .addElement(
                            'A',
                            LoaderReference.Bartworks
                                    ? BorosilicateGlass.ofBoroGlass(
                                            (byte) 0, (t, v) -> t.mGlassTier = v, t -> t.mGlassTier)
                                    : onElementPass(t -> t.mGlassTier = 100, ofBlock(Blocks.glass, 0)))
                    .addElement('B', ofChain(ofBlockAnyMeta(Blocks.dirt, 0), ofBlock(Blocks.grass, 0)))
                    .addElement(
                            'G',
                            ofChain(
                                    onElementPass(t -> t.mCasing++, ofBlock(GregTech_API.sBlockCasings1, 10)),
                                    ofHatchAdder(
                                            GT_MetaTileEntity_MegaIndustrialApiary::addInputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_MetaTileEntity_MegaIndustrialApiary::addOutputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_MetaTileEntity_MegaIndustrialApiary::addEnergyInputToMachineList,
                                            CASING_INDEX,
                                            1),
                                    ofHatchAdder(
                                            GT_MetaTileEntity_MegaIndustrialApiary::addMaintenanceToMachineList,
                                            CASING_INDEX,
                                            1)))
                    .addElement('H', ofBlockAnyMeta(Blocks.planks, 5))
                    .addElement('I', ofBlockAnyMeta(Blocks.wooden_slab, 5))
                    .addElement('J', ofBlock(PluginApiculture.blocks.apiculture, BlockApicultureType.APIARY.getMeta()))
                    .addElement('K', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.PLAIN.ordinal()))
                    .addElement('L', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.HYGRO.ordinal()))
                    .addElement('N', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.STABILIZER.ordinal()))
                    .addElement('O', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.HEATER.ordinal()))
                    .addElement('P', ofBlock(PluginApiculture.blocks.alveary, BlockAlveary.Type.FAN.ordinal()))
                    .addElement('W', ofBlock(Blocks.water, 0))
                    .build();

    @SuppressWarnings("unused")
    public GT_MetaTileEntity_MegaIndustrialApiary(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_MegaIndustrialApiary(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 8, 0);
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
                .addInfo("Use scredriver to change primary mode (INPUT/OUTPUT/OPERATING)")
                .addInfo("Use scredriver + shift to change operation mode (NORMAL/SWARMER)")
                .addInfo("--------------------- INPUT MODE ---------------------")
                .addInfo("- Does not take power")
                .addInfo("- Put your queens in the input bus to put them in the internal buffer")
                .addInfo("-------------------- OUTPUT MODE ---------------------")
                .addInfo("- Does not take power")
                .addInfo("- Will give your bees back to output bus")
                .addInfo("------------------- OPERATING MODE -------------------")
                .addInfo("- NORMAL:")
                .addInfo("  - For each LuV amp you can insert 1 bee")
                .addInfo("  - Processing time: 5 seconds")
                .addInfo("  - Uses 1 LuV amp per bee")
                .addInfo("  - All bees are accelerated 64 times")
                .addInfo("  - 8 production upgrades are applied")
                .addInfo("  - Genetic Stabilizer upgrade applied")
                .addInfo("  - Simulates perfect environment for your bees")
                .addInfo("  - Additionally you can provide royal jelly to increase the outputs:")
                .addInfo("    - 1 royal jelly grants 5% bonus")
                .addInfo("    - They will be consumed on each start of operation")
                .addInfo("    - and be applied to that operation only")
                .addInfo("    - Max bonus: 200%")
                .addInfo("- SWARMER:")
                .addInfo("  - You can only insert 1 queen")
                .addInfo("  - It will slowly produce ignoble princesses")
                .addInfo("  - Consumes 100 royal jelly per operation")
                .addInfo("  - Base processing time: 1 minute")
                .addInfo("  - Uses 1 amp IV")
                .addInfo("  - Can overclock")
                .addInfo(StructureHologram)
                .addSeparator()
                .beginStructureBlock(15, 17, 15, false)
                .addController("Front Bottom Center")
                .addCasingInfo("Bronze Plated Bricks", 190)
                .addOtherStructurePart("Borosilicate Glass", "Look at the hologram")
                .addStructureInfo("The glass tier limits the Energy Input tier")
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
        for (int i = 0; i < mStorage.size(); i++)
            aNBT.setTag("mStorage." + i, mStorage.get(i).toNBTTagCompound());
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mGlassTier = aNBT.getByte("mGlassTier");
        mPrimaryMode = aNBT.getInteger("mPrimaryMode");
        mSecondaryMode = aNBT.getInteger("mSecondaryMode");
        for (int i = 0, isize = aNBT.getInteger("mStorageSize"); i < isize; i++)
            mStorage.add(new BeeSimulator(aNBT.getCompoundTag("mStorage." + i)));
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
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
        long v = GTHelper.getMaxInputEU(this);
        if (v < GT_Values.V[6]) mMaxSlots = 0;
        else if (mSecondaryMode == 0) mMaxSlots = (int) (v / GT_Values.V[6]);
        else mMaxSlots = 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Beeeee rendering inside ?
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        updateMaxSlots();
        if (mPrimaryMode < 2) {
            if (mPrimaryMode == 0 && mStorage.size() < mMaxSlots) {
                World w = getBaseMetaTileEntity().getWorld();
                ArrayList<ItemStack> inputs = getStoredInputs();
                for (ItemStack input : inputs) {
                    if (beeRoot.getType(input) == EnumBeeType.QUEEN) {
                        BeeSimulator bs = new BeeSimulator(input, w);
                        if (bs.isValid) mStorage.add(bs);
                    }
                }
                updateSlots();
            } else if (mPrimaryMode == 1 && mStorage.size() > 0) {
                for (int i = 0, imax = Math.min(10, mStorage.size()); i < imax; i++) {
                    addOutput(mStorage.get(0).queenStack);
                    mStorage.remove(0);
                }
            } else return false;
            mMaxProgresstime = 10;
            mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
            mEfficiencyIncrease = 10000;
            mEUt = 0;
            return true;
        } else if (mPrimaryMode == 2) {
            if (mMaxSlots > 0 && !mStorage.isEmpty()) {
                if (mSecondaryMode == 0) {
                    this.mEUt = -((int) GT_Values.V[6] * mStorage.size());
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mMaxProgresstime = 100;

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
                    this.mOutputItems = stacks.toArray(new ItemStack[0]);
                    return true;
                } else {
                    if (!depleteInput(PluginApiculture.items.royalJelly.getItemStack(64))
                            || !depleteInput(PluginApiculture.items.royalJelly.getItemStack(36))) {
                        this.updateSlots();
                        return false;
                    }
                    calculateOverclockedNessMulti((int) GT_Values.V[5], 1200, 2, getMaxInputVoltage());
                    this.mEfficiency = (10000 - (getIdealStatus() - getRepairStatus()) * 1000);
                    this.mEfficiencyIncrease = 10000;
                    this.mOutputItems = new ItemStack[] {this.mStorage.get(0).createIgnobleCopy()};
                    this.updateSlots();
                    return true;
                }
            }
        }

        return false;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mGlassTier = 0;
        mCasing = 0;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 8, 0)) return false;
        if (this.mGlassTier < 8 && !this.mEnergyHatches.isEmpty())
            for (GT_MetaTileEntity_Hatch_Energy hatchEnergy : this.mEnergyHatches)
                if (this.mGlassTier < hatchEnergy.mTier) return false;
        boolean valid = this.mMaintenanceHatches.size() == 1 && this.mEnergyHatches.size() >= 1 && this.mCasing >= 190;
        if (valid) updateMaxSlots();
        return valid;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MegaIndustrialApiary(this.mName);
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(CASING_INDEX),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_DISTILLATION_TOWER_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(CASING_INDEX)};
    }

    private static class BeeSimulator {
        ItemStack queenStack;
        boolean isValid;
        // boolean isBreadingMode;
        // boolean isInfinite;
        List<BeeDrop> drops;
        List<BeeDrop> specialDrops;
        float beeSpeed;

        float maxBeeCycles;

        public BeeSimulator(ItemStack queenStack, World world) {
            isValid = false;
            this.queenStack = queenStack.copy();
            if (beeRoot.getType(this.queenStack) != EnumBeeType.QUEEN) return;
            IBee queen = beeRoot.getMember(this.queenStack);
            IBeekeepingMode mode = beeRoot.getBeekeepingMode(world);
            IBeeModifier beeModifier = mode.getBeeModifier();
            float mod = beeModifier.getLifespanModifier(null, null, 1.f);
            int h = queen.getMaxHealth();
            maxBeeCycles = (float) h / (1.f / mod);
            IBeeGenome genome = queen.getGenome();
            // isInfinite = queen.isNatural();
            // if (!isInfinite && h < 4) return;
            IAlleleBeeSpecies primary = genome.getPrimary();
            drops = new ArrayList<>();
            specialDrops = new ArrayList<>();
            beeSpeed = genome.getSpeed() * beeModifier.getProductionModifier(null, 1.f);
            genome.getPrimary()
                    .getProductChances()
                    .forEach((key, value) -> drops.add(new BeeDrop(key, value, beeSpeed)));
            genome.getSecondary()
                    .getProductChances()
                    .forEach((key, value) -> drops.add(new BeeDrop(key, value / 2.f, beeSpeed)));
            primary.getSpecialtyChances().forEach((key, value) -> specialDrops.add(new BeeDrop(key, value, beeSpeed)));

            isValid = true;
            queenStack.stackSize--;
        }

        public BeeSimulator(NBTTagCompound tag) {
            queenStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("queenstack"));
            isValid = tag.getBoolean("isValid");
            // isBreadingMode = tag.getBoolean("isBreadingMode");
            // isInfinite = tag.getBoolean("isInfinite");
            drops = new ArrayList<>();
            specialDrops = new ArrayList<>();
            for (int i = 0, isize = tag.getInteger("dropssize"); i < isize; i++)
                drops.add(new BeeDrop(tag.getCompoundTag("drops" + i)));
            for (int i = 0, isize = tag.getInteger("specialDropssize"); i < isize; i++)
                specialDrops.add(new BeeDrop(tag.getCompoundTag("specialDrops" + i)));
            beeSpeed = tag.getFloat("beeSpeed");
            maxBeeCycles = tag.getFloat("maxBeeCycles");
        }

        public NBTTagCompound toNBTTagCompound() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setTag("queenStack", queenStack.writeToNBT(new NBTTagCompound()));
            tag.setBoolean("isValid", isValid);
            // tag.setBoolean("isBreadingMode", isBreadingMode);
            // tag.setBoolean("isInfinite", isInfinite);
            tag.setInteger("dropssize", drops.size());
            for (int i = 0; i < drops.size(); i++)
                tag.setTag("drops" + i, drops.get(i).toNBTTagCompound());
            tag.setInteger("specialDropssize", drops.size());
            for (int i = 0; i < specialDrops.size(); i++)
                tag.setTag("specialDrops" + i, specialDrops.get(i).toNBTTagCompound());
            tag.setFloat("beeSpeed", beeSpeed);
            tag.setFloat("maxBeeCycles", maxBeeCycles);
            return tag;
        }

        HashMap<BeeDrop, Double> dropProgress = new HashMap<>();

        public List<ItemStack> getDrops(final double timePassed) {
            drops.forEach(d -> dropProgress.merge(d, d.getAmount(timePassed / 550d), Double::sum));
            specialDrops.forEach(d -> dropProgress.merge(d, d.getAmount(timePassed / 550d), Double::sum));
            List<ItemStack> currentDrops = new ArrayList<>();
            dropProgress.entrySet().forEach(e -> {
                double v = e.getValue();
                while (v > 1.f) {
                    int size = Math.min((int) v, 64);
                    currentDrops.add(e.getKey().get(size));
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

        private static class BeeDrop {
            ItemStack stack;
            double amount;
            GT_Utility.ItemId id;

            public BeeDrop(ItemStack stack, float chance, float beeSpeed) {
                this.stack = stack;
                this.amount = Bee.getFinalChance(chance, beeSpeed, 2.f, 8.f);
                id = GT_Utility.ItemId.createNoCopy(stack);
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
                amount = tag.getDouble("amount");
                id = GT_Utility.ItemId.createNoCopy(stack);
            }

            public NBTTagCompound toNBTTagCompound() {
                NBTTagCompound tag = new NBTTagCompound();
                tag.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
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

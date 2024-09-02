package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.system.material.WerkstoffLoader;
import goodgenerator.blocks.tileEntity.base.MTETooltipMultiBlockBaseEM;
import goodgenerator.crossmod.thaumcraft.LargeEssentiaEnergyData;
import goodgenerator.items.GGMaterial;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoMulti;
import tectech.thing.metaTileEntity.hatch.MTEHatchDynamoTunnel;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.aspects.AspectList;
import thaumcraft.common.config.ConfigBlocks;

public class MTELargeEssentiaGenerator extends MTETooltipMultiBlockBaseEM
    implements IConstructable, ISurvivalConstructable {

    private IStructureDefinition<MTELargeEssentiaGenerator> multiDefinition = null;
    protected int mStableValue = 0;
    protected int mTierLimit = -1;
    protected long mLeftEnergy;
    private int mUpgrade = 1;
    final XSTR R = new XSTR();
    protected ArrayList<MTEEssentiaHatch> mEssentiaHatch = new ArrayList<>();

    public MTELargeEssentiaGenerator(String name) {
        super(name);
    }

    public MTELargeEssentiaGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 4, 0, 4, itemStack, hintsOnly);
    }

    @Override
    protected void clearHatches_EM() {
        super.clearHatches_EM();
        mEssentiaHatch.clear();
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mStableValue = 0;
        return structureCheck_EM(mName, 4, 0, 4) && (mDynamoHatches.size() + eDynamoMulti.size()) == 1
            && checkHatchTier()
            && checkNoLaser()
            && updateEssentiaHatchState();
    }

    private boolean checkNoLaser() {
        for (MTEHatchDynamoMulti tHatch : eDynamoMulti) {
            if (tHatch instanceof MTEHatchDynamoTunnel) {
                return false;
            }
        }
        return true;
    }

    public boolean checkHatchTier() {
        for (MTEHatchInput tHatch : mInputHatches) {
            if (tHatch.mTier > mTierLimit) return false;
        }
        for (MTEHatchDynamo tHatch : mDynamoHatches) {
            if (tHatch.mTier > mTierLimit) return false;
        }
        for (MTEHatchDynamoMulti tHatch : eDynamoMulti) {
            if (tHatch.mTier > mTierLimit) return false;
        }
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mStableValue = aNBT.getInteger("mStableValue");
        this.mLeftEnergy = aNBT.getLong("mLeftEnergy");
        this.mUpgrade = aNBT.getInteger("mUpgrade");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mStableValue", this.mStableValue);
        aNBT.setLong("mLeftEnergy", this.mLeftEnergy);
        aNBT.setInteger("mUpgrade", this.mUpgrade);
    }

    public boolean updateEssentiaHatchState() {
        for (MTEEssentiaHatch hatch : mEssentiaHatch) {
            hatch.mState = mUpgrade;
        }
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()) {
            ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
            if (tCurrentItem != null && tCurrentItem.getItem()
                .equals(
                    ItemRefer.Essentia_Upgrade_Empty.get(1)
                        .getItem())) {
                int tMeta = tCurrentItem.getItemDamage();
                if ((mUpgrade & (1 << tMeta)) == 0 && tMeta != 0) {
                    tCurrentItem.stackSize--;
                    mUpgrade = mUpgrade | (1 << tMeta);
                    GTUtility.sendChatToPlayer(
                        aPlayer,
                        tCurrentItem.getDisplayName() + StatCollector.translateToLocal("largeessentiagenerator.chat"));
                }
                updateEssentiaHatchState();
                return true;
            }
        }
        super.onRightclick(aBaseMetaTileEntity, aPlayer);
        return true;
    }

    @Override
    public IStructureDefinition<MTELargeEssentiaGenerator> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<MTELargeEssentiaGenerator>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "A       A", "         ", "         ", "         ", "    ~    ", "         ", "         ",
                                "         ", "A       A" },
                            { "T   C   T", "   CEC   ", "  CEEEC  ", " CEEEEEC ", "CEEEEEEEC", " CEEEEEC ", "  CEEEC  ",
                                "   CEC   ", "T   C   T" },
                            { "T  TXT  T", "  TCXCT  ", " TCCXCCT ", "TCCCXCCCT", "XXXXXXXXX", "TCCCXCCCT", " TCCXCCT ",
                                "  TCXCT  ", "T  TXT  T" } }))
                .addElement('A', ofBlock(ConfigBlocks.blockCosmeticOpaque, 1))
                .addElement('T', ofBlock(ConfigBlocks.blockCosmeticSolid, 7))
                .addElement('C', ofBlock(Loaders.magicCasing, 0)) //
                .addElement('E', ofChain(onElementPass(x -> {
                    ++x.mStableValue;
                    x.mTierLimit = Math.max(x.mTierLimit, 4);
                }, ofBlock(Loaders.essentiaCell, 0)), onElementPass(x -> {
                    x.mStableValue += 2;
                    x.mTierLimit = Math.max(x.mTierLimit, 5);
                }, ofBlock(Loaders.essentiaCell, 1)), onElementPass(x -> {
                    x.mStableValue += 5;
                    x.mTierLimit = Math.max(x.mTierLimit, 6);
                }, ofBlock(Loaders.essentiaCell, 2)), onElementPass(x -> {
                    x.mStableValue += 10;
                    x.mTierLimit = Math.max(x.mTierLimit, 8);
                }, ofBlock(Loaders.essentiaCell, 3))))
                .addElement(
                    'X',
                    ofChain(
                        buildHatchAdder(MTELargeEssentiaGenerator.class)
                            .atLeast(
                                tectech.thing.metaTileEntity.multi.base.TTMultiblockBase.HatchElement.DynamoMulti
                                    .or(gregtech.api.enums.HatchElement.Dynamo),
                                gregtech.api.enums.HatchElement.Maintenance,
                                gregtech.api.enums.HatchElement.InputHatch)
                            .casingIndex(1536)
                            .dot(1)
                            .build(),
                        ofBlock(Loaders.magicCasing, 0),
                        ofSpecificTileAdder(
                            MTELargeEssentiaGenerator::addEssentiaHatch,
                            MTEEssentiaHatch.class,
                            Loaders.magicCasing,
                            0)))
                .build();
        }
        return multiDefinition;
    }

    public final boolean addEssentiaHatch(MTEEssentiaHatch aTileEntity) {
        return this.mEssentiaHatch.add(aTileEntity);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing_EM() {
        this.mEfficiency = 10000;
        this.mMaxProgresstime = 1;
        setEssentiaToEUVoltageAndAmp(getVoltageLimit(), getAmpLimit());
        return CheckRecipeResultRegistry.GENERATING;
    }

    public int getVoltageLimit() {
        long voltage = 0;
        for (MTEHatch tHatch : this.eDynamoMulti) {
            voltage += tHatch.maxEUOutput();
        }
        for (MTEHatch tHatch : this.mDynamoHatches) {
            voltage += tHatch.maxEUOutput();
        }
        if (voltage > Integer.MAX_VALUE) voltage = Integer.MAX_VALUE;
        return (int) voltage;
    }

    public int getAmpLimit() {
        long amp = 0;
        for (MTEHatch tHatch : this.eDynamoMulti) {
            amp += tHatch.maxAmperesOut();
        }
        for (MTEHatch tHatch : this.mDynamoHatches) {
            amp += tHatch.maxAmperesOut();
        }
        if (amp > Integer.MAX_VALUE) amp = Integer.MAX_VALUE;
        return (int) amp;
    }

    public long getPerAspectEnergy(Aspect aspect) {
        int type = LargeEssentiaEnergyData.getAspectTypeIndex(aspect);
        if (!isValidEssentia(aspect)) return 0;
        switch (type) {
            case 0:
                return normalEssentia(aspect);
            case 1:
                return airEssentia(aspect);
            case 2:
                return thermalEssentia(aspect);
            case 3:
                return unstableEssentia(aspect);
            case 4:
                return victusEssentia(aspect);
            case 5:
                return taintedEssentia(aspect);
            case 6:
                return mechanicEssentia(aspect);
            case 7:
                return spiritEssentia(aspect);
            case 8:
                return radiationEssentia(aspect);
            case 9:
                return electricEssentia(aspect);
            default:
                return 0;
        }
    }

    public long normalEssentia(Aspect aspect) {
        return LargeEssentiaEnergyData.getAspectFuelValue(aspect);
    }

    public long airEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 0;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 8;
        if (depleteInput(Materials.LiquidAir.getFluid(ceoInput))) {
            ceoOutput = 1.5D;
        } else if (depleteInput(Materials.Air.getGas(ceoInput))) {
            ceoOutput = 1.0D;
        }
        return (long) (baseValue * ceoOutput);
    }

    public long thermalEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 0;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 2;
        if (depleteInput(Materials.SuperCoolant.getFluid(ceoInput))) {
            ceoOutput = 9.0D;
        } else if (depleteInput(FluidRegistry.getFluidStack("cryotheum", ceoInput))) {
            ceoOutput = 5.0D;
        } else if (depleteInput(FluidRegistry.getFluidStack("ic2coolant", ceoInput))) {
            ceoOutput = 1.5D;
        } else if (depleteInput(Materials.Ice.getSolid(ceoInput))) {
            ceoOutput = 1.2D;
        } else if (depleteInput(FluidRegistry.getFluidStack("ic2distilledwater", ceoInput))) {
            ceoOutput = 1.0D;
        } else if (depleteInput(Materials.Water.getFluid(ceoInput))) {
            ceoOutput = 0.5D;
        }

        return (long) (baseValue * ceoOutput);
    }

    public long unstableEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 0;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 4;
        if (depleteInput(WerkstoffLoader.Xenon.getFluidOrGas(ceoInput))) {
            ceoOutput = 4.0D;
        } else if (depleteInput(WerkstoffLoader.Krypton.getFluidOrGas(ceoInput))) {
            ceoOutput = 3.0D;
        } else if (depleteInput(Materials.Argon.getFluid(ceoInput))) {
            ceoOutput = 2.5D;
        } else if (depleteInput(WerkstoffLoader.Neon.getFluidOrGas(ceoInput))) {
            ceoOutput = 2.2D;
        } else if (depleteInput(Materials.Helium.getFluid(ceoInput))) {
            ceoOutput = 2.0D;
        } else if (depleteInput(Materials.Nitrogen.getFluid(ceoInput))) {
            ceoOutput = 1.0D;
        }
        return (long) (baseValue * ceoOutput);
    }

    public long victusEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 1.0D;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 18;
        if (depleteInput(FluidRegistry.getFluidStack("xpjuice", ceoInput))) {
            ceoOutput = 2.0D;
        } else if (depleteInput(FluidRegistry.getFluidStack("lifeessence", ceoInput))) {
            ceoOutput = 6.0D;
        }
        return (long) (baseValue * ceoOutput);
    }

    public long taintedEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 1.0D;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 3;
        int chance = 2000;
        if (depleteInput(FluidRegistry.getFluidStack("fluidpure", ceoInput))) {
            ceoOutput = 60.0D;
            chance = 0;
        } else if (depleteInput(FluidRegistry.getFluidStack("fluiddeath", ceoInput))) {
            ceoOutput = Math.pow(25000D / baseValue, 4);
            chance = 4000;
        }

        if (R.nextInt(10000) < chance) {
            World world = getBaseMetaTileEntity().getWorld();
            int tX = getBaseMetaTileEntity().getXCoord() + R.nextInt(5) - 2;
            int tY = getBaseMetaTileEntity().getYCoord();
            int tZ = getBaseMetaTileEntity().getZCoord() + R.nextInt(5) - 2;
            if (world.isAirBlock(tX, tY, tZ)) world.setBlock(tX, tY, tZ, ConfigBlocks.blockFluxGas, R.nextInt(8), 3);
        }

        return (long) (baseValue * ceoOutput);
    }

    public long mechanicEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 0;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 20;
        if (depleteInput(Materials.Lubricant.getFluid(ceoInput))) {
            ceoOutput = 1.0D;
        }
        return (long) (baseValue * ceoOutput);
    }

    public long spiritEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 1.0D;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 2;
        if (depleteInput(FluidRegistry.getFluidStack("witchery:fluidspirit", ceoInput))) {
            ceoOutput = 10D * (1 + mStableValue / 100D);
        } else if (depleteInput(FluidRegistry.getFluidStack("witchery:hollowtears", ceoInput))) {
            ceoOutput = 15D * (1 + 100D / mStableValue);
        }
        return (long) (baseValue * ceoOutput);
    }

    public long radiationEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = 1.0D;
        int ceoInput = (int) LargeEssentiaEnergyData.getAspectCeo(aspect) * 6;
        if (depleteInput(Materials.Caesium.getMolten(ceoInput))) {
            ceoOutput = 2.0D;
        } else if (depleteInput(Materials.Uranium235.getMolten(ceoInput))) {
            ceoOutput = 3.0D;
        } else if (depleteInput(Materials.Naquadah.getMolten(ceoInput))) {
            ceoOutput = 4.0D;
        } else if (depleteInput(GGMaterial.atomicSeparationCatalyst.getMolten(ceoInput))) {
            ceoOutput = 16.0D;
        }
        return (long) (baseValue * ceoOutput);
    }

    public long electricEssentia(Aspect aspect) {
        long baseValue = LargeEssentiaEnergyData.getAspectFuelValue(aspect);
        double ceoOutput = Math.pow(3.0, GTUtility.getTier(getVoltageLimit()));
        return (long) (baseValue * ceoOutput);
    }

    public void setEssentiaToEUVoltageAndAmp(long voltageLimit, long ampLimit) {
        long EUt = mLeftEnergy;
        long EUVoltage = voltageLimit, EUAmp = 1;

        for (MTEEssentiaHatch hatch : this.mEssentiaHatch) {
            AspectList aspects = hatch.getAspects();
            for (Aspect aspect : aspects.aspects.keySet()) {
                if (!isValidEssentia(aspect)) continue;
                while (EUt <= (voltageLimit * ampLimit) && aspects.getAmount(aspect) > 0) {
                    long addedEU = getPerAspectEnergy(aspect) * mStableValue / 25;
                    if (addedEU == 0) break;
                    EUt += addedEU;
                    aspects.reduce(aspect, 1);
                    if (aspects.getAmount(aspect) == 0) aspects.remove(aspect);
                }
            }
        }

        if (EUt <= voltageLimit) {
            EUVoltage = EUt;
            EUAmp = 1;
            mLeftEnergy = 0;
        } else {
            while (EUVoltage * (EUAmp + 1) <= EUt && EUAmp + 1 <= ampLimit) {
                EUAmp++;
            }
            mLeftEnergy = EUt - (EUVoltage * EUAmp);
        }

        this.mEUt = (int) EUVoltage;
        this.eAmpereFlow = (int) EUAmp;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("LargeEssentiaGenerator.hint", 6);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeEssentiaGenerator(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Essentia Generator")
            .addInfo("Controller block for the Large Essentia Generator")
            .addInfo("Maybe some Thaumaturges are upset by it. . .")
            .addInfo("Transform Essentia into energy!")
            .addInfo("The Diffusion Cell determines the highest hatch tier that the LEG can accept.")
            .addInfo("Supports normal Dynamo Hatches or TecTech ones for up to 64A, but no Laser Hatches.")
            .addInfo("You can find more information about this generator in the Thaumonomicon.")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .addMaintenanceHatch("Hint block with dot 1", 1)
            .addInputHatch("Hint block with dot 1", 1)
            .addDynamoHatch("Hint block with dot 1", 1)
            .addOtherStructurePart("Essentia Input Hatch", "Essentia Input", 1)
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1536),
                new GTRenderedTexture(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG), TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1536),
                new GTRenderedTexture(Textures.BlockIcons.MACHINE_CASING_DRAGONEGG) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(1536) };
    }

    public boolean isValidEssentia(Aspect aspect) {
        int type = LargeEssentiaEnergyData.getAspectTypeIndex(aspect);
        return type != -1 && (mUpgrade & (1 << type)) != 0;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 4, 0, 4, elementBudget, env, false, true);
    }
}

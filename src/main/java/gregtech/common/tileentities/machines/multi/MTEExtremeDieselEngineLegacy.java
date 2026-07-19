package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTUtility.validMTEList;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDynamo;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTEExtremeDieselEngineLegacy extends MTEDieselEngineLegacy {

    public MTEExtremeDieselEngineLegacy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEExtremeDieselEngineLegacy(String aName) {
        super(aName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.ece")
            .addInfo("gt.ece.tips")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 4, false)
            .addController("Front center")
            .addCasingInfoRange("Robust Tungstensteel Machine Casing", 16, 22, false)
            .addOtherStructurePart("Titanium Gear Box Machine Casing", "gt.ece.info.ti_gearbox")
            .addOtherStructurePart("Extreme Engine Intake Machine Casing", "gt.ece.info.intake")
            .addStructureInfo("gt.ece.info.intake_caution")
            .addDynamoHatch("gt.mbtt.structure.back_center", 2)
            .addMaintenanceHatch("gt.ece.info.maintenance", 1)
            .addMufflerHatch("gt.ece.info.muffler", 1)
            .addInputHatch("gt.ece.info.i_hatch.1", 1)
            .addInputHatch("gt.ece.info.i_hatch.2", 1)
            .addInputHatch("gt.ece.info.i_hatch.3", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public RecipeMap<FuelBackend> getRecipeMap() {
        return RecipeMaps.extremeDieselFuels;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][60], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][60], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_EXTREME_DIESEL_ENGINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][60] };
    }

    @Override
    public byte getCasingMeta() {
        return 0;
    }

    @Override
    public Block getIntakeBlock() {
        return GregTechAPI.sBlockCasings8;
    }

    @Override
    public byte getIntakeMeta() {
        return 4;
    }

    @Override
    public byte getCasingTextureIndex() {
        return 60;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEExtremeDieselEngineLegacy(this.mName);
    }

    @Override
    protected int getNominalOutput() {
        return 10900;
    }

    @Override
    protected Materials getBooster() {
        return Materials.LiquidOxygen;
    }

    @Override
    protected int getEfficiencyIncrease() {
        return 20;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GTMod.proxy.mPollutionExtremeCombustionEnginePerSecond;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchDynamo tHatch : validMTEList(mDynamoHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] { "GT5U.infodata.extreme_diesel_engine.hdr",
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.energy",
                EnumChatFormatting.GREEN + formatNumber(storedEnergy) + EnumChatFormatting.RESET,
                EnumChatFormatting.YELLOW + formatNumber(maxEnergy) + EnumChatFormatting.RESET),
            getIdealStatus() == getRepairStatus()
                ? EnumChatFormatting.GREEN + IGregTechDeviceInformation.decode("GT5U.turbine.maintenance.false")
                    + EnumChatFormatting.RESET
                : EnumChatFormatting.RED + IGregTechDeviceInformation.decode("GT5U.turbine.maintenance.true")
                    + EnumChatFormatting.RESET,
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.engine.output",
                EnumChatFormatting.RED + formatNumber((long) -mEUt * mEfficiency / 10000) + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.engine.consumption",
                EnumChatFormatting.YELLOW + formatNumber(fuelConsumption) + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.engine.value",
                EnumChatFormatting.YELLOW + formatNumber(fuelValue) + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.turbine.fuel",
                EnumChatFormatting.GOLD + formatNumber(fuelRemaining) + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.engine.efficiency",
                EnumChatFormatting.YELLOW + "" + (mEfficiency / 100F) + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.turbine.pollution",
                EnumChatFormatting.GREEN + "" + getAveragePollutionPercentage() + EnumChatFormatting.RESET),
            IGregTechDeviceInformation.encode(
                "GT5U.infodata.multiblock.recipes_done",
                EnumChatFormatting.GREEN + formatNumber(recipesDone) + EnumChatFormatting.RESET) };
    }
}

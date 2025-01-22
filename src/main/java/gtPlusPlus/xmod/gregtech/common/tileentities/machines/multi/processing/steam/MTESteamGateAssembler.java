package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GTRenderedTexture;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;

public class MTESteamGateAssembler extends MTESteamMultiBase<MTESteamGateAssembler> implements ISurvivalConstructable {

    public MTESteamGateAssembler(String aName) {
        super(aName);
    }

    public MTESteamGateAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "An unholy amalgamation of pipes and cogs, capable of using incredible amounts of steam.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "Each chamber is carefully calibrated with near-infinite amounts of pressure.")
            .addInfo(
                EnumChatFormatting.AQUA + ""
                    + EnumChatFormatting.ITALIC
                    + "What would drive someone to create such a device? What purpose could this possibly serve?")
            .beginStructureBlock(3, 3, 3, false)
            .addInputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addOutputBus(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "Basic " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(EnumChatFormatting.GOLD + "14-25x" + EnumChatFormatting.GRAY + " Bronze Plated Bricks")
            .addStructureInfo(EnumChatFormatting.GOLD + "2x" + EnumChatFormatting.GRAY + " Bronze Pipe Casing")
            .addStructureInfo("")
            .addStructureInfo(EnumChatFormatting.BLUE + "High Pressure " + EnumChatFormatting.DARK_PURPLE + "Tier")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "14-25x" + EnumChatFormatting.GRAY + " Solid Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "2x" + EnumChatFormatting.GRAY + " Steel Pipe Casing")
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected GTRenderedTexture getFrontOverlay() {
        return new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_GATE_ASSEMBLER);
    }

    @Override
    protected GTRenderedTexture getFrontOverlayActive() {
        return new GTRenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_STEAM_GATE_ASSEMBLER);
    }

    @Override
    public int getTierRecipes() {
        return 0;
    }

    @Override
    public String getMachineType() {
        return "Boundless Creation Engine";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    public IStructureDefinition<MTESteamGateAssembler> getStructureDefinition() {
        return null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamGateAssembler(this.mName);
    }
}

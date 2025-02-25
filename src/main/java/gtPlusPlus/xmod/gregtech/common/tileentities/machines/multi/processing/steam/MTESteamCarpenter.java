package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorSteamIsTheNumber;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTEBetterSteamMultiBase;

public class MTESteamCarpenter extends MTEBetterSteamMultiBase<MTESteamCarpenter> implements ISurvivalConstructable {

    public MTESteamCarpenter(String aName) {
        super(aName);
    }

    public MTESteamCarpenter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public String getMachineType() {
        return "Carpenter";
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamCarpenter> getStructureDefinition() {
        return StructureDefinition.<MTESteamCarpenter>builder()
            .addShape(
                STRUCTURE_PIECE_MAIN,
                (transpose(
                    new String[][] { { "  AABAA  " }, { " BA   AB " }, { "AA     AA" }, { "A       A" },
                        { "B       B" }, { "A       A" }, { "AA     AA" }, { " BA   AB " }, { "  AA~AA  " } })))
            .addElement('A', ofBlock(GregTechAPI.sBlockCasingsSteam, 0))
            .addElement('B', ofBlock(GregTechAPI.sBlockCasingsSteam, 1))
            .build();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {

    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addInfo(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Impossible machine.")
            .addInfo("Created by: ")
            .addInfo(AuthorSteamIsTheNumber)
            .beginStructureBlock(3, 3, 3, false)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamCarpenter(this.mName);
    }
}

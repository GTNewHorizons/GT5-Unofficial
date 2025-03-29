package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_STEAMGATE_CONTROLLER;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

public class MTESteamgate extends MTEEnhancedMultiBlockBase<MTESteamgate> implements ISurvivalConstructable {

    public MTESteamgate(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTESteamgate(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESteamgate(this.mName);
    }

    @Override
    protected boolean explodesImmediately() {
        return false;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == facing) {
            rTexture = new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 0)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_STEAMGATE_CONTROLLER)
                    .extFacing()
                    .glow()
                    .build() };

        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasingsSteam, 0)) };
        }
        return rTexture;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 8, 0, elementBudget, env, false, true);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";

    @Override
    public IStructureDefinition<MTESteamgate> getStructureDefinition() {
        return StructureDefinition.<MTESteamgate>builder()
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
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addInfo(EnumChatFormatting.AQUA + "" + EnumChatFormatting.ITALIC + "Impossible machine.")
            .addInfo("Must be linked to another steamgate with a Steamgate Dialing Device.")
            .addInfo("Left click to save steamgate data. Right click to link to another steamgate.")
            .addInfo("Right click a linked steamgate to teleport.")
            .beginStructureBlock(3, 3, 3, false)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(STRUCTURE_PIECE_MAIN, 4, 8, 0);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    // YOINKED all this code n im NOT giving it back

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;
        if (!mMachine) return;

        ItemStack device = aPlayer.inventory.getCurrentItem();
        if (ItemList.Steamgate_Dialing_Device.isStackEqual(device, false, true)) {

            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("type", "SteamgateLinker");
            tag.setInteger("x", aBaseMetaTileEntity.getXCoord());
            tag.setInteger("y", aBaseMetaTileEntity.getYCoord());
            tag.setInteger("z", aBaseMetaTileEntity.getZCoord());

            device.stackTagCompound = tag;
            aPlayer.addChatMessage(new ChatComponentText("Saved Steamgate Data to Data Stick"));
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!mMachine) return false;
        if (linkedGate == null) tryLinkDataStick(aPlayer);
        else {
            BaseMetaTileEntity gate = (BaseMetaTileEntity) linkedGate.getBaseMetaTileEntity();
            aPlayer.setPositionAndUpdate(gate.getXCoord(), gate.getYCoord() + 2, gate.getZCoord());
        }
        return true;
    }

    private void tryLinkDataStick(EntityPlayer aPlayer) {
        // Make sure the held item is a DIALING DEVICE
        ItemStack device = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Steamgate_Dialing_Device.isStackEqual(device, false, true)) {
            return;
        }

        // Make sure this DIALING DEVICE is a proper STEAMGATE LETS GO link DIALING DEVICE.
        if (!device.hasTagCompound() || !device.stackTagCompound.getString("type")
            .equals("SteamgateLinker")) {
            return;
        }

        // Now read link coordinates from the DIALING DEVICE.
        NBTTagCompound nbt = device.stackTagCompound;
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");

        // Try to link, and report the result back to the player.
        boolean result = trySetControllerFromCoord(x, y, z);
        if (result) {
            aPlayer.addChatMessage(new ChatComponentText("Steamgate dialed successfully"));
        } else {
            aPlayer.addChatMessage(new ChatComponentText("Steamgate could not be dialed"));
        }

    }

    MTESteamgate linkedGate;

    private boolean trySetControllerFromCoord(int x, int y, int z) {
        // Find the block at the requested coordinated and check if it is a STEAMGATE WOOOOOOOOO.
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return false;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return false;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof MTESteamgate)) return false;
        if (metaTileEntity == this) return false;

        // Before linking, unlink from current STEAMGATE YEAAAAAAAA
        if (linkedGate != null) {
            linkedGate.linkedGate = null;
        }
        linkedGate = (MTESteamgate) metaTileEntity;
        linkedGate.linkedGate = this;
        return true;
    }
}

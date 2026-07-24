package tectech.thing.metaTileEntity.hatch;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.util.CommonValues;

public class MTEHatchDataItemsOutput extends MTEHatchDataConnector<ALRecipeDataPacket> {

    public ALRecipeDataPacket previousPacket;

    public MTEHatchDataItemsOutput(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { CommonValues.TEC_MARK_EM,
                StatCollector.translateToLocal("gt.blockmachines.hatch.dataoutass.desc.0"),
                StatCollector.translateToLocal("gt.blockmachines.hatch.dataoutass.desc.1"),
                EnumChatFormatting.AQUA + StatCollector.translateToLocal("gt.blockmachines.hatch.dataoutass.desc.2") });
    }

    public MTEHatchDataItemsOutput(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataItemsOutput(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    protected ALRecipeDataPacket loadPacketFromNBT(NBTTagCompound nbt) {
        return new ALRecipeDataPacket(nbt);
    }

    @Override
    public boolean isDataInputFacing(ForgeDirection side) {
        return isInputFacing(side);
    }

    @Override
    public boolean canConnectData(ForgeDirection side) {
        return isOutputFacing(side);
    }

    @Override
    public CheckState moveAround(IGregTechTileEntity aBaseMetaTileEntity, CheckState checkState) {
        var result = super.moveAround(aBaseMetaTileEntity, checkState);

        switch (result) {
            // these states should not modify this hatch or the connected one
            case CONNECTION, DISCONNECTED, UNKNOWN -> {}
            case NEW_DATA -> {
                previousPacket = q;
                q = null;
            }
            case CONNECTED -> {} // ((MTEHatchDataItemsInput) connected).setContents(q);
            default -> {
                // TODO: log this new state
            }
        }
        return result;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("recipeCount", previousPacket == null ? 0 : previousPacket.getContent().length);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(translate("tt.keyphrase.AL_Recipe_Transmitting", tag.getInteger("recipeCount")));
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> lines = new ArrayList<>();

        if (previousPacket != null) {
            for (RecipeAssemblyLine recipe : previousPacket.getContent()) {
                lines.add(
                    IGregTechDeviceInformation.encode("tt.keyphrase.AL_Recipe_Desc", recipe.mOutput.getDisplayName()));
            }
        } else {
            lines.add("tt.keyphrase.AL_Recipe_None");
        }

        lines.sort(String::compareTo);

        lines.add(0, "tt.keyphrase.AL_Recipe_Header");

        return lines.toArray(new String[0]);
    }
}

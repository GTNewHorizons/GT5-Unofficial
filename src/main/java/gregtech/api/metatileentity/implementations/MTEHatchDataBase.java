package gregtech.api.metatileentity.implementations;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;

public abstract class MTEHatchDataBase extends MTEHatch {

    public MTEHatchDataBase(int aID, String aName, String aNameRegional, int aTier, int aInvSlotCount,
        String[] aDescription) {
        super(aID, aName, aNameRegional, aTier, aInvSlotCount, aDescription);
    }

    public MTEHatchDataBase(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @NotNull
    public abstract ALRecipeDataPacket getAssemblyLineRecipes();

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setInteger("recipeCount", getAssemblyLineRecipes().contents.size());
    }

    protected abstract String getWailaDataKey();

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(StatCollector.translateToLocalFormatted(getWailaDataKey(), tag.getInteger("recipeCount")));
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> lines = new ArrayList<>();

        var packet = getAssemblyLineRecipes();

        if (!packet.isEmpty()) {
            for (RecipeAssemblyLine recipe : packet.contents) {
                lines.add(
                    StatCollector
                        .translateToLocalFormatted("tt.keyphrase.AL_Recipe_Desc", recipe.mOutput.getDisplayName()));
            }
        } else {
            lines.add(StatCollector.translateToLocalFormatted("tt.keyphrase.AL_Recipe_None"));
        }

        lines.sort(String::compareTo);

        lines.add(0, StatCollector.translateToLocalFormatted("tt.keyphrase.AL_Recipe_Header"));

        return lines.toArray(GTValues.emptyStringArray);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    /**
     * @return whether the available recipe set changed between two snapshots, compared by content rather than count so
     *         a same-size data-stick swap is still detected. Data input hatches call this to decide when to notify.
     */
    public static boolean recipesChanged(ALRecipeDataPacket a, ALRecipeDataPacket b) {
        int aSize = a == null ? 0 : a.contents.size();
        int bSize = b == null ? 0 : b.contents.size();
        if (aSize != bSize) return true;
        if (aSize == 0) return false;
        return !new HashSet<>(a.contents).equals(new HashSet<>(b.contents));
    }
}

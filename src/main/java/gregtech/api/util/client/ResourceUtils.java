package gregtech.api.util.client;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ResourceUtils {

    /**
     * Checks whether a resource exists.
     *
     * @param resLoc the {@link ResourceLocation} identifying the resource to check
     * @return {@code true} if the resource exists; {@code false} otherwise
     */
    @SideOnly(Side.CLIENT)
    public static boolean resourceExists(@NotNull ResourceLocation resLoc) {
        final IResourceManager resMan = Minecraft.getMinecraft()
            .getResourceManager();
        try {
            resMan.getResource(resLoc);
            return true;
        } catch (IOException ignored) {
            return false;
        }
    }
}

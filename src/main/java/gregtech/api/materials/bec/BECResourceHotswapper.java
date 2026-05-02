package gregtech.api.materials.bec;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.IResourceManagerReloadListener;
import net.minecraft.client.resources.SimpleReloadableResourceManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/// This is used to detect resource reloads, so that the BEC texture set system can reload its assets.
@SideOnly(Side.CLIENT)
public class BECResourceHotswapper implements IResourceManagerReloadListener {

    public static final BECResourceHotswapper INSTANCE = new BECResourceHotswapper();

    public BECResourceHotswapper() {
        SimpleReloadableResourceManager manager = (SimpleReloadableResourceManager) Minecraft.getMinecraft()
            .getResourceManager();

        // Add this listener to the front of the list so that we can update the required
        manager.reloadListeners.add(0, this);

        onResourceManagerReload(null);
    }

    @Override
    public void onResourceManagerReload(IResourceManager ignored) {
        BECMaterialList.loadResources();
        BECTextureSet.reload();
    }

    public static void init() {

    }
}

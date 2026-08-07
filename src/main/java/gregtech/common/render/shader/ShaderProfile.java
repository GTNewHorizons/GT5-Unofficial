package gregtech.common.render.shader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL32;
import org.lwjgl.opengl.GLContext;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.DefaultArtifactVersion;
import gregtech.api.enums.Mods;

public enum ShaderProfile {

    LEGACY(""),
    ANGELICA_CORE(".330"); // 3.3 core + GLSM caching

    static final Logger LOG = LogManager.getLogger("ShaderAPI");

    /* Force rendering through the 120 variants. */
    private static final String FORCE_LEGACY = "gt.render.forceLegacy";

    private static final String MIN_ANGELICA = "2.1";

    private static ShaderProfile preferred;
    private static Boolean angelicaSupported;

    private final String suffix;

    ShaderProfile(String suffix) {
        this.suffix = suffix;
    }

    public String suffix() {
        return suffix;
    }

    public static void init() {
        if (preferred != null) return;
        preferred = choose();
        LOG.info("Preferred shader profile: {}", preferred);
    }

    public static ShaderProfile preferred() {
        if (preferred == null) throw new IllegalStateException("ShaderProfile.init() has not run");
        return preferred;
    }

    private static ShaderProfile choose() {
        if (Boolean.getBoolean(FORCE_LEGACY)) {
            LOG.info("Core profile disabled by jvm option");
            return LEGACY;
        }
        if (!angelicaSupported()) return LEGACY;
        if (!GLContext.getCapabilities().OpenGL32) {
            LOG.info("Core profile unavailable: driver reports < GL 3.2");
            return LEGACY;
        }
        final int mask = GL11.glGetInteger(GL32.GL_CONTEXT_PROFILE_MASK);
        if ((mask & GL32.GL_CONTEXT_CORE_PROFILE_BIT) == 0) {
            LOG.info("Core profile unavailable - compatibility context detected");
            return LEGACY;
        }
        return ANGELICA_CORE;
    }

    private static boolean angelicaSupported() {
        if (angelicaSupported == null) angelicaSupported = checkAngelica();
        return angelicaSupported;
    }

    private static boolean checkAngelica() {
        if (!Mods.Angelica.isModLoaded()) {
            LOG.info("Angelica is not loaded");
            return false;
        }
        final ArtifactVersion found = Loader.instance()
            .getIndexedModList()
            .get(Mods.ModIDs.ANGELICA)
            .getProcessedVersion();
        if (found.compareTo(new DefaultArtifactVersion(MIN_ANGELICA)) < 0) {
            LOG.info("Angelica {} version lower than required {}", found.getVersionString(), MIN_ANGELICA);
            return false;
        }
        return true;
    }
}

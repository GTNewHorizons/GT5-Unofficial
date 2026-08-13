package gregtech.common.render.shader;

import static gregtech.GTLoggers.GT_SHADER_LOGGER;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormat;
import com.gtnewhorizon.gtnhlib.client.renderer.vertex.VertexFormatElement;

public final class ShaderRecipe {

    static final String MVP_UNIFORM = "u_MVP";

    enum ModelTransport {
        NONE,
        UNIFORM
    }

    private final String domain;
    private final String name;
    private final List<String> required = new ArrayList<>();
    private final List<String> coreOnly = new ArrayList<>();
    private final List<String> legacyOnly = new ArrayList<>();

    private final List<String> declared = new ArrayList<>();

    private ModelTransport transport = ModelTransport.NONE;
    private String modelUniform;
    private Uniform mvpToken;
    private Uniform modelToken;
    private String vertexAttrib;
    private int vertexCount;

    private final List<String> attributeNames = new ArrayList<>();
    private final List<VertexAttribute> attributeKinds = new ArrayList<>();
    private final List<ShaderConstant> constants = new ArrayList<>();

    private ShaderRecipe(String domain, String name) {
        this.domain = domain;
        this.name = name;
    }

    public static ShaderRecipe of(String domain, String name) {
        return new ShaderRecipe(domain, name);
    }

    public ShaderRecipe required(String... uniforms) {
        Collections.addAll(required, uniforms);
        addDeclared(uniforms);
        return this;
    }

    public ShaderRecipe coreOnly(String... uniforms) {
        Collections.addAll(coreOnly, uniforms);
        addDeclared(uniforms);
        return this;
    }

    public ShaderRecipe legacyOnly(String... uniforms) {
        Collections.addAll(legacyOnly, uniforms);
        addDeclared(uniforms);
        return this;
    }

    public ShaderRecipe constant(String uniform, float... values) {
        constants.add(ShaderConstant.of(uniform, values));
        return required(uniform);
    }

    public ShaderRecipe constantArray(String uniform, int components, float... values) {
        constants.add(ShaderConstant.array(uniform, components, values));
        return required(uniform);
    }

    public ShaderRecipe sampler(String uniform, int unit) {
        constants.add(ShaderConstant.sampler(uniform, unit));
        return required(uniform);
    }

    public ShaderRecipe modelUniform(String legacyName) {
        this.transport = ModelTransport.UNIFORM;
        this.modelUniform = legacyName;
        this.mvpToken = declare(MVP_UNIFORM);
        this.modelToken = declare(legacyName);
        return this;
    }

    public Uniform uniform(String name) {
        final int slot = declared.indexOf(name);
        if (slot < 0) {
            throw new IllegalArgumentException(
                id() + ": " + name + " was not declared; declared: " + String.join(", ", declared));
        }
        return new Uniform(this, name, slot);
    }

    private Uniform declare(String name) {
        addDeclared(name);
        return new Uniform(this, name, declared.size() - 1);
    }

    private void addDeclared(String... uniforms) {
        for (String uniform : uniforms) {
            if (declared.contains(uniform)) {
                throw new IllegalArgumentException(id() + ": " + uniform + " is declared twice");
            }
            declared.add(uniform);
        }
    }

    public ShaderRecipe attribute(String name, VertexAttribute kind) {
        attributeNames.add(name);
        attributeKinds.add(kind);
        return this;
    }

    /**
     * Declares a draw of {@code vertexCount} vertices - core draws it attributeless, legacy feeds the index
     * through {@code attrib}, which the 120 variant must declare.
     */
    public ShaderRecipe attributeless(String attrib, int vertexCount) {
        this.vertexAttrib = attrib;
        this.vertexCount = vertexCount;
        return this;
    }

    /**
     * Builds the shader in the preferred profile, falling back to legacy if the modern variant does not link or
     * does not match this declaration. Never returns null; check {@link ShaderHandle#isValid()}.
     */
    public ShaderHandle bake() {
        if (vertexAttrib == null && attributeNames.isEmpty()) {
            throw new IllegalStateException(id() + ": declare either attributeless() or attribute()");
        }

        final ShaderProfile preferred = ShaderProfile.preferred();
        ShaderHandle handle = tryBake(preferred);
        if (handle == null && preferred != ShaderProfile.LEGACY) {
            GT_SHADER_LOGGER.error("Shader {}: falling back to the legacy variant", id());
            handle = tryBake(ShaderProfile.LEGACY);
        }
        if (handle == null) {
            GT_SHADER_LOGGER.error("Shader {}: unavailable in every profile, it will not render", id());
            return ShaderHandle.invalid(this);
        }
        return handle;
    }

    private ShaderHandle tryBake(ShaderProfile profile) {
        final String vert = "shaders/" + name + ".vert" + profile.suffix() + ".glsl";
        final String frag = "shaders/" + name + ".frag" + profile.suffix() + ".glsl";

        final ShaderProgram program = new ShaderProgram(domain, vert, frag);
        if (program.getProgram() == 0) {
            GT_SHADER_LOGGER.error(
                "Shader {} [{}]: {} + {} did not compile, link or validate",
                id(),
                profile,
                vert,
                frag);
            program.close();
            return null;
        }

        final int[] slots = new int[declared.size()];
        Arrays.fill(slots, -1);
        if (!resolve(profile, program.getProgram(), slots)) {
            program.close();
            return null;
        }

        final VertexFormat format = buildVertexFormat(profile, program.getProgram());
        if (!attributeNames.isEmpty() && format == null) {
            program.close();
            return null;
        }

        final ShaderMesh mesh;
        if (vertexAttrib == null) {
            mesh = null;
        } else {
            mesh = profile == ShaderProfile.ANGELICA_CORE ? new CoreMesh(vertexCount)
                : new LegacyMesh(program.getProgram(), vertexAttrib, vertexCount);
        }
        return new ShaderHandle(this, profile, program, slots, mesh, format);
    }

    private VertexFormat buildVertexFormat(ShaderProfile profile, int program) {
        if (attributeNames.isEmpty()) return null;

        final VertexFormatElement[] elements = new VertexFormatElement[attributeNames.size()];
        for (int i = 0; i < elements.length; i++) {
            final String name = attributeNames.get(i);
            final int location = GL20.glGetAttribLocation(program, name);
            if (location < 0) {
                GT_SHADER_LOGGER
                    .error("Shader {} [{}]: vertex attribute {} is missing or unused", id(), profile, name);
                return null;
            }
            final VertexAttribute kind = attributeKinds.get(i);
            elements[i] = new VertexFormatElement(
                location,
                kind.type,
                VertexFormatElement.Usage.GENERIC,
                kind.count,
                kind.vertexBit,
                kind.writer,
                kind.padding);
        }
        return new VertexFormat(elements);
    }

    private boolean resolve(ShaderProfile profile, int program, int[] out) {
        boolean ok = true;

        for (String uniform : required) {
            final int location = GL20.glGetUniformLocation(program, uniform);
            out[declared.indexOf(uniform)] = location;
            if (location < 0) {
                GT_SHADER_LOGGER
                    .error("Shader {} [{}]: required uniform {} is missing or unused", id(), profile, uniform);
                ok = false;
            }
        }

        for (String uniform : coreOnly) {
            ok &= expectOnlyIn(profile, program, uniform, ShaderProfile.ANGELICA_CORE, out);
        }
        for (String uniform : legacyOnly) {
            ok &= expectOnlyIn(profile, program, uniform, ShaderProfile.LEGACY, out);
        }

        if (transport != ModelTransport.NONE) {
            ok &= expectOnlyIn(profile, program, MVP_UNIFORM, ShaderProfile.ANGELICA_CORE, out);
        }
        if (modelUniform != null) {
            ok &= expectOnlyIn(profile, program, modelUniform, ShaderProfile.LEGACY, out);
        }

        return ok;
    }

    private boolean expectOnlyIn(ShaderProfile profile, int program, String uniform, ShaderProfile owner, int[] out) {
        final int location = GL20.glGetUniformLocation(program, uniform);
        out[declared.indexOf(uniform)] = location;

        if (profile == owner) {
            if (location < 0) {
                GT_SHADER_LOGGER.error("Shader {} [{}]: uniform {} is missing or unused", id(), profile, uniform);
                return false;
            }
        } else if (location >= 0) {
            GT_SHADER_LOGGER.error(
                "Shader {} [{}]: uniform {} was declared {}-only but this variant uses it too",
                id(),
                profile,
                uniform,
                owner);
            return false;
        }
        return true;
    }

    List<ShaderConstant> constants() {
        return constants;
    }

    VertexAttribute[] layout() {
        return attributeKinds.toArray(new VertexAttribute[0]);
    }

    ModelTransport transport() {
        return transport;
    }

    String modelUniform() {
        return modelUniform;
    }

    String id() {
        return domain + ":" + name;
    }

    Uniform mvpToken() {
        return mvpToken;
    }

    Uniform modelToken() {
        return modelToken;
    }

    int declaredCount() {
        return declared.size();
    }

    int slotOf(String uniform) {
        return declared.indexOf(uniform);
    }
}

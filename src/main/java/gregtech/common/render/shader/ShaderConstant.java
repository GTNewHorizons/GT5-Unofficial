package gregtech.common.render.shader;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL20;

abstract class ShaderConstant {

    final String name;

    private ShaderConstant(String name) {
        this.name = name;
    }

    abstract void apply(int location);

    static ShaderConstant of(String name, float[] values) {
        if (values.length < 1 || values.length > 4) {
            throw new IllegalArgumentException(name + ": a scalar constant is 1 to 4 components, not " + values.length);
        }
        return new Scalar(name, values);
    }

    static ShaderConstant array(String name, int components, float[] values) {
        if (components < 1 || components > 4) {
            throw new IllegalArgumentException(name + ": an array element is 1 to 4 components, not " + components);
        }
        if (values.length == 0 || values.length % components != 0) {
            throw new IllegalArgumentException(
                name + ": " + values.length + " values do not divide into vec" + components + " elements");
        }
        return new Array(name, components, values);
    }

    static ShaderConstant sampler(String name, int unit) {
        return new Sampler(name, unit);
    }

    private static final class Scalar extends ShaderConstant {

        private final float[] values;

        private Scalar(String name, float[] values) {
            super(name);
            this.values = values.clone();
        }

        @Override
        void apply(int location) {
            switch (values.length) {
                case 1 -> GL20.glUniform1f(location, values[0]);
                case 2 -> GL20.glUniform2f(location, values[0], values[1]);
                case 3 -> GL20.glUniform3f(location, values[0], values[1], values[2]);
                default -> GL20.glUniform4f(location, values[0], values[1], values[2], values[3]);
            }
        }
    }

    private static final class Array extends ShaderConstant {

        private final int components;
        private final FloatBuffer values;

        private Array(String name, int components, float[] values) {
            super(name);
            this.components = components;
            this.values = BufferUtils.createFloatBuffer(values.length);
            this.values.put(values)
                .flip();
        }

        @Override
        void apply(int location) {
            switch (components) {
                case 1 -> GL20.glUniform1(location, values);
                case 2 -> GL20.glUniform2(location, values);
                case 3 -> GL20.glUniform3(location, values);
                default -> GL20.glUniform4(location, values);
            }
        }
    }

    private static final class Sampler extends ShaderConstant {

        private final int unit;

        private Sampler(String name, int unit) {
            super(name);
            this.unit = unit;
        }

        @Override
        void apply(int location) {
            GL20.glUniform1i(location, unit);
        }
    }
}

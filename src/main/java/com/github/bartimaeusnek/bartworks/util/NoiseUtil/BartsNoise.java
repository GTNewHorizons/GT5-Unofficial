/*
 * Copyright (c) 2018-2019 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.util.NoiseUtil;

import com.github.bartimaeusnek.bartworks.API.INoiseGen;
import com.github.bartimaeusnek.bartworks.util.MathUtils;
import gregtech.api.objects.XSTR;
import java.util.Random;

public class BartsNoise implements INoiseGen {
    public void setUsePhase(boolean usePhase) {
        this.usePhase = usePhase;
    }

    public void setRandom(Random random) {
        this.random = random;
    }

    boolean usePhase;
    int octaves;
    double frequency;
    double amplitude;
    long seed;
    Random random;

    public enum NoiseColor {
        Red(-1),
        Pink(-0.5),
        White(0),
        Blue(0.5),
        Violet(1);

        NoiseColor(double num) {
            this.ampl = num;
        }

        double ampl;

        public BartsNoise getColoredNoise() {
            BartsNoise noise = new BartsNoise();
            noise.setAmplitude(this.ampl);
            return noise;
        }
    }

    public BartsNoise(int octaves, double frequency, double amplitude, long seed) {
        this.octaves = octaves;
        this.frequency = frequency;
        this.amplitude = amplitude;
        this.seed = seed;
        this.random = new XSTR(seed);
    }

    public BartsNoise() {
        this.seed = new XSTR().nextLong();
        this.random = new XSTR(this.seed);
        this.octaves = 1;
        this.frequency = this.random.nextGaussian();
        this.amplitude = this.random.nextGaussian();
    }

    public BartsNoise(long seed) {
        this.seed = seed;
        this.random = new XSTR(seed);
        this.octaves = 1;
        this.frequency = this.random.nextGaussian();
        this.amplitude = this.random.nextGaussian();
    }

    public BartsNoise copy() {
        return new BartsNoise(this.octaves, this.frequency, this.amplitude, this.seed);
    }

    public BartsNoise copy(long seed) {
        return new BartsNoise(this.octaves, this.frequency, this.amplitude, seed);
    }
    //    public static void main(String[] args) throws IOException {
    //
    //        BartsNoise noiseGen1 = new BartsNoise(2,0.005F,1D,System.nanoTime());
    ////        BartsNoise noiseGen2 = new BartsNoise(1,0.002F,-1D,System.nanoTime());
    ////        BartsNoise noiseGen3 = new BartsNoise(1,0.002F,-1D,System.nanoTime());
    //
    //        noiseGen1.setUsePhase(false);
    ////        noiseGen2.setUsePhase(false);
    ////        noiseGen3.setUsePhase(false);
    //
    //
    //        BufferedImage image = new BufferedImage(640, 640, BufferedImage.TYPE_INT_RGB);
    //
    //        for (int chunkX = 0; chunkX < 40; chunkX++) {
    //            for (int chunkZ = 0; chunkZ < 40; chunkZ++) {
    //                for (int x = 0; x < 16; ++x) {
    //                    for (int z = 0; z < 16; ++z) {
    //                        double d = noiseGen1.getNoise(x + chunkX * 16, z + chunkZ * 16) * 2D;
    ////                        double d2 = noiseGen2.getNoise(x + chunkX * 16, z + chunkZ * 16) * 2D;
    ////                        double d3 = (noiseGen3.getNoise(x + chunkX * 16, z + chunkZ * 16) - 0.5D);
    ////                        d3 *= 2;
    //                        double yDev;
    ////                        if (d3 < 0.0D) {
    ////                            yDev = d;
    ////                        } else if (d3 > 1.0D) {
    ////                            yDev = d2;
    ////                        } else {
    //                            yDev = d*4;
    ////                        }
    //                        yDev=wrap(yDev,1);
    //                        image.setRGB(x + chunkX * 16,z + chunkZ * 16,new
    // Color((float)(1f*yDev),(float)(1f*yDev),(float)(1f*yDev)).getRGB());
    //                    }
    //                }
    //            }
    //        }
    //        File file = new File("myimage.png");
    //        ImageIO.write(image, "png", file);
    ////        BartsNoise redNoise = NoiseColor.Blue.getColoredNoise();
    ////        redNoise.setOctaves(1);
    ////        redNoise.setFrequency(0.05F);
    ////        for (int i = 40; i < 50; i++) {
    ////            String line = "";
    ////            for (int j = 0; j < 10; j++) {
    ////                double num = (redNoise.getNoise(i,j)-0.5D);
    ////                line += num+",";
    ////            }
    ////            System.out.println(line);
    ////        }
    //    }

    public double getCosNoise(double x, double y) {
        double pr = x * this.frequency;
        double r1 = Math.cos(pr);
        if (r1 < 0) r1 = Math.abs(r1);
        double result = Math.pow(r1, this.amplitude);
        double pr2 = y * this.frequency;
        double r2 = Math.cos(pr2);
        if (r2 < 0) r2 = Math.abs(r2);
        double result2 = Math.pow(r2, this.amplitude);
        result *= result2;
        if (result == Double.POSITIVE_INFINITY) result = Double.MAX_VALUE;
        if (result == Double.NEGATIVE_INFINITY) result = Double.MIN_VALUE;
        return MathUtils.wrap(result, 1D);
    }

    double getNonOctavedNoise(double x, double y) {
        double phase = SimplexNoise.noise(
                Math.pow(x * this.frequency, this.amplitude), Math.pow(y * this.frequency, this.amplitude));
        return MathUtils.wrap(phase, 1);
    }

    public double getNeighbouringNoise(int x, int y) {
        return (this.getNoiseSingle(x - 1, y - 1)
                        + this.getNoiseSingle(x, y - 1)
                        + this.getNoiseSingle(x - 1, y)
                        + this.getNoiseSingle(x + 1, y)
                        + this.getNoiseSingle(x, y + 1)
                        + this.getNoiseSingle(x + 1, y + 1)
                        + this.getNoiseSingle(x - 1, y + 1)
                        + this.getNoiseSingle(x + 1, y - 1))
                / 8;
    }

    public double getNoiseSingle(int x, int y) {
        double result = 0;
        for (double i = 1; i <= this.octaves; i++) {
            result += 1d / i * this.getNonOctavedNoise(i * x, i * y);
        }
        return result;
    }

    @Override
    public double getNoise(int x, int y) {
        double result = 0;
        for (double i = 1; i <= this.octaves; i++) {
            result += 1d / i * this.getNonOctavedNoise(i * x, y);
        }
        // result = (this.getNeighbouringNoise(x,y)+result)/2;
        return MathUtils.wrap(result, 1D);
    }

    @Override
    public double[][] getNoiseForRegion(int xStart, int zStart, int xEnd, int zEnd) {
        //        double[][] results = new double[Math.abs(xEnd)-Math.abs(xStart)][Math.abs(zEnd)-Math.abs(zStart)];
        //        for (int i = xStart; i < xEnd; i++) {
        //            for (int j = zStart; j < zEnd; j++) {
        //                results
        //            }
        //        }
        return new double[0][0];
    }

    @Override
    public void setAmplitude(double amplitude) {
        this.amplitude = amplitude;
    }

    @Override
    public void setOctaves(int octaves) {
        this.octaves = octaves;
    }

    @Override
    public void setFrequency(double freq) {
        this.frequency = freq;
    }

    @Override
    public void setSeed(long seed) {
        this.seed = seed;
    }
}

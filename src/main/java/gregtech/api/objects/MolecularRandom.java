package gregtech.api.objects;

import java.util.Random;

/**
 * A standard {@link Random}, but without the atomics and synchronization.
 * Useful in cases where you need to swap out a random instance without changing its results.
 */
public class MolecularRandom extends Random {

    private long seed;
    private double nextNextGaussian;
    private boolean haveNextNextGaussian;

    public MolecularRandom(long var1) {
        this.haveNextNextGaussian = false;
        this.seed = initialScramble(var1);
    }

    private static long initialScramble(long var0) {
        return (var0 ^ 25214903917L) & 281474976710655L;
    }

    public void setSeed(long var1) {
        this.seed = initialScramble(var1);
        this.haveNextNextGaussian = false;
    }

    protected int next(int var1) {
        seed = seed * 25214903917L + 11L & 281474976710655L;

        return (int) (seed >>> 48 - var1);
    }

    public void nextBytes(byte[] var1) {
        int var2 = 0;
        int var3 = var1.length;

        while (var2 < var3) {
            int var4 = this.nextInt();

            for (int var5 = Math.min(var3 - var2, 4); var5-- > 0; var4 >>= 8) {
                var1[var2++] = (byte) var4;
            }
        }

    }

    final long internalNextLong(long var1, long var3) {
        long var5 = this.nextLong();
        if (var1 < var3) {
            long var7 = var3 - var1;
            long var9 = var7 - 1L;
            if ((var7 & var9) == 0L) {
                var5 = (var5 & var9) + var1;
            } else if (var7 > 0L) {
                for (long var11 = var5 >>> 1; var11 + var9 - (var5 = var11 % var7)
                    < 0L; var11 = this.nextLong() >>> 1) {}

                var5 += var1;
            } else {
                while (var5 < var1 || var5 >= var3) {
                    var5 = this.nextLong();
                }
            }
        }

        return var5;
    }

    final int internalNextInt(int var1, int var2) {
        if (var1 >= var2) {
            return this.nextInt();
        } else {
            int var3 = var2 - var1;
            if (var3 > 0) {
                return this.nextInt(var3) + var1;
            } else {
                int var4;
                do {
                    do {
                        var4 = this.nextInt();
                    } while (var4 < var1);
                } while (var4 >= var2);

                return var4;
            }
        }
    }

    final double internalNextDouble(double var1, double var3) {
        double var5 = this.nextDouble();
        if (var1 < var3) {
            var5 = var5 * (var3 - var1) + var1;
            if (var5 >= var3) {
                var5 = Double.longBitsToDouble(Double.doubleToLongBits(var3) - 1L);
            }
        }

        return var5;
    }

    public int nextInt() {
        return this.next(32);
    }

    public int nextInt(int var1) {
        if (var1 <= 0) {
            throw new IllegalArgumentException("bound must be positive");
        } else {
            int var2 = this.next(31);
            int var3 = var1 - 1;
            if ((var1 & var3) == 0) {
                var2 = (int) ((long) var1 * (long) var2 >> 31);
            } else {
                for (int var4 = var2; var4 - (var2 = var4 % var1) + var3 < 0; var4 = this.next(31)) {}
            }

            return var2;
        }
    }

    public long nextLong() {
        return ((long) this.next(32) << 32) + (long) this.next(32);
    }

    public boolean nextBoolean() {
        return this.next(1) != 0;
    }

    public float nextFloat() {
        return (float) this.next(24) / 1.6777216E7F;
    }

    public double nextDouble() {
        return (double) (((long) this.next(26) << 27) + (long) this.next(27)) * 1.1102230246251565E-16;
    }

    public double nextGaussian() {
        if (this.haveNextNextGaussian) {
            this.haveNextNextGaussian = false;
            return this.nextNextGaussian;
        } else {
            double var1;
            double var3;
            double var5;
            do {
                do {
                    var1 = 2.0 * this.nextDouble() - 1.0;
                    var3 = 2.0 * this.nextDouble() - 1.0;
                    var5 = var1 * var1 + var3 * var3;
                } while (var5 >= 1.0);
            } while (var5 == 0.0);

            double var7 = StrictMath.sqrt(-2.0 * StrictMath.log(var5) / var5);
            this.nextNextGaussian = var3 * var7;
            this.haveNextNextGaussian = true;
            return var1 * var7;
        }
    }
}

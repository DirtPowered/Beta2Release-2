/*
 * Copyright (c) 2020 Dirt Powered
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

package com.github.dirtpowered.betatorelease.data.chunk;

import lombok.Getter;

import java.util.Arrays;

public class BetaChunk {
    private static final int WIDTH = 16, HEIGHT = 16, DEPTH = 128;

    @Getter
    private final int x, z;
    private final byte[] types, metaData, skyLight, blockLight;

    public BetaChunk(int x, int z) {
        this.x = x;
        this.z = z;
        this.types = new byte[WIDTH * HEIGHT * DEPTH];
        this.metaData = new byte[WIDTH * HEIGHT * DEPTH];
        this.skyLight = new byte[WIDTH * HEIGHT * DEPTH];
        this.blockLight = new byte[WIDTH * HEIGHT * DEPTH];

        Arrays.fill(blockLight, (byte) 15);
    }

    public void setBlock(int x, int y, int z, int type) {
        this.types[getIndex(x, y, z)] = (byte) type;
    }

    public void setMetaData(int x, int z, int y, int metaData) {
        this.metaData[getIndex(x, z, y)] = (byte) metaData;
    }

    private int getIndex(int x, int y, int z) {
        return x << 11 | z << 7 | y;
        //return (Math.min(16, x) * 16 + Math.min(16, z)) * 128 + Math.min(128, y);
    }

    public void setSkyLight(int x, int z, int y, int skyLight) {
        this.skyLight[getIndex(x, z, y)] = (byte) skyLight;
    }

    public void setBlockLight(int x, int z, int y, int blockLight) {
        this.blockLight[getIndex(x, z, y)] = (byte) blockLight;
    }

    private int getData(byte[] dest, int pos, byte[] skyLight) {
        for (int i = 0; i < skyLight.length; i += 2) {
            byte light1 = skyLight[i];
            byte light2 = skyLight[i + 1];
            dest[pos++] = (byte) ((light2 << 4) | light1);
        }
        return pos;
    }

    public byte[] serializeTileData() {
        byte[] dest = new byte[((WIDTH * HEIGHT * DEPTH * 5) / 2)];

        System.arraycopy(types, 0, dest, 0, types.length);

        int pos = types.length;

        pos = getData(dest, pos, metaData);
        pos = getData(dest, pos, skyLight);
        pos = getData(dest, pos, blockLight);
        return dest;
    }
}
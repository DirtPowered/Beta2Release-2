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

package com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.MapChunkPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PreChunkPacketData;
import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.github.dirtpowered.betatorelease.data.chunk.BetaChunk;
import com.github.dirtpowered.betatorelease.data.mappings.OldBlock;
import com.github.dirtpowered.betatorelease.data.mappings.PreFlatteningData;
import com.github.dirtpowered.betatorelease.network.client.ModernClient;
import com.github.dirtpowered.betatorelease.network.session.ServerSession;
import com.github.dirtpowered.betatorelease.network.translator.model.ModernToBeta;
import com.github.steveice10.mc.protocol.data.game.chunk.Chunk;
import com.github.steveice10.mc.protocol.data.game.chunk.Column;
import com.github.steveice10.mc.protocol.data.game.world.block.BlockState;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import org.pmw.tinylog.Logger;

public class ServerChunkDataTranslator implements ModernToBeta<ServerChunkDataPacket> {

    @Override
    public void translate(BetaToRelease main, ServerChunkDataPacket packet, ServerSession session, ModernClient modernClient) {
        Column chunkColumn = packet.getColumn();
        int xPosition = chunkColumn.getX();
        int zPosition = chunkColumn.getZ();

        BetaChunk betaChunk = new BetaChunk(xPosition, zPosition);

        session.sendPacket(new PreChunkPacketData(xPosition, zPosition, true /* allocate space */));
        //https://wiki.vg/index.php?title=Protocol&oldid=689#Pre-Chunk_.280x32.29

        Chunk[] chunks = packet.getColumn().getChunks();

        try {
            int index = 0;
            while (index < chunks.length) {
                Chunk chunk = chunks[index];

                if (chunk == null) {
                    index++;
                    continue;
                }

                final int columnCurrentHeight = index * 16; //(0-127)

                for (int x = 0; x < 16; x++) {
                    for (int y = 0; y < 16; y++) {
                        for (int z = 0; z < 16; z++) {
                            int internalId = chunk.get(x, y, z).getId();
                            OldBlock oldBlock = PreFlatteningData.fromInternalId(internalId);

                            betaChunk.setBlock(x, y + columnCurrentHeight, z, oldBlock.getBlockId());
                            betaChunk.setMetaData(x, y + columnCurrentHeight, z, oldBlock.getBlockData());
                            betaChunk.setSkyLight(x, y, z, 15);
                            betaChunk.setBlockLight(x, y, z, 15);
                        }
                    }
                }
                index++;
            }

            session.sendPacket(new MapChunkPacketData(
                    betaChunk.getX() * 16,
                    (short) 0,
                    betaChunk.getZ() * 16,
                    16,
                    128,
                    16,
                    betaChunk.serializeTileData()
            ));
        } catch (ArrayIndexOutOfBoundsException e) {
            Logger.error("Chunk error: {}", e);
        }
    }
}

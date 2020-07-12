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

package com.github.dirtpowered.betatorelease.network.protocol;

import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.AnimationPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.BlockItemSwitchPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.ChatPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.FlyingPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.HandshakePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerLookMovePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerLookPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerPositionPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.LoginPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.RespawnPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_8.data.ServerListPingPacketData;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.AnimationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.BlockItemSwitchTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.ChatTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.FlyingTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.HandshakeTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.LoginTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.PlayerLookMoveTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.PlayerLookTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.PlayerPositionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.RespawnTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B_1_9.ServerListPingTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerBlockChangeTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerChatTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerChunkDataTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerDifficultyTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerEntityDestroyTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerEntityPositionRotationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerEntityPositionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerEntityTeleportTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerEntityVelocityTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerJoinGameTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerNotifyClientTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerPlayerHealthTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerPlayerPositionRotationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerPlayerSetExperienceTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerRespawnTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerSpawnPositionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerUnloadChunkTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B_1_9.ServerUpdateTimeTranslator;
import com.github.dirtpowered.betatorelease.network.translator.registry.BetaToModernTranslatorRegistry;
import com.github.dirtpowered.betatorelease.network.translator.registry.ModernToBetaTranslatorRegistry;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerSetExperiencePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

public class B_1_9 {

    public B_1_9(BetaToModernTranslatorRegistry betaToModernTranslatorRegistry, ModernToBetaTranslatorRegistry modernToBetaTranslatorRegistry) {
        betaToModernTranslatorRegistry.registerTranslator(ServerListPingPacketData.class, new ServerListPingTranslator());
        betaToModernTranslatorRegistry.registerTranslator(HandshakePacketData.class, new HandshakeTranslator());
        betaToModernTranslatorRegistry.registerTranslator(LoginPacketData.class, new LoginTranslator());
        betaToModernTranslatorRegistry.registerTranslator(PlayerPositionPacketData.class, new PlayerPositionTranslator());
        betaToModernTranslatorRegistry.registerTranslator(PlayerLookMovePacketData.class, new PlayerLookMoveTranslator());
        betaToModernTranslatorRegistry.registerTranslator(ChatPacketData.class, new ChatTranslator());
        betaToModernTranslatorRegistry.registerTranslator(RespawnPacketData.class, new RespawnTranslator());
        betaToModernTranslatorRegistry.registerTranslator(BlockItemSwitchPacketData.class, new BlockItemSwitchTranslator());
        betaToModernTranslatorRegistry.registerTranslator(FlyingPacketData.class, new FlyingTranslator());
        betaToModernTranslatorRegistry.registerTranslator(AnimationPacketData.class, new AnimationTranslator());
        betaToModernTranslatorRegistry.registerTranslator(PlayerLookPacketData.class, new PlayerLookTranslator());

        modernToBetaTranslatorRegistry.registerTranslator(ServerJoinGamePacket.class, new ServerJoinGameTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerSpawnPositionPacket.class, new ServerSpawnPositionTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerPlayerPositionRotationPacket.class, new ServerPlayerPositionRotationTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerUpdateTimePacket.class, new ServerUpdateTimeTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerChatPacket.class, new ServerChatTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityTeleportPacket.class, new ServerEntityTeleportTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityPositionPacket.class, new ServerEntityPositionTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityPositionRotationPacket.class, new ServerEntityPositionRotationTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityVelocityPacket.class, new ServerEntityVelocityTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerPlayerHealthPacket.class, new ServerPlayerHealthTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityDestroyPacket.class, new ServerEntityDestroyTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerUnloadChunkPacket.class, new ServerUnloadChunkTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerChunkDataPacket.class, new ServerChunkDataTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerRespawnPacket.class, new ServerRespawnTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerPlayerSetExperiencePacket.class, new ServerPlayerSetExperienceTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerDifficultyPacket.class, new ServerDifficultyTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerNotifyClientPacket.class, new ServerNotifyClientTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerBlockChangePacket.class, new ServerBlockChangeTranslator());
    }
}

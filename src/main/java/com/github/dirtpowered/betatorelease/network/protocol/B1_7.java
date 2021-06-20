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
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.EntityActionPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.FlyingPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.HandshakePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.LoginPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerLookMovePacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerLookPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.PlayerPositionPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.RespawnPacketData;
import com.github.dirtpowered.betaprotocollib.packet.Version_B1_7.data.UseEntityPacketData;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.AnimationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.BlockItemSwitchTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.ChatTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.EntityActionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.FlyingTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.HandshakeTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.LoginTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.PlayerLookMoveTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.PlayerLookTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.PlayerPositionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.RespawnTranslator;
import com.github.dirtpowered.betatorelease.network.translator.betatomodern.B1_7.UseEntityPacketDataTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerBlockChangeTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerChatTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerChunkDataTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerDifficultyTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityAnimationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityDestroyTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityMetadataTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityPositionRotationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityPositionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityStatusTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityTeleportTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerEntityVelocityTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerJoinGameTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerNotifyClientTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerPlayerHealthTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerPlayerListEntryTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerPlayerPositionRotationTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerRespawnTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerSetSlotTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerSpawnEntityTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerSpawnLivingEntityTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerSpawnParticleTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerSpawnPlayerTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerSpawnPositionTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerUnloadChunkTranslator;
import com.github.dirtpowered.betatorelease.network.translator.moderntobeta.B1_7.ServerUpdateTimeTranslator;
import com.github.dirtpowered.betatorelease.network.translator.registry.BetaToModernTranslatorRegistry;
import com.github.dirtpowered.betatorelease.network.translator.registry.ModernToBetaTranslatorRegistry;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerDifficultyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityAnimationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityDestroyPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityMetadataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityStatusPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityTeleportPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.ServerEntityVelocityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerHealthPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.player.ServerPlayerPositionRotationPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnLivingEntityPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.entity.spawn.ServerSpawnPlayerPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.window.ServerSetSlotPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerBlockChangePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerChunkDataPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerNotifyClientPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnParticlePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerSpawnPositionPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUnloadChunkPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.world.ServerUpdateTimePacket;

public class B1_7 {

    public B1_7(BetaToModernTranslatorRegistry betaToModernTranslatorRegistry, ModernToBetaTranslatorRegistry modernToBetaTranslatorRegistry) {
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
        betaToModernTranslatorRegistry.registerTranslator(EntityActionPacketData.class, new EntityActionTranslator());
        betaToModernTranslatorRegistry.registerTranslator(UseEntityPacketData.class, new UseEntityPacketDataTranslator());

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
        modernToBetaTranslatorRegistry.registerTranslator(ServerDifficultyPacket.class, new ServerDifficultyTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerNotifyClientPacket.class, new ServerNotifyClientTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerBlockChangePacket.class, new ServerBlockChangeTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerSpawnLivingEntityPacket.class, new ServerSpawnLivingEntityTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityAnimationPacket.class, new ServerEntityAnimationTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityStatusPacket.class, new ServerEntityStatusTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerSpawnPlayerPacket.class, new ServerSpawnPlayerTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerSetSlotPacket.class, new ServerSetSlotTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerPlayerListEntryPacket.class, new ServerPlayerListEntryTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerSpawnParticlePacket.class, new ServerSpawnParticleTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerEntityMetadataPacket.class, new ServerEntityMetadataTranslator());
        modernToBetaTranslatorRegistry.registerTranslator(ServerSpawnEntityPacket.class, new ServerSpawnEntityTranslator());
    }
}

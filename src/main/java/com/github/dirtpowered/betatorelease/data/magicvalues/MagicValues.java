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

package com.github.dirtpowered.betatorelease.data.magicvalues;

import com.github.dirtpowered.betatorelease.data.magicvalues.model.DataHolder;
import com.github.steveice10.mc.protocol.data.game.entity.EntityStatus;
import com.github.steveice10.mc.protocol.data.game.entity.player.Animation;
import com.github.steveice10.mc.protocol.data.game.entity.player.GameMode;
import com.github.steveice10.mc.protocol.data.game.entity.type.EntityType;
import com.github.steveice10.mc.protocol.data.game.setting.Difficulty;
import com.github.steveice10.mc.protocol.data.game.world.notify.ClientNotification;

public class MagicValues {
    private static final AnimationMap animationMap;
    private static final EntityStatusMap entityStatusMap;
    private static final DifficultyMap difficultyMap;
    private static final GameModeMap gamemodeMap;
    private static final ClientNotificationMap clientNotificationMap;
    private static final EntityTypeMap entityTypeMap;

    static {
        animationMap = new AnimationMap();
        entityStatusMap = new EntityStatusMap();
        difficultyMap = new DifficultyMap();
        gamemodeMap = new GameModeMap();
        clientNotificationMap = new ClientNotificationMap();
        entityTypeMap = new EntityTypeMap();
    }

    public static int getAnimationId(Animation animation) {
        return animationMap.getFromNamespace(animation);
    }

    public static int getEntityStatusId(EntityStatus entityStatus) {
        return entityStatusMap.getFromNamespace(entityStatus);
    }

    public static int getDifficultyId(Difficulty difficulty) {
        return difficultyMap.getFromNamespace(difficulty);
    }

    public static int getGameModeId(GameMode gameMode) {
        return gamemodeMap.getFromNamespace(gameMode);
    }

    public static int getClientNotificationId(ClientNotification clientNotification) {
        return clientNotificationMap.getFromNamespace(clientNotification);
    }

    public static int getEntityTypeId(EntityType entityType) {
        return entityTypeMap.getFromNamespace(entityType);
    }

    static class AnimationMap extends DataHolder<Animation> {

        AnimationMap() {
            add(Animation.SWING_ARM, 1);
            add(Animation.DAMAGE, 2);
            add(Animation.LEAVE_BED, 3);
        }
    }


    static class EntityStatusMap extends DataHolder<EntityStatus> {

        EntityStatusMap() {
            add(EntityStatus.LIVING_HURT, 2);
            add(EntityStatus.LIVING_DEATH, 3);
            add(EntityStatus.TAMEABLE_TAMING_FAILED, 6);
            add(EntityStatus.TAMEABLE_TAMING_SUCCEEDED, 7);
            add(EntityStatus.WOLF_SHAKE_WATER, 8);
        }
    }

    static class DifficultyMap extends DataHolder<Difficulty> {

        DifficultyMap() {
            add(Difficulty.PEACEFUL, 0);
            add(Difficulty.EASY, 1);
            add(Difficulty.NORMAL, 2);
            add(Difficulty.HARD, 3);
        }
    }

    static class GameModeMap extends DataHolder<GameMode> {

        GameModeMap() {
            add(GameMode.SURVIVAL, 0);
            add(GameMode.CREATIVE, 1);
            add(GameMode.ADVENTURE, 0);
            add(GameMode.SPECTATOR, 1);
        }
    }

    static class ClientNotificationMap extends DataHolder<ClientNotification> {

        ClientNotificationMap() {
            add(ClientNotification.INVALID_BED, 0);
            add(ClientNotification.START_RAIN, 1);
            add(ClientNotification.STOP_RAIN, 2);
            add(ClientNotification.CHANGE_GAMEMODE, 3);
            add(ClientNotification.ENTER_CREDITS, 4);
        }
    }

    static class EntityTypeMap extends DataHolder<EntityType> {

        EntityTypeMap() {
            add(EntityType.PIG, 90);
            add(EntityType.SHEEP, 91);
            add(EntityType.COW, 92);
            add(EntityType.CHICKEN, 93);
            add(EntityType.SQUID, 94);
            add(EntityType.WOLF, 95);
            add(EntityType.CREEPER, 50);
            add(EntityType.SKELETON, 51);
            add(EntityType.SPIDER, 52);
            add(EntityType.GIANT, 53);
            add(EntityType.ZOMBIE, 54);
            add(EntityType.SLIME, 55);
            add(EntityType.GHAST, 56);
            add(EntityType.ZOMBIE_PIGMAN, 57);

            //entity replacements
            add(EntityType.DROWNED, 54);
            add(EntityType.STRAY, 54);
            add(EntityType.GUARDIAN, 94);
            add(EntityType.PILLAGER, 51);

            //TODO: add more
        }
    }
}

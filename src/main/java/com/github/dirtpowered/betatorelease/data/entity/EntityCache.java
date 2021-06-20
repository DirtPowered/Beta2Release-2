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

package com.github.dirtpowered.betatorelease.data.entity;

import lombok.Getter;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class EntityCache {
    private final Map<UUID, Entity> players = new ConcurrentHashMap<>();
    private final Map<Integer, Entity> entities = new ConcurrentHashMap<>();

    public void addPlayerEntity(UUID entityUID, Entity entity) {
        players.put(entityUID, entity);
    }

    public void addEntity(int entityId, Entity entity) {
        entities.put(entityId, entity);
    }

    public Entity getEntityById(int entityId) {
        return entities.get(entityId);
    }

    public Entity getPlayerEntityByUUID(UUID uuid) {
        return players.get(uuid);
    }

    public void removeEntity(int entityId) {
        entities.remove(entityId);
    }

    public void removePlayerEntity(UUID entityUID) {
        players.remove(entityUID);
    }

    public void clear() {
        players.clear();
        entities.clear();
    }
}

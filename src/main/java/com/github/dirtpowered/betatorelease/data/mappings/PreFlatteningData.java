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

package com.github.dirtpowered.betatorelease.data.mappings;

import com.github.dirtpowered.betatorelease.BetaToRelease;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PreFlatteningData {
    private static BetaToRelease main;
    private static Map<Integer, OldBlock> newToOldMap = new HashMap<>();
    private final static OldBlock DEFAULT = new OldBlock(1, 0);

    public static void setInstance(BetaToRelease betaToRelease) {
        main = betaToRelease;
    }

    public static void loadMappings() {
        main.getLogger().info("loading 1.15.2 -> 1.0 mappings");

        File f;
        try {

            Path p = Paths.get("src/main/resources/blocks.json");
            if (!Files.exists(p)) {

                f = new File(PreFlatteningData.class.getResource("/blocks.json").toURI());
            } else {
                f = p.toFile();
            }

            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(new FileReader(f));
            JsonObject json = jsonElement.getAsJsonObject();

            JsonArray blockStatesArray = json.getAsJsonArray("blockstates");
            int count = 0;

            for (JsonElement block : blockStatesArray) {
                JsonObject legacyData = block.getAsJsonObject();

                for (Map.Entry<String, JsonElement> legacyEntry : legacyData.entrySet()) {
                    JsonElement data = legacyEntry.getValue();

                    int internalId = Integer.parseInt(legacyEntry.getKey());
                    int blockId = data.getAsJsonObject().get("legacy_id").getAsInt();
                    int blockData = data.getAsJsonObject().get("legacy_data").getAsInt();

                    newToOldMap.put(internalId, new OldBlock(blockId, blockData));
                    count++;
                }
            }

            main.getLogger().info("loaded " + count + " blocks");
        } catch (IOException | URISyntaxException e) {
            main.getLogger().error("unable to parse blocks.json");
        }
    }

    public static OldBlock fromInternalId(int internalId) {
        if (newToOldMap.containsKey(internalId)) {
            return newToOldMap.get(internalId);
        } else {
            main.getLogger().warning("Missing 1.15.2 -> 1.0 block mapping for ID: " + internalId);
            return DEFAULT;
        }
    }

    public static void cleanup() {
        newToOldMap.clear();
    }
}

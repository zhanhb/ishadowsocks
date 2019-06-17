/*
 * The MIT License
 *
 * Copyright 2015 zhanhb.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.github.zhanhb.ishadowsocks;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Slf4j
public class GuiConfigs {

    private static final Gson gson = new Gson();

    public static GuiConfigs parse(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return GuiConfigs.parse(reader);
        }
    }

    public static GuiConfigs parse(Reader reader) {
        return new GuiConfigs(gson.fromJson(reader, JsonObject.class));
    }

    private final JsonObject jsonObject;

    public GuiConfigs(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
    }

    public void writeTo(Writer writer) throws IOException {
        Objects.requireNonNull(writer);
        String json = gson.toJson(jsonObject);
        log.debug("gui-config.json: {}", json);
        writer.write(json);
    }

    public void writeTo(Path path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            writeTo(bw);
        }
    }

    void setConfigs(Collection<JsonObject> configs) {
        JsonArray array = new JsonArray(configs.size());
        for (JsonObject config : configs) {
            array.add(config);
        }
        jsonObject.add("configs", array);
    }

    JsonArray getConfigs() {
        return jsonObject.get("configs").getAsJsonArray();
    }

}

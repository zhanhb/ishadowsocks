/*
 * Copyright 2015 zhanhb.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.zhanhb.ishadowsocks;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Objects;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Data
@Slf4j
public class GuiConfigs {

    private static final Gson gson = new Gson();

    public static GuiConfigs parse(Path path) throws IOException {
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return GuiConfigs.parse(reader);
        }
    }

    public static GuiConfigs parse(Reader reader) {
        return gson.fromJson(reader, GuiConfigs.class);
    }

    private LinkedHashSet<Server> configs;
    private String strategy;
    private Integer index;
    private Boolean global;
    private Boolean enabled;
    private Boolean shareOverLan;
    private Boolean isDefault;
    private Integer localPort;
    private String pacUrl;
    private Boolean useOnlinePac;
    private Boolean availabilityStatistics;

    public void writeTo(Writer writer) throws IOException {
        Objects.requireNonNull(writer);
        String json = gson.toJson(this);
        log.debug("gui-config.json: {}", json);
        writer.write(json);
    }

    public void writeTo(Path path) throws IOException {
        try (BufferedWriter bw = Files.newBufferedWriter(path)) {
            writeTo(bw);
        }
    }
}

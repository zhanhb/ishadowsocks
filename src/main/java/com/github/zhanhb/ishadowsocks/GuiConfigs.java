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

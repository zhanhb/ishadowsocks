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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import static java.util.function.Function.identity;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 *
 * @author zhanhb
 */
public class Application {

    public static void main(String[] args) throws IOException {
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            WebClientOptions options = webClient.getOptions();
            options.setCssEnabled(false);
            options.setDoNotTrackEnabled(true);
            options.setDownloadImages(false);
            options.setGeolocationEnabled(false);
            options.setJavaScriptEnabled(false);
            options.setThrowExceptionOnFailingStatusCode(false);
            options.setThrowExceptionOnScriptError(false);

            HtmlPage page = webClient.getPage("https://free.ishadowx.org");

            Stream<JsonObject> parsed = page.querySelectorAll("#portfolio .portfolio-item")
                    .stream()
                    .map(node -> node.getTextContent().trim())
                    .map(Server::newServer)
                    .filter(Objects::nonNull)
                    .map(Server::toJsonObject);

            if (args.length > 0) {
                Path path = Paths.get(args[0]);
                GuiConfigs guiConfigs = GuiConfigs.parse(path);
                JsonArray configs = guiConfigs.getConfigs();
                Collection<JsonObject> servers
                        = Stream.concat(
                                StreamSupport.stream(Spliterators.spliterator(configs.iterator(),
                                        configs.size(), Spliterator.ORDERED), false)
                                        .map(JsonElement::getAsJsonObject),
                                parsed
                        )
                                .collect(Collectors.toMap(jo -> {
                                    return jo.get("server").getAsString() + ":" + jo.get("server_port").getAsString();
                                }, identity(), (a, b) -> b, LinkedHashMap::new))
                                .values();

                guiConfigs.setConfigs(servers);
                guiConfigs.writeTo(path);
            } else {
                parsed.forEach(System.out::println);
            }
        }
    }

}

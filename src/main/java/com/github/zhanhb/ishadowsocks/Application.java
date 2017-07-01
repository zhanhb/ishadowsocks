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

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebClientOptions;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;
import static java.util.function.Function.identity;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author zhanhb
 */
public class Application {

    public static void main(String[] args) throws IOException {
        try (WebClient webClient = new WebClient(BrowserVersion.CHROME)) {
            WebClientOptions options = webClient.getOptions();
            options.setThrowExceptionOnFailingStatusCode(false);
            options.setThrowExceptionOnScriptError(false);
            HtmlPage page = webClient.getPage("http://www.ishadow.info/");

            Path path = Paths.get("D:\\Program Files\\zhanhb\\ss\\gui-config.json");
            GuiConfigs guiConfigs = GuiConfigs.parse(path);
            Collection<Server> servers
                    = Stream.concat(
                            guiConfigs.getConfigs().stream(),
                            page.querySelectorAll("#free .row>div[class*='col-'][class*='-4']")
                            .stream()
                            .map(node -> node.getTextContent().trim())
                            .map(Server::newServer)
                            .filter(Objects::nonNull)
                    )
                    .collect(Collectors.toMap(identity(), identity(), (a, b) -> b, LinkedHashMap::new))
                    .values();

            guiConfigs.setConfigs(new LinkedHashSet<>(servers));
            guiConfigs.writeTo(path);
        }
    }
}

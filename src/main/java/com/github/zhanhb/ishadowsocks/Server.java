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

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author zhanhb
 */
@Data
@EqualsAndHashCode(of = {"server", "server_port"})
@Slf4j
public class Server {

    private static final Pattern HOST_PATTERN = Pattern.compile("(?i)IP Address:\\s*([a-z0-9](?:[a-z0-9-]*[a-z0-9])?+(?:\\.[a-z0-9](?:[a-z0-9-]*[a-z0-9])?+)++)");
    private static final Pattern PORT_PATTERN = Pattern.compile("(?i)Port:\\s*(\\d{2,5}+)");
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("(?i)Password:\\s*([^\\s]+)");
    private static final Pattern METHOD_PATTERN = Pattern.compile("(?i)Method:\\s*(table|rc4-md5|salsa20|chacha20|"
            + "(aes|camellia)-\\d+-(?:cfb|ctr|gcm)|"
            + "rc4|bf-cfb|salsa20|x?chacha20(?:-ietf(?:-poly1305)?)?)");

    public static Server newServer(String text) {
        try {
            return new Server(text);
        } catch (IllegalStateException e) {
            log.error("", e);
            return null;
        }
    }

    private final String server;
    private int server_port;
    private final String password;
    private final String method;
    private final String remarks;

    // gson can new a instance without a NoArgsConstructor.
    private Server(String text) {
        server = get(HOST_PATTERN, text).toLowerCase();
        server_port = Integer.parseInt(get(PORT_PATTERN, text));
        password = get(PASSWORD_PATTERN, text);
        method = get(METHOD_PATTERN, text).toLowerCase();
        remarks = server + ":" + server_port;
    }

    private String get(Pattern pattern, String text) {
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            // throw an exception if not found
            return matcher.group(1);
        }
        throw new IllegalStateException(String.format("No match found, text='%s', pattern='%s'", text, pattern));
    }

}

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

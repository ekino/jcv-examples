package com.ekino.oss.jcv.example.jcvcustomvalidatorexample.util;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

import org.apache.commons.io.IOUtils;

public final class ResourceLoader {

    public static String loadJson(String filename) {
        try {
            return IOUtils.resourceToString(
                Paths.get("/", filename).toString(),
                StandardCharsets.UTF_8
            );
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

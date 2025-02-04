package com.collett.github;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNull;

public abstract class AbstractSearchTest {
  protected String loadJson(final String filename) throws Exception {
    requireNonNull(filename, "filename");
    final URL resource = getClass().getResource("/json/" + filename);
    if (resource != null) {
      return Files.readString(Path.of(resource.toURI()));
    }
    throw new IllegalArgumentException("Resource not found: " + filename);
  }
}

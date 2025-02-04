package com.collett.github.modeladapter;

import com.collett.github.model.SearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

public final class JacksonSearchResponseAdapter implements SearchResponseAdapter {
  private final ObjectMapper mapper;
  private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
      .configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, true);

  public JacksonSearchResponseAdapter(final ObjectMapper mapper) {
    this.mapper = requireNonNull(mapper);
  }

  public JacksonSearchResponseAdapter() {
    this(DEFAULT_MAPPER);
  }

  @Override
  public SearchResponse adapt(String json) throws IOException {
    requireNonNull(json);
    try {
      final JsonNode root = mapper.readTree(json);
      return new SearchResponse(
          root.path("total_count").asInt(),
          items(root.path("items"))
      );
    } catch (final JsonProcessingException e) {
      throw new IOException("Failed to parse response", e);
    }
  }

  private List<SearchResponse.SearchItem> items(final JsonNode items) {
    requireNonNull(items, "items");
    if (!items.isArray()) {
      return Collections.emptyList();
    }
    return StreamSupport.stream(items.spliterator(), false)
        .map(this::item)
        .collect(Collectors.toList());
  }

  private SearchResponse.SearchItem item(final JsonNode node) {
    requireNonNull(node, "node");
    return new SearchResponse.SearchItem(
        node.path("name").asText(),
        node.path("full_name").asText(),
        node.path("description").asText(null),
        node.path("html_url").asText(),
        node.path("stargazers_count").asInt(),
        node.path("owner").path("login").asText(),
        node.path("visibility").asText(null),
        node.path("created_at").asText(),
        node.path("updated_at").asText(),
        node.path("archived").asBoolean(),
        node.path("fork").asBoolean(),
        topics(node.path("topics"))
    );
  }

  private List<String> topics(final JsonNode topics) {
    requireNonNull(topics, "topics");
    if (!topics.isArray()) {
      return Collections.emptyList();
    }
    return StreamSupport.stream(topics.spliterator(), false)
        .map(JsonNode::asText)
        .collect(Collectors.toList());
  }
}

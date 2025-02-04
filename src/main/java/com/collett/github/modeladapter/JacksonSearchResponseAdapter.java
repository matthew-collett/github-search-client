package com.collett.github.modeladapter;

import com.collett.github.model.SearchResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.Objects.requireNonNull;

public final class JacksonSearchResponseAdapter implements SearchResponseAdapter {
  private final ObjectMapper objectMapper;

  public JacksonSearchResponseAdapter(final ObjectMapper objectMapper) {
    this.objectMapper = requireNonNull(objectMapper);
  }

  public JacksonSearchResponseAdapter() {
    this(new ObjectMapper());
  }

  public SearchResponse adapt(final String jsonResponse) throws IOException {
    requireNonNull(jsonResponse, "jsonResponse");
    final JsonNode root = this.objectMapper.readTree(jsonResponse);
    int totalCount = root.get("total_count").asInt();
    final List<SearchResponse.SearchItem> items = StreamSupport
        .stream(root.get("items").spliterator(), false)
        .map(JacksonSearchResponseAdapter::toSearchItem)
        .collect(Collectors.toList());
    return new SearchResponse(totalCount, items);
  }

  private static SearchResponse.SearchItem toSearchItem(final JsonNode node) {
    requireNonNull(node, "node");
    return new SearchResponse.SearchItem(
        node.get("name").asText(),
        node.get("full_name").asText(),
        node.get("description").asText(null),
        node.get("html_url").asText(""),
        node.get("stargazers_count").asInt(),
        node.get("owner").get("login").asText(""),
        node.get("visibility").asText(null),
        node.get("created_at").asText(),
        node.get("updated_at").asText(),
        node.get("archived").asBoolean(),
        node.get("fork").asBoolean(),
        Optional.ofNullable(node.get("topics"))
            .map(JacksonSearchResponseAdapter::resolveTopics)
            .orElse(Collections.emptyList())
    );
  }

  private static List<String> resolveTopics(final JsonNode node) {
    requireNonNull(node, "node");
    return StreamSupport.stream(node.spliterator(), false)
        .map(JsonNode::asText)
        .collect(Collectors.toList());
  }
}

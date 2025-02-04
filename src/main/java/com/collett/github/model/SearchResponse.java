package com.collett.github.model;

import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

public record SearchResponse(Integer totalCount, List<SearchItem> items) {
  public SearchResponse {
    items = Collections.unmodifiableList(items);
  }

  public String toPrettyString() {
    return new GsonBuilder().setPrettyPrinting().create().toJson(this);
  }

  public record SearchItem(
      String name,
      String full_name,
      String description,
      String url,
      Integer stargazersCount,
      String owner,
      String visibility,
      String createdAt,
      String updatedAt,
      Boolean archived,
      Boolean fork,
      List<String> topics
  ) {}
}

package com.collett.github;

import com.collett.github.client.GitHubSearchClient;
import com.collett.github.model.SearchQuery;
import com.collett.github.model.SearchResponse;
import com.collett.github.modeladapter.JacksonSearchResponseAdapter;

import java.net.http.HttpClient;
import java.time.LocalDateTime;

public class Main {
  public static void main(final String... args) {
    final GitHubSearchClient client = GitHubSearchClient.builder()
        .withHttpClient(HttpClient.newHttpClient())
        .withAdapter(new JacksonSearchResponseAdapter())
        .build();

    final SearchQuery query = SearchQuery.builder()
        .withLanguage("go")
        .withCreatedAfter(LocalDateTime.of(2020, 1, 1, 0, 0))
        .withOwner("matthew-collett")
        .withIsFork(false)
        .withMinStars(0)
        .withIsArchived(false)
        .withVisibility(SearchQuery.Visibility.PUBLIC)
        .build();

    try {
      final SearchResponse response = client.searchRepositories(query);
      System.out.println(response.toPrettyString());
    } catch (final Exception e) {
      System.err.println(e.getMessage());
    }
  }
}

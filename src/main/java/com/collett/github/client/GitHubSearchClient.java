package com.collett.github.client;

import com.collett.github.model.SearchQuery;
import com.collett.github.model.SearchResponse;
import com.collett.github.modeladapter.SearchResponseAdapter;

import java.net.http.HttpClient;

public sealed interface GitHubSearchClient permits GitHubSearchClientImpl {
  HttpClient httpClient();

  String baseUrl();

  SearchResponseAdapter adapter();

  SearchResponse searchRepositories(final SearchQuery query) throws Exception;

  sealed interface Builder permits GitHubSearchClientImpl.Builder {
    Builder withHttpClient(final HttpClient httpClient);

    Builder withBaseUrl(final String baseUrl);

    Builder withAdapter(final SearchResponseAdapter adapter);

    GitHubSearchClient build();
  }

  static GitHubSearchClient defaultClient() {
    return builder().build();
  }

  static Builder builder() {
    return new GitHubSearchClientImpl.Builder();
  }
}
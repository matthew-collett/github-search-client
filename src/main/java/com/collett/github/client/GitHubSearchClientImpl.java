package com.collett.github.client;

import com.collett.github.model.SearchQuery;
import com.collett.github.model.SearchResponse;
import com.collett.github.modeladapter.SearchResponseAdapter;
import com.collett.github.modeladapter.JacksonSearchResponseAdapter;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import static java.util.Objects.requireNonNull;

final class GitHubSearchClientImpl implements GitHubSearchClient {
  private static final String DEFAULT_BASE_URL = "https://api.github.com/search/repositories";

  private final HttpClient httpClient;
  private final String baseUrl;
  private final SearchResponseAdapter adapter;

  private GitHubSearchClientImpl(final Builder builder) {
    requireNonNull(builder, "builder");
    this.httpClient = requireNonNull(builder.httpClient, "httpClient");
    this.baseUrl = requireNonNull(builder.baseUrl, "baseUrl");
    this.adapter = requireNonNull(builder.adapter, "adapter");
  }

  @Override
  public SearchResponse searchRepositories(final SearchQuery query) throws Exception {
    requireNonNull(query, "query");
    final URI uri = new URI(String.format("%s?q=%s", baseUrl, URLEncoder.encode(query.toString(), StandardCharsets.UTF_8)));
    final HttpRequest request = HttpRequest.newBuilder()
        .uri(uri)
        .header("Accept", "application/vnd.github.v3+json")
        .GET()
        .build();
    final HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    if (response.statusCode() != 200) {
      throw new Exception("Request failed: " + response.body());
    }
    return this.adapter.adapt(response.body());
  }

  @Override
  public HttpClient httpClient() {
    return this.httpClient;
  }

  @Override
  public String baseUrl() {
    return this.baseUrl;
  }

  @Override
  public SearchResponseAdapter adapter() {
    return this.adapter;
  }

  static final class Builder implements GitHubSearchClient.Builder {
    private HttpClient httpClient;
    private String baseUrl;
    private SearchResponseAdapter adapter;

    Builder() {
      this.httpClient = HttpClient.newHttpClient();
      this.baseUrl = DEFAULT_BASE_URL;
      this.adapter = new JacksonSearchResponseAdapter();
    }

    @Override
    public GitHubSearchClient.Builder withHttpClient(final HttpClient httpClient) {
      requireNonNull(httpClient, "httpClient");
      this.httpClient = httpClient;
      return this;
    }

    @Override
    public GitHubSearchClient.Builder withBaseUrl(String baseUrl) {
      requireNonNull(baseUrl, "baseUrl");
      this.baseUrl = baseUrl;
      return this;
    }

    @Override
    public GitHubSearchClient.Builder withAdapter(final SearchResponseAdapter adapter) {
      requireNonNull(adapter, "adapter");
      this.adapter = adapter;
      return this;
    }

    @Override
    public GitHubSearchClient build() {
      return new GitHubSearchClientImpl(this);
    }
  }
}
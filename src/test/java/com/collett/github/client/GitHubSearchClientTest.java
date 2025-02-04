package com.collett.github.client;

import com.collett.github.AbstractSearchTest;
import com.collett.github.model.SearchQuery;
import com.collett.github.model.SearchResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GitHubSearchClientTest extends AbstractSearchTest {
  private HttpClient httpClient;
  private HttpResponse<String> response;
  private GitHubSearchClient client;

  @BeforeEach
  void setUp() {
    httpClient = mock(HttpClient.class);
    response = mock(HttpResponse.class);
    client = GitHubSearchClient.builder()
        .withHttpClient(httpClient)
        .build();
  }

  @Test
  void shouldSearchRepositories() throws Exception {
    when(response.statusCode()).thenReturn(200);
    when(response.body()).thenReturn(loadJson("ValidResponse.json"));
    when(httpClient.send(any(HttpRequest.class), eq(BodyHandlers.ofString()))).thenReturn(response);
    final SearchResponse searchResponse = client.searchRepositories(SearchQuery.defaultQuery());
    assertEquals(1, searchResponse.totalCount());
    assertEquals("test-repo", searchResponse.items().getFirst().name());
  }

  @Test
  void shouldHandleSearchError() throws Exception {
    when(response.statusCode()).thenReturn(400);
    when(response.body()).thenReturn("Bad Request");
    when(httpClient.send(any(HttpRequest.class), eq(BodyHandlers.ofString()))).thenReturn(response);
    assertThrows(Exception.class, () -> client.searchRepositories(SearchQuery.defaultQuery()));
  }
}
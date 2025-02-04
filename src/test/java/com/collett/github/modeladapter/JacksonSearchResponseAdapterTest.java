package com.collett.github.modeladapter;

import com.collett.github.AbstractSearchTest;
import com.collett.github.model.SearchResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JacksonSearchResponseAdapterTest extends AbstractSearchTest {
  private SearchResponseAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new JacksonSearchResponseAdapter(new ObjectMapper());
  }

  @Test
  void shouldParseValidResponse() throws Exception {
    final SearchResponse response = adapter.adapt(loadJson("ValidResponse.json"));
    assertEquals(1, response.totalCount());
    final SearchResponse.SearchItem item = response.items().getFirst();
    assertEquals("test-repo", item.name());
    assertEquals("octocat/test-repo", item.full_name());
    assertEquals(100, item.stargazersCount());
  }

  @Test
  void shouldHandleEmptyResponse() throws Exception {
    final SearchResponse response = adapter.adapt(loadJson("EmptyResponse.json"));
    assertEquals(0, response.totalCount());
    assertTrue(response.items().isEmpty());
  }

  @Test
  void shouldThrowOnNullInput() {
    assertThrows(NullPointerException.class, () -> adapter.adapt(null));
  }
}
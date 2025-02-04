package com.collett.github.model;

import com.collett.github.AbstractSearchTest;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

class SearchQueryTest extends AbstractSearchTest {
  @Test
  void testBuildDefaultQuery() {
    SearchQuery query = SearchQuery.defaultQuery();
    assertEquals("Q", query.toString());
  }

  @Test
  void testBuildCompleteQuery() {
    LocalDateTime dateTime = LocalDateTime.of(2024, 1, 1, 0, 0);
    SearchQuery query = SearchQuery.builder()
        .withLanguage("java")
        .withMinStars(100)
        .withCreatedAfter(dateTime)
        .withOwner("octocat")
        .withVisibility(SearchQuery.Visibility.PUBLIC)
        .build();

    assertTrue(query.toString().contains("language:java"));
    assertTrue(query.toString().contains("stars:>=100"));
    assertTrue(query.toString().contains("created:>=2024-01-01T00:00:00Z"));
    assertTrue(query.toString().contains("user:octocat"));
    assertTrue(query.toString().contains("visibility:public"));
  }
}

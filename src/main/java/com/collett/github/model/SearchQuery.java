package com.collett.github.model;

import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

public sealed interface SearchQuery permits SearchQueryImpl {
  Optional<String> language();

  Optional<Integer> minStars();

  Optional<LocalDateTime> createdAfter();

  Optional<LocalDateTime> updatedAfter();

  Optional<String> owner();

  Optional<String> topic();

  Optional<Boolean> isFork();

  Optional<Boolean> isArchived();

  Optional<Visibility> visibility();

  sealed interface Builder permits SearchQueryImpl.Builder {
    Builder withLanguage(final String language);

    Builder withMinStars(final Integer minStars);

    Builder withCreatedAfter(final LocalDateTime createdAfter);

    Builder withUpdatedAfter(final LocalDateTime updatedAfter);

    Builder withOwner(final String owner);

    Builder withTopic(final String topic);

    Builder withIsFork(final Boolean isFork);

    Builder withIsArchived(final Boolean isArchived);

    Builder withVisibility(final Visibility visibility);

    SearchQuery build();
  }

  static SearchQuery defaultQuery() {
    return builder().build();
  }

  static Builder builder() {
    return new SearchQueryImpl.Builder();
  }

  enum Visibility {
    PUBLIC("public"),
    PRIVATE("private");

    private final String value;

    Visibility(final String value) {
      this.value = requireNonNull(value, "value");
    }

    @Override
    public String toString() {
      return value;
    }
  }
}
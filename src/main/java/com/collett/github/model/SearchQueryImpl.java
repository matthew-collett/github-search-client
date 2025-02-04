package com.collett.github.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Objects.requireNonNull;

final class SearchQueryImpl implements SearchQuery {
  private static final String DEFAULT_QUERY = "Q";

  private final String queryString;
  private final String language;
  private final Integer minStars;
  private final LocalDateTime createdAfter;
  private final LocalDateTime updatedAfter;
  private final String owner;
  private final String topic;
  private final Boolean isFork;
  private final Boolean isArchived;
  private final Visibility visibility;

  private SearchQueryImpl(final Builder builder) {
    requireNonNull(builder, "builder");
    this.queryString = requireNonNull(builder.queryString, "queryString");
    this.language = builder.language;
    this.minStars = builder.minStars;
    this.createdAfter = builder.createdAfter;
    this.updatedAfter = builder.updatedAfter;
    this.owner = builder.owner;
    this.topic = builder.topic;
    this.isFork = builder.isFork;
    this.isArchived = builder.isArchived;
    this.visibility = builder.visibility;
  }

  @Override
  public Optional<String> language() {
    return Optional.ofNullable(this.language);
  }

  @Override
  public Optional<Integer> minStars() {
    return Optional.ofNullable(this.minStars);
  }

  @Override
  public Optional<LocalDateTime> createdAfter() {
    return Optional.ofNullable(this.createdAfter);
  }

  @Override
  public Optional<LocalDateTime> updatedAfter() {
    return Optional.ofNullable(this.updatedAfter);
  }

  @Override
  public Optional<String> owner() {
    return Optional.ofNullable(this.owner);
  }

  @Override
  public Optional<String> topic() {
    return Optional.ofNullable(this.topic);
  }

  @Override
  public Optional<Boolean> isFork() {
    return Optional.ofNullable(this.isFork);
  }

  @Override
  public Optional<Boolean> isArchived() {
    return Optional.ofNullable(this.isArchived);
  }

  @Override
  public Optional<Visibility> visibility() {
    return Optional.ofNullable(this.visibility);
  }

  @Override
  public String toString() {
    return this.queryString;
  }

  static final class Builder implements SearchQuery.Builder {
    private String queryString;
    private String language;
    private Integer minStars;
    private LocalDateTime createdAfter;
    private LocalDateTime updatedAfter;
    private String owner;
    private String topic;
    private Boolean isFork;
    private Boolean isArchived;
    private Visibility visibility;

    @Override
    public SearchQuery.Builder withLanguage(final String language) {
      requireNonNull(language, "language");
      this.language = language;
      return this;
    }

    @Override
    public SearchQuery.Builder withMinStars(final Integer minStars) {
      requireNonNull(minStars, "minStars");
      this.minStars = minStars;
      return this;
    }

    @Override
    public SearchQuery.Builder withCreatedAfter(final LocalDateTime createdAfter) {
      requireNonNull(createdAfter, "createdAfter");
      this.createdAfter = createdAfter;
      return this;
    }

    @Override
    public SearchQuery.Builder withUpdatedAfter(final LocalDateTime updatedAfter) {
      requireNonNull(updatedAfter, "updatedAfter");
      this.updatedAfter = updatedAfter;
      return this;
    }

    @Override
    public SearchQuery.Builder withOwner(final String owner) {
      requireNonNull(owner, "owner");
      this.owner = owner;
      return this;
    }

    @Override
    public SearchQuery.Builder withTopic(final String topic) {
      requireNonNull(topic, "topic");
      this.topic = topic;
      return this;
    }

    @Override
    public SearchQuery.Builder withIsFork(final Boolean isFork) {
      requireNonNull(isFork, "isFork");
      this.isFork = isFork;
      return this;
    }

    @Override
    public SearchQuery.Builder withIsArchived(final Boolean isArchived) {
      requireNonNull(isArchived, "isArchived");
      this.isArchived = isArchived;
      return this;
    }

    @Override
    public SearchQuery.Builder withVisibility(final Visibility visibility) {
      requireNonNull(visibility, "visibility");
      this.visibility = visibility;
      return this;
    }

    @Override
    public SearchQuery build() {
      final List<String> conditions = new ArrayList<>();
      addCondition(conditions, language, "language:%s");
      addCondition(conditions, minStars, "stars:>=%s");
      addCondition(conditions, formatDate(createdAfter), "created:>=%s");
      addCondition(conditions, formatDate(updatedAfter), "updated:>=%s");
      addCondition(conditions, owner, "user:%s");
      addCondition(conditions, topic, "topic:%s");
      addCondition(conditions, isFork, "fork:%s");
      addCondition(conditions, isArchived, "archived:%s");
      addCondition(conditions, visibility, "visibility:%s");
      this.queryString = conditions.isEmpty() ? DEFAULT_QUERY : String.join(" ", conditions);
      return new SearchQueryImpl(this);
    }

    private static String formatDate(final LocalDateTime date) {
      if (date != null) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
      }
      return null;
    }

    private <T> void addCondition(final List<String> conditions, final T value, final String format) {
      requireNonNull(conditions, "conditions");
      requireNonNull(format, "format");
      Optional.ofNullable(value)
          .map(v -> String.format(format, v))
          .ifPresent(conditions::add);
    }
  }
}

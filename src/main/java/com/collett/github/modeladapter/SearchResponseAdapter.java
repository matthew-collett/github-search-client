package com.collett.github.modeladapter;

import com.collett.github.model.SearchResponse;

import java.io.IOException;

public interface SearchResponseAdapter {
  SearchResponse adapt(final String jsonResponse) throws IOException;
}

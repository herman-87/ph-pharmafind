package com.ph.backoffice.domain.certifications.model;

import java.util.List;

public record DomainPage<T>(
    List<T> content,
    int page,
    int size,
    long totalElements,
    int totalPages) {

  public boolean hasNext() {
    return page + 1 < totalPages;
  }

  public boolean hasPrevious() {
    return page > 0;
  }
}

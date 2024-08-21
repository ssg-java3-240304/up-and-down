package com.up.and.down.search.controller;

import lombok.Getter;

@Getter
public enum SearchSort {
    VIEW_COUNT("viewCount"),
    LIKE_COUNT("likeCount");

    private final String sortKey;

    SearchSort(String sortKey) {
        this.sortKey = sortKey;
    }

    public static SearchSort fromString(String sortKey) {
        for (SearchSort sort : SearchSort.values()) {
            if (sort.getSortKey().equalsIgnoreCase(sortKey)) {
                return sort;
            }
        }
        throw new IllegalArgumentException("Invalid sort key: " + sortKey);
    }
}

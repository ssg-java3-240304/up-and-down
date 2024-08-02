package com.up.and.down.product.entity;

public enum CommentState {
    COMMENT_EDITED, // 댓글 수정됨
    COMMENT_DELETE, // 댓글 삭제됨
    COMMENT_REPORTED, // 댓글 신고됨
    COMMENT_REPORTED_RESOLVED, // 신고된 댓글 처리됨
}
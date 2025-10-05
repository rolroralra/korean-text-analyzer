package com.kakaobank.tools.comment.analyzer.exception;

@SuppressWarnings("unused")
public class CommentAnalyzeGlobalException extends RuntimeException{

    public CommentAnalyzeGlobalException() {
        super("Comment Analyze Exception");
    }

    public CommentAnalyzeGlobalException(String message) {
        super(message);
    }

    public CommentAnalyzeGlobalException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommentAnalyzeGlobalException(Throwable cause) {
        super(cause);
    }
}

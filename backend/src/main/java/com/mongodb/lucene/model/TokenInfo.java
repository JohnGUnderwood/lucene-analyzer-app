package com.mongodb.lucene.model;

import java.util.Objects;

public class TokenInfo {
    private String text;
    private int length;
    private boolean matched;

    // Constructors
    public TokenInfo() {}

    public TokenInfo(String text) {
        this.text = text;
        this.length = text.length();
        this.matched = false;
    }

    public TokenInfo(String text, boolean matched) {
        this.text = text;
        this.length = text.length();
        this.matched = matched;
    }

    // Getters and Setters
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
        this.length = text.length();
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TokenInfo tokenInfo = (TokenInfo) o;
        return Objects.equals(text, tokenInfo.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(text);
    }
}

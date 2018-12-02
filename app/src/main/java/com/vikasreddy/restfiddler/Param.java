package com.vikasreddy.restfiddler;

public class Param {
    public String key;
    public String value;

    Param(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public boolean isNull() {
        return this.key == null && this.value == null;
    }

    boolean isKeyNullOrWhiteSpace() {
        return isNullOrWhiteSpace(this.key);
    }

    boolean isValueNullOrWhiteSpace() {
        return isNullOrWhiteSpace(this.value);
    }

    public boolean isEmpty() {
        this.isNull();
        return isNullOrWhiteSpace(this.key) && isNullOrWhiteSpace(this.value);
    }

    private boolean isNullOrWhiteSpace(String value) {
        if (value == null) {
            return true;
        }

        for (int i = 0; i < value.length(); i++) {
            if (!Character.isWhitespace(value.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        } else if(!(obj instanceof Param)) {
            return false;
        } else {
            return this.key.equals(((Param) obj).key);
        }
    }
}

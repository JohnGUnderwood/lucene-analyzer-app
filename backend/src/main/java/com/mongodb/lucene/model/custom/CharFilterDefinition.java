package com.mongodb.lucene.model.custom;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.util.Map;
import java.util.Set;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CharFilterDefinition.HtmlStripCharFilter.class, name = "htmlStrip"),
    @JsonSubTypes.Type(value = CharFilterDefinition.IcuNormalizeCharFilter.class, name = "icuNormalize"),
    @JsonSubTypes.Type(value = CharFilterDefinition.MappingCharFilter.class, name = "mapping"),
    @JsonSubTypes.Type(value = CharFilterDefinition.PersianCharFilter.class, name = "persian")
})
public abstract class CharFilterDefinition {
    @JsonProperty("type")
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // HTML Strip CharFilter
    public static class HtmlStripCharFilter extends CharFilterDefinition {
        @JsonProperty("ignoredTags")
        private Set<String> ignoredTags;

        public Set<String> getIgnoredTags() {
            return ignoredTags;
        }

        public void setIgnoredTags(Set<String> ignoredTags) {
            this.ignoredTags = ignoredTags;
        }
    }

    // ICU Normalize CharFilter
    public static class IcuNormalizeCharFilter extends CharFilterDefinition {
        // No additional properties
    }

    // Mapping CharFilter
    public static class MappingCharFilter extends CharFilterDefinition {
        @JsonProperty("mappings")
        private Map<String, String> mappings;

        public Map<String, String> getMappings() {
            return mappings;
        }

        public void setMappings(Map<String, String> mappings) {
            this.mappings = mappings;
        }
    }

    // Persian CharFilter
    public static class PersianCharFilter extends CharFilterDefinition {
        // No additional properties
    }
}

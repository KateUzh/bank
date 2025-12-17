package ru.star.bank.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DynamicRuleDto {
    private String query;
    private List<ArgumentsDto> arguments = new ArrayList<>();
    private boolean negate;

    public DynamicRuleDto() {
    }

    public DynamicRuleDto(String query, List<ArgumentsDto> arguments, boolean negate) {
        this.query = query;
        this.arguments = arguments;
        this.negate = negate;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<ArgumentsDto> getArguments() {
        return arguments;
    }

    public void setArguments(List<ArgumentsDto> arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof DynamicRuleDto that)) return false;
        return negate == that.negate && Objects.equals(query, that.query) && Objects.equals(arguments,
                that.arguments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(query, arguments, negate);
    }

    @Override
    public String toString() {
        return "DynamicRuleDto{" +
                "query='" + query + '\'' +
                ", arguments=" + arguments +
                ", negate=" + negate +
                '}';
    }
}

package pl.intelligent.finance.persistence.entity.impl;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.intelligent.finance.persistence.entity.ExpenditureCategoryMatcherType;
import pl.intelligent.finance.persistence.entity.IExpenditureCategoryMatcher;

import javax.persistence.*;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "EXPENDITURE_CATEGORY_MATCHER")
public class ExpenditureCategoryMatcher implements IExpenditureCategoryMatcher {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "EXPENDITURE_CATEGORY_MATCHER_SEQ"
    )
    @SequenceGenerator(
            name = "EXPENDITURE_CATEGORY_MATCHER_SEQ",
            allocationSize = 5
    )
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "PATTERN", nullable = false, length = 50)
    private String pattern;

    @Enumerated(EnumType.STRING)
    @Column(name = "MATCHER_TYPE_ID")
    private ExpenditureCategoryMatcherType matcherType;

    @ManyToOne
    @JoinColumn(name = "EXPENDITURE_CATEGORY_ID")
    private ExpenditureCategory category;

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getPattern() {
        return pattern;
    }

    @Override
    public ExpenditureCategoryMatcherType getMatcherType() {
        return matcherType;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public void setMatcherType(ExpenditureCategoryMatcherType matcherType) {
        this.matcherType = matcherType;
    }

    public ExpenditureCategory getCategory() {
        return category;
    }

    public void setCategory(ExpenditureCategory category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenditureCategoryMatcher that = (ExpenditureCategoryMatcher) o;
        return Objects.equals(id, that.id) && Objects.equals(pattern, that.pattern) && matcherType == that.matcherType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, pattern, matcherType);
    }

    @Override
    public String toString() {
        return "ExpenditureCategoryMatcher{" +
                "id=" + id +
                ", pattern='" + pattern + '\'' +
                ", matcherType=" + matcherType + // category.toString can lead to stackoverflow
                '}';
    }
}

package pl.intelligent.finance.entity.impl;

import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.intelligent.finance.entity.IExpenditureCategory;
import pl.intelligent.finance.entity.IExpenditureCategoryMatcher;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@NoArgsConstructor
@Builder
@Entity
@Table(name = "EXPENDITURE_CATEGORY")
public class ExpenditureCategory implements IExpenditureCategory {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "EXPENDITURE_CATEGORY_SEQ"
    )
    @SequenceGenerator(
            name = "EXPENDITURE_CATEGORY_SEQ",
            allocationSize = 5
    )
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @Column(name = "PARENT_CATEGORY_ID", nullable = true)
    private Integer parentCategoryId;

    @OneToMany(mappedBy = "category",
            fetch = FetchType.EAGER,
            cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    private List<ExpenditureCategoryMatcher> matchers;

    public ExpenditureCategory(Integer id, String name, Integer parentCategoryId, List<ExpenditureCategoryMatcher> matchers) {
        this.id = id;
        this.name = name;
        this.parentCategoryId = parentCategoryId;
        setMatchers(matchers);
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Integer getParentCategoryId() {
        return parentCategoryId;
    }

    public void addMatcher(ExpenditureCategoryMatcher matcher) {
        if (matchers != null) {
            matcher.setCategory(this);
            matchers.add(matcher);
        }
    }

    @Override
    public List<IExpenditureCategoryMatcher> getMatchers() {
        return matchers.stream()
                .map(matcher -> (IExpenditureCategoryMatcher) matcher)
                .collect(Collectors.toList());
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public void setMatchers(List<ExpenditureCategoryMatcher> matchers) {
        if (matchers == null) {
            return;
        }

        this.matchers = matchers.stream()
                .peek(mat -> mat.setCategory(this))
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExpenditureCategory that = (ExpenditureCategory) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) &&
                Objects.equals(parentCategoryId, that.parentCategoryId) &&
                Objects.equals(matchers, that.matchers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentCategoryId, matchers);
    }

    @Override
    public String toString() {
        return "ExpenditureCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentCategoryId=" + parentCategoryId +
                ", matchers=" + matchers +
                '}';
    }
}

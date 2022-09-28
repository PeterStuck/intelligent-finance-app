package pl.intelligent.finance.persistence.impl;

import lombok.Builder;
import lombok.NoArgsConstructor;
import pl.intelligent.finance.persistence.entity.IExpenditureCategory;
import pl.intelligent.finance.persistence.entity.IExpenditureCategoryMatcher;

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
            cascade = CascadeType.ALL)
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

    @Override
    public List<? extends IExpenditureCategoryMatcher> getMatchers() {
        return matchers;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setParentCategoryId(Integer parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    @Override
    public void setMatchers(List<? extends IExpenditureCategoryMatcher> matchers) {
        if (matchers == null) {
            return;
        }

        this.matchers = matchers.stream()
                .map(mat -> (ExpenditureCategoryMatcher) mat)
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

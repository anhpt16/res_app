package com.anhpt.res_app.common.dto.result;

import com.anhpt.res_app.common.utils.ApiCategory;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryMatchResult {
    private boolean matched; // true nếu Api trùng
    private boolean uncategorized; // true nếu UNCATEGORIZED
    private ApiCategory.CategoryType category; // null nếu UNCATEGORIZED hoặc unmatched

    public static CategoryMatchResult notMatched() {
        return new CategoryMatchResult(false, false, null);
    }

    public static CategoryMatchResult uncategorized() {
        return new CategoryMatchResult(true, true, null);
    }

    public static CategoryMatchResult categorized(ApiCategory.CategoryType category) {
        return new CategoryMatchResult(true, false, category);
    }
}

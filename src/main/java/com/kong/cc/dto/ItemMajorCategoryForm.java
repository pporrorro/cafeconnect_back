package com.kong.cc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemMajorCategoryForm {

	private Integer itemCategoryNum;
    private String itemCategoryName;
    
    
    private List<ItemMiddleCategoryForm> midCategories;
}

package com.kong.cc.service;

import java.util.List;
import java.util.Map;

import com.kong.cc.dto.ItemDto;
import com.kong.cc.dto.ShopOrderDto;
import com.kong.cc.dto.StockDto;
import com.kong.cc.dto.StoreDto;

public interface StockService {
	public List<ShopOrderDto> selectOrderList(Integer storeCode) throws Exception;
	public String addStockByOrderNum(List<Integer> orderNumList) throws Exception;
	public Map<String, List<StockDto>> selectStockByStoreCode(Integer storeCode) throws Exception;
	public String addStock(StockDto stockDto) throws Exception;
	public String updateStock(StockDto stockDto) throws Exception;
	public Integer deleteStock(Integer stockNum) throws Exception;
	public Map<String, List<StockDto>> selectStockByCategory(Map<String, String> param) throws Exception;
	public Map<String, List<StockDto>> selectStockByKeyword(Integer storeCode, String keyword) throws Exception;
	public List<ItemDto> selectItemList() throws Exception;
	public List<ItemDto> selectItemByName(String itemName) throws Exception;
	public List<StoreDto> selectStoreByItemCode(String itemCode) throws Exception;
	public Map<String, Object> selectCategory() throws Exception;
}

package com.kong.cc.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kong.cc.dto.ItemDto;
import com.kong.cc.dto.ShopOrderDto;
import com.kong.cc.dto.StockDto;
import com.kong.cc.dto.StoreDto;
import com.kong.cc.service.StockService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class StockController {
	
	public final StockService stockService;
	
	@GetMapping("/selectOrderList/{storeCode}") // StockOrderItemAdd.js
	public ResponseEntity<List<ShopOrderDto>> selectOrderList(@PathVariable Integer storeCode) {
		try {
			List<ShopOrderDto> shopOrderDtoList = stockService.selectOrderList(storeCode);
			return new ResponseEntity<List<ShopOrderDto>>(shopOrderDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ShopOrderDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/addStockByOrderNum/{orderNumList}") // StockOrderItemAdd.js
	public ResponseEntity<String> addStockByOrderNum(@PathVariable Integer[] orderNumList) {
		try {
			System.out.println(orderNumList);
			stockService.addStockByOrderNum(orderNumList==null? null:Arrays.asList(orderNumList));
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/selectStockByStoreCode/{storeCode}") // StockManage.js
	public ResponseEntity<Map<String, List<StockDto>>> selectStockByStoreCode(@PathVariable Integer storeCode) {
		try {
			Map<String, List<StockDto>> stockDtoList = stockService.selectStockByStoreCode(storeCode);
			return new ResponseEntity<Map<String, List<StockDto>>>(stockDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, List<StockDto>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/addStock") // StockManage.js
	public ResponseEntity<String> addStock(@ModelAttribute StockDto stockDto) {
		try {
			stockService.addStock(stockDto);
			return new ResponseEntity<String>("true", HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/updateStock") // StockManage.js
	public ResponseEntity<Map<String, List<StockDto>>> updateStock(@ModelAttribute StockDto stockDto) {
		try {
			String str = stockService.updateStock(stockDto);
			Map<String, List<StockDto>> stockDtoList = new HashMap<>();
			if(str.equals("true")) {
				stockDtoList = stockService.selectStockByStoreCode(stockDto.getStoreCode());
			}
			return new ResponseEntity<Map<String, List<StockDto>>>(stockDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, List<StockDto>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/deleteStock/{stockNum}") // StockManage.js
	public ResponseEntity<Map<String, List<StockDto>>> deleteStock(@PathVariable Integer stockNum) {
		try {
			Integer storeCode = stockService.deleteStock(stockNum);
			Map<String, List<StockDto>> stockDtoList = new HashMap<>();
			stockDtoList = stockService.selectStockByStoreCode(storeCode);
			return new ResponseEntity<Map<String, List<StockDto>>>(stockDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, List<StockDto>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping("/selectStockByCategory") // StockManage.js
	public ResponseEntity<Map<String, List<StockDto>>> selectStockByCategory(@RequestParam Map<String, String> param) {
		try {
			// {"storeCode":12354, "category":"middle", "categoryNum":1, "expirationDate":"true"}
			System.out.println(param);
			Map<String, List<StockDto>> stockDtoList = stockService.selectStockByCategory(param);
			return new ResponseEntity<Map<String, List<StockDto>>>(stockDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, List<StockDto>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/selectStockByKeyword/{storeCode}/{keyword}") // StockManage.js
	public ResponseEntity<Map<String, List<StockDto>>> selectStockByKeyword(@PathVariable Integer storeCode, @PathVariable String keyword) {
		try {
			Map<String, List<StockDto>> stockDtoList = stockService.selectStockByKeyword(storeCode, keyword);
			return new ResponseEntity<Map<String, List<StockDto>>>(stockDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, List<StockDto>>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/selectItemList") // StockOtherStore.js
	public ResponseEntity<List<ItemDto>> selectItemList() {
		try {
			List<ItemDto> itemDtoList = stockService.selectItemList();
			return new ResponseEntity<List<ItemDto>>(itemDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ItemDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/selectItemByName/{itemName}") // StockOtherStore.js
	public ResponseEntity<List<ItemDto>> selectItemByName(@PathVariable String itemName) {
		try {
			List<ItemDto> itemDtoList = stockService.selectItemByName(itemName);
			return new ResponseEntity<List<ItemDto>>(itemDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ItemDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	//같은 가맹점 행 합치기, 수량은 합산
	@GetMapping("/selectStoreByItemCode/{itemCode}") // StockOtherStoreItem.js
	public ResponseEntity<List<StoreDto>> selectStoreByItemCode(@PathVariable String itemCode) {
		try {
			List<StoreDto> storeDtoList = stockService.selectStoreByItemCode(itemCode);
			return new ResponseEntity<List<StoreDto>>(storeDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<StoreDto>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/selectCategory") // StockManage.js
	public ResponseEntity<Map<String, Object>> selectCategory() {
		try {
			// {"major":{}, "middle":{}, "sub":{}}
			Map<String, Object> categoryList = stockService.selectCategory();
			return new ResponseEntity<Map<String, Object>>(categoryList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/allItemList") // StockManage.js
	public ResponseEntity<List<ItemDto>> allItemList() {
		try {
			List<ItemDto> itemDtoList = stockService.selectItemList();
			return new ResponseEntity<List<ItemDto>>(itemDtoList, HttpStatus.OK);
		} catch(Exception e) {
			e.printStackTrace();
			return new ResponseEntity<List<ItemDto>>(HttpStatus.BAD_REQUEST);
		}
	}
}

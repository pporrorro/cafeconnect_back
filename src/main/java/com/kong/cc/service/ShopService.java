package com.kong.cc.service;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import com.kong.cc.dto.CartDto;
import com.kong.cc.dto.ItemDto;
import com.kong.cc.dto.ItemExpenseDto;
import com.kong.cc.dto.PaymentRequestDto;
import com.kong.cc.dto.PaymentResponseDto;
import com.kong.cc.dto.ShopOrderDto;
import com.kong.cc.util.PageInfo;

public interface ShopService {
	
	Map<String, List<ItemDto>> getshopMainItems() throws Exception;
	List<ItemDto> selectItemsByCategroy(Integer majorNum,Integer middleNum,Integer subNum,PageInfo pageInfo) throws Exception;
	List<ItemDto> selectItemsByKeyword(String Keyword,PageInfo pageInfo) throws Exception;
	ItemDto selectItem(String itemCode) throws Exception;
	
	//관심상품 시작
	//Boolean checkIsWished (String itemCode,Integer storeCode)throws Exception;
	Integer checkIsWished (String itemCode,Integer storeCode)throws Exception;	
	Boolean toggleWishItem(String itemCode,Integer storeCode) throws Exception;
	List<ItemDto> selectAllWishItems(Integer storeCode) throws Exception;
	List<ItemDto> selectAllWishItemsByCategory (Integer storeCode,Integer majorNum,Integer middleNum,Integer subNum) throws Exception;
	Boolean deleteCheckedWishItem(Integer storeCode,List<Integer>wishItemNumList) throws Exception;
	//관심상품 끝
	
	//장바구니 시작 
	CartDto addItemToCart(CartDto cartDto) throws Exception;
	List<CartDto>selectAllCartItems(Integer sotreCode) throws Exception;
	Integer selectAllCountCartItem(Integer storeCode) throws Exception;
	CartDto updateCartItemCount(Integer cartNum,Integer count) throws Exception;
	Boolean deleteCartItem(Integer storeCode,Integer cartNum) throws Exception;
	Map<String,Object>selectPreOrderedDate(Integer storeCode) throws Exception;
	Map<String,Object>selectOrderedItemLisByDate(Integer storeCode,Date selectedDate,PageInfo pageInfo)throws Exception;
	List<CartDto> addPreviousItemsToCart(Integer storeCode,List<String>itemCodesList) throws Exception;
	//장바구니 끝
		
	//주문 및 결제 시작
	List<CartDto>selectAllOrderItemAndInfo(Integer storeCode,List<Integer>cartItemNumList) throws Exception;
	Boolean validatePaymentRequest(PaymentRequestDto paymentRequest)throws Exception;
	PaymentRequestDto  requestPayment(Integer storeCode,List<Integer> cartNums)throws Exception;
	PaymentResponseDto verifyPayment(String imUid,String merchanUid,Integer amount) throws Exception; 
	List<ShopOrderDto> createOrder(String merchantUid, String impUid,String paymentMethod, Integer storeCode, List<Integer> cartNums) throws Exception;
	//주문 끝


	//주문내역-가맹점 
	Map<String,Object> selectAllOrderListForStore(Integer storeCode,Date startDate,Date endDate,String orderState,PageInfo pageInfo) throws Exception;
	Map<String,Object> selectOrderByOrderCode(Integer storeCode,String orderCode) throws Exception;
	Boolean cancelItemOrder(Integer storeCode,String orderCode) throws Exception;
	
	//주문내역 -본사 
	Map<String,Object> selectAllOrderListForMainStore(Date startDate,Date endDate,String searchType,String keyword,PageInfo pageInfo)throws Exception;
	Boolean updateOrderStatus(String orderCode, String orderState) throws Exception;

	//지출 내역
	Map<String,Object> selectExpenseItemList(Integer storeCode,Date startDate,Date endDate) throws Exception; 
	
}

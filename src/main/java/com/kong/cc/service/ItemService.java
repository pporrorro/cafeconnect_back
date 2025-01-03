package com.kong.cc.service;

import com.kong.cc.dto.ItemResponseDto;
import com.kong.cc.dto.ItemSaveForm;
import com.kong.cc.dto.ItemSearchCondition;
import com.kong.cc.dto.ItemUpdateForm;
import com.kong.cc.entity.*;
import com.kong.cc.repository.*;
import com.kong.cc.util.TranslateUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.sql.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Transactional
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;
    private final ItemQuerydslRepositoryImpl itemQueryDslRepository;
    private final ImageFileRepository imageFileRepository;
    private final ItemMajorCategoryRepository itemMajorCategoryRepository;
    private final ItemMiddleCategoryRepository itemMiddleCategoryRepository;
    private final ItemSubCategoryRepository itemSubCategoryRepository;
    private static AtomicLong sequence = new AtomicLong(0L);

    @Value("${upload.path}")
    private String uploadDir;

    public String saveItem(ItemSaveForm itemSaveForm, MultipartFile file) throws IOException {

        ImageFile imageFile = null;
        if (file.isEmpty()){
            throw new RuntimeException("해당하는 파일이 없습니다");
        }

        try{
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.indexOf(".");
            String originalName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(index);
            File saveFile = new File(directory, originalName);
            file.transferTo(saveFile);

            imageFile = ImageFile.builder()
                    .fileContentType(file.getContentType())
                    .fileUploadDate(new Date(System.currentTimeMillis()))
                    .fileName(originalName)
                    .fileSize(file.getSize())
                    .fileDirectory(uploadDir)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
        imageFileRepository.save(imageFile);
        ItemMajorCategory itemMajorCategory = itemMajorCategoryRepository.findByItemCategoryName(itemSaveForm.getItemCategoryMajorName());
        ItemMiddleCategory itemMiddleCategory = itemMiddleCategoryRepository.findByItemCategoryName(itemSaveForm.getItemCategoryMiddleName());
        ItemSubCategory itemSubCategory = itemSubCategoryRepository.findByItemCategoryName(itemSaveForm.getItemCategorySubName());

        Item.ItemBuilder itemBuilder = Item.builder()
                .itemCode("I24"
                        + String.format("%02d", itemMajorCategory != null ? itemMajorCategory.getItemCategoryNum() : 0)
                        + String.format("%02d", itemMiddleCategory != null ? itemMiddleCategory.getItemCategoryNum() : 0)
                        + String.format("%02d", itemSubCategory != null ? itemSubCategory.getItemCategoryNum() : 0)
                        + TranslateUtil.translate(itemSaveForm.getItemName()).toUpperCase().charAt(0)
                        +((int) (Math.random() * 9000) + 1000))
                .itemName(itemSaveForm.getItemName())
                .itemPrice(itemSaveForm.getItemPrice())
                .itemCapacity(itemSaveForm.getItemCapacity())
                .itemUnitQuantity(itemSaveForm.getItemUnitQuantity())
                .itemUnit(itemSaveForm.getItemUnit())
                .itemStandard(itemSaveForm.getItemStandard())
                .itemStorage(itemSaveForm.getItemStorage())
                .itemCountryOrigin(itemSaveForm.getItemCountryOrigin())
                .itemImageFile(imageFile);

        if(itemSubCategory != null){
            itemBuilder.itemSubCategory(itemSubCategory);
        }
        if(itemMiddleCategory != null){
            itemBuilder.itemMiddleCategory(itemMiddleCategory);
        }
        if(itemMajorCategory != null){
            itemBuilder.itemMajorCategory(itemMajorCategory);
        }
        Item item = itemBuilder.build();

        itemRepository.save(item);
        return item.getItemCode();
    }

    public void updateItem(String itemCode, ItemUpdateForm itemUpdateForm, MultipartFile file) {
        ImageFile imageFile = null;
        if (file.isEmpty()){
            throw new RuntimeException("해당하는 파일이 없습니다");
        }

        try{
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String originalFilename = file.getOriginalFilename();
            int index = originalFilename.indexOf(".");
            String originalName = UUID.randomUUID().toString() + file.getOriginalFilename().substring(index);
            File saveFile = new File(directory, originalName);
            file.transferTo(saveFile);

            imageFile = ImageFile.builder()
                    .fileContentType(file.getContentType())
                    .fileUploadDate(new Date(System.currentTimeMillis()))
                    .fileName(originalName)
                    .fileSize(file.getSize())
                    .fileDirectory(uploadDir)
                    .build();
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        imageFileRepository.save(imageFile);

        ItemMajorCategory itemMajorCategory = itemMajorCategoryRepository.findByItemCategoryName(itemUpdateForm.getItemCategoryMajorName());
        ItemMiddleCategory itemMiddleCategory = itemMiddleCategoryRepository.findByItemCategoryName(itemUpdateForm.getItemCategoryMiddleName());
        ItemSubCategory itemSubCategory = itemSubCategoryRepository.findByItemCategoryName(itemUpdateForm.getItemCategorySubName());
        Item item = itemRepository.findByItemCode(itemCode);

        if(item == null){
            throw new IllegalArgumentException("해당하는 아이템이 없습니다");
        }


        item.setItemName(itemUpdateForm.getItemName());
        item.setItemPrice(itemUpdateForm.getItemPrice());
        item.setItemCapacity(itemUpdateForm.getItemCapacity());
        item.setItemUnitQuantity(itemUpdateForm.getItemUnitQuantity());
        item.setItemUnit(itemUpdateForm.getItemUnit());
        item.setItemStandard(itemUpdateForm.getItemStandard());
        item.setItemStorage(itemUpdateForm.getItemStorage());
        item.setItemCountryOrigin(itemUpdateForm.getItemCountryOrigin());
        item.setItemImageFile(imageFile);

        if(itemSubCategory != null){
            item.setItemSubCategory(itemSubCategory);
        }
        if(itemMiddleCategory != null){
            item.setItemMiddleCategory(itemMiddleCategory);
        }
        if(itemMajorCategory != null){
            item.setItemMajorCategory(itemMajorCategory);
        }




    }

    public void deleteItem(String itemCode) {
        Item item = itemRepository.findByItemCode(itemCode);
        if(item == null){
            throw new IllegalArgumentException("해당하는 아이템이 없습니다");
        }

        int wishItemListSize = item.getWishItemList().size();
        int cartListSize = item.getCartList().size();
        int orderListSize = item.getOrderList().size();
        int repairListSize = item.getRepairList().size();
        int stockListSize = item.getStockList().size();

        if(wishItemListSize > 0){
            throw new IllegalArgumentException("wishItemList 존재합니다");
        }
        if(cartListSize > 0){
            throw new IllegalArgumentException("cartList 존재합니다");
        }
        if(orderListSize > 0){
            throw new IllegalArgumentException("orderList 존재합니다");
        }
        if(repairListSize > 0){
            throw new IllegalArgumentException("repairList 존재합니다");
        }
        if(stockListSize > 0){
            throw new IllegalArgumentException("stockList 존재합니다");
        }

        itemRepository.delete(item);
    }

    public ItemResponseDto selectItemByItemCode(String itemCode) throws IOException {
        Item item = itemRepository.findByItemCode(itemCode);
        if(item == null){
            throw new IllegalArgumentException("해당하는 아이템이 없습니다");
        }
        String fileContentType = null;
        String fileDirectory = null;
        String fileName = null;
        String imageUrl = null;
        ImageFile imageFile = item.getItemImageFile();

        if(imageFile != null){
            fileContentType = imageFile.getFileContentType();
            fileDirectory = imageFile.getFileDirectory();
            fileName = imageFile.getFileName();
            Path imagePath = Paths.get(fileDirectory+fileName);
            byte[] imageBytes = Files.readAllBytes(imagePath);
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);
            imageUrl = "data:"+fileContentType+";base64,"+base64Image;
        }



        String itemMajorCategoryName = item.getItemMajorCategory() != null ? item.getItemMajorCategory().getItemCategoryName() : null;
        String itemMiddleCategoryName = item.getItemMiddleCategory() != null ? item.getItemMiddleCategory().getItemCategoryName() : null;
        String itemSubCategoryName = item.getItemSubCategory() != null ? item.getItemSubCategory().getItemCategoryName() : null;
        return  ItemResponseDto.builder()
                .itemCode(item.getItemCode())
                .itemName(item.getItemName())
                .itemCapacity(item.getItemCapacity())
                .itemUnitQuantity(item.getItemUnitQuantity())
                .itemUnit(item.getItemUnit())
                .itemStandard(item.getItemStandard())
                .itemStorage(item.getItemStorage())
                .itemCountryOrigin(item.getItemCountryOrigin())
                .itemMajorCategoryName(itemMajorCategoryName)
                .itemMiddleCategoryName(itemMiddleCategoryName)
                .itemSubCategoryName(itemSubCategoryName)
                .itemPrice(item.getItemPrice())
                .imageUrl(item.getItemImageFile() != null ? imageUrl : null)
                .build();
    }

    public Page<ItemResponseDto> itemResponseDtoListByKeyword(Integer pageNum, Integer pageSize, String keyword) {
        PageRequest pageRequest = PageRequest.of(pageNum , pageSize, Sort.by(Sort.Direction.ASC, "itemCode"));
        return itemQueryDslRepository.findItemResponseDtoListByKeyword(keyword,pageRequest);


    }

    public Page<ItemResponseDto> itemResponseDtoListByCategory(Integer pageNum, Integer pageSize, ItemSearchCondition condition) {
        PageRequest pageRequest = PageRequest.of(pageNum , pageSize, Sort.by(Sort.Direction.ASC, "itemCode"));
        Page<Item> page = itemQueryDslRepository.findItemListByCategory(condition, pageRequest);
        return itemQueryDslRepository.findItemResponseDtoListByCategory(page);
    }
}

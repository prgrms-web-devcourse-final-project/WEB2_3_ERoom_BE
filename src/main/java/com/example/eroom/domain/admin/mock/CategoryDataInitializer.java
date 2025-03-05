//package com.example.eroom.domain.admin.mock;
//
//import com.example.eroom.domain.admin.repository.AdminCategoryJPARepository;
//import com.example.eroom.domain.admin.repository.AdminSubCategoryJPARepository;
//import com.example.eroom.domain.admin.repository.AdminTagJPARepository;
//import com.example.eroom.domain.entity.Category;
//import com.example.eroom.domain.entity.SubCategory;
//import com.example.eroom.domain.entity.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
////@Component
//@RequiredArgsConstructor
//public class CategoryDataInitializer implements CommandLineRunner {
//    private final AdminCategoryJPARepository adminCategoryJPARepository;
//    private final AdminSubCategoryJPARepository adminSubCategoryJPARepository;
//    private final AdminTagJPARepository adminTagJPARepository;
//
//    @Override
//    public void run(String... args) throws Exception {
//        // 카테고리
//        Category category1 = new Category();
//        category1.setName("개발");
//
//        Category category2 = new Category();
//        category2.setName("교육");
//
//        Category category3 = new Category();
//        category3.setName("디자인");
//
//        adminCategoryJPARepository.save(category1);
//        adminCategoryJPARepository.save(category2);
//        adminCategoryJPARepository.save(category3);
//
//        // 서브 카테고리
//        SubCategory subCategory1 = new SubCategory();
//        subCategory1.setCategory(category1);
//        subCategory1.setName("언어");
//
//        SubCategory subCategory2 = new SubCategory();
//        subCategory2.setCategory(category1);
//        subCategory2.setName("프레임워크/라이브러리");
//
//        adminSubCategoryJPARepository.save(subCategory1);
//        adminSubCategoryJPARepository.save(subCategory2);
//
//        // 태그
//        Tag tag1 = new Tag();
//        tag1.setSubCategory(subCategory1);
//        tag1.setName("C");
//
//        Tag tag2 = new Tag();
//        tag2.setSubCategory(subCategory2);
//        tag2.setName("Spring");
//
//        adminTagJPARepository.save(tag1);
//        adminTagJPARepository.save(tag2);
//
//    }
//}

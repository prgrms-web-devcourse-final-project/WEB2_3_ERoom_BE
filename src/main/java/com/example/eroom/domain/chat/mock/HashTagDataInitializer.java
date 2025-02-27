package com.example.eroom.domain.chat.mock;

import com.example.eroom.domain.chat.repository.CategoryRepository;
import com.example.eroom.domain.chat.repository.SubCategoryRepository;
import com.example.eroom.domain.chat.repository.TagRepository;
import com.example.eroom.domain.entity.Category;
import com.example.eroom.domain.entity.SubCategory;
import com.example.eroom.domain.entity.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HashTagDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final SubCategoryRepository subCategoryRepository;
    private final TagRepository tagRepository;

    @Override
    public void run(String... args) {
        if (categoryRepository.count() > 0) {
            System.out.println("초기 데이터가 이미 존재.");
            return;
        }

        // 카테고리 생성
        Category dev = new Category(null, "개발", new ArrayList<>());
        Category design = new Category(null, "디자인", new ArrayList<>());
        Category marketing = new Category(null, "마케팅", new ArrayList<>());
        Category planning = new Category(null, "기획", new ArrayList<>());
        Category business = new Category(null, "경영", new ArrayList<>());

        categoryRepository.saveAll(List.of(dev, design, marketing, planning, business));

        // 서브카테고리 및 태그 데이터 생성
        createSubCategoriesAndTags(dev, "백엔드 개발", List.of("Java", "Spring", "JPA", "MySQL"));
        createSubCategoriesAndTags(dev, "프론트엔드 개발", List.of("React", "Vue", "TypeScript", "Tailwind"));

        createSubCategoriesAndTags(design, "UI/UX 디자인", List.of("Figma", "Sketch", "Adobe XD"));
        createSubCategoriesAndTags(design, "그래픽 디자인", List.of("Photoshop", "Illustrator", "InDesign"));

        createSubCategoriesAndTags(marketing, "디지털 마케팅", List.of("SEO", "Google Ads", "SNS 마케팅"));
        createSubCategoriesAndTags(marketing, "브랜드 마케팅", List.of("브랜드 전략", "시장 조사", "콘텐츠 마케팅"));

        createSubCategoriesAndTags(planning, "서비스 기획", List.of("기획서 작성", "와이어프레임", "사용자 분석"));
        createSubCategoriesAndTags(planning, "전략 기획", List.of("SWOT 분석", "경쟁사 분석", "비즈니스 모델"));

        createSubCategoriesAndTags(business, "경영 전략", List.of("리더십", "비즈니스 전략", "데이터 분석"));
        createSubCategoriesAndTags(business, "재무 관리", List.of("회계", "투자 분석", "자산 관리"));
    }

    private void createSubCategoriesAndTags(Category category, String subCategoryName, List<String> tagNames) {
        SubCategory subCategory = new SubCategory(null, subCategoryName, category, new ArrayList<>());
        subCategory = subCategoryRepository.save(subCategory); // 서브카테고리 저장

        SubCategory finalSubCategory = subCategory;
        List<Tag> tags = tagNames.stream()
                .map(tagName -> new Tag(null, tagName, 0, finalSubCategory))
                .collect(Collectors.toList());

        tagRepository.saveAll(tags); // 태그 저장
    }
}

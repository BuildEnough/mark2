package com.buildenough.logisticsmanagement.service;

import com.buildenough.logisticsmanagement.domain.Product;
import com.buildenough.logisticsmanagement.domain.ProductStatus;
import com.buildenough.logisticsmanagement.dto.ProductCreateRequest;
import com.buildenough.logisticsmanagement.dto.ProductResponse;
import com.buildenough.logisticsmanagement.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // 상품 등록
    @Transactional // 예외 터지면 롤백
    public ProductResponse createProduct(ProductCreateRequest request) {
        if (productRepository.existsByCode(request.getCode())) {
            throw new IllegalArgumentException(("이미 사용 중인 상품 코드입니다: " + request.getCode()));
        }

        // ProductCreateRequest, 즉 dto를 entity로 변환
        Product product = new Product(
                request.getCode(),
                request.getName(),
                request.getUnit(),
                ProductStatus.ACTIVE
        );

        Product saved = productRepository.save(product);
        return new ProductResponse(saved);
    }

    // 상품 전체 조회
    @Transactional(readOnly = true) // 조회 최적화
    public List<ProductResponse> getAllProducts() {
        return productRepository.findByStatus(ProductStatus.ACTIVE)   // ACTIVE만 조회
                .stream()
                .map(ProductResponse::new)
                .toList();
    }

    // 상품 삭제 = 비활성화 처리
    @Transactional
    public void deleteProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" + productId));
        product.deactivate();
    }

    @Transactional
    public void activateProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 상품입니다. id=" + productId));

        product.activate();
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getInactiveProducts() {
        return productRepository.findByStatus(ProductStatus.INACTIVE)
                .stream()
                .map(ProductResponse::new)
                .toList();
    }
}

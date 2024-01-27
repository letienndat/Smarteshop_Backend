package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.ProductInShopCart;
import com.smarteshop_backend.repository.ProductInShopCartRepository;
import com.smarteshop_backend.service.ProductInShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductInShopCartServiceImpl implements ProductInShopCartService {
    @Autowired
    private ProductInShopCartRepository productInShopCartRepository;

    /**
     * Find product in shop cart by id
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ProductInShopCart findById(Long id) throws Exception {
        return productInShopCartRepository.findById(id).orElseThrow(() -> new Exception("Cannot find product in shop cart with id = " + id));
    }

    /**
     * Remove product in shop cart by id
     *
     * @param id
     */
    @Override
    public void removeProductInShopCart(Long id) {
        productInShopCartRepository.deleteById(id);
    }

    /**
     * Find list product in shop cart by list id
     *
     * @param ids
     * @return
     */
    @Override
    public List<ProductInShopCart> findByIdIn(List<Long> ids) {
        return productInShopCartRepository.findByIdIn(ids);
    }

    /**
     * Remove list product in shop cart by list id
     *
     * @param ids
     */
    @Override
    public void removeByIds(List<Long> ids) {
        productInShopCartRepository.deleteByIdIn(ids);
    }
}

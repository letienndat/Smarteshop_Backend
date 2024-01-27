package com.smarteshop_backend.service.impl;

import com.smarteshop_backend.entity.ProductInShopCart;
import com.smarteshop_backend.entity.ShopCart;
import com.smarteshop_backend.repository.ShopCartRepository;
import com.smarteshop_backend.service.ProductInShopCartService;
import com.smarteshop_backend.service.ShopCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ShopCartServiceImpl implements ShopCartService {
    @Autowired
    private ShopCartRepository shopCartRepository;

    @Autowired
    private ProductInShopCartService productInShopCartService;

    /**
     * Save shop cart
     *
     * @param shopCart
     * @return
     */
    @Override
    public ShopCart save(ShopCart shopCart) {
        return shopCartRepository.save(shopCart);
    }

    /**
     * Find shop cart by id
     *
     * @param id
     * @return
     * @throws Exception
     */
    @Override
    public ShopCart findById(Long id) throws Exception {
        return shopCartRepository.findById(id).orElseThrow(() -> new Exception("Cannot find shop cart with id = " + id));
    }

    /**
     * Check proudct and size in shop cart
     *
     * @param id
     * @param idProduct
     * @param size
     * @return
     * @throws Exception
     */
    @Override
    public boolean checkProductAndSize(Long id, Long idProduct, Double size) throws Exception {
        ShopCart shopCart = findById(id);
        for (ProductInShopCart product : shopCart.getProductInShopCarts()) {
            if (product.getProduct().getId() == idProduct && product.getSize().getNumber().equals(size)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Update product in shop cart (update quantity)
     *
     * @param id
     * @param idProduct
     * @param size
     * @param quantity
     * @return
     * @throws Exception
     */
    @Override
    public ProductInShopCart addQuantity(Long id, Long idProduct, Double size, Integer quantity) throws Exception {
        ShopCart shopCart = findById(id);
        List<ProductInShopCart> productInShopCarts = shopCart.getProductInShopCarts();
        for (ProductInShopCart product : productInShopCarts) {
            if (product.getProduct().getId().equals(idProduct) && product.getSize().getNumber().equals(size)) {
                product.setQuantity(product.getQuantity() + quantity);
                product.setUpdateAt(new Date());
                shopCartRepository.save(shopCart);

                return product;
            }
        }

        return null;
    }

    /**
     * Remove product in shop cart
     *
     * @param id
     * @param idProductInShopCart
     * @return
     * @throws Exception
     */
    @Override
    public boolean removeProduct(Long id, Long idProductInShopCart) throws Exception {
        ShopCart shopCart = findById(id);
        List<ProductInShopCart> productInShopCarts = shopCart.getProductInShopCarts();
        ProductInShopCart productInShopCart = null;
        for (ProductInShopCart inShopCart : productInShopCarts) {
            if (inShopCart.getId().equals(idProductInShopCart)) {
                productInShopCart = inShopCart;
                break;
            }
        }
        if (productInShopCart != null) {
            productInShopCartService.removeProductInShopCart(productInShopCart.getId());

            return true;
        }

        return false;
    }
}

package com.hiccup.Hiccupify.service.cart;

import com.hiccup.Hiccupify.exception.ProductNotFoundException;
import com.hiccup.Hiccupify.exception.ResourceNotFound;
import com.hiccup.Hiccupify.model.Cart;
import com.hiccup.Hiccupify.model.CartItem;
import com.hiccup.Hiccupify.model.Product;
import com.hiccup.Hiccupify.repository.CartItemRepository;
import com.hiccup.Hiccupify.repository.CartRepository;
import com.hiccup.Hiccupify.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService implements ICartItemService{
    private final CartItemRepository cartItemRepository;
    private final IProductService productService;
    private final ICartService cartService;
    private final CartRepository cartRepository;

    @Override
    public void addCartItem(Long cartId, Long productId, int quantity) {
        Cart cart=cartService.getCart(cartId);
        Product product=productService.getProductById(productId);
        CartItem cartItem= cart.getCartItems().stream()
                .filter(item-> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());
        if(cartItem.getId()==null){
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
        }else{
            cartItem.setQuantity(cartItem.getQuantity()+quantity);
        }
        cartItem.setTotalPrice();
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void removeItemFromCart(Long cartId, Long productId) {
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFound("Cart not found"));
        CartItem itemToRemove=cart.getCartItems().stream()
                .filter(item->item.getProduct().getId().
                        equals(productId)).findFirst().orElseThrow(()->new ResourceNotFound("Product not found"));
        cart.removeItem(itemToRemove);
        cartRepository.save(cart);
    }

    @Override
    public void updateItemQuantity(Long cartId, Long productId, int quantity) {
        Cart cart=cartRepository.findById(cartId).orElseThrow(()->new ResourceNotFound("Product not found"));
        CartItem cartItem = cart.getCartItems().
                stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()->new ResourceNotFound("item not found"));
        cartItem.setQuantity(quantity);
        cartItem.setUnitPrice(cartItem.getProduct().getPrice());
        cartItem.setTotalPrice();

        cart.updateTotalAmount();
        cartRepository.save(cart);
    }

   @Override
   public CartItem getCartItem(Long cartId, Long productId){
        Cart cart=cartService.getCart(cartId);
        return cart.getCartItems()
                .stream().filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(()->new ProductNotFoundException("Item not found"));
    }
}

package com.dental.lab.model.payloads;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import com.dental.lab.model.entities.ProductOrder;
import com.dental.lab.model.entities.ProductItem;

/**
 * Contains a {@linkplain List} of {@linkplain PreOrderProductInfo} objects.<br>
 * It is used in the {@code create order view-controller} to hold the products added to 
 * the {@code Oder} before they are actually used to create an {@linkplain ProductOrder}
 * Entity with {@linkplain ProductItem} Entities associated.<br>
 * Each {@code PreOrderProductInfo} will be used to create one {@code ProductItem} object
 * and then add it to the new {@code Oder} entity.
 * 
 * @author Luis Fernando Martinez Oritz
 *
 */
public class PreOrderCurrentStatePayload {
	
	/**
	 * A list containing information of each product added to the pre-Order
	 * (shopping card) before they are used to actually create an {@linkplain ProductOrder}
	 * with its corresponding {@linkplain ProductItem}s.<br>
	 * One {@linkplain ProductItem} will be created for each product added to this List.
	 */
	private List<PreOrderProductInfo> preOrderProducts = new ArrayList<>();
	/**
	 * Is used to generate a temporal id used to identify
	 * each {@code preOrderProductInfo} object in the {@link #preOrderProducts} List.
	 * Such identifier is used by the controller since one {@linkplain Product} may
	 * be added more than once to the {@link #preOrderProducts} List. Latter, one 
	 * {@linkplain ProductItem} will be created for each product added in the
	 * {@link #preOrderProducts} List. repeated {@code Products} added will create
	 * different {@code ProductItem}s associated with the same {@code Product}.
	 */
	private AtomicInteger productInfoIdGenerator = new AtomicInteger();
	
	/**
	 * username of the {@linkplain User} to which the current pre-order
	 * belongs to. An {@code Admin} can create {@code Orders} for any
	 * valid {@code User}.
	 */
	private volatile String selectedUserUsername;

	public String getSelectedUserUsername() {
		return selectedUserUsername;
	}

	public void setSelectedUserUsername(String selectedUserUsername) {
		this.selectedUserUsername = selectedUserUsername;
	}

	/**
	 * Adds a {@code tempId} value to the {@code productInfo} object, and
	 * then adds {@code productInfo} to the {@link #preOrderProducts} list.
	 * 
	 * @param productInfo
	 */
	public void addProductInfo(PreOrderProductInfo productInfo) {
		productInfo.setTempId(productInfoIdGenerator.getAndIncrement());
		preOrderProducts.add(productInfo);
	}
	
	public boolean removeProductInfo(
			Integer productTempId, Long productId) {

		PreOrderProductInfo productToRemove = null;
		for(PreOrderProductInfo productInfo: preOrderProducts) {
			if(productInfo.getTempId() == productTempId 
					&& productInfo.getProductId() == productId) {
				productToRemove = productInfo;
				break;
			}
		}
		if(productToRemove != null) {
			return preOrderProducts.remove(productToRemove);
		}
		
		return false;
	}
	
	public boolean isEmpty() {
		return preOrderProducts.isEmpty();
	}
	
	public List<PreOrderProductInfo> getPreOrderProducts() {
		return preOrderProducts;
	}
	
	public int getTotalPrice() {
		int totalPrice = 0;
		for(PreOrderProductInfo p : preOrderProducts) {
			totalPrice += p.getPrice().intValue();
		}
		return totalPrice;
	}
	
	public void clean() {
		this.preOrderProducts = new ArrayList<>();
		this.productInfoIdGenerator = new AtomicInteger();
	}

}

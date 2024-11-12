let selectedColorId = null;
let selectedSizeId = null;


function selectColor(element) {
    // Remove active class from all colors
    document.querySelectorAll('.product-grid-item__color').forEach(el => {
        el.classList.remove('active');
    });
    // Add active class to selected color
    element.querySelector('.product-grid-item__color').classList.add('active');
    selectedColorId = element.dataset.colorId;
    console.log("selectedColorId", selectedColorId);
    updateStock();
}

function selectSize(element) {
    // Remove active class from all sizes
    document.querySelectorAll('.product__available-size a').forEach(el => {
        el.classList.remove('active');
    });
    // Add active class to selected size
    element.classList.add('active');
    selectedSizeId = element.dataset.sizeId;
    console.log("selectedSizeId", selectedSizeId);
    updateStock();
}

function updateStock() {
    if (selectedColorId && selectedSizeId) {
        const variant = variants.find(v =>
            v.color.id == selectedColorId &&
            v.size.id == selectedSizeId
        );

        const quantityInput = document.getElementById('quantity');
        if (variant && variant.stock !== undefined) {
            quantityInput.dataset.maxStock = variant.stock;
            if (variant.stock === 0) {
                // Disable quantity input if out of stock
                quantityInput.disabled = true;
                quantityInput.value = 0;
            } else {
                quantityInput.disabled = false;
                quantityInput.value = Math.min(quantityInput.value, variant.stock);
            }
        } else {
            // Handle the case where variant or stock is undefined
            console.error('Variant or stock is not defined');
            quantityInput.disabled = true;
            quantityInput.value = 0;
        }
    }
}


function increaseQuantity() {
    const input = document.getElementById('quantity');
    const maxStock = parseInt(input.dataset.maxStock);
    const currentValue = parseInt(input.value);
    if (currentValue < maxStock) {
        input.value = currentValue + 1;
    }
}

function decreaseQuantity() {
    const input = document.getElementById('quantity');
    const currentValue = parseInt(input.value);
    if (currentValue > 1) {
        input.value = currentValue - 1;
    }
}



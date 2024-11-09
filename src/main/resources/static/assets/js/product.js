let variants = /*[[${variants}]]*/ [];
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
    updateStock();
}
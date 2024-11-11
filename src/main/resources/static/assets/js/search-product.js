// Hàm xử lý tìm kiếm khi người dùng nhấn enter hoặc submit form
function searchProduct(event) {
    event.preventDefault(); // Ngăn form tự động reload trang

    // Lấy giá trị tìm kiếm từ input
    const name = document.getElementById("searchInput").value.trim();

    if (name) {
        // Gọi API tìm kiếm sản phẩm với tên người dùng nhập
        fetch(`/search-product/name/${name}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error("Network response was not ok");
                }
                return response.json(); // Chuyển đổi dữ liệu trả về thành JSON
            })
            .then(data => {
                // Dữ liệu trả về là một Page<Product>
                console.log("Kết quả tìm kiếm:", data);
                // Hiển thị các sản phẩm tìm thấy
                displayProducts(data.content); // Gọi hàm hiển thị sản phẩm
            })
            .catch(error => {
                console.error("Lỗi khi lấy kết quả tìm kiếm:", error);
            });
    } else {
        console.log("Vui lòng nhập tên sản phẩm để tìm kiếm.");
    }
}

function displayProducts(products) {
    const productList = document.getElementById("productList");
    productList.innerHTML = ""; // Xóa danh sách cũ

    products.forEach(product => {
        // Lấy ảnh đầu tiên từ productFirstImageMap
        const firstImage = productImagesMap[product.id].find(image => image.isPrimary);
        console.log("image of product", firstImage);
        const productItem = document.createElement("div");
        productItem.classList.add("product-grid-item");
        productItem.innerHTML = `
                <!-- Tag -->
                <div class="product-grid-item__tag product-grid-item__tag--sold-out">
                    Sold out
                </div>
                <!-- End tag -->
                <!-- Wishlist -->
                <div class="product-grid-item__wishlist">
                    <a href="#"><i class="lnil lnil-heart"></i></a>
                </div>
                <!-- End wishlist -->
                <!-- Image -->
                <div class="product-grid-item__image">
                    <a href="/product/${product.id}/${product.slug}">
                        <img
                            alt="Image"
                            data-sizes="auto"
                            src="${firstImage.imageUrl}"
                            class="lazyload lazy-effect"
                        />
                    </a>
                    <!-- Action -->
                    <div class="product-grid-item__action d-flex">
                        <!-- Button -->
                        <div class="product-grid-item__button">
                            <a href="#">Add to cart</a>
                        </div>
                        <!-- End button -->
                        <!-- Button -->
                        <div class="product-grid-item__button">
                            <a href="#">Quick shop</a>
                        </div>
                        <!-- End button -->
                    </div>
                    <!-- End action -->
                </div>
                <!-- End image -->
                <!-- Title -->
                <div class="product-grid-item__title">
                    <a href="/product/${product.id}/${product.slug}">${product.name}</a>
                </div>
                <!-- End title -->
                <!-- Price -->
                <div class="product-grid-item__price">$${product.price}</div>
                <!-- End price -->
            `;
        productList.appendChild(productItem);
    });
}
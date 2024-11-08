let offset = 0; // Số sản phẩm hiện tại đã được tải
const limit = 10; // Số sản phẩm tải mỗi lần

async function loadMore() {
    try {
        // Gửi yêu cầu AJAX để tải thêm sản phẩm
        const response = await axios.post(`/api/products/load-more-products?offset=${offset}&limit=${limit}`);
        const data = await response.json();
        console.log(data);

        let html = "";

        const productList = document.getElementsByClassName("row");

        // Duyệt qua các sản phẩm trả về và thêm chúng vào danh sách sản phẩm
        data.forEach(product => {
            const productItem = `
                <!-- Product -->
          <div class="col-12 col-md-6 col-lg-4 col-xl-3 col-xxl-25">
            <div class="product-grid-item">
              <!-- Tag -->
              <!--<div class="product-grid-item__tag">10% off</div>-->
              <!-- End tag -->
              <!-- Wishlist -->
              <div class="product-grid-item__wishlist"><a href="#"><i class="lnil lnil-heart"></i></a></div>
              <!-- End wishlist -->
              <!-- Image -->
              <div class="product-grid-item__image">
                <a href="product.html">
                  <img
                          alt="Image"
                          data-sizes="auto"
                          data-srcset="/web-assets/products/1/2_1-a.jpg 400w,
                    /web-assets/products/1/2_1-a.jpg 800w"
                          src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                          class="lazyload lazy-effect" />
                  <img
                          alt="Image"
                          data-sizes="auto"
                          data-srcset="/web-assets/products/1/2_1-b.jpg 400w,
                    /web-assets/products/1/2_1-b.jpg 800w"
                          src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw=="
                          class="lazyload lazy-effect hover" />
                </a>
                <!-- Action -->
                <div class="product-grid-item__action d-flex">
                  <!-- Button -->
                  <div class="product-grid-item__button"><a href="#">Add to cart</a></div>
                  <!-- End button -->
                  <!-- Button -->
                  <div class="product-grid-item__button"><a href="#">Quick shop</a></div>
                  <!-- End button -->
                </div>
                <!-- End action -->
              </div>
              <!-- End image -->
              <!-- Title -->
              <div class="product-grid-item__title"><a href="product.html" >Zipped cotton cardigan</a></div>
              <!-- End title -->
              <!-- Price -->
              <div class="product-grid-item__price">
                <span class="price-new">$49.5</span>
                <span class="price-old">$68.5</span>
              </div>
              <!-- End price -->
              <!-- Tag -->
              <div class="product-grid-item__content-tag">2 colours</div>
              <!-- End tag -->
            </div>
          </div>
          <!-- End product -->
            `;
            productList.innerHTML += html;
        });

        // Tăng giá trị offset để lần tải tiếp theo biết
        offset += limit;

        // Ẩn nút "LOAD MORE" nếu không còn sản phẩm để tải
        if (data.length < limit) {
            document.getElementById("loadMore").style.display = 'none';
        }
    } catch (error) {
        console.error('Lỗi khi tải sản phẩm:', error);
    }
}


//Them san pham vao localStorage
function addToListViewed(product) {
    let listViewed = JSON.parse(localStorage.getItem("listViewed")) || []; //Lay list hien tai hoac tao list moi
    listViewed.push(product); //Them san pham vao list
    localStorage.setItem('listViewed', JSON.stringify(listViewed)); //Luu list vao localStorage
}

//Lay san pham va hien thi ra man hinh

let listViewedEl = document.getElementById("last-items-viewed");

function displayListViewed() {
    let listViewed = JSON.parse(localStorage.getItem("listViewed")) || [];//Lay list hien tai hoac tao list moi
    let html = "";
    if (listViewed.length > 0) {
        listViewed.forEach(product => {
            html += `
            <div class="product-grid-item"">
            <!-- Wishlist -->
            <div class="product-grid-item__wishlist">
              <a href="#" tabindex="0"><i class="lnil lnil-heart"></i></a>
            </div>
            <!-- End wishlist -->
            <!-- Image -->
            <div class="product-grid-item__image">
              <a href="#" tabindex="0">
                <img alt="Image" data-sizes="auto" data-srcset="/web-assets/products/1/1b.jpg 400w,
                /web-assets/products/1/1b.jpg 800w" src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==" class="lazy-effect lazyautosizes ls-is-cached lazyloaded" sizes="338px" srcset="/assets/products/1/1b.jpg 400w,
                /web/assetsducts/1/1b.jpg 800w">
                <img alt="Image" data-sizes="auto" data-srcset="/web-assets/products/1/1c.jpg 400w,
                /web-assets/products/1/1c.jpg 800w" src="data:image/gif;base64,R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==" class="lazy-effect hover lazyautosizes ls-is-cached lazyloaded" sizes="338px" srcset="/assets/products/1/1c.jpg 400w,
                /web/assetsducts/1/1c.jpg 800w">
              </a>
            </div>
            <!-- End image -->
            <!-- Title -->
            <div class="product-grid-item__title">
              <a href="#" tabindex="0" ${product.name}>Crew neck cotton sweatshirt</a>
            </div>
            <!-- End title -->
            <!-- Rating -->
            <div class="product-grid-item__rating">
              <i class="lnir lnir-star-filled active"></i>
              <i class="lnir lnir-star-filled active"></i>
              <i class="lnir lnir-star-filled active"></i>
              <i class="lnir lnir-star-filled active"></i>
              <i class="lnir lnir-star-filled active"></i>
              <span>3 reviews</span>
            </div>
            <!-- End rating -->
            <!-- Price -->
            <div class="product-grid-item__price" ${product.price}>$79.9</div>
            <!-- End price -->
            <!-- Action -->
            <div class="product-grid-item__action d-flex">
              <!-- Button -->
              <div class="product-grid-item__button">
                <a href="#" tabindex="0">Add to cart</a>
              </div>
              <!-- End button -->
            </div>
            <!-- End action -->
          </div>
            
            `;
        });
        listViewedEl.innerHTML = html;
    } else {
        listViewedEl.innerHTML = `
        <div class="col-12 col-md-6 col-lg-4 col-xl-3 col-xxl-25">
            <p class="text-center">Chưa có sản phẩm nào được xem</p>
        </div>
        `;
    }
}


displayListViewed();





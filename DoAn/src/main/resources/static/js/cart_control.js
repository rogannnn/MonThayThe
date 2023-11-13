$(document).ready(function (){

    $(".minusButton").on("click",function (evt) {
        evt.preventDefault();
        decreaseQuantity($(this));
    })

    $(".plusButton").on("click",function (evt) {
        evt.preventDefault();
        increaseQuantity($(this));
    })

    $(".link-remove").on("click",function (evt) {
        evt.preventDefault();
        Swal.fire({
            title: MESSAGE_NOTIFY.CONFIRM_DEL_ITEM_CART,
            text: '',
            icon: 'warning',
            confirmButtonColor: '#3085d6',
            confirmButtonText:'Xóa',
            showCancelButton: true,
            cancelButtonText:'Quay lại',
            allowOutSideClick: false
        }).then((result) => {
            if(result.isConfirmed){
                removeFromCart($(this));
            }
        });
    })
    updateTotal();
});


function updateCartItemQuantity() {
    $.ajax({
        url:'/cart/count',
        method:'GET',
        success:function (response) {
            if(typeof  response === 'number'){
                $("#cart-item-quantity").text(response);
            }
        }
    })
}

function removeFromCart(link) {
    url = link.attr("href");
    console.log(url);
    $.ajax({
        type: "POST",
        url: url
    }).done(function (response) {
        $("#modalTitle").text("Shopping Cart");
        if(response.includes("removed")){
            rowNumber = link.attr("rowNumber");
            removeProduct(rowNumber);
            updateTotal();
            updateCartItemQuantity();
        }
    }).fail(function () {
        Swal.fire({
            title: '',
            text: MESSAGE_NOTIFY.DEL_ITEM_CART_FAIL,
            icon: 'error',
            confirmButtonColor: '#3085d6',
            timer : 3000,
            timerProgressBar: true,
            allowOutSideClick: false
        })

    })
}


function removeProduct(rowNumber) {
    rowId = "row" + rowNumber;
    $("#"+rowId).remove();
}

function decreaseQuantity(link){
    productId = link.attr("pid");
    qtyInput = $("#quantity" + productId);
    newQuantity = parseInt(qtyInput.val());
    if(newQuantity > 0 ) {
        qtyInput.val(newQuantity);
        updatePriceForItem(productId, newQuantity);
    }
}

function increaseQuantity(link) {
    productId = link.attr("pid");
    qtyInput = $("#quantity" + productId);

    newQuantity = parseInt(qtyInput.val());
    if(newQuantity < 999) {
        qtyInput.val(newQuantity);
        updatePriceForItem(productId, newQuantity);
    }
}

function updatePriceForItem(productId, quantity) {
    quantity = $("#quantity" + productId).val();

    url = contextPath + "cart/update/" + productId + "/" + quantity;

    $.ajax({
        type:"GET",
        url: '/cart/check-quantity-product',
        data:{
            quantity: quantity,
            product: productId
        }
    }).done(function (response) {
        if(response === true){
            $.ajax({
                type: "POST",
                url: url
            }).done(function(newSubTotal){
                updateSubTotal(newSubTotal, productId);
                updateTotal();
            }).fail(function () {
                Swal.fire({
                    title: '',
                    text: MESSAGE_NOTIFY.CONNECT_ERROR,
                    icon: 'error',
                    confirmButtonColor: '#3085d6',
                    timer : 3000,
                    timerProgressBar: true
                })
            });
        }else {
            $("#quantity" + productId).val(quantity - 1);
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.CART_MORE_THAN_INSTOCK,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true,
            })
        }
    }).fail(function () {
        Swal.fire({
            title: '',
            text: MESSAGE_NOTIFY.CONNECT_ERROR,
            icon: 'error',
            confirmButtonColor: '#3085d6',
            timer : 3000,
            timerProgressBar: true,
        })
    })
}

function updateSubTotal(newSubTotal, productId) {
    newSubTotal =  parseInt(newSubTotal);
    var formattedNumber = newSubTotal.toLocaleString();
    $("#subtotal"+productId).text(formattedNumber);
}

function updateTotal() {
    total = 0;

    $(".productSubtotal").each(function (index,element) {
        total = total + parseInt((element.innerHTML).split(',').join(""));
    })
    var formattedNumber = total.toLocaleString();
    $("#totalAmount").text(formattedNumber + "đ");

}


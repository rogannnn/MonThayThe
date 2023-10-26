function updateCartItemQuantity() {
    $.ajax({
        url:'/cart/count',
        method:'GET',
        success:function (response) {
            if(typeof  response === 'number'){
                $("#cart-item-quantity").text(response);
            }else {
            }
        }
    })
}

$(document).ready(function (){
    $("#buttonAddToCart").on("click",function (e) {
        quantityToCart = $("#quantity" + productId).val();
        inStock = parseInt(inStock);
        quantityToCart = parseInt(quantityToCart);
        if(inStock < quantityToCart){
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.MORE_THAN_INSTOCK,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true,
                allowOutSideClick: false
            })
        }else {
            addToCart();
        }

    });
});

function addToCart() {
    quantity = $("#quantity" + productId).val();

    url = contextPath + "cart/add/" + productId + "/" + quantity;

    $.ajax({
        type: "POST",
        url: url
    }).done(function (response) {
        if(response.includes("thành công")){
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.ADD_TO_CART_SUCCESS,
                icon: 'success',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true,
                allowOutSideClick: false
            })
            updateCartItemQuantity();
        }else {
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.NEED_LOGIN_CART,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true,
                allowOutSideClick: false
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
            allowOutSideClick: false
        })
    });

}

// function updateCartItemQuantity() {
//     urlCountItem = contextPath + "cart/count";
//
//     $.ajax({
//         type: "GET",
//         url: urlCountItem
//     }).done(function(response){
//         $("#cart_quantity").text(response);
//     }).fail(function () {
//         $("#cart_quantity").text(0);
//     });
// }


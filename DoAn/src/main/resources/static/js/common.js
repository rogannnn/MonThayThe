function getCartItemCount() {
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
    $("#logoutLink").on("click",function (e) {
        e.preventDefault();
        document.logoutForm.submit();
    });

    getCartItemCount();

});
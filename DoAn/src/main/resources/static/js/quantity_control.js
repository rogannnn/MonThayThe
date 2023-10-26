$(document).ready(function (){
     $(".minusButton").on("click",function (evt) {
          evt.preventDefault();
          productId = $(this).attr("pid");
          qtyInput = $("#quantity" + productId);

          newQuantity = parseInt(qtyInput.val()) - 1;
          if(newQuantity > 0 ) qtyInput.val(newQuantity);
     })

     $(".plusButton").on("click",function (evt) {
          evt.preventDefault();
          productId = $(this).attr("pid");
          qtyInput = $("#quantity" + productId);

          newQuantity = parseInt(qtyInput.val()) + 1;
          if(newQuantity < 99 ) qtyInput.val(newQuantity);
     })
});
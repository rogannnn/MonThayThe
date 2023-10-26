
$(document).ready(function() {
    $("#total").on("click",function (e){
        e.preventDefault();
        loadOrder(7);
        $("a.active").removeClass("active")
        $("#total").addClass("active");
    })
    $("#waitConfirm").on("click",function (e){
        e.preventDefault();
        loadOrder(1);
        $("a.active").removeClass("active")
        $("#waitConfirm").addClass("active");
    })
    $("#waitGood").on("click",function (e){
        e.preventDefault();
        loadOrder(2);
        $("a.active").removeClass("active")
        $("#waitGood").addClass("active");
    })
    $("#delivering").on("click",function (e){
        e.preventDefault();
        loadOrder(3);
        $("a.active").removeClass("active")
        $("#delivering").addClass("active");
    })
    $("#delivered").on("click",function (e){
        e.preventDefault();
        loadOrder(4);
        $("a.active").removeClass("active")
        $("#delivered").addClass("active");
    })
    $("#requestCancel").on("click",function (e){
        e.preventDefault();
        loadOrder(5);
        $("a.active").removeClass("active")
        $("#requestCancel").addClass("active");
    })
    $("#cancelled").on("click",function (e){
        e.preventDefault();
        loadOrder(6);
        $("a.active").removeClass("active")
        $("#cancelled").addClass("active");
    })

})

function loadOrder(number) {
    $.ajax({
        url: "/Shopme/order/" + number,
        type: "GET"
    }).done(function (response) {
        const orderList = response;
        // $('#originalDiv').attr('th:each',response);

        var orderDiv = $("<div>")
        $.each(response, function (index, order) {
            var cloneDiv = $("#originalDiv").clone();
            var cloneSubDiv = $("#originalSubDiv").clone();
            cloneSubDiv.empty();
            cloneDiv.find("#order-id-name").text("Order ID: " + order.id +' | ' + order.orderStatus.name);
            $.each(order.orderDetails, function (index, orderDetails){
                var cloneItem = $("#originalItem").clone();
                cloneItem.find("#order-detail-img").attr("src","/Shopme" + orderDetails.product.productImagePath);
                cloneItem.find("#order-detail-product-name").text(orderDetails.product.name);
                cloneItem.find("#order-detail-product-quantity").text("x" + orderDetails.quantity);
                cloneItem.find("#order-detail-product-init-price").text("$" + orderDetails.unitPrice);

                cloneItem.appendTo(cloneSubDiv);
            })
            cloneDiv.find("#originalSubDiv").remove();
            cloneDiv.find("#headerr").after(cloneSubDiv);
            cloneDiv.find("#order-total-price").text("Total: $" + order.totalPrice);
            // if(number === 1){
            //     $("#requestCancelBtn").attr("display","block");
            // }else $("#requestCancelBtn").attr("display","none");
            if(number != 1) {
                cloneDiv.find('#requestCancelBtn').remove();
            }
            cloneDiv.appendTo(orderDiv);
        })
        $("#displayOrder").empty();
        $("#displayOrder").append(orderDiv);

    }).fail(function () {
        alert("error");
    })
}
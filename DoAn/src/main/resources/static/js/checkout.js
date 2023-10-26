function onlinePayment() {
    url = contextPath + "api/payment/create_payment";
    $.ajax({
        type:"GET",
        url: url,
        data:{
            amount:total,
            addressId:selectedAddressId
        },
        dataType: 'json',
        success: function (response) {
            if(response.status === "OK"){
                paymentURL = response.url;
                window.location.href = paymentURL;
            }else {
                Swal.fire({
                    title: '',
                    text: MESSAGE_NOTIFY.LOGIC_ERROR,
                    icon: 'error'
                })
            }
        },
        error: function (xhr, status, error) {
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.CONNECT_ERROR,
                icon: 'error'
            })
        }
});
}

$(document).ready(function () {
    selectedAddressId = null;
    payment = $("#paymentMethod").val();
    total = 0;
    $(window).one('load',function (){
        loadAddress();
    });
    $(".btn-link").on("click",function (e) {
        displayAddress();
    });
    updateTotalMoney();
    $("#addressModal").on("hidden.bs.modal", function() {
        $("#addressList").empty();
    });
    
    $(".btn-new-address").on('click',function () {
        $("#addressModal").modal('hide');
        $("#newAddressModal").modal('show');
    })

    $("#paymentMethod").on("change", function() {
        payment = $(this).val();

    });

    $('#addressModal').on('shown.bs.modal', function() {
        $('.item').click(function() {
            var radio = $(this).find('input[type="radio"]');
            radio.prop('checked', true);
            selectedAddressId = radio.val();
        });

        $("#addressList").on("change", "input[name='selectedAddress']", function() {
            selectedAddressId = $('input[name="selectedAddress"]:checked').val();
        });
    });

    $(".btn-confirm-address").on("click",function () {
        changeAddress();
    })

    $(".btn-purchase").on("click",function (){
        $.ajax({
            type: "GET",
            url: 'order/check-quantity'
        }).done(function (response) {
            if(response === true){
                if(payment == 1){
                    createOrder();
                }
                else {
                    onlinePayment();
                }
            } else {
                Swal.fire({
                    title: '',
                    text: MESSAGE_NOTIFY.CHECKOUT_MORE_THAN_INSTOCK,
                    icon: 'error'
                })
            }
        }).fail(function () {
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.LOGIC_ERROR,
                icon: 'error'
            })
        });

    });

    closeModalButton = $("#closeModal");
    $("#closeModal").on("click",function () {
        pageRedirect();
    })
});

function pageRedirect(){
    window.location.href = contextPath;
}

function loadAddress() {
    url = contextPath +  "address/all";
    $.ajax({
        type: "GET",
        url: url
    }).done(function (response) {
        addressList = response;
        selectedAddressId = addressList[0].id;
    }).fail(function () {
    });
}

function createOrder() {
    url = contextPath + "order/purchase/" + selectedAddressId;
    $.ajax({
        type:"POST",
        url: url
    }).done(function (response) {

        if(response.includes("success")){
            Swal.fire({
                title: '',
                text: "Đặt hàng thành công",
                icon: 'success',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true,
                allowOutSideClick: false
            }).then((result) => {
                if(result.dismiss  == Swal.DismissReason.timer){
                    window.location.href = '/order';
                } else if(result.isConfirmed){
                    window.location.href = '/order';
                }
            })
        }else {
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.LOGIC_ERROR,
                icon: 'error'
            })
        }
    }).fail(function () {
        Swal.fire({
            title: '',
            text: MESSAGE_NOTIFY.CONNECT_ERROR,
            icon: 'error'
        })
    });
}

function changeAddress() {
    url = contextPath +  "address/" + selectedAddressId;
    $.ajax({
        type: "GET",
        url: url
    }).done(function (response) {
        address = response;
        $(".address-name").text(address.name);
        $(".address-phone").text(address.phone);
        $(".address-specific").text('  ' + address.specificAddress + ',' + address.ward.fullName + ',' +
            address.ward.district.fullName + ',' + address.ward.district.province.fullName);
        selectedAddressId = address.id;
        $("#addressModal").modal('hide');
    }).fail(function () {
        alert("");
    });
}

function displayAddress() {

    url = contextPath +  "address/all";

    $.ajax({
        type: "GET",
        url: url
    }).done(function (response) {
        $("#modalAddressTitle").text("My Address");
        addressList = response;
        addressList.forEach(function (address) {
            li = '<li class="list-group-item" style="text-align: left">' +
                '<div class="item">' +
                '<input type="radio" name = "selectedAddress" value="' + address.id +'"style="margin-right: 10px">' +
                '<span class="bold" style="font-size: 17px">' + address.name + '</span>' +
                '&nbsp; | &nbsp;' +
                '<span class="text-muted">' + address.phone + '</span>' + '&nbsp;' +
                '<br>' +
                '<span class="text-muted">' + address.specificAddress + '</span>' +
                '<br>' +
                '<span class="text-muted">' +address.ward.fullName + '</span>' +
                ','+
                '<span class="text-muted">' +address.ward.district.fullName + '</span>' +
                ','+
                '<span class="text-muted">' + address.ward.district.province.fullName + '</span>' +
                '<span class="is-default">' + (address.isDefault ? 'Default' : '') + '</span>' +
                '</li> </div>';
            $("#addressList").append(li);
        });
        $("#addressModal").modal('show');
    }).fail(function () {
        $("#modalTitle").text("My Address");
        $("#modalBody").text("Error while displaying your addresses");
        $("#myModal").modal('show');
    });
}

function updateTotalMoney() {

    var subPrice = $(".item-price-total");
    total = 0.0;

    subPrice.each(function (){
        priceText = $(this).text();
        console.log(priceText);
        price = parseFloat(priceText.split(',').join(""));
        console.log(price);
        total += price;
    });

    var formattedNumber = total.toLocaleString();

    $(".total-price").text(formattedNumber + 'đ');
}
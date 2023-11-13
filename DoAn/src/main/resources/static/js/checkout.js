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

    $(".btn-link-new-address").on("click",function (e) {
        displayAddressNewAddressModal();
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
            let radio = $(this).find('input[type="radio"]');
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
      if(selectedAddressId == null) {
          Swal.fire({
              title: '',
              text: MESSAGE_NOTIFY.NO_ADDRESS_ERROR,
              icon: 'error',
              confirmButtonColor: '#3085d6',
              timer: 3000,
              timerProgressBar: true
          });
          return;
      }

        if(payment == 1){
            createOrder();
        }
        else {
            onlinePayment();
        }
    });

    closeModalButton = $("#closeModal");
    $("#closeModal").on("click",function () {
        pageRedirect();
    })


    loadProvince();


    $('.btn-confirm-new-address').on("click",function (){
        let name = $('#name').val();
        let phone = $('#phone').val();
        let specificAddress = $('#specificAddress').val();
        let wardCode = $('#inputWard').val();


        if(!name){
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.NAME_BLANK_ERROR,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true
            })
            return;
        }
        let vietnamPhoneRegex = /^(0|\+84)[1-9]\d{8,9}$/;
        if(!phone){
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.PHONE_BLANK_ERROR,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true
            })
            return;
        }
        if (!vietnamPhoneRegex.test(phone)) {
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.PHONE_VALIDATE_ERROR,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true
            })
            return;
        }

        if(!specificAddress){
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.SPECIFIC_ADDRESS_BLANK_ERROR,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true
            })
            return;
        }

        if(!wardCode){
            Swal.fire({
                title: '',
                text: MESSAGE_NOTIFY.FULL_ADDRESS_ERROR,
                icon: 'error',
                confirmButtonColor: '#3085d6',
                timer : 3000,
                timerProgressBar: true
            })
            return;
        }

        $.ajax({
            method:"POST",
            url : '/address/new',
            data: {
                name: name,
                phone : phone,
                specificAddress : specificAddress,
                wardCode: wardCode
            },
            success:function (response) {
                if(response.includes("success")){
                    Swal.fire({
                        title: '',
                        text: MESSAGE_NOTIFY.NEW_ADDRESS_SUCCESS,
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        timer : 3000,
                        timerProgressBar: true
                    }).then((result) => {
                        if(result.dismiss  == Swal.DismissReason.timer){
                            window.location.reload();
                        } else if(result.isConfirmed){
                            window.location.reload();
                        }
                    })
                }else {
                    Swal.fire({
                        title: '',
                        text: MESSAGE_NOTIFY.NEW_ADDRESS_FAIL,
                        icon: 'success',
                        confirmButtonColor: '#3085d6',
                        timer : 3000,
                        timerProgressBar: true
                    })
                }
            },
            error: function () {
                Swal.fire({
                    title: '',
                    text: MESSAGE_NOTIFY.CONNECT_ERROR,
                    icon: 'error'
                })
            }
        })
    });

    //new address modal
    $('#inputProvince').change(function () {
        let selectedValue = $(this).val();
        let selectedText = $(this).find('option:selected').text();

        // var urlGetDistrict =
        // AJAX request to fetch district data
        let urlGetDistrict = contextPath + "address/district/" + selectedValue;
        $.ajax({
            url: urlGetDistrict,
            type: 'GET'
        }).done(function (response) {
            let districtSelect = $('#inputDistrict');
            let wardSelect = $('#inputWard');

            // Clear previous options
            districtSelect.empty();
            wardSelect.empty();

            // Add new options based on the response
            $.each(response, function (index, district) {
                districtSelect.append($('<option>', {
                    value: district.code,
                    text: district.name
                }));
            });
        }).fail(function (xhr, status, error) {
            console.error(error);
        });
    })


    $('#inputDistrict').change(function () {
        let selectedValue = $(this).val();
        let selectedText = $(this).find('option:selected').text();

        // AJAX request to fetch district data
        let urlGetWard = contextPath + "address/ward/" + selectedValue;
        $.ajax({
            url: urlGetWard,
            type: 'GET'
        }).done(function (response) {
            let wardSelect = $('#inputWard');

            // Clear previous options
            wardSelect.empty();

            // Add new options based on the response
            $.each(response, function (index, ward) {
                wardSelect.append($('<option>', {
                    value: ward.code,
                    text: ward.name
                }));
            });
        }).fail(function (xhr, status, error) {
            console.error(error);
        });
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
                text: MESSAGE_NOTIFY.ORDER_SUCCESS,
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
        $(".address-specific").text('  ' + address.specificAddress + ', ' + address.ward.fullName + ', ' +
            address.ward.district.fullName + ', ' + address.ward.district.province.fullName);
        $(".div_address").css("padding-left", "");
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
        $("#modalAddressTitle").text("Địa chỉ của tôi");
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
                ', '+
                '<span class="text-muted">' +address.ward.district.fullName + '</span>' +
                ', '+
                '<span class="text-muted">' + address.ward.district.province.fullName + '</span>' +
                '<span class="is-default">' + (address.isDefault ? 'Default' : '') + '</span>' +
                '</li> ' +
                '</div>';
            $("#addressList").append(li);
        });
        $("#addressModal").modal('show');
    }).fail(function () {
        $("#modalTitle").text("Địa chỉ của tôi");
        $("#modalBody").text("Có lỗi xảy ra khi hiển thị địa chỉ");
        $("#myModal").modal('show');
    });
}


function displayAddressNewAddressModal() {
    $("#newAddressModal").modal('show');
}

function updateTotalMoney() {

    var subPrice = $(".item-price-total");
    total = 0.0;

    subPrice.each(function (){
        priceText = $(this).text();
        price = parseFloat(priceText.split(',').join(""));
        total += price;
    });

    var formattedNumber = total.toLocaleString();

    $(".total-price").text(formattedNumber + 'đ');
}



//new address modal


function loadProvince() {

    let getListProvince = contextPath + "address/province";

    $.ajax({
        type: "GET",
        url: getListProvince
    }).done(function(response){
        var provinceSelect = $('#inputProvince');

        // Add new options based on the response
        $.each(response, function (index, province) {
            provinceSelect.append($('<option>', {
                value: province.code,
                text: province.name
            }));
        });
    }).fail(function (xhr, status, error) {
        console.error(error);
    });
}


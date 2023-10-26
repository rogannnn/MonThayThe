
$(document).ready(function() {
    if(addressId.length > 0){
        updateAddress(wardCode, districtCode, provinceCode);
    }

    loadProvince();

    $('#inputProvince').change(function () {
        var selectedValue = $(this).val();
        var selectedText = $(this).find('option:selected').text();

        // var urlGetDistrict =
        // AJAX request to fetch district data
        var urlGetDistrict = contextPath + "address/district/" + selectedValue;
        $.ajax({
            url: urlGetDistrict,
            type: 'GET'
        }).done(function (response) {
            var districtSelect = $('#inputDistrict');
            var wardSelect = $('#inputWard');

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
        var selectedValue = $(this).val();
        var selectedText = $(this).find('option:selected').text();

        // AJAX request to fetch district data
        var urlGetWard = contextPath + "address/ward/" + selectedValue;
        $.ajax({
            url: urlGetWard,
            type: 'GET'
        }).done(function (response) {
            var wardSelect = $('#inputWard');

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

})

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

function updateAddress(wardId, districtId, provinceId) {
    loadProvince1(provinceId);
    loadDistrict1(provinceId, districtId);
    loadWard1(wardId, districtId);
}

function loadProvince1(provinceId) {

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
        provinceSelect.val(provinceId);
    }).fail(function (xhr, status, error) {
        console.error(error);
    });
}
function loadDistrict1(provinceId, districtId) {
    var urlGetDistrict = contextPath + "address/district/" + provinceId;
    $.ajax({
        url: urlGetDistrict,
        type: 'GET'
    }).done(function (response) {
        var districtSelect = $('#inputDistrict');

        // Add new options based on the response
        $.each(response, function (index, district) {
            districtSelect.append($('<option>', {
                value: district.code,
                text: district.name
            }));
        });
        districtSelect.val(districtId);
    }).fail(function (xhr, status, error) {
        console.error(error);
    });
}
function loadWard1(wardId, districtId){
    var urlGetWard = contextPath + "address/ward/" + districtId;
    $.ajax({
        url: urlGetWard,
        type: 'GET'
    }).done(function (response) {
        var wardSelect = $('#inputWard');

        // Add new options based on the response
        $.each(response, function (index, ward) {
            wardSelect.append($('<option>', {
                value: ward.code,
                text: ward.name
            }));
        });
        wardSelect.val(wardId);
    }).fail(function (xhr, status, error) {
        console.error(error);
    });
}

$('.dropdown-profile').click(function(){
    $('.dropdown-menu-profile').toggleClass('show');
    $('.dropdown-filter').toggleClass('hide-opacity');
});

$('.dropdown-filter').click(function(){

    $('.dropdown-menu-filter').toggleClass('show');

});


function formatNumber(event, input) {
    if(event.which >= 37 && event.which <= 40){
        event.preventDefault();
    }
    input.val(function(index, value) {
        return value
            .replace(/\D/g, "")
            .replace(/([0-9])([0-9]{3})$/, '$1.$2')
            .replace(/\B(?=(\d{3})+(?!\d)\.?)/g, ".")
            .replace(/^0+(\d)/, '$1')
            ;
    });
}

function formatNumber2(event, input) {
    if(event.which >= 37 && event.which <= 40){
        event.preventDefault();
    }
    input.val(function(index, value) {
         value = value
            .replace(/\D/g, "")
            .replace(/^0+(\d)/, '$1')
            ;
         value = Math.min(Math.max(value, 0), 100);
         return value;

    });
}

/** double to price */
function formatPrice(inputPrice) {
    return parseFloat(inputPrice).toLocaleString('en-US', {minimumFractionDigits: 0, maximumFractionDigits: 0}).replaceAll(",", ".");
}


/**  yyyy-mm-dd to dd/mm/yyyy   */
function formatDate(inputDate) {
    let date = new Date(inputDate);
    if (!isNaN(date.getTime())) {
        // Months use 0 index.
        const day = String(date.getDate()).padStart(2, '0');

        return day + '/' + date.getMonth() + 1 + '/' + date.getFullYear();
    }
}


let loadFile = function(event) {
    let output = document.getElementById('image-output');
    let url = URL.createObjectURL(event.target.files[0]);
    output.src = url;

    output.onload = function(){
        URL.revokeObjectURL(output.src) // free memory
        $('#image-output').css('display', 'block')
        $('#image').val(url);
        $('.upload-zone-content').css('display', 'none');
    };
};


function clearFilter(entityName) {
    window.location = `/admin/${entityName}/page/1`;
}


$(document).on('click', '.dropdown-menu', function (e) {
    e.stopPropagation();
});





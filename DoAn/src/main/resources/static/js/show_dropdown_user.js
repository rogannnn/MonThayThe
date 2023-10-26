$(document).ready(function () {
    var button = $('.custom-hidden-mobile-user');
    var div_log_in_out = $('.log-in-out');
    var dropdown_user_menu = $('.dropdown-user-menu');
    var user_avatar = $('.hidden-user-avatar');
    var search_button = $('.search_button');
    var div_search = $('.div-input-search');

    if (button.length > 0) {
        button.click(function () {

            // Kiểm tra xem trạng thái hiện tại của div_log_in_out là gì (block/none)
            const currentDisplay = div_log_in_out.css('display');
            // Nếu div_log_in_out đang ẩn (display là none), thì hiển thị nó (display là block)
            if (currentDisplay === 'none') {
                div_search.addClass('div-input-search');
                div_log_in_out.removeClass('log-in-out');
            } else {
                // Nếu div_log_in_out đang hiển thị, thì ẩn nó
                div_log_in_out.addClass('log-in-out');
            }
        });
    }

    if (user_avatar.length > 0) {
        user_avatar.click(function () {
            const currentDisplay = dropdown_user_menu.css('display');
            if (currentDisplay === 'none') {
                div_search.addClass('div-input-search');
                dropdown_user_menu.removeClass('dropdown-user-menu');
            } else {
                // Nếu div_log_in_out đang hiển thị, thì ẩn nó
                dropdown_user_menu.addClass('dropdown-user-menu');
            }
        });
    }

    search_button.click(function () {

        const currentDisplay = div_search.css('display');
        if (currentDisplay === 'none') {
            div_search.removeClass('div-input-search');
            if (button.length > 0) {
                div_log_in_out.addClass('log-in-out');
            }
            if (user_avatar.length > 0) {
                dropdown_user_menu.addClass('dropdown-user-menu');
            }
        } else {
            // Nếu div_log_in_out đang hiển thị, thì ẩn nó
            div_search.addClass('div-input-search');
        }
    });
});

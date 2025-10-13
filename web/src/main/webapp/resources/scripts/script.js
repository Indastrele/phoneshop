$(document).ready(function() {
    $.ajax({
        headers: {
            'Accept': 'application/json'
        },
        type: "GET",
        url: "/phoneshop-web/ajaxCart",
        success: function (response) {
            updateMiniCartInfo(response);
        }
    })
})

function clickToAdd(phoneId, url) {
    let quantityInput = $("#quantity"+phoneId);
    let quantityTd = quantityInput.closest("td");
    let quantity = quantityInput.val();
    if (isNumericParseInt(quantity)) {
        $.ajax({
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            type: "POST",
            url: url,
            data: JSON.stringify({phoneId: phoneId, quantity: parseInt(quantity)}),
            dataType: "json",
            success: function (response) {
                if (response['errorMessage'] !== null) {
                    handleErrorMessage(quantityTd, response['errorMessage']);
                    return;
                }

                updateMiniCartInfo(response);
                cleanTd(quantityTd);
                quantityTd.append(`<div id='success-message' style='color: green;'>Item has been added to cart</div>`);
            }
        }).fail(function () {
            handleErrorMessage(quantityTd, "Cannot add item");
        });
        return;
    }
    handleErrorMessage(quantityTd,"Quantity can be only integer value");
}

function isNumericParseInt(str) {
    return String(parseInt(str)) === str;
}

function handleErrorMessage(td, message) {
    cleanTd(td);
    console.log(message);
    td.append("<div id='error-message' style='color: red;'>" + message + "</div>");
}

function cleanTd(td) {
    if (td.children().length === 1) {
        return;
    }

    td.children().slice(1).remove();
}

function updateMiniCartInfo(response) {
    let cartTotalQuantitySpan = $("#cart-total-quantity");
    let cartTotalCostSpan = $("#cart-total-cost");

    cartTotalQuantitySpan.text(response.totalQuantity);
    cartTotalCostSpan.text(response.totalCost);
}

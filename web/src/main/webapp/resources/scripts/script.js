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

function clickToAddFromListPage(phoneId, url) {
    let quantityInput = $("#quantity"+phoneId);
    let quantityTd = quantityInput.closest("td");
    cleanBlock(quantityTd, 1);
    checkAndSend(phoneId, url, quantityInput, quantityTd);
}

function clickToAddFromDetailsPage(phoneId, url) {
    let quantityInput = $("#add-to-cart-block input");
    let quantitySpan = $("#add-to-cart-block");
    cleanBlock(quantitySpan, 3);
    checkAndSend(phoneId, url, quantityInput, quantitySpan);
}

function isNumericParseInt(str) {
    return String(parseInt(str)) === str;
}

function checkAndSend(phoneId, url, quantityInput, quantityBlock) {
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
                    handleErrorMessage(quantityBlock, response['errorMessage']);
                    return;
                }

                updateMiniCartInfo(response);
                quantityBlock.append(`<div id='success-message' style='color: green;'>Item has been added to cart</div>`);
            }
        }).fail(function (jqXHR) {
            if (jqXHR.status === 417) {

            }
            handleErrorMessage(quantityBlock, "Cannot add item");
        });
        return;
    }
    handleErrorMessage(quantityBlock,"Quantity can be only integer value");
}

function handleErrorMessage(td, message) {
    td.append("<div id='error-message' style='color: red;'>" + message + "</div>");
}

function cleanBlock(block, amountToSave) {
    if (block.children().length === 1) {
        return;
    }

    block.children().slice(amountToSave).remove();
}

function updateMiniCartInfo(response) {
    let cartTotalQuantitySpan = $("#cart-total-quantity");
    let cartTotalCostSpan = $("#cart-total-cost");

    cartTotalQuantitySpan.text(response.totalQuantity);
    cartTotalCostSpan.text(response.totalCost);
}

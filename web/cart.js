//let cart = $("#cart-items");
console.log("cart.js");

function handleCartData(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);
    var total = 0;
    let confirmation_table = jQuery("#confirmation_table_body")
    let element = jQuery(".cart-items");
    element.html("");
    console.log("handleCartData")
    for (let i = 0; i < resultDataJson.length; i++) {
        let row = "";
        row += '<div class = "cart-row"><div class = "cart-item cart-column">' +
            '<span class ="cart-movie-title">' + resultDataJson[i]['movie_title'] + '</span></div>'

        var price = parseFloat(resultDataJson[i]['quantity']) * 9.99;
        price = Math.round(price * 100) / 100;
        row += '<span class = "cart-price cart-column">$' + price + '</span>'
        row += '<div class = "cart-quantity cart-column">' + '<input class = "cart-quantity-input type = "number" '
            + 'value = "' + resultDataJson[i]['quantity'] + '">'
            + '<button class = "btn btn-danger remove-item-from-cart-button" type ="button">REMOVE</button></div></div>'

        element.append(row);

        let confirmation_table_row = "";
        confirmation_table_row += '<tr>';
        confirmation_table_row += '<th>' + resultDataJson[i]['movie_title'] + '</th>'
        confirmation_table_row += '<th>' + resultDataJson[i]['quantity'] + '</th>'
        confirmation_table_row += '<th>$' + price + '</th>'
        confirmation_table_row += "</tr>";

        confirmation_table.append(confirmation_table_row);
        total += parseFloat(resultDataJson[i]['quantity']) * 9.99;

    }


    console.log("total")
    console.log(total);
    total = Math.round(total * 100) / 100;
    $(".cart-total-price").text("$" + total);

    let total_price_row = '<tr><th></th><th></th><th>$' + total + '</th></tr>';
    confirmation_table.append(total_price_row);

    var removeItemFromCartButtons= document.getElementsByClassName('remove-item-from-cart-button');
    console.log(removeItemFromCartButtons.length);
    for (var i = 0; i < removeItemFromCartButtons.length; i++) {
        var button = removeItemFromCartButtons[i];
        button.addEventListener('click', removeFromCartClicked);
    }

    var quantityInputs = document.getElementsByClassName('cart-quantity-input')
    for (var i = 0; i < quantityInputs.length; i++) {
        var inputs = quantityInputs[i]
        inputs.addEventListener('change', changeQuantity)
    }


}

function removeFromCartClicked(event) {
    event.preventDefault();
    var button = event.target;
    var shopItem = button.parentElement.parentElement;

    console.log(shopItem.getElementsByClassName('cart-movie-title')[0].innerHTML);
    var movietitle = shopItem.getElementsByClassName('cart-movie-title')[0].innerHTML;
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '#cart_page');
    form.setAttribute('id', "remove");
    var removeMovie = document.createElement("input");

    removeMovie.value = movietitle;
    removeMovie.name = "movie-title";
    form.appendChild(removeMovie);
    document.body.appendChild(form);
    form.submit();

    let cart = $("#remove")
    $.ajax("api/remove", {
        method: "POST",
        data: cart.serialize(),
        success: handleCartData

    })
}

function changeQuantity(event) {
    var user_input = event.target;
    var button = event.target;
    var shopItem = button.parentElement.parentElement;

    if (user_input.value < 1 || isNaN(user_input.value)) {
        user_input.value = 1;
    }
    console.log(shopItem.getElementsByClassName('cart-movie-title')[0].innerHTML);
    var movietitle = shopItem.getElementsByClassName('cart-movie-title')[0].innerHTML;
    console.log(user_input.value);
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', '#cart_page');
    form.setAttribute('id', "changeQuantity");
    var quantity = document.createElement("input");
    var movie = document.createElement("input");
    movie.value = movietitle;
    movie.name = "movie-title";
    quantity.value = user_input.value;
    quantity.name = "quantity";
    form.appendChild(movie);
    form.appendChild(quantity);
    document.body.appendChild(form);
    form.submit();

    let cart = $("#changeQuantity")
    $.ajax("api/change-quantity", {
        method: "POST",
        data: cart.serialize(),
        success: handleCartData

    })

}



$.ajax("api/cart", {
    method: "GET",
    success: handleCartData
});




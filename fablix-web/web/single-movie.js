function getParameterByName(target) {
    let url = window.location.href;
    target = target.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function singleMovieResult(resultData) {
    console.log("handleStarResult: populating single movie data from resultData");

    let movieTitle = jQuery("#movie_title");
    movieTitle.append('<p class = "movie-title">' + resultData[0]['movie_title'] + "(" + resultData[0]['movie_year'] + ")</p>");

    let element = jQuery("#single_movie_table_body");

    let row = "";
    row += "<tr>";
    var genreArray = resultData[0]['movie_genres'].split(",");
    var starArray;
    if(resultData[0]['movie_stars'] === null){
        starArray = ['N/A'];
    }else{
        starArray = resultData[0]['movie_stars'].split(",");
    }
    var starIdArray;
    if(resultData[0]['star_id']=== null){
        starIdArray = ['N/A'];
    }else{
        starIdArray = resultData[0]['star_id'].split(",");
    }

    row += "<th>" + resultData[0]['movie_director'] + "</th>";

    row += "<th>";
    for(let j=0; j<genreArray.length; j++){
        row += '<a href="browse.html?browse=' + genreArray[j] + '">' + genreArray[j] + '</a>';
        if (j != genreArray.length-1){
            row += ", ";
        }
    }
    row += "</th>";
    row += "<th>";
    for(let k=0; k< starArray.length; k++){
        row += '<a href="single-star.html?id=' + starIdArray[k] + '">' + starArray[k] + '</a>';
        if (k != starArray.length-1){
            row += ", ";
        }
    }
    row += "</th>";

    if(resultData[0]['movie_rating'] === null){
        row += "<th>N/A</th>";
    }else{
        row += "<th>" + resultData[0]['movie_rating'] + "</th>";
    }

    row += "<th>$9.99</th>";
    row += '<th><a href = "index.html#cart_page" class = "btn btn-primary add-to-cart-button">ADD TO CART</a></th>'
    row += "</tr>";
    element.append(row);

    var addToCartButtons = document.getElementsByClassName('add-to-cart-button');
    console.log(addToCartButtons.length);
    for (var i = 0; i < addToCartButtons.length; i++) {
        var button = addToCartButtons[i];
        button.addEventListener('click', addToCartClicked);
    }
}


function addToCartClicked(event) {
    event.preventDefault();
    var button = event.target;
    var shopItem = button.parentElement.parentElement;
    var movietitle = document.getElementsByClassName("movie-title")[0].outerText.split("(")[0];
    console.log(movietitle);
    var form = document.createElement('form');
    form.setAttribute('method', 'post');
    form.setAttribute('action', 'index.html#cart_page');
    form.setAttribute('id', "cart");
    var product = document.createElement("input");
    var quantity = document.createElement("input");
    quantity.value = "1";
    quantity.name = "quantity";
    product.value = movietitle;
    product.name = "movie-title";
    form.appendChild(product);
    form.appendChild(quantity);
    document.body.appendChild(form);
    form.submit();

    let cart = $("#cart")
    $.ajax("api/cart", {
        method: "POST",
        data: cart.serialize(),
        success: handleCartArray

    })
}

function handleCartArray(resultDataString) {
    console.log(resultDataString);
}


let movieId = getParameterByName('id');

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "single-movie?id=" + movieId,
    success: function(resultData){
        singleMovieResult(resultData);
    }
});

/*
Autocomplete
 */
var cache_sug = {};

function handleLookup(query, doneCallback) {
    console.log("autocomplete initiated")

    // TODO: if you want to check past query results first, you can do it here
    if(query in cache_sug){
        handleCacheLookup(cache_sug[query], query, doneCallback)
    }else{
        jQuery.ajax({
            "method": "GET",
            "url": "movie-suggestion?query=" + escape(query),
            "success": function(data) {
                handleLookupAjaxSuccess(data, query, doneCallback);
            },
            "error": function(errorData) {
                console.log("lookup ajax error")
                console.log(errorData)
            }
        })
    }
}

function handleCacheLookup(data, query, doneCallback) {
    console.log("Suggestion list from FRONT-END")
    console.log(data)
    var temp = [];
    for(var k in data){
        temp.push(data[k]['value']);
    }
    console.log("Suggestion List")
    console.log(temp)
    doneCallback({suggestions: data})
}

function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful with " + query);
    console.log("Suggestion list from BACK-END")
    var jsonData = JSON.parse(data);
    console.log(jsonData)
    var temp = [];
    for(var k in jsonData){
        temp.push(jsonData[k]['value']);
    }
    console.log("Suggestion List")
    console.log(temp)
    // TODO: if you want to cache the result into a global variable you can do it here
    cache_sug[query] = jsonData;
    console.log(query + " cached")
    doneCallback( { suggestions: jsonData } );

}

function handleSelectSuggestion(suggestion) {
    // TODO: jump to the specific result page based on the selected suggestion
    //console.log("you select " + suggestion["title"] + " with ID " + suggestion["data"]["heroID"])
    console.log(suggestion['value']);
    location.replace("single-movie.html?id="+suggestion["data"]["id"]);
}


$('#autocomplete').autocomplete({
    lookup: function (query, doneCallback) {
        handleLookup(query, doneCallback)
    },
    onSelect: function(suggestion) {
        handleSelectSuggestion(suggestion)
    },
    deferRequestBy: 300,
    // there are some other parameters that you might want to use to satisfy all the requirements
    // TODO: add other parameters, such as minimum characters
});

function handleNormalSearch(query) {
    console.log("doing normal search with query: " + query);
    // TODO: you should do normal search here
}

$('#autocomplete').keypress(function(event) {
    if (event.keyCode == 13) {
        handleNormalSearch($('#autocomplete').val())
    }
})
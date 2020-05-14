if ( window.history.replaceState ) {
    window.history.replaceState( null, null, window.location.href );
}

function getParameterByName(target) {
    let url = window.location.href;
    target = target.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

/* Movie list page functions */

let pageNum = 0; // current page number
let numPage = 0; // total page number
let jsonData = []; // json data
let numResult = 0; // number of rows a page
let ilist = [];

function splitPage() {

    let userInput = document.getElementById("numResult").value;
    numResult = 10;
    if(userInput==="10"){
        numResult = 10;
    }else if(userInput==="25"){
        numResult = 25;
    }else if(userInput==="50"){
        numResult = 50;
    }else if(userInput==="100"){
        numResult = 100;
    }

    numPage = Math.ceil(jsonData.length/numResult);
    ilist = splitArray(jsonData, numResult);
    $("#page_number").text((pageNum+1) + " of " + numPage);
    pagination();
}

function splitArray(array, part) {
    var tmp = [];
    for(var i = 0; i < array.length; i += part) {
        tmp.push(array.slice(i, i + part));
    }
    return tmp;
}


function pagination() {

    $("#movie_table_body tr").remove();
    let element = jQuery("#movie_table_body");

    for (let i = 0; i < ilist[pageNum].length; i++) {
        let row = "";
        row += "<tr>";

        //row += "<th>" + '<a href="single-movie.html?id=' + list[pageNum][i]['movie_id'] + '">' + list[pageNum][i]['movie_title'] + "</a>" + "</th>";
        row += "<th>" + '<a class = "movie-title" href="single-movie.html?id=' + ilist[pageNum][i]['movie_id'] + '">' + ilist[pageNum][i]['movie_title'] + "</a>" + "</th>";
        row += "<th>" + ilist[pageNum][i]['movie_year'] + "</th>";
        row += "<th>" + ilist[pageNum][i]['movie_director'] + "</th>";

        var genreArray = ilist[pageNum][i]['movie_genres'].split(",");
        var starArray = ilist[pageNum][i]['movie_stars'].split(",");
        var starIdArray = ilist[pageNum][i]['star_id'].split(",");

        row += "<th>";
        for (let j = 0; j < Math.min(3, genreArray.length); j++) {
            row += '<a href="browse.html?browse=' + genreArray[j] + '">' + genreArray[j] + '</a>';
            if (j < (Math.min(3, genreArray.length)) - 1) {
                row += ", ";
            }
        }
        row += "</th>";
        row += "<th>";
        for (let k = 0; k < Math.min(3, starArray.length); k++) {
            row += '<a href="single-star.html?id=' + starIdArray[k] + '">' + starArray[k] + '</a>';
            if (k < (Math.min(3, starArray.length)) - 1) {
                row += ", ";
            }
        }
        row += "</th>";
        if(ilist[pageNum][i]['movie_rating'] === null){
            row += "<th>N/A</th>";
        }else{
            row += "<th>" + ilist[pageNum][i]['movie_rating'] + "</th>";
        }
        row += "<th>$9.99</th>";
        row += '<th><a href = "index.html#cart_page" class = "btn btn-primary add-to-cart-button">ADD TO CART</a></th>'
        row += "</tr>";
        element.append(row);

    }
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
    console.log(shopItem);
    console.log(shopItem.getElementsByClassName('movie-title'));
    console.log(shopItem.getElementsByClassName('movie-title')[0].innerHTML);
    var movietitle = shopItem.getElementsByClassName('movie-title')[0].innerHTML;
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


function getSortedData(sortData) {
    jsonData = sortData;
    splitPage();
}

function nextPage() {
    if ((pageNum + 1) < numPage) {
        pageNum = pageNum + 1;
    }
    splitPage();
}

function prevPage() {
    if ((pageNum - 1) >= 0) {
        pageNum = pageNum - 1;
    }
    splitPage();
}


/* Advanced search functions */

function openForm() {
    document.getElementById("searchPopUp").style.display = "block";
}

function closeForm() {
    document.getElementById("searchPopUp").style.display = "none";
}

window.onclick = function (event) {
    var x = document.getElementById("searchPopUp");
    if (event.target == x) {
        closeForm();
    }
}

/* Main page functions */

function popularMovies(resultData) {
    let first_popular = jQuery("#first_popular");
    first_popular.append("<h3>" + resultData[0]['movie_title'] + "</h3>");
    first_popular.append("<p>" + resultData[0]['movie_year'] + "</p>");
    first_popular.append("<p>" + resultData[0]['movie_director'] + "</p>");
    first_popular.append('<a href="single-movie.html?id=' + resultData[0]['movie_id'] + '">' + "(more info..)</a>");

    let second_popular = jQuery("#second_popular");
    second_popular.append("<h3>" + resultData[1]['movie_title'] + "</h3>");
    second_popular.append("<p>" + resultData[1]['movie_year'] + "</p>");
    second_popular.append("<p>" + resultData[1]['movie_director'] + "</p>");
    second_popular.append('<a href="single-movie.html?id=' + resultData[1]['movie_id'] + '">' + "(more info..)</a>");

    let third_popular = jQuery("#third_popular");
    third_popular.append("<h3>" + resultData[2]['movie_title'] + "</h3>");
    third_popular.append("<p>" + resultData[2]['movie_year'] + "</p>");
    third_popular.append("<p>" + resultData[2]['movie_director'] + "</p>");
    third_popular.append('<a href="single-movie.html?id=' + resultData[2]['movie_id'] + '">' + "(more info..)</a>");
}

function newRelease(resultData) {
    var list = resultData.slice(0);
    list.sort(function (a, b) {
        var x = a['movie_year'];
        var y = b['movie_year'];
        return x > y ? -1 : x < y ? 1 : 0;
    });

    let first = jQuery("#first_release");
    first.append("<h3>" + list[0]['movie_title'] + "</h3>");
    first.append("<p>" + list[0]['movie_year'] + "</p>");
    first.append("<p>" + list[0]['movie_director'] + "</p>");
    first.append('<a href="single-movie.html?id=' + list[0]['movie_id'] + '">' + "(more info..)</a>");

    let second = jQuery("#second_release");
    second.append("<h3>" + list[1]['movie_title'] + "</h3>");
    second.append("<p>" + list[1]['movie_year'] + "</p>");
    second.append("<p>" + list[1]['movie_director'] + "</p>");
    second.append('<a href="single-movie.html?id=' + list[1]['movie_id'] + '">' + "(more info..)</a>");

    let third = jQuery("#third_release");
    third.append("<h3>" + list[2]['movie_title'] + "</h3>");
    third.append("<p>" + list[2]['movie_year'] + "</p>");
    third.append("<p>" + list[2]['movie_director'] + "</p>");
    third.append('<a href="single-movie.html?id=' + list[2]['movie_id'] + '">' + "(more info..)</a>");
}

jQuery.ajax(
    {
        dataType: "json",
        method: "GET",
        url: "movie-list",
        success: function (resultData) {
            jsonData = resultData;
            popularMovies(resultData);
            newRelease(resultData);
            splitPage();
            getAllGenres(resultData);
        }
    }
);


/* SPA functions */

function showPage(pageId) {
    $(".page").hide();
    $(pageId).show();
}

$(window).on("hashchange", function (event) {
        showPage(location.hash);
    }
);

showPage(window.location.hash);


/* browse function */

function getAllGenres(resultData){
    let temp = [];
    for(let i=0; i < resultData.length; i++){
        let genreArray = resultData[i]['movie_genres'].split(",");
        for(let j=0; j < genreArray.length; j++){
            if(!temp.includes(genreArray[j])){
                temp.push(genreArray[j]);
            }
        }
    }
    let genreElement = jQuery("#browse_by_genre");
    for(let i=0; i < temp.length; i++){
        let row = "";
        if(i===0){
            row += '<a href="browse.html?browse=' + temp[i] + '">' + temp[i] + "</a> ";
        }else{
            row += '| <a href="browse.html?browse=' + temp[i] + '">' + temp[i] + "</a> ";
        }
        genreElement.append(row);
    }
}


/*
    Autocomplete functions
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
    doneCallback({suggestions: data})
}

function handleLookupAjaxSuccess(data, query, doneCallback) {
    console.log("lookup ajax successful with " + query);
    console.log("Suggestion list from BACK-END")
    var jsonData = JSON.parse(data);
    console.log(jsonData)
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


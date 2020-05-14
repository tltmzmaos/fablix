function getUrlVars() {
    var vars = {};
    var parts = window.location.href.replace(/[?&]+([^=&]+)=([^&]*)/gi,
        function(m,key,value) {
            vars[key] = value;
        });
    return vars;
}

var p_num = 0;
var total_page_number = 0;
var jdata = [];
var number_row = 0;
var jlist = [];

function pageSplit(){
    number_row = 10;
    let user_row = document.getElementById("num_rows").value;
    if(user_row==="10"){
        number_row = 10;
    }else if(user_row==="25"){
        number_row = 25;
    }else if(user_row==="50"){
        number_row = 50;
    }else if(user_row==="100"){
        number_row = 100;
    }

    total_page_number = Math.ceil(jdata.length/number_row);
    jlist = arrSplit(jdata, number_row);
    $("#page_num").text((p_num+1) + " of " + total_page_number);
    paginations();
}

function arrSplit(array, part) {
    var tmp = [];
    for(var i = 0; i < array.length; i += part) {
        tmp.push(array.slice(i, i + part));
    }
    return tmp;
}

function paginations(){

    $("#advanced_search_table_body tr").remove();
    let element = jQuery("#advanced_search_table_body");

    if(jdata.length === 0){
        $("#advanced_search_no_result").text("No Result");
    }

    for(let i=0; i < jlist[p_num].length; i++){
        let row = "";
        row += "<tr>";
        row += "<th>" + '<a class = "movie-title" href="single-movie.html?id=' + jlist[p_num][i]['movie_id'] + '">' + jlist[p_num][i]['movie_title'] + "</a>" + "</th>";
        row += "<th>" + jlist[p_num][i]['movie_year'] + "</th>";
        row += "<th>" + jlist[p_num][i]['movie_director'] + "</th>";

        var genreArray = jlist[p_num][i]['movie_genres'].split(",");
        var starArray = jlist[p_num][i]['movie_stars'].split(",");
        var starIdArray = jlist[p_num][i]['star_id'].split(",");

        row += "<th>";
        for(let j=0; j<Math.min(3, genreArray.length); j++){
            row += '<a href="browse.html?browse=' + genreArray[j] + '">' + genreArray[j] + '</a>';
            if (j < (Math.min(3, genreArray.length))-1){
                row += ", ";
            }
        }
        row += "</th>";
        row += "<th>";
        for(let k=0; k<Math.min(3, starArray.length); k++){
            row += '<a href="single-star.html?id=' + starIdArray[k] + '">' + starArray[k] + '</a>';
            if (k < (Math.min(3, starArray.length))-1){
                row += ", ";
            }
        }
        row += "</th>";
        if(jlist[p_num][i]['movie_rating'] === null){
            row += "<th>N/A</th>";
        }else{
            row += "<th>" + jlist[p_num][i]['movie_rating'] + "</th>";
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
    var movietitle = shopItem.getElementsByClassName('movie-title')[0].innerHTML
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


function getSortedData(sortData){
    jdata = sortData;
    pageSplit();
}

function nextPage(){
    if((p_num + 1) < total_page_number){
        p_num = p_num + 1;
    }
    pageSplit();
}

function prevPage() {
    if((p_num - 1) >= 0){
        p_num = p_num - 1;
    }
    pageSplit();
}

function searchSorting(){
    let sortType = document.getElementById("sort_selects").value;
    let list = jdata.slice(0);
    if(sortType === "TDRD"){ // Title Desc, Rating Desc
        list.sort(function(a,b){
            var x = a['movie_title'];
            var y = b['movie_title'];
            return x > y ? -1 : x < y ? 1 : 0;
        });
    }else if(sortType === "TDRA"){ // Title Desc, Rating Asc
        list.sort(function(a,b){
            var x = a['movie_title'];
            var y = b['movie_title'];
            if(x > y){
                return -1;
            }else if(x < y){
                return 1;
            }else if(x === y){
                var aRate = a['movie_rating'];
                var bRate = b['movie_rating'];
                if(aRate < bRate){
                    return -1;
                }else if(aRate > bRate){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
    }

    // Rating Desc
    else if(sortType === "RDTA"){
        list.sort(function(a,b){
            var x = a['movie_rating'];
            var y = b['movie_rating'];
            if(x > y){
                return -1;
            }else if(x < y){
                return 1;
            }else if(x == y){
                var aTitle = a['movie_title'];
                var bTitle = b['movie_title'];
                if(aTitle > bTitle){
                    return 1;
                }else if(aTitle < bTitle){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }
    else if(sortType === "RDTD"){
        list.sort(function(a,b){
            var x = a['movie_rating'];
            var y = b['movie_rating'];
            if(x > y){
                return -1;
            }else if(x < y){
                return 1;
            }else if(x === y){
                var aTitle = a['movie_title'];
                var bTitle = b['movie_title'];
                if(aTitle < bTitle){
                    return 1;
                }else if(aTitle > bTitle){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }

    // Title Asc
    else if(sortType === "TARD"){
        list.sort(function(a,b){
            var x = a['movie_title'];
            var y = b['movie_title'];
            return x < y ? -1 : x > y ? 1 : 0;
        });
    } else if(sortType === "TARA"){ // Title Asc, Rating Asc
        list.sort(function(a,b){
            var x = a['movie_title'];
            var y = b['movie_title'];
            if(x < y){
                return -1;
            }else if(x > y){
                return 1;
            }else if(x === y){
                var aRate = a['movie_rating'];
                var bRate = b['movie_rating'];
                if(aRate < bRate){
                    return -1;
                }else if(aRate > bRate){
                    return 1;
                }else{
                    return 0;
                }
            }
        });
    }

    // Rating Asc
    else if(sortType === "RATA"){
        list.sort(function(a,b){
            var x = a['movie_rating'];
            var y = b['movie_rating'];
            if(x < y){
                return -1;
            }else if(x > y){
                return 1;
            }else if(x === y){
                var aTitle = a['movie_title'];
                var bTitle = b['movie_title'];
                if(aTitle > bTitle){
                    return 1;
                }else if(aTitle < bTitle){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }
    else if(sortType === "RATD"){
        list.sort(function(a,b){
            var x = a['movie_rating'];
            var y = b['movie_rating'];
            if(x < y){
                return -1;
            }else if(x > y){
                return 1;
            }else if(x === y){
                var aTitle = a['movie_title'];
                var bTitle = b['movie_title'];
                if(aTitle < bTitle){
                    return 1;
                }else if(aTitle > bTitle){
                    return -1;
                }else{
                    return 0;
                }
            }
        });
    }
    getSortedData(list);
}

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

let titleInput = getUrlVars()['title'];
let yearInput = getUrlVars()['year'];
let directorInput = getUrlVars()['director'];
let starInput = getUrlVars()['star'];

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "advanced-search?title=" + titleInput + "&year=" + yearInput + "&director=" + directorInput + "&star=" + starInput,
    success: function (resultJsonData) {
        jdata = resultJsonData;
        pageSplit();
    }
});

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
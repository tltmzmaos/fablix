function getParameterByName(target) {
    let url = window.location.href;
    target = target.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

var page_number = 0;
var total_page_number = 0;
var data = [];
var number_row = 0;
var list = [];

function pSplit(){
    let user_row = document.getElementById("num_row").value;
    number_row = 10;
    if(user_row==="10"){
        number_row = 10;
    }else if(user_row==="25"){
        number_row = 25;
    }else if(user_row==="50"){
        number_row = 50;
    }else if(user_row==="100"){
        number_row = 100;
    }

    total_page_number = Math.ceil(data.length/number_row);
    list = aSplit(data, number_row);
    $("#page_num").text((page_number+1) + " of " + total_page_number);
    pgination();
}

function aSplit(array, part) {
    var tmp = [];
    for(var i = 0; i < array.length; i += part) {
        tmp.push(array.slice(i, i + part));
    }
    return tmp;
}

function pgination(){
    $("#search_header").text("searched by '" + getParameterByName('searchBar') + "'");

    $("#search_table_body tr").remove();
    let element = jQuery("#search_table_body");

    if(data.length === 0){
        $("#no_result").text("No Result");
    }

    for(let i=0; i < list[page_number].length; i++){
        let row = "";
        row += "<tr>";
        //row += "<th>" + '<a href="single-movie.html?id=' + list[pageNum][i]['movie_id'] + '">' + list[pageNum][i]['movie_title'] + "</a>" + "</th>";
        row += "<th>" + '<a class = "movie-title" href="single-movie.html?id=' + list[page_number][i]['movie_id'] + '">' + list[page_number][i]['movie_title'] + "</a>" + "</th>";
        row += "<th>" + list[page_number][i]['movie_year'] + "</th>";
        row += "<th>" + list[page_number][i]['movie_director'] + "</th>";

        var genreArray = list[page_number][i]['movie_genres'].split(",");
        var starArray = list[page_number][i]['movie_stars'].split(",");
        var starIdArray = list[page_number][i]['star_id'].split(",");

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
        if(list[page_number][i]['movie_rating'] === null){
            row += "<th>N/A</th>";
        }else{
            row += "<th>" + list[page_number][i]['movie_rating'] + "</th>";
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

}

function getSortedData(sortData){
    data = sortData;
    pSplit();
}

function nextPage(){
    if((page_number + 1) < total_page_number){
        page_number = page_number + 1;
    }
    pSplit();
}

function prevPage() {
    if((page_number - 1) >= 0){
        page_number = page_number - 1;
    }
    pSplit();
}

function searchSorting(){
    let sortType = document.getElementById("sort_select").value;
    let list = data.slice(0);

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
    if (event.target === x) {
        closeForm();
    }
}

let searchInput = getParameterByName("searchBar");

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "search?searchBar=" + searchInput,
    success: function (rdata) {
        data = rdata;
        pSplit();
    }
});

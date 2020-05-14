function getParameterByName(target) {
    let url = window.location.href;
    target = target.replace(/[\[\]]/g, "\\$&");
    let regex = new RegExp("[?&]" + target + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}

function singleStarResult(resultData) {
    console.log("handleStarResult: populating single movie data from resultData");

    let starInfoElement = jQuery("#star_info");
    var starName = resultData[0]['star_name'];
    var starBY = resultData[0]['star_by'];

    let starRow = "";
    starRow += "<p>" + starName + "</p>";
    if (starBY == null){
        starRow += "<p id='details'>Birth of Year: N/A</p>";
    }else{
        starRow += "<p id='details'>Birth of Year: " + starBY + "</p>";
    }

    starInfoElement.append(starRow);

    let movieElement = jQuery("#star_movie_table");
    let row = "";
    var movieArray = resultData[0]['movies'].split(",");
    var movieIdArray = resultData[0]['movieId'].split(",");
    for(let i=0; i < movieArray.length; i++){
        row += "<tr><th>"
        row += '<a href="single-movie.html?id=' + movieIdArray[i] + '">' + movieArray[i] + '</a>';
        row += "</th></tr>"
    }

    movieElement.append(row);
}

let starId = getParameterByName('id');

jQuery.ajax({
    dataType: "json",
    method: "GET",
    url: "single-star?id=" + starId,
    success: (resultData) => singleStarResult(resultData)
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
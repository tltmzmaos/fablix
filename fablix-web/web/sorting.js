var sortType = "RDTA";
var x = [];
function sorting(){
    var sortType = document.getElementById("sortSelect").value;
    var list = x.slice(0);

    // Title Desc
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

jQuery.ajax(
    {
        dataType: "json",
        method: "GET",
        url: "movie-list",
        success: function(resultData){
            x = resultData;
        }
    }
);


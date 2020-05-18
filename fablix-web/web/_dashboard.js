
function handleResult(resultData){
    let div = jQuery("#meta-div");
    let jsonKeys = Object.keys(resultData);
    for(let i=0; i<jsonKeys.length; i++){
        let temp = "";
        temp += "<div class='col-lg-3'>";
        temp += "<h2>" + jsonKeys[i] + "</h2>";
        for(let j=0; j<resultData[jsonKeys[i]].length; j++){
            temp += "<p class='meta-p'>" + resultData[jsonKeys[i]][j]['column_name'] + " - ";
            temp += resultData[jsonKeys[i]][j]['column_type'];
            if(resultData[jsonKeys[i]][j]['nullable'] === 0){
                temp += ", NOT NULL";
            }
            if(resultData[jsonKeys[i]][j]['increment'] === 'true'){
                temp += ", AUTO INCREMENT";
            }
        }
        div.append(temp);
    }
}

jQuery.ajax(
    {
        dataType: "json",
        method: "GET",
        url: "metadata",
        success: function (resultData) {
            handleResult(resultData)
        }
    }
);
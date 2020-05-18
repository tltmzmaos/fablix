let new_star_form = $("#newstar-form");

function handleSubmitResult(resultdata){
    let jsonData = JSON.parse(resultdata);
    console.log(jsonData);

    console.log("Star name " + jsonData['star_name']);
    console.log("star id " + jsonData['star_id']);
    console.log("star year " + jsonData['star_year']);

    jQuery("#new_star_confirm").text(jsonData['message']);
}

function submitNewStar(submitEvent){
    console.log("Add a new star");
    submitEvent.preventDefault();
    $.ajax(
        "new-star", {
            method: "POST",
            data: new_star_form.serialize(),
            success: handleSubmitResult
        }
    )
}


new_star_form.submit(submitNewStar);
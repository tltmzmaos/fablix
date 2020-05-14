let new_movie_form = $("#newmovie-form");

function handleSubmitResult(resultdata){
    let jsonData = JSON.parse(resultdata);
    console.log(jsonData);

    console.log(jsonData['message']);
    jQuery("#new_movie_confirm").text(jsonData['message']);
}

function submitNewMovie(submitEvent){
    console.log("add new movie");
    submitEvent.preventDefault();
    $.ajax(
        "add-movie", {
            method: "POST",
            data: new_movie_form.serialize(),
            success: handleSubmitResult
        }
    )
}


new_movie_form.submit(submitNewMovie);
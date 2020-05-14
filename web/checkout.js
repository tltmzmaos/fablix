let checkout_form = $("#checkout_form");

function handleCheckoutResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);
    console.log(resultDataJson);
    if (resultDataJson["status"] === "success") {
        console.log("Sale Id" + resultDataJson["sale_id"]);
        jQuery("#sale_id").text("Sale ID# " + resultDataJson["sale_id"]);

        window.location.replace("index.html#confirmation-page");



    } else {
        alert("Incorrect Information");
    }

}


function submitCheckoutForm(formSubmitEvent) {
    console.log("submit checkout form");
    formSubmitEvent.preventDefault();
    $.ajax(
        "checkout", {
            method: "POST",
            data: checkout_form.serialize(),
            success: handleCheckoutResult
        }
    );
}

checkout_form.submit(submitCheckoutForm);
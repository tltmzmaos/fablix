let login_form = $("#login_form");

function handleLoginResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    if (resultDataJson["status"] === "success") {
        window.location.replace("index.html#main");
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#login_error_message").text(resultDataJson["message"]);
    }
}

function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    formSubmitEvent.preventDefault();
    $.ajax(
        "login", {
            method: "POST",
            data: login_form.serialize(),
            success: handleLoginResult
        }
    );
}

login_form.submit(submitLoginForm);


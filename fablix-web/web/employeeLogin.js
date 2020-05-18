let login_form = $("#employee_login_form");

function handleLoginResult(resultDataString) {
    let resultDataJson = JSON.parse(resultDataString);

    console.log("handle login response");
    console.log(resultDataJson);
    console.log(resultDataJson["status"]);

    if (resultDataJson["status"] === "success") {
        window.location.replace("_dashboard.html");
    } else {
        console.log("show error message");
        console.log(resultDataJson["message"]);
        $("#employee_login_error_message").text(resultDataJson["message"]);
    }
}

function submitLoginForm(formSubmitEvent) {
    console.log("submit login form");
    formSubmitEvent.preventDefault();
    $.ajax(
        "employeeLogin", {
            method: "POST",
            data: login_form.serialize(),
            success: handleLoginResult
        }
    );
}

login_form.submit(submitLoginForm);
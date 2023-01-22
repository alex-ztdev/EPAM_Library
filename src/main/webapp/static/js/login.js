function setFormMessage(formElement, type, message) {
    const messageElement = formElement.querySelector(".form__message");

    messageElement.textContent = message;
    messageElement.classList.remove("form__message--success", "form__message--error");
    messageElement.classList.add(`form__message--${type}`);
}

function setInputError(inputElement, message) {
    inputElement.classList.add("form__input--error");
    inputElement.parentElement.querySelector(".form__input-error-message").textContent = message;
}

function clearInputError(inputElement) {
    inputElement.classList.remove("form__input--error");
    inputElement.parentElement.querySelector(".form__input-error-message").textContent = "";
}

document.addEventListener("DOMContentLoaded", () => {
    const loginForm = document.querySelector("#login");
    const createAccountForm = document.querySelector("#createAccount");

    document.querySelector("#linkCreateAccount").addEventListener("click", e => {
        e.preventDefault();
        loginForm.classList.add("form--hidden");
        createAccountForm.classList.remove("form--hidden");
    });

    document.querySelector("#linkLogin").addEventListener("click", e => {
        e.preventDefault();
        loginForm.classList.remove("form--hidden");
        createAccountForm.classList.add("form--hidden");
    });

    loginForm.addEventListener("submit", e => {
        let username = document.getElementById('loginUsername').value;
        let password = document.getElementById('loginPassword').value;

        const usernameRegex = /^\w{3,30}$/;
        const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,30}$/; //TODO: rewrite regex

        if (!username.match(usernameRegex)) {
            setFormMessage(loginForm, "error", "Invalid username");
            e.preventDefault();

        } else if (!password.match(passwordRegex)) {
            setFormMessage(loginForm, "error", "Invalid username/password combination");
            e.preventDefault();

        }
    });

    createAccountForm.addEventListener("submit", e => {
        e.preventDefault();

    });

    document.querySelectorAll(".form__input").forEach(inputElement => {
        inputElement.addEventListener("blur", e => {
            if (e.target.id === "signupUsername" && e.target.value.length > 0 && e.target.value.length < 10) {
                setInputError(inputElement, "Username must be at least 10 characters in length");
            }
            // else if (e.target.id = "sig") {
            // }
        });

        inputElement.addEventListener("input", e => {
            clearInputError(inputElement);
        });
    });
});

// function loginFormValidation() {
//     let username = document.getElementById('loginUsername').value;
//     let password = document.getElementById('loginPassword').value;
//
//     const usernameRegex = /^\w+$/;
//
//     console.log(username);
//     console.log(password);
//
//     if (!username.match(usernameRegex)) {
//         alert("validation failed false");
//         // returnToPreviousPage()
//         return false;
//     }
//     alert("validations passed");
//     return true;
// }
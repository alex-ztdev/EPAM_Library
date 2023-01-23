const usernameRegex = /^\w{3,30}$/;
const passwordRegex = /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{6,30}$/;
const emailRegex = /^(?=.{1,64}@)[A-Za-z0-9_-]+(\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\.[A-Za-z0-9-]+)*(\.[A-Za-z]{2,})$/;
const nameRegex = /^[a-z ,.'\-]{1,30}$/;
const phoneRegex = /^[\d]{12}$/;


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
        let locale = document.documentElement.lang;


        if (!password.match(passwordRegex) || !username.match(usernameRegex)) {
            setFormMessage(loginForm, "error", locale === 'en' ? en.login_error_msg : ua.login_error_msg);
            e.preventDefault();
        }
    });

    createAccountForm.addEventListener("submit", e => {

    });

    document.querySelectorAll(".form__input").forEach(inputElement => {
        inputElement.addEventListener("blur", e => {
            let locale = document.documentElement.lang;

            if (e.target.id === "signupUsername" && !e.target.value.match(usernameRegex)) {
                console.log("signupUsername ERROR");
                setInputError(inputElement, locale === 'en' ? en.registration_login_error_msg : ua.registration_login_error_msg);
                e.preventDefault();
            }
            if (e.target.id === "signupEmail" && !e.target.value.match(emailRegex)) {
                console.log("signupEmail ERROR");
                setInputError(inputElement, locale === 'en' ? en.registration_email_error_msg : ua.registration_email_error_msg);
                e.preventDefault();
            }
            if (e.target.id === "signupPhone" && e.target.value && !e.target.value.match(phoneRegex)) {
                console.log("signupPhone ERROR");
                setInputError(inputElement, locale === 'en' ? en.registration_phone_error_msg : ua.registration_phone_error_msg);
                e.preventDefault();
            }
            if (e.target.id === "signupPassword" && !e.target.value.match(passwordRegex)) {
                console.log("signupPassword ERROR");
                setInputError(inputElement, locale === 'en' ? en.registration_password_validation_error_msg : ua.registration_password_validation_error_msg);
                e.preventDefault();
            }


            if (e.target.id === "signupConfirmPassword") {
                if(document.querySelector('#signupPassword').value !== document.querySelector('#signupConfirmPassword').value) {
                    setInputError(inputElement, locale === 'en' ? en.registration_passwords_match_error_msg : ua.registration_passwords_match_error_msg);
                    e.preventDefault();
                }
            }
        });

        inputElement.addEventListener("input", e => {
            clearInputError(inputElement);
        });
    });
    const flag = document.querySelector("#registration_flag");
    if (flag.textContent === 'regForm') {
        document.querySelector("#linkCreateAccount").click();
    }
});
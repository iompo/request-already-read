let isVoltaCssLoaded = false;
let isColorLoaded = false;
let isLogoLoaded = false;
let isApplicationInfoLoaded = false;
let isLoginMessageLoaded = false;
let isDiskSpaceStatusLoaded = false;
let isServerDiskSpaceCritical = false;

document.addEventListener("DOMContentLoaded", function (event) {
    const loginOnEnterKey = event => {
        if (event.key === 'Enter') {
            login();
        }
    };

    document.getElementById('username').addEventListener('keyup', loginOnEnterKey);
    document.getElementById('password').addEventListener('keyup', loginOnEnterKey);
});

function showLoginError() {
    if (isServerDiskSpaceCritical === "true") {
        document.getElementById('restricted-access-error').style.removeProperty('display');
    } else {
        document.getElementById('invalid-credentials-error').style.removeProperty('display');
    }
}

function login() {
    let requestedAppUrl = window.location.hash
    const username = document.getElementById('username').value;
    const password = document.getElementById('password').value;
    const urlEncodedDataPairs = [
        encodeURIComponent('j_username') + '=' + encodeURIComponent(username),
        encodeURIComponent('j_password') + '=' + encodeURIComponent(password)
    ];
    const urlEncodedData = urlEncodedDataPairs.join('&').replace(/%20/g, '+');

    const xmlHttp = new XMLHttpRequest();
    xmlHttp.open("POST", "/j_security_check");
    xmlHttp.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
    xmlHttp.onreadystatechange = () => {
        if (xmlHttp.readyState === XMLHttpRequest.DONE) {
            if (xmlHttp.responseURL.includes('error')) {
                showLoginError();
            } else {
                console.log('login - redirecting to: ', xmlHttp.responseURL);
                window.location.href = '/' + requestedAppUrl;
            }
        }
    };
    xmlHttp.send(urlEncodedData);
}

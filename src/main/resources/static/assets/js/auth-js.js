//Dang nhap
const loginFormEL = document.getElementById('loginForm');
// console.log(loginFormEL);
const emailEl = document.getElementById('email');
const passwordEl = document.getElementById('password');


loginFormEL.addEventListener('submit', async (e) => {
    e.preventDefault();
    const email = emailEl.value;
    const password = passwordEl.value;

    const data = {
        email,
        password
    };
    try {
        const response = await axios.post('/api/auth/login', data);
        toastr.success('Login success!');
        setTimeout(() => {
            window.location.href = '/';
        }, 1000);

    } catch (error) {
        if (error.response && error.response.data === 'User is not active') {
            toastr.error('Account is not active!');
        } else {
            toastr.error('Login failed!');
        }
    }
});


//Dang ky

const formSignUpEL = document.getElementById('formSignUp');
const usernameSuEl = document.getElementById('usernameSu');
const emailSignUpEl = document.getElementById('emailSu');
const passwordSignUpEl = document.getElementById('passwordSu');
const fullNameEl = document.getElementById('fullnameSu');
const confirmPasswordEl = document.getElementById('passwordConfirmSu');
const phoneEl = document.getElementById('phoneSu');

formSignUpEL.addEventListener('submit', async (e) => {
    e.preventDefault();
    const username = usernameSuEl.value;
    const email = emailSignUpEl.value;
    const password = passwordSignUpEl.value;
    const fullName = fullNameEl.value;
    const confirmPassword = confirmPasswordEl.value;
    const phone = phoneEl.value;

    if (password !== confirmPassword) {
        toastr.error('Password and Confirm Password are not the same');
        return;
    }

    const data = {
        username,
        email,
        password,
        confirmPassword,
        fullName,
        phone
    };
    try {
        const response = await axios.post('/api/auth/sign-up', data);
        toastr.success('Sign up success!');
        setTimeout(() => {
            window.location.href = '/account';
        }, 1000);

    } catch (error) {
        toastr.error('Sign up failed!');
    }
});

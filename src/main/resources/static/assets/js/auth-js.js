//Dang nhap
const loginFormEL = document.getElementById('loginForm');
console.log(loginFormEL);
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
        toastr.success('Đăng nhập thành công');
        setTimeout(() => {
            window.location.href = '/';
        }, 1000);

    } catch (error) {
        toastr.error('Đăng nhập thất bại');
    }
});


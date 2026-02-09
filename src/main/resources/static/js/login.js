const form = document.getElementById('login-form')
const username_input = document.getElementById('username-input')
const password_input = document.getElementById('password-input')
const error_message = document.getElementById('error-message')

form.addEventListener('submit', async (e) => {
    e.preventDefault()
    error_message.innerText = ''

    const errors = getLoginErrors(username_input, password_input)
    if (errors.length > 0) {
        error_message.innerText = errors.join(' ')
        return
    }

    try {
        const url = new URL('http://localhost:8080/api/auth/login')
        url.searchParams.append('username', username_input.value.trim())
        url.searchParams.append('password', password_input.value)
        const response = await fetch(url.toString(),{
            method: 'GET'
        })

        if (!response.ok) {
            throw new Error('Invalid username or password')
        }

        const data = await response.json()
        localStorage.setItem('token', data.token)
        window.location.href = 'add-expense.html'
    }
    catch (error) {
    error_message.innerText = error.message
    }
})

function getLoginErrors(username, password) {
    let errors = [];

    if (username.value === '' || username.value == null) {
        errors.push('Enter your username!')
        username_input.parentElement.classList.add('incorrect')
    }

    if (password.value === '' || password.value == null) {
        errors.push('Enter your password!')
        password.parentElement.classList.add('incorrect')
    }
    return errors
}
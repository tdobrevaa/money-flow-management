const form = document.getElementById('register-form')
const firstname_input = document.getElementById('firstname-input')
const lastname_input = document.getElementById('lastname-input')
const username_input = document.getElementById('username-input')
const email_input = document.getElementById('email-input')
const password_input = document.getElementById('password-input')
const error_message = document.getElementById('error-message')

form.addEventListener('submit', async (e) => {
    e.preventDefault()
    error_message.innerText = ''

    const errors = getRegisterErrors(firstname_input, lastname_input, username_input, email_input, password_input)

    if (errors.length > 0) {
        error_message.innerText = errors.join(" ")
        return
    }

    const registerData = {
        firstName: firstname_input.value.trim(),
        lastName: lastname_input.value.trim(),
        username: username_input.value.trim(),
        email: email_input.value.trim(),
        password: password_input.value.trim(),
    }

    try {
        const response = await fetch('http://localhost:8080/api/auth/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(registerData)
        })

        const data = await response.json()
        localStorage.setItem('token', data.token)
        window.location.href = 'login.html'
    }
    catch (error) {
        error_message.innerText = error.message
    }
})

function getRegisterErrors(firstname, lastname, username, email, password) {
    let errors = []

    if (firstname.value === '' || firstname.value == null) {
        errors.push('First name is required.')
        firstname_input.parentElement.classList.add('incorrect')
    }

    if (lastname.value === '' || lastname.value == null) {
        errors.push('Last name is required.')
        lastname_input.parentElement.classList.add('incorrect')
    }

    if (username.value === '' || username.value == null) {
        errors.push('Username is required.')
        username_input.parentElement.classList.add('incorrect')
    }

    if (email.value === '' || email.value == null) {
        errors.push('Email is required.')
        email_input.parentElement.classList.add('incorrect')
    }

    if (password.value === '' || password.value == null) {
        errors.push('Password is required.')
        password.parentElement.classList.add('incorrect')
    }
    else if (password.value.length < 6) {
        errors.push('Password must have at least 6 characters')
        password_input.parentElement.classList.add('incorrect')
    }
    return errors
}

const allInputs = [firstname_input, lastname_input, username_input, email_input, password_input]

allInputs.forEach(input => {
    input.addEventListener('input', () => {
        if (input.parentElement.classList.contains('incorrect')) {
            input.parentElement.classList.remove('incorrect')
            error_message.innerText = ''
        }
    })
})
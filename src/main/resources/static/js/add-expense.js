const form = document.getElementById('add-expense-form')
const amount_input = document.getElementById('amount-input')
const category_select = document.getElementById('category-select')
const date_input = document.getElementById('date-input')
const error_message = document.getElementById('error-message')
const success_message = document.getElementById('success-message')
const allInputs = [amount_input, category_select, date_input];

form.addEventListener('submit', async (e) => {
    e.preventDefault()
    error_message.innerText = ''
    success_message.innerText = ''

    const errors = getAddExpenseErrors(amount_input, category_select, date_input)
    if (errors.length > 0) {
        error_message.innerText = errors.join(" ");
        return;
    }

    const token = localStorage.getItem('token')
    if (!token) {
        error_message.innerText = 'You are not logged in'
        return
    }

    const expenseData = {
        amount: Number(amount_input.value),
        category: category_select.value,
        date: date_input.value
    }

    try {
        const response = await fetch('http://localhost:8080/user/expenses', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(expenseData)
        })

        if (!response.ok) {
            throw new Error('Failed to add expense')
        }
        form.reset()
        success_message.innerText = 'Expense added successfully'
    }
    catch (error) {
        error_message.innerText = error.message
    }
})

function getAddExpenseErrors(amount, category, date) {
    let errors = [];

    if (amount.value === '' || amount.value == null) {
        errors.push('Enter your expense.')
        amount.parentElement.classList.add('incorrect')
    }

    if (category.value === '') {
        errors.push('Select a category.')
        category.parentElement.classList.add('incorrect')
    }

    if (date.value === '' || date.value == null) {
        errors.push('Choose a date.')
        date.parentElement.classList.add('incorrect')
    }
    return errors
}

allInputs.forEach(input => {
    input.addEventListener('input', () => {
        if (input.parentElement.classList.contains('incorrect')) {
            input.parentElement.classList.remove('incorrect')
            error_message.innerText = ''
        }
    })
})
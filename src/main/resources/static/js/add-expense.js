const form = document.getElementById('add-expense-form')
const amount_input = document.getElementById('amount-input')
const category_select = document.getElementById('category-select')
const date_input = document.getElementById('date-input')
const error_message = document.getElementById('error-message')
const success_message = document.getElementById('success-message')
const expenses_list = document.getElementById('expenses-list')

const editModal = document.getElementById('edit-modal')
const editAmount = document.getElementById('edit-amount')
const editCategory = document.getElementById('edit-category')
const editDate = document.getElementById('edit-date')
const saveEditBtn = document.getElementById('save-edit')
const cancelEditBtn = document.getElementById('cancel-edit')

let currentEditId = null

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
        getExpenses();
    }
    catch (error) {
        error_message.innerText = error.message
    }
})

async function getExpenses() {
    const token = localStorage.getItem('token')

    try {
        const response = await fetch('http://localhost:8080/user/expenses', {
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })

        if (!response.ok) {
            throw new Error('Failed to load expenses.')
        }
        const expenses = await response.json()
        showExpenses(expenses)
    }
    catch(error) {
        error_message.innerText = error.message
    }
}

function editExpense(id) {
    const expense = document.querySelector(`[data-id="${id}"]`).closest('li')
    const spans = expense.querySelectorAll('.expenses span')

    editAmount.value = spans[0].innerText.replace(' €', '')
    editCategory.value = spans[1].innerText
    editDate.value = spans[2].innerText

    currentEditId = id
    editModal.classList.remove('hidden')
}

cancelEditBtn.addEventListener('click', () => {
    editModal.classList.add('hidden')
})


saveEditBtn.addEventListener('click', async () => {
    const token = localStorage.getItem('token')

    const updatedExpense = {
        amount: Number(editAmount.value),
        category: editCategory.value,
        date: editDate.value
    }

    try {
        const response = await fetch(`http://localhost:8080/user/expenses/${currentEditId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updatedExpense)
        })
        if (!response.ok) {
            throw new Error('Failed to update expense.')
        }

        editModal.classList.add('hidden')
        getExpenses()
    }
    catch (error) {
        error_message.innerText = error.message
    }
})

async function deleteExpense(id) {
    const token = localStorage.getItem('token')

    try {
        const response = await fetch(`http://localhost:8080/user/expenses/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        if (!response.ok) {
            throw new Error('Failed to delete expense.')
        }
        getExpenses()
    }
    catch (error) {
        error_message.innerText = error.message
    }
}

function showExpenses(expenses) {
    expenses_list.innerHTML = ''

    expenses.forEach(expense => {
        const li = document.createElement('li')
        li.classList.add('expense-item')

        li.innerHTML = `<div class="expenses">
                         <span>${expense.amount} €</span>
                         <span>${expense.category}</span>
                         <span>${expense.date}</span>
                         </div>
                         <div class="actions">
                            <img src="images/edit.svg" class="edit-icon" data-id="${expense.id}">
                            <img src="images/delete.svg" class="delete-icon" data-id="${expense.id}">
                         </div>`;
        expenses_list.appendChild(li)
    })
    attachEventListeners()
}

getExpenses();

function attachEventListeners() {
    document.querySelectorAll('.edit-icon').forEach (icon => {
        icon.addEventListener('click', () => {
            const id = icon.dataset.id
            editExpense(id)
        })
    })

    document.querySelectorAll('.delete-icon').forEach(icon => {
        icon.addEventListener('click', () => {
            const id = icon.dataset.id
            deleteExpense(id)
        })
    })
}

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


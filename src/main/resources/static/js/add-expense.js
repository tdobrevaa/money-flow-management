const form = document.getElementById('add-expense-form')
const amountInput = document.getElementById('amount-input')
const categorySelect = document.getElementById('category-select')
const dateInput = document.getElementById('date-input')
const errorMessage = document.getElementById('error-message')
const successMessage = document.getElementById('success-message')
const expensesList = document.getElementById('expenses-list')

const editModal = document.getElementById('edit-modal')
const editAmount = document.getElementById('edit-amount')
const editCategory = document.getElementById('edit-category')
const editDate = document.getElementById('edit-date')
const saveEditBtn = document.getElementById('save-edit')
const cancelEditBtn = document.getElementById('cancel-edit')
const modal = document.getElementById('edit-modal')

const startDateInput = document.getElementById('start-date')
const endDateInput = document.getElementById('end-date')
const filterBtn = document.getElementById('filter-button')
const resetBtn = document.getElementById('reset-button')

let currentEditId = null
let expensesData = []

const allInputs = [amountInput, categorySelect, dateInput];

form.addEventListener('submit', async (e) => {
    e.preventDefault()
    errorMessage.innerText = ''
    successMessage.innerText = ''

    const errors = getAddExpenseErrors(amountInput, categorySelect, dateInput)
    if (errors.length > 0) {
        errorMessage.innerText = errors.join(" ");
        return;
    }

    const token = localStorage.getItem('token')
    if (!token) {
        errorMessage.innerText = 'You are not logged in'
        return
    }

    const expenseData = {
        amount: Number(amountInput.value),
        category: categorySelect.value,
        date: dateInput.value
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
        successMessage.innerText = 'Expense added successfully'
        getExpenses();
    }
    catch (error) {
        errorMessage.innerText = error.message
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
        expensesData = await response.json()
        showExpenses(expensesData)
    }
    catch(error) {
        errorMessage.innerText = error.message
    }
}

function editExpense(id) {
    const expense = document.querySelector(`[data-id="${id}"]`).closest('li')
    const spans = expense.querySelectorAll('.expenses span')

    editAmount.value = spans[0].innerText.replace(' €', '')
    editCategory.value = spans[1].innerText
    editDate.value = convertDateFormat(spans[2].innerText)

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
        errorMessage.innerText = error.message
    }
})

modal.addEventListener('click', (e) => {
    if (e.target === modal) {
        closeModal()
    }
})

function closeModal() {
    modal.classList.add('hidden')
}

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
        errorMessage.innerText = error.message
    }
}

function showExpenses(expenses) {
    expensesList.innerHTML = ''

    expenses
        .sort((a,b) => b.id - a.id)
        .forEach(expense => {
        const li = document.createElement('li')
        li.classList.add('expense-item')

        li.innerHTML = `<div class="expenses">
                         <span>${expense.amount.toFixed(2)} €</span>
                         <span>${expense.category}</span>
                         <span>${expense.date}</span>
                         </div>
                         <div class="actions">
                            <img src="images/edit.svg" class="edit-icon" data-id="${expense.id}">
                            <img src="images/delete.svg" class="delete-icon" data-id="${expense.id}">
                         </div>`;
        expensesList.appendChild(li)
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
            errorMessage.innerText = ''
        }
    })
})

function convertDateFormat (date) {
    const parts = date.split('.')
    return `${parts[2]}-${parts[1]}-${parts[0]}`
}

function parseDate(dateStr) {
    const [day, month, year] = dateStr.split('.')
    return new Date(`${year}-${month}-${day}`)
}

filterBtn.addEventListener('click', () => {
    if (startDateInput.value === '' || endDateInput.value === '') {
        errorMessage.innerText = 'Select both dates'
        return
    }

    const start = new Date(startDateInput.value)
    const end = new Date(endDateInput.value)

    const filtered = expensesData.filter(exp => {
        const expenseDate = parseDate(exp.date)
        return expenseDate >= start && expenseDate <= end
    })
    showExpenses(filtered)
})

resetBtn.addEventListener('click', () => {
    startDateInput.value = ''
    endDateInput.value = ''
    showExpenses(expensesData)
    errorMessage.innerText = ''
})
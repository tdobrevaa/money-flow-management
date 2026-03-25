const form = document.getElementById('add-income-form')
const incomeInput = document.getElementById('income-input')
const targetInput = document.getElementById('target-input')
const categorySelect = document.getElementById('category-select')
const startDate = document.getElementById('start-date-input')
const endDate = document.getElementById('end-date-input')
const errorMessage = document.getElementById('error-message')
const successMessage = document.getElementById('success-message')
const incomeList = document.getElementById('income-list')

const editModal = document.getElementById('edit-modal')
const editAmount = document.getElementById('edit-amount')
const editTarget = document.getElementById('edit-target')
const editCategory = document.getElementById('edit-category')
const editStartDate = document.getElementById('edit-start-date')
const editEndDate = document.getElementById('edit-end-date')
const saveEditBtn = document.getElementById('save-edit')
const cancelEditBtn = document.getElementById('cancel-edit')
const modal = document.getElementById('edit-modal')

const startDateInput = document.getElementById('start-date')
const endDateInput = document.getElementById('end-date')
const filterBtn = document.getElementById('filter-button')
const resetBtn = document.getElementById('reset-button')

let currentEditId = null
let incomeData = []

const allInputs = [incomeInput, targetInput, categorySelect, startDate, endDate];

form.addEventListener('submit', async (e) => {
    e.preventDefault()
    errorMessage.innerText = ''
    successMessage.innerText = ''

    const errors = getAddIncomeErrors(incomeInput, targetInput, categorySelect, startDate)
    if (errors.length > 0) {
        errorMessage.innerText = errors.join(" ");
        return;
    }

    const token = localStorage.getItem('token')
    if (!token) {
        errorMessage.innerText = 'You are not logged in'
        return
    }

    const newIncome = {
        income: Number(incomeInput.value),
        targetSavedMoney: Number(targetInput.value),
        incomeCategories: categorySelect.value,
        startDate: startDate.value,
        endDate: endDate.value
    }

    try {
        const response = await fetch('http://localhost:8080/user/income', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(newIncome)
        })

        if (!response.ok) {
            throw new Error('Failed to add income.')
        }
        form.reset()
        endDate.classList.add('hidden')
        successMessage.innerText = 'Income added successfully.'
        getIncome();
    }
    catch (error) {
        errorMessage.innerText = error.message
    }
})

endDate.classList.add('hidden')

categorySelect.addEventListener('change', () => {
    if (categorySelect.value === 'SALARY') {
        endDate.classList.remove('hidden')
    }
    else {
        endDate.classList.add('hidden')
        endDate.value = ''
    }
})

async function getIncome() {
    const token = localStorage.getItem('token')

    try {
        const response = await fetch('http://localhost:8080/user/income', {
            method: "GET",
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })

        if (!response.ok) {
            throw new Error('Failed to load all incomes.')
        }
        incomeData = await response.json()
        showIncome(incomeData)
    }
    catch(error) {
        errorMessage.innerText = error.message
    }
}

function editIncome(id) {
    const income = document.querySelector(`[data-id="${id}"]`).closest('li')
    const spans = income.querySelectorAll('.income span')

    editAmount.value = spans[0].innerText.replace(' €', '')
    editTarget.value = spans[1].innerText.replace(' €', '')
    editCategory.value = spans[2].innerText
    editStartDate.value = convertDateFormat(spans[3].innerText)
    editEndDate.value = convertDateFormat(spans[4].innerText)

    if (editCategory.value === 'SALARY') {
        editEndDate.classList.remove('hidden')
        editEndDate.value = income.endDate ? income.endDate : ''
    }
    else {
        editEndDate.classList.add('hidden')
        editEndDate.value = ''
    }

    currentEditId = id
    editModal.classList.remove('hidden')
}

editEndDate.classList.add('hidden')

editCategory.addEventListener('change', () => {
    if (editCategory.value === 'SALARY') {
        editEndDate.classList.remove('hidden')
    }
    else {
        editEndDate.classList.add('hidden')
        editEndDate.value = ''
    }
})

cancelEditBtn.addEventListener('click', () => {
    editModal.classList.add('hidden')
})

saveEditBtn.addEventListener('click', async () => {
    const token = localStorage.getItem('token')

    const updatedIncome = {
        income: Number(editAmount.value),
        targetSavedMoney: Number(editTarget.value),
        incomeCategories: editCategory.value,
        startDate: editStartDate.value,
        endDate: editEndDate.value
    }

    try {
        const response = await fetch(`http://localhost:8080/user/income/${currentEditId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': `Bearer ${token}`
            },
            body: JSON.stringify(updatedIncome)
        })
        if (!response.ok) {
            throw new Error('Failed to update income.')
        }

        editModal.classList.add('hidden')
        getIncome()
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

async function deleteIncome(id) {
    const token = localStorage.getItem('token')

    try {
        const response = await fetch(`http://localhost:8080/user/income/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        })
        if (!response.ok) {
            throw new Error('Failed to delete income.')
        }
        getIncome()
    }
    catch (error) {
        errorMessage.innerText = error.message
    }
}

function showIncome(income) {
    incomeList.innerHTML = ''

    income
        .sort((a,b) => b.id - a.id)
        .forEach(income => {
            const li = document.createElement('li')
            li.classList.add('income-item')

            li.innerHTML = `<div class="income">
                         <span>${income.income.toFixed(2)} €</span>
                         <span>${income.targetSavedMoney.toFixed(2)} €</span>
                         <span>${income.incomeCategories}</span>
                         <span>${income.startDate}</span>
                         <span>${income.endDate ?? ''}</span>
                         </div>
                         <div class="actions">
                            <img src="images/edit.svg" class="edit-icon" data-id="${income.id}">
                            <img src="images/delete.svg" class="delete-icon" data-id="${income.id}">
                         </div>`;
            incomeList.appendChild(li)
        })
    attachEventListeners()
}

getIncome();

function attachEventListeners() {
    document.querySelectorAll('.edit-icon').forEach (icon => {
        icon.addEventListener('click', () => {
            const id = icon.dataset.id
            editIncome(id)
        })
    })

    document.querySelectorAll('.delete-icon').forEach(icon => {
        icon.addEventListener('click', () => {
            const id = icon.dataset.id
            deleteIncome(id)
        })
    })
}

function getAddIncomeErrors(amount, target, category, startDate) {
    let errors = [];

    if (amount.value === '' || amount.value == null) {
        errors.push('Enter your income.')
        amount.parentElement.classList.add('incorrect')
    }

    if (target.value === '' || target.value == null) {
        errors.push('Enter your target.')
        target.parentElement.classList.add('incorrect')
    }

    if (category.value === '') {
        errors.push('Select category.')
        category.parentElement.classList.add('incorrect')
    }

    if (startDate.value === '' || startDate.value == null) {
        errors.push('Choose start date.')
        startDate.parentElement.classList.add('incorrect')
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
    const start = new Date(startDateInput.value)
    const end = new Date(endDateInput.value)

    const filtered = incomeData.filter(exp => {
        const incomeDate = parseDate(exp.startDate)
        return incomeDate >= start && incomeDate <= end
    })
    showIncome(filtered)
})

resetBtn.addEventListener('click', () => {
    startDateInput.value = ''
    endDateInput.value = ''
    showIncome(incomeData)
    errorMessage.innerText = ''
})
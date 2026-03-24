const token = localStorage.getItem('token')

let currentDate = new Date()
let charts = {}

const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December']

const categoryColors = {
    CLOTHING:   { bg: 'rgba(109,172,17,0.75)', border: '#65a30d' },
    FOOD:       { bg: 'rgba(22,163,74,0.75)',  border: '#16a34a' },
    TAXI:       { bg: 'rgba(234,179,8,0.75)',  border: '#eab308' },
    PHARMACY:   { bg: 'rgba(59,130,246,0.75)', border: '#3b82f6' },
    RESTAURANT: { bg: 'rgba(239,68,68,0.75)',  border: '#ef4444' },
    OTHER:      { bg: 'rgba(255,139,0,0.75)',border: '#e4870d' },
}

document.getElementById('prev-month').onclick = () => {
    currentDate.setMonth(currentDate.getMonth() - 1)
    loadDashboard()
}

document.getElementById('next-month').onclick = () => {
    currentDate.setMonth(currentDate.getMonth() + 1)
    loadDashboard()
}

function updateMonthDisplay() {
    document.getElementById('current-month').textContent = `${monthNames[currentDate.getMonth()]} ${currentDate.getFullYear()}`
}

async function fetchDashboard(month, year) {
    const response = await fetch(
        `http://localhost:8080/user/dashboard?month=${month}&year=${year}`, {
            headers: {
                Authorization: `Bearer ${token}`
            }
        }
    )
    return response.json()
}

async function loadDashboard() {
    updateMonthDisplay()

    const month = currentDate.getMonth() + 1
    const year = currentDate.getFullYear()

    try {
        const current = await fetchDashboard(month, year)

        const trend = await Promise.all(
            Array.from({ length: 6 }, async (_, i) => {
                const d = new Date(year, month - 1 - (5 - i))
                const data = await fetchDashboard(d.getMonth() + 1, d.getFullYear())
                return {
                    label: monthNames[d.getMonth()].substring(0, 3),
                    spent: data.totalSpent || 0,
                    saved: data.totalSaved || 0
                }
            })
        )

        updateSummary(current)
        renderLine('chart-spent-line', 'Monthly Spending (€)', trend, 'spent', '#c0392b', 'rgba(192,57,43,0.12)')
        renderLine('chart-saved-line', 'Monthly Savings (€)', trend, 'saved', '#57914b', 'rgba(87,145,75,0.12)')
        renderPie(current.allCategories)
        renderBar(current.topCategories)

    } catch (error) {
        error_message.innerText = error.message
    }
}

function updateSummary(data) {
    document.getElementById('total-spent').textContent = `${(data.totalSpent || 0).toFixed(2)} €`
    document.getElementById('total-saved').textContent = `${(data.totalSaved || 0).toFixed(2)} €`
}

function renderLine(canvasId, title, trend, field, borderColor, bgColor) {
    if (charts[canvasId]) charts[canvasId].destroy()

    charts[canvasId] = new Chart(
        document.getElementById(canvasId),
        {
            type: 'line',
            data: {
                labels: trend.map(t => t.label),
                datasets: [{
                    data: trend.map(t => t[field]),
                    borderColor,
                    backgroundColor: bgColor,
                    fill: true,
                    tension: 0.4,
                    pointRadius: 5
                }]
            },
            options: chartOptions(title)
        }
    )
}

function renderPie(categories) {
    if (charts.pie) charts.pie.destroy()
    if (!categories.length) return

    charts.pie = new Chart(
        document.getElementById('chart-pie'),
        {
            type: 'doughnut',
            data: {
                labels: categories.map(c => formatCategory(c.category)),
                datasets: [{
                    data: categories.map(c => c.total),
                    backgroundColor: categories.map(c => categoryColors[c.category].bg),
                    borderColor: categories.map(c => categoryColors[c.category].border),
                    borderWidth: 2
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: true,
                        position: 'bottom',
                        labels: {
                            color: '#465a16',
                            font: {
                                family: 'Montserrat',
                                size: 12
                            },
                            padding: 14
                        }
                    },
                    title: {
                        display: true,
                        text: 'Spending by Category',
                        color: '#465a16',
                        font: {
                            family: 'Montserrat',
                            size: 14,
                            weight: '700'
                        },
                        padding: {
                            bottom: 12
                        }
                    }
                }
            }
        }
    )
}

function renderBar(categories) {
    if (charts.bar) charts.bar.destroy()
    if (!categories.length) return

    const top3 = categories.slice(0, 3)

    charts.bar = new Chart(
        document.getElementById('chart-bar'),
        {
            type: 'bar',
            data: {
                labels: top3.map(c => formatCategory(c.category)),
                datasets: [{
                    data: top3.map(c => c.total),
                    backgroundColor: top3.map(c => categoryColors[c.category].bg),
                    borderColor: top3.map(c => categoryColors[c.category].border),
                    borderWidth: 2,
                    borderRadius: 8
                }]
            },
            options: chartOptions('Top 3 Categories')
        }
    )
}

function chartOptions(title, showLegend = false) {
    return {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: {
                display: showLegend,
                position: 'bottom'
            },
            title: {
                display: true,
                text: title,
                color: '#465a16',
                font: {
                    family: 'Montserrat',
                    size: 14,
                    weight: '700'
                },
                padding: {
                    bottom: 12
                }
            }
        },
        scales: {
            y: {
                beginAtZero: true,
                ticks: {
                    callback: v => v + ' €',
                    color: '#465a16'
                }
            },
            x: {
                ticks: {
                    color: '#465a16'
                }
            }
        }
    }
}

function formatCategory(category) {
    return category ? category[0] + category.slice(1).toLowerCase() : ''
}

loadDashboard()
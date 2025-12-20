document.addEventListener('DOMContentLoaded', () => {
    // Set Current Date
    const dateOptions = { weekday: 'long', year: 'numeric', month: 'long', day: 'numeric' };
    document.getElementById('currentDate').innerText = new Date().toLocaleDateString('en-US', dateOptions);

    // Initial Data Load
    fetch('/api/analytics')
        .then(response => response.json())
        .then(data => {
            // Dashboard
            updateKPIs(data);
            initDashboardCharts(data);
            populateTable(data, '#customerTable tbody');

            // Customers View (Full Table)
            populateTable(data, '#fullCustomerTable tbody', true);

            // Performance View
            initPerformanceCharts(data);
        })
        .catch(error => console.error('Error fetching data:', error));

    // Sidebar Navigation Logic
    const navLinks = document.querySelectorAll('.nav-links li');
    const views = document.querySelectorAll('.view-section');
    const pageTitle = document.getElementById('pageTitle');

    navLinks.forEach(link => {
        link.addEventListener('click', (e) => {
            e.preventDefault();
            const targetView = link.getAttribute('data-view');

            // Update Active Link
            navLinks.forEach(l => l.classList.remove('active'));
            link.classList.add('active');

            // Switch View
            views.forEach(view => {
                view.style.display = view.id === `${targetView}-view` ? 'block' : 'none';
            });

            // Update Title
            pageTitle.innerText = link.innerText;
        });
    });
});

function updateKPIs(data) {
    const totalCustomers = data.length;
    const totalSpend = data.reduce((sum, customer) => sum + customer.totalSpend, 0);

    animateValue('totalCustomers', 0, totalCustomers, 1000);
    animateValue('totalRevenue', 0, totalSpend, 1500, true);
}

function populateTable(data, selector, isDetailed = false) {
    const tbody = document.querySelector(selector);
    tbody.innerHTML = '';

    data.forEach(customer => {
        const isActive = customer.visitCount > 10;
        let row = '';

        if (isDetailed) {
            // Simple row for detailed view
            row = `
                <tr>
                    <td>#${customer.id}</td>
                    <td style="font-weight: 500; color: white;">${customer.name}</td>
                    <td>${customer.email}</td>
                    <td>₹${customer.totalSpend.toFixed(2)}</td>
                    <td><button class="btn-primary" style="padding: 5px 10px; font-size: 0.8rem;">View Profile</button></td>
                </tr>
            `;
        } else {
            // Detailed row for dashboard
            row = `
                <tr>
                    <td>#${customer.id}</td>
                    <td style="font-weight: 500; color: white;">${customer.name}</td>
                    <td><span style="opacity:0.7">${customer.email}</span></td>
                    <td>₹${customer.totalSpend.toFixed(2)}</td>
                    <td>${customer.visitCount} visits</td>
                    <td>${customer.lastVisit}</td>
                    <td><span class="status ${isActive ? 'active' : 'inactive'}">${isActive ? 'Active' : 'Inactive'}</span></td>
                </tr>
            `;
        }
        tbody.innerHTML += row;
    });
}

function initDashboardCharts(data) {
    // Chart 1: Top 5 Spenders
    const sortedBySpend = [...data].sort((a, b) => b.totalSpend - a.totalSpend).slice(0, 5);
    const topLabels = sortedBySpend.map(c => c.name.split(' ')[0]);
    const topData = sortedBySpend.map(c => c.totalSpend);

    const ctx1 = document.getElementById('revenueChart').getContext('2d');
    new Chart(ctx1, {
        type: 'bar',
        data: {
            labels: topLabels,
            datasets: [{
                label: 'Revenue (₹)',
                data: topData,
                backgroundColor: 'rgba(99, 102, 241, 0.7)',
                borderRadius: 6,
                borderWidth: 0
            }]
        },
        options: getChartOptions()
    });

    // Chart 2: Visit Distribution (Pie)
    let low = 0, medium = 0, high = 0;
    data.forEach(c => {
        if (c.visitCount < 10) low++;
        else if (c.visitCount < 30) medium++;
        else high++;
    });

    const ctx2 = document.getElementById('visitsChart').getContext('2d');
    new Chart(ctx2, {
        type: 'doughnut',
        data: {
            labels: ['Low', 'Medium', 'Freq'],
            datasets: [{
                data: [low, medium, high],
                backgroundColor: ['#94a3b8', '#fbbf24', '#10b981'],
                borderWidth: 0
            }]
        },
        options: getChartOptions({ cutOut: '70%' })
    });
}

function initPerformanceCharts(data) {
    // Chart 3: Live Traffic Trend (Simulated)
    // Grouping by last digit of ID to simulate "Days"
    const trafficData = Array(7).fill(0).map(() => Math.floor(Math.random() * 50) + 10);
    const days = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];

    const ctxLines = document.getElementById('trendChart').getContext('2d');
    new Chart(ctxLines, {
        type: 'line',
        data: {
            labels: days,
            datasets: [{
                label: 'Daily Active Users',
                data: trafficData,
                borderColor: '#ec4899',
                backgroundColor: 'rgba(236, 72, 153, 0.1)',
                fill: true,
                tension: 0.4,
                pointRadius: 4
            }]
        },
        options: getChartOptions()
    });

    // Chart 4: Scatter Plot (Spend vs Visits)
    const scatterPoints = data.map(c => ({ x: c.visitCount, y: c.totalSpend }));
    const ctxScatter = document.getElementById('scatterChart').getContext('2d');
    new Chart(ctxScatter, {
        type: 'scatter',
        data: {
            datasets: [{
                label: 'Customer Value',
                data: scatterPoints,
                backgroundColor: '#10b981'
            }]
        },
        options: getChartOptions()
    });

    // Chart 5: Polar Area (Satisfaction)
    const ctxPolar = document.getElementById('polarChart').getContext('2d');
    new Chart(ctxPolar, {
        type: 'polarArea',
        data: {
            labels: ['Support', 'Product', 'Delivery', 'Price', 'UX'],
            datasets: [{
                data: [4.5, 4.8, 4.2, 3.9, 4.7],
                backgroundColor: [
                    'rgba(236, 72, 153, 0.6)',
                    'rgba(99, 102, 241, 0.6)',
                    'rgba(16, 185, 129, 0.6)',
                    'rgba(245, 158, 11, 0.6)',
                    'rgba(59, 130, 246, 0.6)'
                ],
                borderWidth: 0
            }]
        },
        options: getChartOptions()
    });
}

function getChartOptions(overrides = {}) {
    const defaults = {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
            legend: { display: false } // Hide legend by default for cleaner look
        },
        scales: {
            y: {
                grid: { color: 'rgba(255, 255, 255, 0.05)' },
                ticks: { color: '#94a3b8' }
            },
            x: {
                grid: { display: false },
                ticks: { color: '#94a3b8' }
            }
        }
    };

    // Merge overrides
    if (overrides.cutOut) defaults.cutout = overrides.cutOut;

    // Remove scales for Polar/Doughnut if in overrides or inferred type (not doing deep check here)
    return defaults;
}

function animateValue(id, start, end, duration, isCurrency = false) {
    const obj = document.getElementById(id);
    let startTimestamp = null;
    const step = (timestamp) => {
        if (!startTimestamp) startTimestamp = timestamp;
        const progress = Math.min((timestamp - startTimestamp) / duration, 1);
        const value = Math.floor(progress * (end - start) + start);
        obj.innerHTML = isCurrency
            ? '₹' + value.toLocaleString()
            : value;
        if (progress < 1) {
            window.requestAnimationFrame(step);
        } else {
            obj.innerHTML = isCurrency
                ? '₹' + end.toLocaleString()
                : end;
        }
    };
    window.requestAnimationFrame(step);
}

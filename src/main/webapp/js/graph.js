function createGraph(id, data) {
	new Chart(document.getElementById(id).getContext('2d'), {
		type: 'doughnut',
		data: {
			labels: ["Warnings", "Violations", "Success"],
			datasets: [{
				data: data,
				backgroundColor: ['#feba49', '#cf4747', '#2a94a2']
			}]
		}
	});
}

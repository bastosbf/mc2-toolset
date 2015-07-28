(function() {
	var app = angular.module('menu', []);

	app.controller('MenuController', function() {		
		this.isActive = function(tab) {	
			var old = localStorage.getItem("tab");
			if(old == null) {
				old = 1;
			}
			var active = (tab == old);
			return active;
		};
		this.selectTab = function(tab) {
			localStorage.setItem("tab", tab);
		};
	});
})();

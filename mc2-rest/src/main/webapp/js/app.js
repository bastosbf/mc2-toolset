function login($scope, $http) {
	$scope.doLogin = function() {
		var req = {
			method : 'POST',
			url : 'rest/authentication/login',
			headers : {
				'Content-Type' : 'application/x-www-form-urlencoded'
			},
			transformRequest : function(obj) {
				var str = [];
				for ( var p in obj)
					str.push(encodeURIComponent(p) + "="
							+ encodeURIComponent(obj[p]));
				return str.join("&");
			},
			data : {
				username : document.getElementById("username").value,
				password : document.getElementById("password").value
			}
		};

		$http(req).success(function(data) {
			$scope.result = data;
		});
	};
}

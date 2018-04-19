<!DOCTYPE html>
<html lang="en">
<head>
	<script src="https://unpkg.com/vue/dist/vue.min.js"></script>
	<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
	<meta charset="UTF-8">
	<title>Create Short URL & Redirect URL Test</title>
</head>
<body>
<div id="app">
	<input type="text" placeholder="URL을 입력하세요" v-model="inputUrl"/>
	<button @click="createUrl">생성</button>
	<div id="result" v-if='shortUrl.id !== undefined'>
		<br/>
		<span>id : {{shortUrl.id}}</span>
		<br/>
		<a :href="shortUrl.shortUrl">{{shortUrl.shortUrl}}</a>
		<br/>
		<span>생성일 : {{shortUrl.createdAt}}</span>
	</div>
</div>
<script>
	window.addEventListener("load", function () {
		var app = new Vue({
			el: "#app",
			data: {
				shortUrl: {},
				inputUrl: ""
			},
			methods: {
			    createUrl: function(event) {
			        if (app.inputUrl.length !== 0) {
			            axios.post("/url", "url=" + app.inputUrl)
							.then(function(response) {
							    if (response.status !== 200) {
							        alert("오류 입니다: " + response.statusText);
								} else {
							    	app.shortUrl = response.data;
								}
							})
					} else {
			            alert("값을 입력하세요");
					}
				}
			}
		});
	});
</script>
</body>
</html>
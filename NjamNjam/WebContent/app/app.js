// const RestoraniKomponenta = { template: '<></>'}
const RegistracijaKomponenta = { template: '<registracija></registracija>'}
const PrijavaKomponenta = { template: '<prijava></prijava>'}


const router = new VueRouter({
	  mode: 'hash',
	  routes: [
		  { path: '/', component: RegistracijaKomponenta},
	//	  { path: '/restorani', component: RestoraniKomponenta},
		  { path: '/registracija', component: RegistracijaKomponenta},
		  { path: '/prijava', component: PrijavaKomponenta}
		  
	  ]
});

var app = new Vue({
	router,
	el: '#application'
});
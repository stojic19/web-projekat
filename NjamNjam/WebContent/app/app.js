// const RestoraniKomponenta = { template: '<view-apartments></view-apartments>'}
const RegistracijaKomponenta = { template: '<registracija></registracija>'}
const PrijavaKomponenta = { template: '<app-login></app-login>'}


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
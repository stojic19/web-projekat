const ProfilKomponenta 					= { template: '<admin-profil></admin-profil>'}
const DostupnePorudzbineKomponenta 		= { template: '<dostupne-porudzbine-dostavljac></dostupne-porudzbine-dostavljac>'}
const MojePorudzbineKomponenta			= { template: '<moje-porudzbine-dostavljac></moje-porudzbine-dostavljac>'}
const RestoraniKomponenta 				= { template: '<restorani></restorani>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
      	    {path : '/', component: RestoraniKomponenta},
      	    {path : '/home', component: RestoraniKomponenta},
      	    {path : '/dostupnePorudzbine', component: DostupnePorudzbineKomponenta},
        	{path : '/mojePorudzbine', component: MojePorudzbineKomponenta},
        	{path : '/profil', component: ProfilKomponenta}
    ]
})

var dostavljacApp = new Vue({
    router,
    el: '#dostavljacID'
});
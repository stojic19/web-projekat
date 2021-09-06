const ProfilKomponenta 					= { template: '<dostavljac-profil></dostavljac-profil>'}
const DostupnePorudzbineKomponenta 		= { template: '<dostupne-porudzbine-dostavljac></dostupne-porudzbine-dostavljac>'}
const MojePorudzbineKomponenta			= { template: '<moje-porudzbine-dostavljac></moje-porudzbine-dostavljac>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
      	    {path : '/', component: ProfilKomponenta},
      	    {path : '/dostupnePorudzbine', component: DostupnePorudzbineKomponenta},
        	{path : '/mojePorudzbine', component: MojePorudzbineKomponenta},
        	{path : '/profil', component: ProfilKomponenta}
    ]
})

var dostavljacApp = new Vue({
    router,
    el: '#dostavljacID'
});
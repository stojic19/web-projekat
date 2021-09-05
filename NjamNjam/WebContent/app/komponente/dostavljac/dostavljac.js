DostupnePorudzbineKomponenta 		= { template: '<dostupne-porudzbine-dostavljac></dostupne-porudzbine-dostavljac>'}
const MojePorudzbineKomponenta		= { template: '<moje-porudzbine-dostavljac></moje-porudzbine-dostavljac>'}
const ProfilKomponenta 				= { template: '<dostavljac-profil></dostavljac-profil>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
      	    {path : '/', component: DostupnePorudzbineKomponenta},
      	    {path : '/dostupnePorudzbine', component: DostupnePorudzbineKomponenta},
        	{path : '/mojePorudzbine', component: MojePorudzbineKomponenta},
        	{path : '/profil', component: ProfilKomponenta}
    ]
})

var dostavljacApp = new Vue({
    router,
    el: '#dostavljacID'
});
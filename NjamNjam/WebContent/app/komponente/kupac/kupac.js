const RestoraniKomponenta 				= { template: '<restorani></restorani>'}
const ProfilKomponenta = 		{ template: '<kupac-profil></kupac-profil>'}
const PregledRestoranaKomponenta 	= { template: '<pregled-restorana></pregled-restorana>'}
const KorpaKomponenta = 	{ template: '<korpa></korpa>'}
const PorudzbineKomponenta = 	{ template: '<kupac-porudzbine></kupac-porudzbine>'}
// const KomentariKomponenta = 	{ template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        	{path : '/', component: RestoraniKomponenta},
        	{path : '/home', component: RestoraniKomponenta},
        	{ path: '/pregledPonude', component: PregledRestoranaKomponenta},
        	{path : '/profil', component: ProfilKomponenta},
        	{path : '/korpa', component: KorpaKomponenta},
        	{path : '/porudzbine', component: PorudzbineKomponenta},
    //    	{path : '/komentari', component: KomentariKomponenta}
    ]
})

var kupacApp = new Vue({
    router,
    el: '#guestID'
});
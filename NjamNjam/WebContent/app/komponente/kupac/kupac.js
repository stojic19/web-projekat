const PocetnaStranaKomponenta = { template: '<kupac-pocetna-strana></kupac-pocetna-strana>'}
const RestoraniKomponenta 				= { template: '<restorani></restorani>'}
const ProfilKomponenta = 		{ template: '<kupac-profil></kupac-profil>'}
const PregledRestoranaKomponenta 	= { template: '<pregled-restorana></pregled-restorana>'}
// const KorisniciKomponenta = 	{ template: '<admin-korisnici></admin-korisnici>'}
// const KomentariKomponenta = 	{ template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        	{path : '/', component: RestoraniKomponenta},
        	{path : '/home', component: RestoraniKomponenta},
        	{ path: '/pregledPonude', component: PregledRestoranaKomponenta},
        	{path : '/profil', component: ProfilKomponenta},
    //    	{path : '/korisnici', component: KorisniciKomponenta},
    //    	{path : '/komentari', component: KomentariKomponenta}
    ]
})

var kupacApp = new Vue({
    router,
    el: '#guestID'
});
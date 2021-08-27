const PocetnaStranaKomponenta = { template: '<kupac-pocetna-strana></kupac-pocetna-strana>'}
// const RestoraniKomponenta = 	{ template: '<admin-restorani></admin-restorani>'}
 const ProfilKomponenta = 		{ template: '<kupac-profil></kupac-profil>'}
// const KorisniciKomponenta = 	{ template: '<admin-korisnici></admin-korisnici>'}
// const KomentariKomponenta = 	{ template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        	{path : '/', component: PocetnaStranaKomponenta},
    //    	{path : '/restorani', component: RestoraniKomponenta},
        	{path : '/home', component: PocetnaStranaKomponenta},
        	{path : '/profil', component: ProfilKomponenta},
    //    	{path : '/korisnici', component: KorisniciKomponenta},
    //    	{path : '/komentari', component: KomentariKomponenta}
    ]
})

var kupacApp = new Vue({
    router,
    el: '#guestID'
});
const PocetnaStranaKomponenta = { template: '<admin-pocetna-strana></admin-pocetna-strana>'}
// const RestoraniKomponenta = 	{ template: '<admin-restorani></admin-restorani>'}
const ProfilKomponenta = 		{ template: '<admin-profil></admin-profil>'}
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

var adminApp = new Vue({
    router,
    el: '#administratorID'
});
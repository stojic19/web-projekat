RestoraniKomponenta 				= { template: '<restorani></restorani>'}
const ProfilKomponenta 				= { template: '<admin-profil></admin-profil>'}
//const KorisniciKomponenta 			= { template: '<admin-korisnici></admin-korisnici>'}
//const DodavanjeRestoranaKomponenta 	= { template: '<admin-dodavanje-restorana></admin-dodavanje-restorana>'}
const KomentariKomponenta 			= { template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        	{path : '/', component: RestoraniKomponenta},
        	{path : '/home', component: RestoraniKomponenta},
        	{path : '/profil', component: ProfilKomponenta},
        	//{path : '/korisnici', component: KorisniciKomponenta},
			//{path: '/restoran', component: RestoranKomponenta},
        	//{path : '/artikli', component: ArtikliKomponenta},
			//{path: '/dodavanjeArtikla', component: DodavanjeArtiklaKomponenta},
			{path: '/komentari', component: KomentariKomponenta},
        	//{path : '/svePorudzbine', component: SvePorudzbineKomponenta}
			//{path: '/aktivnePorudzbine', component: AktivnePorudzbineKomponenta},
    ]
})

var menadzerApp = new Vue({
    router,
    el: '#menadzerID'
});
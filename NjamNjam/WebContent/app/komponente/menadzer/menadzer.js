RestoraniKomponenta 				= { template: '<restorani></restorani>'}
const ProfilKomponenta 				= { template: '<admin-profil></admin-profil>'}
const KupciKomponenta 			= { template: '<menadzer-kupci></menadzer-kupci>'}
//const DodavanjeRestoranaKomponenta 	= { template: '<admin-dodavanje-restorana></admin-dodavanje-restorana>'}
const KomentariKomponenta 			= { template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        	{path : '/', component: RestoraniKomponenta},
        	{path : '/home', component: RestoraniKomponenta},
        	{path : '/profil', component: ProfilKomponenta},
        	{path : '/kupci', component: KupciKomponenta},
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
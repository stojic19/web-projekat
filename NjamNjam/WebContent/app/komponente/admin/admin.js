const SumnjiviKorisniciKomponenta		= { template: '<admin-sumnjivi-korisnici></admin-sumnjivi-korisnici>'}
const RestoraniKomponenta 				= { template: '<admin-restorani></admin-restorani>'}
const ProfilKomponenta 				= { template: '<admin-profil></admin-profil>'}
const KorisniciKomponenta 			= { template: '<admin-korisnici></admin-korisnici>'}
const DodavanjeRestoranaKomponenta 	= { template: '<admin-dodavanje-restorana></admin-dodavanje-restorana>'}
const KomentariKomponenta 			= { template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
        	{path : '/', component: RestoraniKomponenta},
        	{path : '/sumnjiviKorisnici', component: SumnjiviKorisniciKomponenta},
        	{path : '/home', component: RestoraniKomponenta},
        	{path : '/profil', component: ProfilKomponenta},
        	{path : '/korisnici', component: KorisniciKomponenta},
        	{path : '/dodavanjeRestorana', component: DodavanjeRestoranaKomponenta},
        	{path : '/komentari', component: KomentariKomponenta}
    ]
})

var adminApp = new Vue({
    router,
    el: '#administratorID'
});
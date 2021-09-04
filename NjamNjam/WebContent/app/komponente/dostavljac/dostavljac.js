//const PocetnaStranaKomponenta		= { template: '<admin-pocetna-strana></admin-pocetna-strana>'}
//RestoraniKomponenta 				= { template: '<admin-restorani></admin-restorani>'}
//const ProfilKomponenta 				= { template: '<admin-profil></admin-profil>'}
//const KorisniciKomponenta 			= { template: '<admin-korisnici></admin-korisnici>'}
//const DodavanjeRestoranaKomponenta 	= { template: '<admin-dodavanje-restorana></admin-dodavanje-restorana>'}
//const KomentariKomponenta 			= { template: '<admin-komentari></admin-komentari>'}

const router = new VueRouter({
    mode: 'hash',
    routes:[
  //      	{path : '/', component: PocetnaStranaKomponenta},
    //    	{path : '/restorani', component: RestoraniKomponenta},
      //  	{path : '/home', component: PocetnaStranaKomponenta},
        //	{path : '/profil', component: ProfilKomponenta},
        	//{path : '/korisnici', component: KorisniciKomponenta},
        	//{path : '/dodavanjeRestorana', component: DodavanjeRestoranaKomponenta},
        	//{path : '/komentari', component: KomentariKomponenta}
    ]
})

var dostavljacApp = new Vue({
    router,
    el: '#dostavljacID'
});
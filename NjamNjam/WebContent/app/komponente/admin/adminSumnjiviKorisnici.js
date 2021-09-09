Vue.component("admin-sumnjivi-korisnici", {
    data() {
        return {
            korisnici: [],
            pretraga: {
                korisnickoIme: '',
                ime: '',
                prezime: ''
            },
            podaciZaFiltriranjeKorisnika: {
                tip: ""
            },
            imeBrojac : 0,
            prezimeBrojac : 0,
            korisnickoImeBrojac : 0,
            brojBodovaBrojac : 0,
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false
        }
    },

    template: `
    <div id = "stilZaKorisnike">

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaFiltereVidljiv = !prostorZaFiltereVidljiv " class="btn"><i class="fa fa-filter" aria-hidden="true"></i> Filteri </button>
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <br><br>

        <!-- Pretraga -->
        <div class="pretragaKorisnikaAdmin" v-if="prostorZaPretraguVidljiv" >
            <form method='post' >

                <input type="text" v-model="pretraga.korisnickoIme" v-bind:class="{filledInput: pretraga.korisnickoIme != '' }" placeholder="Korisnicko ime" >
                <input type="text" v-model="pretraga.ime" v-bind:class="{filledInput: pretraga.ime != '' }" placeholder="Ime" >
                <input type="text" v-model="pretraga.prezime" v-bind:class="{filledInput: pretraga.prezime != '' }" placeholder="Prezime" >           

            </form>
        </div>
        <!-- Kraj pretrage -->
        

        <!-- Filtriranje korisnika -->
        <div class="filterZaKorisnikeAdmin" v-if="prostorZaFiltereVidljiv">
            <form method='post' 

                <select v-model="podaciZaFiltriranjeKorisnika.tip" @change="onchangeTipKorisnika()">
                    <option value="">Bez filtera za tip</option>
                    <option>Bronza</option>
                    <option>Srebro</option>
                    <option>Zlato</option>
                    <option>Dijamant</option>
                </select>
            </form>
        </div>
        <!-- Kraj filtriranja korisnika -->

        
        <!-- Sortiranje korisnika -->
        <div v-if="prostorZaSortiranjeVidljiv" class="sortiranje">
            <form method='post'>

                <button type="button" @click="sortirajIme"><i class="fa fa-sort" aria-hidden="true"></i> Ime </button>
                <button type="button" @click="sortirajPrezime"><i class="fa fa-sort" aria-hidden="true"></i> Prezime</button>
                <button type="button" @click="sortirajKorisnickoIme"><i class="fa fa-sort" aria-hidden="true"></i> Korisničko ime </button>
                <button type="button" @click="sortirajBrojBodova"><i class="fa fa-sort" aria-hidden="true"></i> Broj sakupljenih poena </button>

            </form>
        </div>
        <!-- Kraj sortiranja korisnika -->


        <!-- Tabela korisnika -->
        <div >
            <table class="stilZaTabelu" style="width:100%">

                <thead>
                    <tr>
                        <th> Korisničko ime </th>
                        <th> Lozinka</th>
                        <th> Ime </th>
                        <th> Prezime </th>
                        <th> Broj bodova </th>
                        <th> Blokiranje </th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="korisnik in filtriraniKorisnici">
                        <td> {{korisnik.korisnickoIme}}</td>
                        <td> {{korisnik.lozinka}}</td>
                        <td> {{korisnik.ime}} </td>
                        <td> {{korisnik.prezime }} </td>
                        <td> {{korisnik.brojSakupljenihBodova}} </td>
                        <td align ="center" >
                            <button v-if="korisnik.blokiran == '1' && korisnik.uloga != 'ADMIN' " type="button" @click="odblokirajKorisnika(korisnik)" ><i class="fa fa-check" aria-hidden="true"></i> Odblokiraj </button>
                            <button v-if="korisnik.blokiran == '0' && korisnik.uloga != 'ADMIN' " type="button" @click="blokirajKorisnika(korisnik)" class="blokirajKorisnika" ><i class="fa fa-ban" aria-hidden="true"></i> Blokiraj </button>
                        </td>
                    </tr>
                </tbody>                

            </table>
        </div>
        <!-- kraj tabele svih korisnika -->

    </div>
    `,
    methods: {
        blokirajKorisnika: function (korisnikParam) {
            axios
                .post('rest/korisnici/blokirajKorisnika', {
                    korisnik: korisnikParam
                })
                .then(response => {
						this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                    toastr["success"]("Uspešno ste blokirali korisnika!", "Uspeh!");
                    return this.korisnici;
                });

        },
        odblokirajKorisnika: function (korisnikParam) {
            axios
                .post('rest/korisnici/odblokirajKorisnika', {
                    korisnik: korisnikParam
                })
                .then(response => {
                    this.korisnici = [];
                    response.data.forEach(el => {
                        this.korisnici.push(el);
                    });
                    toastr["success"]("Uspešno ste odblokirali korisnika!", "Uspeh!");
                    return this.korisnici;
                });
        },
        poklapaSeSaPretragom: function (korisnik) {

            // Korisnicko ime
            if (!korisnik.korisnickoIme.toLowerCase().match(this.pretraga.korisnickoIme.toLowerCase()))
                return false;

            // Ime
            if (!korisnik.ime.toLowerCase().match(this.pretraga.ime.toLowerCase()))
                return false;

            // Prezime
            if (!korisnik.prezime.toLowerCase().match(this.pretraga.prezime.toLowerCase()))
                return false;


            return true;
        },
        onchangeTipKorisnika: function () {
            if (this.podaciZaFiltriranjeKorisnika.status == "") {
                axios
                    .get('rest/apartments/dobaviKorisnikeBezAdmina')
                    .then(response => {
						this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
				});

            } else {
                let filterKorisnici = (this.korisnici).filter(korisnik => korisnik.tip == this.podaciZaFiltriranjeKorisnika.tip);
                this.korisnici = filterKorisnici;
            }
        },
        sortirajIme: function () {
            this.imeBrojac ++;
            if(this.imeBrojac % 3 == 0)
            {
                axios
                    .get('rest/korisnici/dobaviKorisnikeBezAdmina')
                    .then(response => {
						this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
				});
            }else if(this.imeBrojac % 3 == 1)
            {
                this.multisort(this.korisnici, ['ime', 'ime'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.korisnici, ['ime', 'ime'], ['DESC', 'ASC']);
            }  
        },
        sortirajPrezime: function () {
            this.prezimeBrojac ++;
            if(this.prezimeBrojac % 3 == 0)
            {
                axios
                    .get('rest/korisnici/dobaviKorisnikeBezAdmina')
                    .then(response => {
						this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
				});
            }else if(this.prezimeBrojac % 3 == 1)
            {
                this.multisort(this.korisnici, ['prezime', 'prezime'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.korisnici, ['prezime', 'prezime'], ['DESC', 'ASC']);
            }
        },
        sortirajKorisnickoIme: function(){
            this.korisnickoImeBrojac ++;
            if(this.korisnickoImeBrojac % 3 == 0)
            {
                axios
                    .get('rest/korisnici/dobaviKorisnikeBezAdmina')
                    .then(response => {
					this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
				});
            }else if(this.korisnickoImeBrojac % 3 == 1)
            {
                this.multisort(this.korisnici, ['korisnickoIme', 'korisnickoIme'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.korisnici, ['korisnickoIme', 'korisnickoIme'], ['DESC', 'ASC']);
            }
        },
        sortirajBrojBodova: function(){
            this.brojBodovaBrojac ++;
            if(this.brojBodovaBrojac % 3 == 0)
            {
                axios
                    .get('rest/korisnici/dobaviKorisnikeBezAdmina')
                    .then(response => {
					this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
				});
            }else if(this.brojBodovaBrojac % 3 == 1)
            {
                this.multisort(this.korisnici, ['brojBodova', 'brojBodova'], ['ASC', 'DESC']);
            }else{
                this.multisort(this.korisnici, ['brojBodova', 'brojBodova'], ['DESC', 'ASC']);
            }
        },
        multisort: function (arr, columns, order_by) {
            if (typeof columns == 'undefined') {
                columns = []
                for (x = 0; x < arr[0].length; x++) {
                    columns.push(x);
                }
            }

            if (typeof order_by == 'undefined') {
                order_by = []
                for (x = 0; x < arr[0].length; x++) {
                    order_by.push('ASC');
                }
            }

            function multisort_recursive(a, b, columns, order_by, index) {
                var direction = order_by[index] == 'DESC' ? 1 : 0;

                var is_numeric = !isNaN(a[columns[index]] - b[columns[index]]);

                var x = is_numeric ? a[columns[index]] : a[columns[index]].toLowerCase();
                var y = is_numeric ? b[columns[index]] : b[columns[index]].toLowerCase();

                if (!is_numeric) {

                    let sum_x = 0;
                    let sum_y = 0;

                    x.split("").forEach(element => sum_x += element.charCodeAt())
                    y.split("").forEach(element => sum_y += element.charCodeAt())

                    x = sum_x;
                    y = sum_y;
                }

                if (x < y) {
                    return direction == 0 ? -1 : 1;
                }

                if (x == y) {
                    return columns.length - 1 > index ? multisort_recursive(a, b, columns, order_by, index + 1) : 0;
                }

                return direction == 0 ? 1 : -1;
            }

            return arr.sort(function (a, b) {
                return multisort_recursive(a, b, columns, order_by, 0);
            });
        },
    },
    mounted() {
        axios.get('rest/korisnici/dobaviKorisnikeBezAdmina').then(response => {
				this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga == "KUPAC" && el.brojOtkazanihPorudzbina >=5)
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
				});
    },
    computed: {
        filtriraniKorisnici: function () {
            return this.korisnici.filter((korisnik) => {
                return this.poklapaSeSaPretragom(korisnik);
            });
        }
    },
});
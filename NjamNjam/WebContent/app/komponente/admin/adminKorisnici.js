Vue.component("admin-korisnici", {
    data() {
        return {
            korisnici: [],
            pretraga: {
                korisnickoIme: '',
                ime: '',
                prezime: ''
            },
            podaciZaFiltriranjeKorisnika: {
                uloga: "",
                tip: ""
            },
            imeBrojac : 0,
            prezimeBrojac : 0,
            korisnickoImeBrojac : 0,
            brojBodovaBrojac : 0,
            dijalogZaDodavanjeSakriven: true,
            noviKorisnik: {},
            prostorZaPretraguVidljiv: false,
            prostorZaFiltereVidljiv: false,
            prostorZaSortiranjeVidljiv: false
        }
    },

    template: `
    <div id = "styleForProfile">

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click=" prostorZaFiltereVidljiv = !prostorZaFiltereVidljiv " class="btn"><i class="fa fa-filter" aria-hidden="true"></i> Filteri </button>
        <button type="button" @click=" prostorZaSortiranjeVidljiv = !prostorZaSortiranjeVidljiv " class="btn"><i class="fa fa-sort" aria-hidden="true"></i> Sortiranje </button>
        <button type="button" @click="dodajNovogKorisnika()" class="btn"><i class="fa fa-plus" aria-hidden="true"></i>Dodaj korisnika</button>
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
                <select v-model="podaciZaFiltriranjeKorisnika.uloga" @change="onchangeUlogaKorisnika()">
                    <option value="">Bez filtera za ulogu</option>
                    <option>KUPAC</option>
                    <option>MENADZER</option>
                    <option>DOSTAVLJAC</option>
                </select>

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
            <table class="styleForTable" style="width:100%">

                <thead>
                    <tr>
                        <th> Korisnicko ime </th>
                        <th> Lozinka</th>
                        <th> Ime </th>
                        <th> Prezime </th>
						<th> Pol </th>
						<th> Datum rođenja </th>
                        <th> Uloga </th>
                        <th> Blokiranje </th>
                    </tr>
                </thead>

                <tbody>
                    <tr v-for="korisnik in filtriraniKorisnici">
                        <td> {{korisnik.korisnickoIme}}</td>
                        <td> {{korisnik.lozinka}}</td>
                        <td> {{korisnik.ime}} </td>
                        <td> {{korisnik.prezime }} </td>
						<td> {{korisnik.pol}} </td>
						<td> {{korisnik.datumRodjenja}} </td>
                        <td> {{korisnik.uloga }} </td>
                        <td align ="center" >
                            <button v-if="korisnik.blokiran == '1' && korisnik.uloga != 'ADMIN' " type="button" @click="odblokirajKorisnika(korisnik)" ><i class="fa fa-check" aria-hidden="true"></i> Odblokiraj </button>
                            <button v-if="korisnik.blokiran == '0' && korisnik.uloga != 'ADMIN' " type="button" @click="blokirajKorisnika(korisnik)" class="blockUser" ><i class="fa fa-ban" aria-hidden="true"></i> Blokiraj </button>
                        </td>
                    </tr>
                </tbody>                

            </table>
        </div>
        <!-- kraj tabele svih korisnika -->

        <!-- Modalni dijalog za dodavanje korisnika -->
        <div id = "dijalogZaDodavanjeKorisnika" v-bind:class="{bgModal: dijalogZaDodavanjeSakriven, bgModalShow: !dijalogZaDodavanjeSakriven}">
            <div class="modal-contents">
        
                <div class="close" @click="dijalogZaDodavanjeSakriven = !dijalogZaDodavanjeSakriven">+</div>

                <form method='post'>
                    
                    <input type="text" v-model="noviKorisnik.korisnickoIme" placeholder="Korisnicko ime" required>
                    <input type="text" v-model="noviKorisnik.ime" placeholder="Ime" >
                    <input type="text" v-model="noviKorisnik.prezime" placeholder="Prezime">
					<select v-model="noviKorisnik.pol" required>
                		<option>Ženski</option>
                		<option>Muški</option>
            		</select>
            		<input type="date" v-model="noviKorisnik.datum" required>
					<select v-model="noviKorisnik.uloga" required>
                		<option>MENADZER</option>
                		<option>DOSTAVLJAC</option>
            		</select>
                    <input type="password" v-model="noviKorisnik.lozinka" placeholder="Lozinka" required>
                    <input type="password" placeholder="Ponovljena lozinka" required>

                    <button type="button" @click="potvrdiDodavanje" class="btn">Potvrdi</button>
                    <button type="button" @click="dijalogZaDodavanjeSakriven = !dijalogZaDodavanjeSakriven" class="btn">Odustani</button>

                </form>

            </div>
        </div> <!-- Kraj modalnog dijaloga -->


    </div>
    `,
    methods: {
        dodajNovogKorisnika: function () {
            this.dijalogZaDodavanjeSakriven = !this.dijalogZaDodavanjeSakriven;
        },
        potvrdiDodavanje: function () {
            if (!this.noviKorisnik.korisnickoIme || !this.noviKorisnik.lozinka || !this.noviKorisnik.ime || !this.noviKorisnik.prezime) {
                toastr["warning"]("Sva polja su obavezna!", "Proverite unos!");
                return;

            }
            axios
                .post('rest/korisnici/registracija', {
                    "korisnickoIme": this.noviKorisnik.korisnickoIme,
                        "lozinka": this.noviKorisnik.lozinka,
                        "ime": this.noviKorisnik.ime,
                        "prezime": this.noviKorisnik.prezime,
                        "uloga": this.noviKorisnik.uloga,
                        "pol" : this.noviKorisnik.pol,
                        "datumRodjenja": this.noviKorisnik.datum
                })
                .then(response => {
                    location.reload();
                    toastr["success"]("Uspešno dodat novi korisnik!", "Uspešna registracija");
                })
                .catch(err => {
                    console.log("\n\n ------- ERROR -------\n");
                    console.log(err);
                    toastr["error"]("Već imamo korisnika sa istim korisničkim imenom, isprobajte drugo", "Greška");
                    console.log("\n\n ----------------------\n\n");
                })

        },
        blokirajKorisnika: function (korisnikParam) {
            axios
                .post('rest/korisnici/blokirajKorisnika', {
                    korisnik: korisnikParam
                })
                .then(response => {
                    this.korisnici = [];
                    response.data.forEach(el => {
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
		onchangeUlogaKorisnika: function () {
            if (this.podaciZaFiltriranjeKorisnika.uloga == "") {
                axios
                    .get('rest/korisnici/dobaviKorisnikeBezAdmina')
                    .then(response => {
                        this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.uloga != "ADMIN")
                                this.korisnici.push(el);
                        });
                        return this.korisnici;
                    });

            } else {
                let filterKorisnici = (this.korisnici).filter(korisnik => korisnik.uloga == this.podaciZaFiltriranjeKorisnika.uloga);
                this.korisnici = filterKorisnici;
            }
        },
        onchangeTipKorisnika: function () {
            if (this.podaciZaFiltriranjeKorisnika.status == "") {
                axios
                    .get('rest/apartments/dobaviKorisnikeBezAdmina')
                    .then(response => {
                        this.korisnici = [];
                        response.data.forEach(el => {
                            if (el.tip != null)
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
                            if (el.uloga != "ADMIN")
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
                            if (el.uloga != "ADMIN")
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
                            if (el.uloga != "ADMIN")
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
            //TODO: uraditi da profil sadrzi broj bodova
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
        axios.get('rest/korisnici/dobaviKorisnikeBezAdmina').then(response => (this.korisnici = response.data));
        axios.get('rest/korisnici/dobaviNovogKorisnika').then(response => (this.noviKorisnik = response.data));
    },
    computed: {
        filtriraniKorisnici: function () {
            return this.korisnici.filter((korisnik) => {
                return this.poklapaSeSaPretragom(korisnik);
            });
        }
    },
});
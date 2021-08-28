Vue.component("admin-korisnici", {
    data() {
        return {
            korisnici: [],
            pretraga: {
                korisnickoIme: '',
                ime: '',
                prezime: ''
            },
            dijalogZaDodavanjeSakriven: true,
            noviKorisnik: {},
            prostorZaPretraguVidljiv: false,
        }
    },

    template: `
    <div>

        <button type="button" @click=" prostorZaPretraguVidljiv = !prostorZaPretraguVidljiv " class="btn"><i class="fa fa-search" aria-hidden="true"></i> Pretraga </button> 
        <button type="button" @click="dodajNovogKorisnika()" class="btn"><i class="fa fa-plus" aria-hidden="true"></i> Dodaj novog korisnika </button>
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
        <br>


        <!-- Tabela korisnika -->
        <div class="styleForTable" style="width: 70%;" >
            <table style="width:100%">

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

                    <button type="button" @click="potvrdiDodavanje">Potvrdi</button>
                    <button type="button" @click="dijalogZaDodavanjeSakriven = !dijalogZaDodavanjeSakriven">Odustani</button>

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
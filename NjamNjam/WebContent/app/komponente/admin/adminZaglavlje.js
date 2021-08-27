Vue.component("admin-zaglavlje",{
    data: function(){
        return{
            fleg:0
        }
    }
    ,
    template: `
    <div id="divHeaderGuest">
        <nav>
            <ul>
                <li><router-link to="/home" exact>Početna stranica</router-link></li>
                <li><router-link to="/profil" exact>Profil</router-link></li>
                <li><router-link to="/korisnici" exact>Korisnici</router-link></li>
                <li><router-link to="/restorani" exact>Restorani</router-link></li>
                <li><router-link to="/komentari" exact>Komentari</router-link></li>
                <li><button @click="odjava" > Odjava </button></li>
                
            </ul>
        </nav>

    </div>		  
    
    `,
    methods: {
        odjava: function(event){
            event.preventDefault
            axios
            .get('rest/odjava/korisnik')
            .then(response => {
                location.href = "/NjamNjam/#/prijava";
            })
            .catch(err => {
                console.log(err);
                alert('Greška prilikom odjave');
            })
        }
    },
});
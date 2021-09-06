Vue.component("moje-porudzbine-dostavljac", {
    data() {
        return {
            porudzbine: [],
            pretraga: {
                restoran: 0,
				cena: 0,
                datum : 0
            },
            imaPorudzbine : false
        }
    },
    template: `
    <div id = "styleForProfile">
        <h1> Pozdrav ! </h1>
    </div>
    `,
    methods: {
    },
     mounted() {
             axios
            .get('rest/Porudzbina/dobaviPorudzbineZaDostavu')
            .then(response => {
                this.porudzbine = [];
        		if(Object.prototype.toString.call(response.data) === '[object Array]')
        		{
					response.data.forEach(el => this.porudzbine.push(el));
					this.imaPorudzbine = true;
				}else{
        			this.imaPorudzbine = false;
        		}
                return this.porudzbine;
            });
     },

});
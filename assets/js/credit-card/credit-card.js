const allcaps = (s) => s.split("").map(c => c.toUpperCase()).join("")

class CreditCard {
    constructor(formdata) {
        this.fulldata = freeze(array(formdata.entries()))
        this.data     = freeze(this.normalize(formdata))
    }

    normalize(formdata) {
        const temp = {}
        const norm = {}

        this.fulldata.forEach(e => { temp[e[0]] = e[1] })

        norm.accname = {
            last:   temp['card-name-last'],
            first:  temp['card-name-first'],
            middle: temp['card-name-middle']
        }

        norm.type = temp['card-type']
        norm.name = allcaps(temp['card-name'])
        norm.soa = temp['credit-soa']

        if (norm.soa === 'Delivery') {
            norm.deliveryaddress = temp['delivery-add']
        } else {
            norm.deliveryaddress = norm.soa
        }

        return norm;
    }

    get normalizeddata() {
        return this.data
    }

    get accname() {
        return `${this.data.accname.last}, ${this.data.accname.first} ${this.data.accname.middle}`
    }

    get type() {
        return this.data.type
    }

    get name() {
        return this.data.name
    }

    get soa() {
        return this.data.soa
    }

    get deliveryaddress() {
        return this.data.deliveryaddress
    }

}
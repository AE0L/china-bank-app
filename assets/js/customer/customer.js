const caps = (s) => s.split(" ").map(w => w.charAt(0).toUpperCase() + w.slice(1)).join(" ")

class Customer {
    constructor(info) {
        if (info instanceof FormData) {
            this.data = freeze(this.normalize(info))
        } else {
            this.data = freeze(this.convert(info))
        }

        this.fulldata = null
    }

    normalize(formdata) {
        const temp     = {}
        const norm     = {}

        this.fulldata = array(formdata.entries())

        this.fulldata.forEach(e => { temp[e[0]] = e[1] })

        norm.name = {
            last:   caps(temp['name-last']),
            first:  caps(temp['name-first']),
            middle: caps(temp['name-middle'])
        }

        norm.birth = {
            date:    temp['birth-date'],
            place:   caps(temp['birth-place']),
            country: caps(temp['birth-country'])
        }

        norm.mother = {
            last:   caps(temp['mother-last']),
            first:  caps(temp['mother-first']),
            middle: caps(temp['mother-middle'])
        }

        norm.per_add = {
            no:       temp['home-address-no'],
            street:   temp['home-address-street'],
            barangay: temp['home-address-barangay'],
            city:     temp['home-address-city'],
            province: temp['home-address-province'],
            zip:      temp['home-address-zip']
        }

        norm.pre_add = {
            no:       temp['present-address-no'],
            street:   temp['present-address-street'],
            barangay: temp['present-address-barangay'],
            city:     temp['present-address-city'],
            province: temp['present-address-province'],
            zip:      temp['present-address-zip']
        }

        norm.emp_add = {
            no:       temp['employment-address-no'],
            street:   temp['employment-address-street'],
            barangay: temp['employment-address-barangay'],
            city:     temp['employment-address-city'],
            province: temp['employment-address-province'],
            zip:      temp['employment-address-zip']
        }

        norm.contact = {
            mobile: temp['mobile-no'],
            phone:   temp['home-no'],
            email:  temp['email-address']
        }

        norm.employment = {
            type:     caps(temp['employment-type']),
            gmi:      temp['gmi'],
            industry: caps(temp['employment-industry']),
            name:     caps(temp['employment-name']),
            position: caps(temp['employment-position']),
            contact:  temp['employment-contact']
        }

        norm.gender    = temp['gender']
        norm.nation    = caps(temp['nationality'])
        norm.civil     = temp['civil-status']
        norm.residency = temp['residency']
        norm.mailing   = temp['mailing-address']

        return norm
    }

    convert(info) {
        return {...info}
    }

    get name() {
        const last  = this.data.name.last
        const first = this.data.name.first
        const mid   = this.data.name.middle

        return `${last}, ${first} ${mid}`
    }

    get gender() {
        return this.data.gender
    }

    get birthdate() {
        return this.data.birth.date
    }

    get birthplace() {
        const place   = this.data.birth.place
        const country = this.data.birth.country

        return `${place}, ${country}`
    }

    get nation() {
        return this.data.nation
    }

    get mother() {
        const last  = this.data.mother.last
        const first = this.data.mother.first
        const mid   = this.data.mother.middle

        return `${last}, ${first} ${mid}`
    }

    get homeadd() {
        const n = this.data.per_add.no
        const s = this.data.per_add.street
        const b = this.data.per_add.barangay
        const c = this.data.per_add.city
        const z = this.data.per_add.zip
        const p = this.data.per_add.province

        return `${n}, ${s}, ${b}, ${z} ${c}, ${p}`
    }

    get presadd() {
        const n = this.data.pre_add.no
        const s = this.data.pre_add.street
        const b = this.data.pre_add.barangay
        const c = this.data.pre_add.city
        const z = this.data.pre_add.zip
        const p = this.data.pre_add.province

        return `${n}, ${s}, ${b}, ${z} ${c}, ${p}`
    }

    get mobile() {
        return this.data.contact.mobile
    }

    get phone() {
        return this.data.contact.phone
    }

    get email() {
        return this.data.contact.email
    }

    get emptype() {
        return this.data.employment.type
    }

    get gmi() {
        return this.data.employment.gmi
    }

    get empindustry() {
        return this.data.employment.industry
    }

    get empname() {
        return this.data.employment.name
    }

    get empposition() {
        return this.data.employment.position
    }

    get empcontact() {
        return this.data.employment.contact
    }

    get empadd() {
        const n = this.data.emp_add.no
        const s = this.data.emp_add.street
        const b = this.data.emp_add.barangay
        const c = this.data.emp_add.city
        const z = this.data.emp_add.zip
        const p = this.data.emp_add.province

        return `${n}, ${s}, ${b}, ${z} ${c}, ${p}`
    }

    get residency() {
        return this.data.residency
    }

    get mailing() {
        return this.data.mailing
    }
}
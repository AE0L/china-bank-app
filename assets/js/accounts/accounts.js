const AccountUtils = {
    is_sole_business: function(formdata) {
        const type = array(formdata.entries()).filter(e => e[0] === 'account-type')[0][1]

        return type === 'Sole Proprietorship'
    },

    is_joint_business: function(formdata) {
        const business = ['Corporation', 'Partnership']
        const type     = array(formdata.entries()).filter(e => e[0] === 'account-type')[0][1]

        return business.includes(type)
    },

    is_joint: function(formdata) {
        const joints = ['Joint AND', 'Joint OR']
        const type   = array(formdata.entries()).filter(e => e[0] === 'account-type')[0][1]

        return joints.includes(type)
    }
}

class Account {
    constructor(formdata) {
        this.fulldata = {}
        this.data     = {}

        for (const e of formdata.entries()) {
            this.fulldata[e[0]] = e[1]
        }

        this.data.account_type    = this.fulldata['account-type']
        this.data.product_type    = this.fulldata['product-type']
        this.data.soa_disposition = this.fulldata['soa-disposition']
        this.data.passbook        = this.fulldata['passbook'] || ''
    }
}

class IndividualAccount extends Account {
    constructor(formdata) {
        super(formdata)

        this.data.account_name = {
            last:   this.fulldata['account-name-last'],
            first:  this.fulldata['account-name-first'],
            middle: this.fulldata['account-name-middle']
        }
    }

    get account_type() {
        return 'individual'
    }
}

class JointAccount extends Account {
    constructor(formdata) {
        super(formdata)

        this.data.signatories = []
        this.data.account_name = {
            last:   this.fulldata['account-name-last'],
            first:  this.fulldata['account-name-first'],
            middle: this.fulldata['account-name-middle']
        }

        const filter_fulldata = (filter) => entries(this.fulldata)
                                                .filter(a => a[0].startsWith(filter))
                                                .map(a => a[1])

        const name_last   = filter_fulldata('sig-name-last')
        const name_first  = filter_fulldata('sig-name-first')
        const name_middle = filter_fulldata('sig-name-middle')
        const birth       = filter_fulldata('sig-birth')

        for (const i in name_last) {
            this.data.signatories.push({
                last:   name_last[i],
                first:  name_first[i],
                middle: name_middle[i]
            })
        }
    }

    get account_type() {
        return 'joint'
    }
}

class JointBusinessAccount extends JointAccount {
    constructor(formdata) {
        super(formdata)

        this.data.account_name   = null
        this.data.business_name  = this.fulldata['business-name']
        this.data.contact_person = {
            name: {
                last:   this.fulldata['contact-name-last'],
                first:  this.fulldata['contact-name-first'],
                middle: this.fulldata['contact-name-middle'],
            },
            position: this.fulldata['contact-person-position'],
            contact:  this.fulldata['contact-person-no'],
            email:    this.fulldata['contact-person-email'],
        }
    }

    get account_type() {
        return 'joint-business'
    }
}

class SoleBusinessAccount extends Account {
    constructor(formdata) {
        super(formdata)

        this.data.business_name = this.fulldata['business-name']
        this.data.contact_person = {
            name: {
                last:   this.fulldata['contact-name-last'],
                first:  this.fulldata['contact-name-first'],
                middle: this.fulldata['contact-name-middle'],
            },
            position: this.fulldata['contact-person-position'],
            contact:  this.fulldata['contact-person-no'],
            email:    this.fulldata['contact-person-email'],
        }
    }

    get account_type() {
        return 'sole-business'
    }
}

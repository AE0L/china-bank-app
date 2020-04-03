class CashTransaction {
    constructor(formdata) {
        this.fulldata = freeze(array(formdata.entries()))
        this.data     = freeze(this.normalize(formdata))
    }

    normalize(formdata) {
        const temp = {}
        const norm = {}

        for (const e of this.fulldata) {
            temp[e[0]] = e[1]
        }

        norm.type         = temp['type']
        norm.customer_name = {
            last:   temp['customer-name-last'],
            first:  temp['customer-name-first'],
            middle: temp['customer-name-middle'],
        }
        norm.account_no   = temp['account-no']
        norm.currency     = temp['currency']
        norm.total_amount = temp['total-amount']

        if (norm.currency === 'PHP') {
            const cash_breakdown = this.fulldata.filter(e => e[0].startsWith('denom-'))

            norm.cash_breakdown = cash_breakdown
                .filter(c => parseInt(c[1]) !== 0)
                .map(c => {
                    const denom = c[0].slice(6)
                    return {
                        denom: denom === 'Centavos' ? '0.5' : denom,
                        piece: c[1]
                    }
                })
        } else {
            const denoms = this.fulldata.filter(e => e[0].startsWith('denom'))
            const serials = this.fulldata.filter(e => e[0].startsWith('serial'))

            norm.cash_breakdown = denoms.map((denom, i) => {
                return {
                    denom: denom[1],
                    serial: serials[i][1]
                }
            })
        }

        return norm
    }

    get type() {
        return this.data.type
    }

    get accountno() {
        return this.data.accountno
    }

    get currency() {
        return this.data.currency
    }

    get cashbreakdown() {
        return this.data.cash_breakdown
    }

    get totalamount() {
        return this.data.totalamount
    }

    islocal() {
        return this.data.currency === 'PHP'
    }
}

class CheckDepositTransaction {
    constructor(formdata) {
        this.fulldata = freeze(array(formdata.entries()))
        this.data     = freeze(this.normalize(formdata))
    }

    normalize(formdata) {
        const temp = {}
        const norm = {}

        for (const e of this.fulldata) {
            temp[e[0]] = e[1]
        }

        norm.type = 'check'
        norm.account_no = temp['account-no']
        norm.customer_name = {
            last:   temp['customer-name-last'],
            first:  temp['customer-name-first'],
            middle: temp['customer-name-middle'],
        }
        norm.check_type = temp['check-type']
        norm.check_details = []
        norm.total_amount = temp['total-amount']

        const branches = this.fulldata.filter(a => a[0].startsWith('check-branch-'))
        const numbers  = this.fulldata.filter(a => a[0].startsWith('check-no-'))
        const amounts  = this.fulldata.filter(a => a[0].startsWith('check-amount-'))

        for (const i in branches) {
            norm.check_details.push({
                branch: branches[i][1],
                no:     numbers[i][1],
                amount: amounts[i][1]
            })
        }

        return norm
    }

    get normalizeddata() {
        return this.data
    }

    get type() {
        return this.data.type
    }

    get accountno() {
        return this.data.accountno
    }

    get checktype() {
        return this.data.checktype
    }

    get checkdetails() {
        return this.data.check_details
    }

    get totalamount() {
        return this.data.totalamount
    }
}

class PaymentTransaction {
    constructor(formdata) {
        this.fulldata = freeze(array(formdata.entries()))
        this.data     = freeze(this.normalize())
    }

    normalize() {
        const temp = {}
        const norm = {}

        for (const e of this.fulldata) {
            temp[e[0]] = e[1]
        }

        norm.type          = 'payment'
        norm.business_name = temp['company-name']
        norm.customer_name = {
            last:   temp['customer-name-last'],
            first:  temp['customer-name-first'],
            middle: temp['customer-name-middle'],
        }
        norm.reference_no  = temp['reference-no']
        norm.other_details = temp['other-details']
        norm.currency      = temp['currency']
        norm.payment_mode  = temp['payment-mode']
        norm.total_amount  = temp['total-amount']

        switch (norm.payment_mode) {
            case 'cash':
                const is_denom  = (e) => e[0].startsWith('denom-')
                const is_serial = (e) => e[0].startsWith('serial')

                norm.cash_breakdown = []

                if (norm.currency === 'PHP') {
                    const cash_breakdown = this.fulldata.filter(e => e[0].startsWith('denom-'))

                    norm.cash_breakdown = cash_breakdown
                        .filter(c => parseInt(c[1]) !== 0)
                        .map(c => {
                            const denom = c[0].slice(6)
                            return {
                                denom: denom === 'Centavos' ? '0.5' : denom,
                                piece: c[1]
                            }
                        })
                } else {
                    const serials = this.fulldata.filter(is_serial)
                    const denoms  = this.fulldata.filter(is_denom)

                    for (const i in serials) {
                        norm.cash_breakdown.push({
                            serial: serials[i][1],
                            denom:  float(denoms[i][1])
                        })
                    }
                }
                break

            case 'check':
                const is_branch = (e) => e[0].startsWith('check-branch-')
                const is_no     = (e) => e[0].startsWith('check-no-')
                const is_amount = (e) => e[0].startsWith('check-amount-')
                const branches  = this.fulldata.filter(is_branch)
                const numbers   = this.fulldata.filter(is_no)
                const amounts   = this.fulldata.filter(is_amount)

                norm.check_details = []

                for (const i in branches) {
                    norm.check_details.push({
                        branch: branches[i][1],
                        no:     numbers[i][1],
                        amount: float(amounts[i][1])
                    })
                }
                break

            case 'debit':
                norm.debit_no = temp['debit-no']
                console.log(norm.debit_no)
                break
        }

        return norm
    }

    islocal() {
        return this.currency === 'PHP'
    }
    
    get normalizedata() {
        return this.data
    }

    get company() {
        return this.data.company
    }

    get customer() {
        return this.data.customer
    }

    get reference() {
        return this.data.reference
    }

    get currency() {
        return this.data.currency
    }

    get mode() {
        return this.data.mode
    }

    get totalamount() {
        return this.data.totalamount
    }

    get cashbreakdown() {
        return this.data.cash_breakdown || 'payment mode is not cash'
    }

    get checkdetails() {
        return this.data.check_details || 'payment mode is not check'
    }

    get debitno() {
        return this.data.debitno || 'payment mode is not debit'
    }

    get otherdetails() {
        return this.data.otherdetails === '' ? 'None' : this.data.otherdetails
    }
}

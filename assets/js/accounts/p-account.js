window.onload = () => {
    const no = new URLSearchParams(location.search).get('no')

    const req = {
        controller: 'account',
        method: {
            type: 'preview',
            data: {
                no: no
            }
        }
    }

    if ('_controller' in window) {
        _controller({
            request: jsontostring(req),

            onSuccess: (res) => {
                const account = jsonparse(res)

                load_data(account)
            }
        })
    }
}

function load_data(account) {
    const account_no     = e('account-no')
    const date_open      = e('date-open')
    const product_type   = e('product-type')
    const account_type   = e('account-type')
    const currency       = e('currency')
    const soa            = e('soa')
    const business_wrap  = e('business-wrapper')
    const business_name  = e('business-name')
    const cp_wrap        = e('contact-wrapper')
    const cp_name        = e('contact-name')
    const cp_position    = e('contact-position')
    const cp_contact     = e('contact-number')
    const cp_email       = e('contact-email')
    const holder_wrapper = e('holder-wrapper')

    account_no.innerText   = account.no
    date_open.innerText    = `Opened: ${account.dateOpen}`
    product_type.innerText = account.product
    account_type.innerText = account.type
    currency.innerText     = account.currency
    soa.innerText          = account.soa

    if (account.isBusiness) {
        style_show(business_wrap)
        style_show(cp_wrap)

        business_name.innerText = account.business.name
        cp_name.innerText       = name(account.business.contact.name)
        cp_position.innerText   = account.business.contact.position
        cp_contact.innerText    = account.business.contact.no
        cp_email.innerText      = account.business.contact.email
    }

    if (account.holders.length > 0) {
        for (holder of account.holders) {
            const holder_wrap = create('div')
            const holder_name = create('label')
            const nation      = create('label')
            const birth_label = create('label')
            const birth_value = create('label')
            const civil_label = create('label')
            const civil_value = create('label')

            holder_wrap.id = holder.id
            holder_wrap.classList.add('holder__entry-wrapper')
            holder_wrap.classList.add('card')
            holder_name.classList.add('holder__name')
            nation.classList.add('holder__nation')
            birth_label.classList.add('account__label')
            civil_label.classList.add('account__label')

            holder_name.innerText = name(holder.name)
            nation.innerText      = `${holder.gender}, ${holder.nation}`
            birth_label.innerText = 'Birth'
            birth_value.innerText = `${holder.birth.date}, ${holder.birth.place}, ${holder.birth.country}`
            civil_label.innerText = 'Civil Status'
            civil_value.innerText = holder.civilStatus

            appendchildren(holder_wrap, holder_name, nation, birth_label, birth_value, civil_label, civil_value)
            appendchild(holder_wrapper, holder_wrap)

            e(holder.id).onclick = function() {
                const param = new URLSearchParams({id: this.id})

                redirectWithParams('pages/p-customer.html', param.toString())
            }
        }
    } else {
        style_hide(e('account-holder-title'))
    }
}

function name(n) {
    return  `${n.last}, ${n.first} ${n.middle}`
}
window.onload = () => {
    const id = new URLSearchParams(location.search).get('id')

    const req = {
        controller: 'customer',
        method: {
            type: 'preview',
            data: {
                id: id
            }
        }
    }

    if ('_controller' in window) {
        _controller({
            request: jsontostring(req),

            onSuccess: (res) => {
                const cif = jsonparse(res)

                load_data(id, cif)
            },

            onFailure: (code, msg) => {
                console.error(`[${code}]: ${msg}`)
            }
        })
    } else {
        console.error('_controller not found')
    }
}

function load_data(id, customer) {
    const cif_id          = e('cif-id')
    const cif_created     = e('cif-created')
    const cif_name        = e('cif-name')
    const cif_nation      = e('cif-nation')
    const cif_civil       = e('cif-civil')
    const cif_birth       = e('cif-birth')
    const cif_mother      = e('cif-mother')
    const cif_residency   = e('cif-residency')
    const contact_mobile  = e('contact-mobile')
    const contact_phone   = e('contact-phone')
    const contact_email   = e('contact-email')
    const contact_mailing = e('contact-mailing')
    const per_add         = e('per-add')
    const pre_add         = e('pre-add')
    const emp_gmi         = e('emp-gmi')
    const emp_type        = e('emp-type')
    const emp_industry    = e('emp-industry')
    const emp_name        = e('emp-name')
    const emp_position    = e('emp-position')
    const emp_contact     = e('emp-contact')
    const emp_add         = e('emp-add')

    cif_id.innerText          = customer.id
    cif_created.innerText     = `Created: ${customer.dateCreated}`
    cif_name.innerText        = name(customer.name)
    cif_nation.innerText      = `${customer.gender}, ${customer.nationality}`
    cif_civil.innerText       = customer.civilStatus
    cif_birth.innerText       = `${customer.birth.date}, ${customer.birth.place} ${customer.birth.country}`
    cif_mother.innerText      = name(customer.mother)
    cif_residency.innerText   = customer.residency
    contact_mobile.innerText  = customer.contact.mobile
    contact_phone.innerText   = customer.contact.phone
    contact_email.innerText   = customer.contact.email
    contact_mailing.innerText = customer.contact.mailing
    per_add.innerText         = address(customer.address.permanent)
    pre_add.innerText         = address(customer.address.present)
    emp_gmi.innerText         = customer.employment.gmi
    emp_type.innerText        = customer.employment.type
    emp_industry.innerText    = customer.employment.industry
    emp_name.innerText        = customer.employment.name
    emp_position.innerText    = customer.employment.position
    emp_contact.innerText     = customer.employment.contact
    emp_add.innerText         = address(customer.address.employment)

    e('edit-btn').onclick = () => {
        const params = new URLSearchParams()
        params.append('id', id)
        redirectWithParams('pages/e-customer.html', params.toString())
    }
}

function name(n) {
    return `${n.last}, ${n.first} ${n.middle}`
}

function address(add) {
    return `${add.unit}, ${add.street}, ${add.village}, ${add.city}, ${add.zip}, ${add.province}`
}
const form               = e('customer-form')
const ver_cancel         = e('ver-cancel')
const ver_submit         = e('ver-submit')
const main_body          = e('main-body')
const modal              = e('modal')
const same_add           = e('same-add')
const fil_nation         = e('fil-nationality')
const other_nation_radio = e('other-nation-radio')
const other_nation_input = e('other-nation-input')
const other_nation       = e('other-nationality')
const other_emp          = e('other-employment')
const other_emp_input    = e('other-emp-input')
const emps               = e('employments')
const other_ind          = e('other-industry')
const other_ind_input    = e('other-ind-input')
const inds               = e('industries')
const add_parts          = ['no', 'street', 'barangay', 'city', 'province', 'zip']
const home_add           = add_parts.map(p => names(`home-address-${p}`, 0))
const pres_add           = add_parts.map(p => names(`present-address-${p}`, 0))

// ADDRESS
same_add.onchange = function() {
    if (this.checked) {
        for (const i in home_add) {
            const pres_part = pres_add[i]
            const home_part = home_add[i]
            const copy_home_value = () => { pres_part.value = home_part.value }

            copy_home_value()
            home_part.oninput = () => { copy_home_value() }
            pres_part.readOnly = true
        }
    } else {
        for (const pres_part of pres_add) { 
            pres_part.value    = ''
            pres_part.readOnly = false
        }

        for (const home_part of home_add) { 
            home_part.oninput = null
        }
    }
}

// NATIONALITY
other_nation.onclick = () => {
    other_nation_radio.checked = true
    enable(other_nation_input)
    focus(other_nation_input)
}

other_nation_input.oninput = () => {
    other_nation_radio.value = other_nation_input.value
}

fil_nation.onchange = () => {
    disable(other_nation_input)
}

// EMPLOYMENT
other_emp.onchange = function() {
    if (this.checked) {
        disable(emps)
        enable(other_emp_input)
        focus(other_emp_input)
    } else {
        enable(emps)
        disable(other_emp_input)
    }
}

// INDUSTRIES
other_ind.onchange = function() {
    if (this.checked) {
        disable(inds)
        enable(other_ind_input)
        focus(other_ind_input)
    } else {
        enable(inds)
        disable(other_ind_input)
    }
}

// SUBMIT
form.onsubmit = (evt) => {
    evt.preventDefault()

    const formdata     = new FormData(form)
    const customer     = new Customer(formdata)

    const ver_ids      = [
        ['ver-name',         customer.name],
        ['ver-gender',       customer.gender],
        ['ver-birth-date',   customer.birthdate],
        ['ver-birth-place',  customer.birthplace],
        ['ver-nationality',  customer.nation],
        ['ver-mother-name',  customer.mother],
        ['ver-home-add',     customer.homeadd],
        ['ver-pres-add',     customer.presadd],
        ['ver-mobile-no',    customer.mobile],
        ['ver-home-no',      customer.phone],
        ['ver-email-add',    customer.email],
        ['ver-emp-type',     customer.emptype],
        ['ver-gmi',          customer.gmi],
        ['ver-emp-industry', customer.empindustry],
        ['ver-emp-name',     customer.empname],
        ['ver-emp-position', customer.empposition],
        ['ver-emp-contact',  customer.empcontact],
        ['ver-emp-add',      customer.empadd],
        ['ver-residency',    customer.residency],
        ['ver-mail-add',     customer.mailing],
    ]

    main_body.classList.add('noscroll')
    modal.classList.remove('hidden')

    for (const ver_part of ver_ids) {
        e(ver_part[0]).innerText = ver_part[1]
    }

    ver_submit.onclick = () => {
        const id = new URLSearchParams(location.search).get('id')
        const request = {
            controller: 'customer',
            method: {
                type: 'update',
                data: {
                    id: id,
                    customer: customer.data
                }
            }
        }

        _controller({
            request: jsontostring(request),

            onSuccess: function(res) {
                const param = new URLSearchParams({id: id});

                redirectWithParams('pages/p-customer.html', param.toString())
            },

            onFailure: function(code, msg) {
                style_hide(modal)
                style_show(e('error-modal'))

                e('error-text').innerText = msg
            },
        })
    }
}

function closeErrorModal() {
    style_hide(e('error-modal'))
    style_scroll(main_body)
}

ver_cancel.onclick = () => {
    style_scroll(main_body)
    style_hide(modal)
}

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
                const customer = jsonparse(res)

                load_data(customer)
            },

            onFailure: (code, msg) => {
                console.log(`[${code}]: ${msg}`)
            }
        })
    }
}

function load_data(customer) {
    const name_last            = e('name-last')
    const name_first           = e('name-first')
    const name_middle          = e('name-middle')
    const gender_f             = e('gender-f')
    const gender_m             = e('gender-m')
    const birth_date           = e('birth-date')
    const birth_place          = e('birth-place')
    const birth_country        = e('birth-country')
    const nation_fil           = e('fil-nationality')
    const nation_other_radio   = e('other-nation-radio')
    const nation_other_input   = e('other-nation-input')
    const civil_status         = e('civil-status')
    const mother_last          = e('mother-last')
    const mother_first         = e('mother-first')
    const mother_middle        = e('mother-middle')
    const per_unit             = e('per-unit')
    const per_street           = e('per-street')
    const per_village          = e('per-village')
    const per_city             = e('per-city')
    const per_province         = e('per-province')
    const per_zip              = e('per-zip')
    const same_add             = e('same-add')
    const pre_unit             = e('pre-unit')
    const pre_street           = e('pre-street')
    const pre_village          = e('pre-village')
    const pre_city             = e('pre-city')
    const pre_province         = e('pre-province')
    const pre_zip              = e('pre-zip')
    const mobile               = e('mobile-no')
    const phone                = e('phone-no')
    const email                = e('email')
    const emp_type             = e('employments')
    const emp_type_other       = e('other-employment')
    const emp_type_other_input = e('other-emp-input')
    const gmi                  = e('gmi')
    const emp_industry         = e('industries')
    const emp_ind_other        = e('other-industry')
    const emp_ind_other_input  = e('other-ind-input')
    const emp_name             = e('emp-name')
    const emp_position         = e('emp-position')
    const emp_contact          = e('emp-contact')
    const emp_unit             = e('emp-unit')
    const emp_street           = e('emp-street')
    const emp_village          = e('emp-village')
    const emp_city             = e('emp-city')
    const emp_province         = e('emp-province')
    const emp_zip              = e('emp-zip')
    const resident             = e('resident')
    const non_resident         = e('non-resident')
    const mail_home            = e('mail-home')
    const mail_pres            = e('mail-pres')
    const mail_work            = e('mail-work')

    name_last.value   = customer.name.last
    name_first.value  = customer.name.first
    name_middle.value = customer.name.middle

    if (customer.gender === 'Female')
        gender_f.checked = true
    else 
        gender_m.checked = true

    birth_date.value    = customer.birth.date
    birth_place.value   = customer.birth.place
    birth_country.value = customer.birth.country

    if (customer.nationality === 'Filipino') {
        nation_fil.checked = true
    } else {
        other_nation_radio.checked = true
        other_nation_input.value   = customer.nationality
    }

    civil_status.value  = customer.civilStatus
    mother_last.value   = customer.mother.last
    mother_first.value  = customer.mother.first
    mother_middle.value = customer.mother.middle
    per_unit.value      = customer.address.permanent.unit
    per_street.value    = customer.address.permanent.street
    per_village.value   = customer.address.permanent.village
    per_city.value      = customer.address.permanent.city
    per_province.value  = customer.address.permanent.province
    per_zip.value       = customer.address.permanent.zip

    if (entries(customer.address.permanent).every(e => e[1] === customer.address.present[e[0]])) {
        same_add.click()
    } else {
        pre_unit.value     = customer.address.present.unit
        pre_street.value   = customer.address.present.street
        pre_village.value  = customer.address.present.village
        pre_city.value     = customer.address.present.city
        pre_province.value = customer.address.present.province
        pre_zip.value      = customer.address.present.zip
    }

    mobile.value       = customer.contact.mobile
    phone.value        = customer.contact.phone
    email.value        = customer.contact.email
    emp_unit.value     = customer.address.employment.unit
    emp_street.value   = customer.address.employment.street
    emp_village.value  = customer.address.employment.village
    emp_city.value     = customer.address.employment.city
    emp_province.value = customer.address.employment.province
    emp_zip.value      = customer.address.employment.zip
    gmi.value          = customer.employment.gmi
    emp_name.value     = customer.employment.name
    emp_position.value = customer.employment.position
    emp_contact.value  = customer.employment.contact

    if (array(emp_type.options).map(o => o.value).includes(customer.employment.type)) {
        emp_type.value = customer.employment.type
    } else {
        emp_type_other.click()
        emp_type_other_input.value = customer.employment.type
    }

    if (array(emp_industry.options).map(o => o.value).includes(customer.employment.industry)) {
        emp_industry.value = customer.employment.industry
    } else {
        emp_ind_other.click()
        emp_ind_other_input.value = customer.employment.industry
    }

    if (customer.residency === 'Resident')
        resident.checked = true
    else
        non_resident.checked = true

    switch (customer.contact.mailing) {
        case 'permanent': mail_home.checked = true; break;
        case 'present':   mail_pres.checked = true; break;
        case 'work':      mail_work.checked = true; break;
    }

    name_last.focus()
}
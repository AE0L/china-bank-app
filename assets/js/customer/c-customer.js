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
        ['ver-emp-position', customer.emppostion],
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
        const request = {
            controller: 'customer',
            method: {
                type: 'save',
                data: customer.data
            }
        }

        _controller({
            request: jsontostring(request),

            onSuccess: function(id) {
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

ver_cancel.onclick = () => {
    style_scroll(main_body)
    style_hide(modal)
}

function closeErrorModal() {
    style_hide(e('error-modal'))
    style_scroll(main_body)
}


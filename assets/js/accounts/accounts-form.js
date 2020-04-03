const account_types       = e('account-types')
const product_types       = e('product-types')

// PASSBOOK
const passbook_label  = e('passbook-label')
const passbook_input  = e('passbook-input')
const passbook_radios = names('passbook')

product_types.onchange = function() {
    const is_passbook     = this.value === 'Passbook Savings'
    const toggle_passbook = (style) => style_apply(style, passbook_label, passbook_input)

    toggle_passbook(is_passbook ? style_show : style_hide)
    passbook_radios.forEach(is_passbook ? enable : disable)
}


// SOA MAILING
const no_soa     = e('no-soa')
const soa_mail   = e('soa-mail')
const soa_pickup = e('soa-pickup')
const mail_label = e('mail-label')
const mail_input = e('mail-input')
const mails      = names('mailing-address')

array([no_soa, soa_pickup]).forEach(r => r.onchange = () => {
    style_apply(style_hide, mail_label, mail_input)
    mails.forEach(disable)
})

soa_mail.onchange = () => {
    style_apply(style_show, mail_label, mail_input)
    mails.forEach(enable)
}

// PARTNERSHIP/COMPANY DETAILS & SINGATORIES
const pcd_title              = e('pcd-title')
const contact_name           = e('contact-name')
const contact_name_input     = e('contact-name-input')
const contact_position       = e('contact-position')
const contact_position_input = e('contact-position-input')
const contact_no             = e('contact-no')
const contact_no_input       = e('contact-no-input')
const contact_email          = e('contact-email')
const contact_email_input    = e('contact-email-input')
const account_name           = e('account-name')
const account_name_input     = e('account-name-input')
const business_name          = e('business-name')
const business_name_input    = e('business-name-input')
const account_names          = ['last', 'first', 'middle'].map(a => names(`account-name-${a}`, 0))
const contact_names          = ['last', 'first', 'middle'].map(a => names(`contact-name-${a}`, 0))
const sig_title              = e('sig-title')
const sig_table              = e('sig-table')
const sig_table_body         = e('sig-table-body')

let sig_count = 0

account_types.onchange = function() {
    const { value }            = this
    const joins                = ['Joint AND', 'Joint OR', 'Corporation', 'Partnership']
    const business             = ['Sole Proprietorship', 'Partnership', 'Corporation']
    const corporation          = ['Partnership', 'Corporation']
    const is_business          = business.includes(value)
    const is_joint             = joins.includes(value)
    const toggle_account_name  = (s) => style_apply(s, account_name, account_name_input)
    const toggle_business_name = (s) => style_apply(s, business_name, business_name_input)
    const toggle_signatory     = (s) => style_apply(s, sig_title, sig_table)
    const toggle_section       = (s) => style_apply(s, pcd_title,
        contact_name, contact_name_input,
        contact_position, contact_position_input,
        contact_no, contact_no_input,
        contact_email, contact_email_input
    )

    clearchildren(sig_table_body)
    sig_count = 0
    
    business_name_input.value = ''
    account_names.forEach(p => { p.value = '' })

    toggle_section(is_business ? style_show : style_hide)
    toggle_section(is_business ? enable : disable)
    toggle_signatory(is_joint ? style_show : style_hide)
    toggle_account_name(is_business ? style_hide : style_show)
    toggle_business_name(is_business ? style_show : style_hide)

    account_names.forEach(is_business ? disable : enable)
    contact_names.forEach(is_business ? enable : disable)

    style_apply(is_business ? enable : disable,
        contact_position_input, contact_no_input, contact_email_input
    )

    { (is_business ? enable : disable)(business_name_input) }


    if (is_joint && sig_count === 0) {
        add_sig_row(sig_count)
    }
}

const add_sig_row = (i) => {
    const row         = create('tr')
    const td1         = create('td')
    const td2         = create('td')
    const add         = create('button')
    const icon        = create('i')
    const name_cont   = create('div')
    const name_parts  = ['last', 'first', 'middle'].map(part => {
        const input       = create('input')
        input.name        = `sig-name-${part}-${i}`
        input.placeholder = string_cap(part)
        input.required    = true

        input.classList.add('form__input-text', 'form__input-no-bottom')

        return input
    })

    name_parts.forEach(p => appendchild(name_cont, p))
    add.type       = 'button'

    name_cont.classList.add('col-3', 'col-05-gap')
    add.classList.add('form__table_button-add')
    icon.classList.add('fas', 'fa-plus')

    appendchild(add, icon)
    appendchild(td1, name_cont)
    appendchild(td2, add)
    appendchildren(row, td1, td2)
    appendchild(sig_table_body, row)

    add.onclick = () => {
        add_sig_row(i + 1)

        icon.classList.replace('fa-plus', 'fa-minus')

        add.onclick = () => sig_table_body.removeChild(row)
    }
}


// FORM SUBMISSION & VERIFICATION
const body        = e('main-body')
const form        = e('accounts-form')
const modal       = e('modal')

const ver_cancel                     = e('ver-cancel')
const ver_submit                     = e('ver-submit')
const vpassbook_label                = e('ver-passbook-label')
const vpassbook                      = e('ver-passbook')
const vbusiness_name_label           = e('ver-business-name-label')
const vbusiness_name                 = e('ver-business-name')
const vaccount_name_label            = e('ver-account-name-label')
const vaccount_name                  = e('ver-account-name')
const vmailing_label                 = e('ver-mailing-label')
const vmailing                       = e('ver-mailing')
const vcontact_person_title          = e('ver-contact-person-title')
const vcontact_person_name_label     = e('ver-contact-person-name-label')
const vcontact_person_name           = e('ver-contact-person-name')
const vcontact_person_position_label = e('ver-contact-person-position-label')
const vcontact_person_position       = e('ver-contact-person-position')
const vcontact_person_no_label       = e('ver-contact-person-no-label')
const vcontact_person_no             = e('ver-contact-person-no')
const vcontact_person_email_label    = e('ver-contact-person-email-label')
const vcontact_person_email          = e('ver-contact-person-email')
const vsig_title                     = e('ver-sig-title')
const vsig_table                     = e('ver-sig-table')
const vsig_table_body                = e('ver-sig-table-body')

const vtoggle_passbook       = (s) => style_apply(s, vpassbook_label, vpassbook)
const vtoggle_account_name   = (s) => style_apply(s, vaccount_name_label, vaccount_name)
const vtoggle_business_name  = (s) => style_apply(s, vbusiness_name_label, vbusiness_name)
const vtoggle_signatories    = (s) => style_apply(s, vsig_title, vsig_table)
const vtoggle_contact_person = (s) => style_apply(s, vcontact_person_title,
        vcontact_person_name_label, vcontact_person_name,
        vcontact_person_position_label, vcontact_person_position,
        vcontact_person_no_label, vcontact_person_no,
        vcontact_person_email_label, vcontact_person_email
    )

form.onsubmit = function(evt) {
    evt.preventDefault()

    style_noscroll(body)
    style_show(modal)

    clearchildren(vsig_table_body)

    const formdata = new FormData(this)
    let   account  = null

    // account type
    if (AccountUtils.is_sole_business(formdata)) {
        account = new SoleBusinessAccount(formdata)
    } else if (AccountUtils.is_joint_business(formdata)) {
        account = new JointBusinessAccount(formdata)
    } else if (AccountUtils.is_joint(formdata)) {
        account = new JointAccount(formdata)
    } else {
        account = new IndividualAccount(formdata)
    }

    const { account_type, product_type, soa_disposition } = account.data

    console.log(account.data)

    const vers = [
        ['ver-account-type', account_type],
        ['ver-product-type', product_type],
        ['ver-soa',          soa_disposition],
    ]

    for (const v of vers) {
        e(v[0]).innerText = v[1]
    }

    const { passbook } = account.data

    if (passbook) {
        vtoggle_passbook(style_show)
        vpassbook.innerText = passbook
    } else {
        vtoggle_passbook(style_hide)
    }

    const { account_name, business_name } = account.data

    if (!!account_name) {
        vtoggle_account_name(style_show)
        vtoggle_business_name(style_hide)
        vaccount_name.innerText = `${account_name.last}, ${account_name.first} ${account_name.middle}`
    } else {
        vtoggle_account_name(style_hide)
        vtoggle_business_name(style_show)
        vbusiness_name.innerText = business_name
    }

    const { contact_person } = account.data

    if (account instanceof JointBusinessAccount) {
        vtoggle_contact_person(style_show)
        vcontact_person_name.innerText     = `${contact_person.name.last}, ${contact_person.name.first} ${contact_person.name.middle}`
        vcontact_person_position.innerText = contact_person.position
        vcontact_person_no.innerText       = contact_person.contact
        vcontact_person_email.innerText    = contact_person.email
    } else {
        vtoggle_contact_person(style_hide)
    }

    const { signatories } = account.data

    if (account instanceof JointAccount || account instanceof JointBusinessAccount) {
        vtoggle_signatories(style_show)

        for (const sig of signatories) {
            const row = create('tr')
            const td1 = create('td')

            td1.innerText = `${sig.last}, ${sig.first} ${sig.middle}`

            appendchild(row, td1)
            appendchild(vsig_table_body, row)
        }
    } else {
        vtoggle_signatories(style_hide)
    }

    ver_submit.onclick = () => {
        const req = {
            controller: 'account',
            method: {
                type: 'save',
                data: {
                    account_type: account.account_type,
                    account: account.data
                }
            }
        }

        if ('_controller' in window) {
            _controller({
                request: jsontostring(req),

                onSuccess: (no) => {
                    const param = new URLSearchParams({no: no})

                    redirectWithParams('pages/p-account.html', param.toString());
                },

                onFailure: (code, msg) => {
                    style_hide(modal)
                    style_show(e('error-modal'))

                    e('error-text').innerText = msg
                }
            })
        }
    }
}

ver_cancel.onclick = () => {
    style_hide(modal)
    style_scroll(body)
}

function closeErrorModal() {
    style_scroll(body)
    style_hide(e('error-modal'))
}

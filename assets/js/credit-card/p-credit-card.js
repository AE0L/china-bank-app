const SAMPLE = {
    no: '5555111122223333',
    name: 'CARL JUSTIN JIMENEZ',
    type: 'Prime',
    delivery: 'Present',
    exp: '3/31/2020',
    limit: 1000,
    customer: {
        name: {
            last: 'Jimenez',
            first: 'Carl Justin',
            middle: 'P'
        },
        birth: {
            date: '2000-06-15',
            place: 'Pasig',
            country: 'Philippines'
        },
        gender: 'Male',
        nation: 'Filipino',
        civilStatus: 'Single'
    }
}

window.onload = () => {
    const no = new URLSearchParams(location.search).get('no')

    const req = {
        controller: 'credit-card',
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
                const card = jsonparse(res)

                load_data(card)
            },

            onFailure: (code, msg) => {
                console.error(`[${code}]: ${msg}`)
            }
        })
    }
}

function load_data(card) {
    const card_no    = e('card-no')
    const card_name  = e('card-name')
    const card_type  = e('card-type')
    const card_mail  = e('card-mail')
    const card_exp   = e('card-exp')
    const card_limit = e('card-limit')
    const c_name     = e('customer-name')
    const c_nation   = e('customer-nation')
    const c_birth    = e('customer-birth')
    const c_civil    = e('customer-civil')
    const c_wrap     = e('customer')

    card_no.innerText    = card_number(card.no)
    card_name.innerText  = card.name
    card_type.innerText  = card.type
    card_mail.innerText  = card.delivery
    card_limit.innerText = card.limit
    card_exp.innerText   = `ExpDate: ${card.exp}`
    c_name.innerText     = `${card.customer.name.last}, ${card.customer.name.first} ${card.customer.name.middle}`
    c_nation.innerText   = `${card.customer.gender}, ${card.customer.nation}`
    c_birth.innerText    = `${card.customer.birth.date}, ${card.customer.birth.place}, ${card.customer.birth.country}`
    c_civil.innerText    = card.customer.civilStatus

    c_wrap.onclick = () => {
        const param = new URLSearchParams({id: card.customer.id})

        redirectWithParams('pages/p-customer.html', param.toString())
    }
}

function card_number(no) {
    return no.match(/.{1,4}/g).join(' ')
}
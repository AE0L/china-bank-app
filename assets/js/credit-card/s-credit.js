const form           = e('search-form')
const size           = e('result-size')
const type           = e('type')
const query          = e('query')
const table          = e('tbody-result')
const holder_wrapper = e('holder-wrapper')
const holder_last    = e('holder-last')
const holder_first   = e('holder-first')
const holder_middle  = e('holder-middle')

function closeErrorModal() {
    style_hide(e('error-modal'))
    style_scroll(e('main-body'))
}

function listAllRecords() {
    if ('_controller' in window) {
        _controller({
            request: jsontostring({
                controller: 'credit-card',
                method: {
                    type: 'list-all',
                    data: {}
                }
            }),

            onSuccess: (res) => {
                load_to_table(jsonparse(res))
            },

            onFailure: (code, msg) => {
                style_show(e('error-modal'))

                e('main-body').innerText = msg
            }
        })
    }
}

form.onsubmit = function(evt) {
    evt.preventDefault()

    const formdata = new FormData(this)
    const data = {}

    for (const entry of formdata.entries()) {
        data[entry[0]] = entry[1]
    }

    const req = {
        controller: 'credit-card',
        method: null
    }

    if (data.type === 'card-holder') {
        req.method = {
            type: 'search-holder',
            data: {
                last:   data.last,
                first:  data.first,
                middle: data.middle
            }
        }
    } else if (data.type === 'card-no') {
        req.method = {
            type: 'search-no',
            data: {
                no: data.query
            }
        }
    } else if (data.type === 'card-name') {
        req.method = {
            type: 'search-name',
            data: {
                name: data.query
            }
        }
    }

    if ('_controller' in window) {
        _controller({
            request: jsontostring(req),

            onSuccess: (res) => {
                const cards = jsonparse(res)

                load_to_table(cards)
            },

            onFailure: (code, msg) => {
                style_show(e('error-modal'))

                e('error-text').innerText = msg
            }
        })
    }
}

type.onchange = function() {

    if (this.value === 'card-holder') {
        style_hide(query)
        style_show(holder_wrapper)
        style_apply(enable, holder_last, holder_first, holder_middle)
    } else {
        style_show(query)
        style_hide(holder_wrapper)
        style_apply(disable, holder_last, holder_first, holder_middle)
    }

    if (this.value === 'card-name') {
        query.placeholder = 'Card Name'
    } else if (this.value === 'card-no') {
        query.placeholder = 'Card Number'
    }

    query.value         = ''
    holder_last.value   = ''
    holder_first.value  = ''
    holder_middle.value = ''
}

function load_to_table(cards) {
    clearchildren(table)

    size.innerText = `Showing ${cards.length} result(s)`

    for (const card of cards) {
        const row = create('tr')
        const cell1 = create('td')
        const cell2 = create('td')
        const cell3 = create('td')
        const cell4 = create('td')
        const cell5 = create('td')

        row.classList.add('tr-hover')

        const { last, first, middle } = card.account_name

        cell1.innerText = card.card_no.match(/.{1,4}/g).join(' ')
        cell2.innerText = card.card_name
        cell3.innerText = `${last}, ${first} ${middle}`
        cell4.innerText = card.card_type
        cell5.innerText = card.exp_date

        row.appendChild(cell1)
        row.appendChild(cell2)
        row.appendChild(cell3)
        row.appendChild(cell4)
        row.appendChild(cell5)

        table.appendChild(row)

        row.onclick = () => {
            const param = new URLSearchParams({no: card.card_no})

            redirectWithParams('pages/p-credit-card.html', param.toString())
        }
    }

}

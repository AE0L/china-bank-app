const form             = e('search-form')
const size             = e('result-size')
const type             = e('type')
const query            = e('query')
const table            = e('tbody-result')
const customer_wrapper = e('holder-wrapper')
const customer_last    = e('holder-last')
const customer_first   = e('holder-first')
const customer_middle  = e('holder-middle')

function closeErrorModal() {
    style_hide(e('error-modal'))
    style_scroll(e('main-body'))
}

function listAllRecords() {
    if ('_controller' in window) {
        _controller({
            request: jsontostring({
                controller: 'account',
                method: {
                    type: 'list-all',
                }
            }),

            onSuccess: (res) => {
                load_to_table(jsonparse(res), false)
            },

            onFailure: (code, msg) => {
                style_show(e('error-modal'))

                e('error-text').innerText = msg
            }
        })
    }
}

form.onsubmit = function(evt) {
    evt.preventDefault()

    const formdata = new FormData(this)
    const data     = {}

    for (const entry of formdata.entries()) {
        data[entry[0]] = entry[1]
    }

    const req = {
        controller: 'account',
        method: null
    }

    if (data.type === 'account-no') {
        req.method = {
            type: 'search-account-no',
            data: {
                no: data.query
            }
        }
    } else if (data.type === 'account-holder') {
        req.method = {
            type: 'search-account-holder',
            data: {
                last:   data.last,
                first:  data.first,
                middle: data.middle
            }
        }
    }

    if ('_controller' in window) {
        _controller({
            request: jsontostring(req),

            onSuccess: (res) => {
                console.log(res)
                const accounts = jsonparse(res)

                load_to_table(accounts, true)
            },

            onFailure: (code, msg) => {
                style_show(e('error-modal'))

                e('error-text').innerText = msg
            }
        })
    }
}

type.onchange = function() {
    const { value } = this

    if (value === 'account-holder') {
        style_hide(query)
        style_show(customer_wrapper)
        style_apply(enable, customer_last, customer_first, customer_middle)
    } else {
        style_show(query)
        style_apply(style_hide, customer_wrapper)
        style_apply(disable, customer_last, customer_first, customer_middle)
    }

    query.value            = ''
    customer_last.value    = ''
    customer_first.value   = ''
    customer_middle.value  = ''
}

function load_to_table(accounts, wHolder) {
    clearchildren(table)

    if (!(accounts instanceof Array)) {
        accounts = [accounts]
    }

    size.innerText = `Showing ${accounts.length} result(s)`

    for (const account of accounts) {
        const row   = create('tr')
        const cell1 = create('td')
        const cell3 = create('td')
        const cell4 = create('td')
        const cell5 = create('td')

        let cell2

    
        row.classList.add('tr-hover')

        const { 
            account_no, account_holder, account_type , product_type, date_opened
        } = account

        cell1.innerText = account_no
        cell3.innerText = account_type
        cell4.innerText = product_type
        cell5.innerText = date_opened

        if (wHolder) {
            style_show(e('holder-header'))
            cell2 = create('td')
            cell2.innerText = account_holder

            appendchildren(row, cell1, cell2, cell3, cell4, cell5)
        } else {
            style_hide(e('holder-header'))

            appendchildren(row, cell1, cell3, cell4, cell5)
        }

        table.appendChild(row)

        row.onclick = () => {
            const param = new URLSearchParams({no: account_no})

            redirectWithParams('pages/p-account.html', param.toString())
        }
    }

}

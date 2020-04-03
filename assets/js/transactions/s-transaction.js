const form             = e('search-form')
const size             = e('result-size')
const type             = e('type')
const query            = e('query')
const table            = e('tbody-result')
const transaction_date = e('date-query')
const customer_wrapper = e('customer-wrapper')
const customer_last    = e('customer-last')
const customer_first   = e('customer-first')
const customer_middle  = e('customer-middle')

function closeErrorModal() {
    style_hide(e('error-modal'))
    style_scroll(e('main-body'))
}

function listAllRecords() {
    if ('_controller' in window) {
        _controller({
            request: jsontostring({
                controller: 'transaction',
                method: {
                    type: 'list-all',
                    data: { }
                }
            }),

            onSuccess: (res) => {
                load_to_table(jsonparse(res))
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
    const data = {}

    for (const entry of formdata.entries()) {
        data[entry[0]] = entry[1]
    }

    const req = {
        controller: 'transaction',
        method: null
    }

    if (data.type === 'transaction-id') {
        req.method = {
            type: 'search-transaction-id',
            data: {
                id: data.query
            }
        }
    } else if (data.type === 'account-no') {
        req.method = {
            type: 'search-account-no',
            data: {
                no: data.query
            }
        }
    } else if (data.type === 'customer-name') {
        req.method = {
            type: 'search-customer-name',
            data: {
                last:   data.last,
                first:  data.first,
                middle: data.middle
            }
        }
    } else if (data.type === 'transaction-date') {
        req.method = {
            type: 'search-transaction-date',
            data: {
                date: data.date
            }
        }
    }

    if ('_controller' in window) {
        _controller({
            request: jsontostring(req),

            onSuccess: (res) => {
                const transactions = jsonparse(res)

                load_to_table(transactions)
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

    if (value === 'customer-name') {
        style_hide(query)
        style_show(customer_wrapper)
        style_apply(enable, customer_last, customer_first, customer_middle)
        style_hide(transaction_date)
        disable(transaction_date)
    } else if (value === 'transaction-date') {
        style_show(transaction_date)
        enable(transaction_date)
        style_apply(style_hide, query, customer_wrapper)
        style_apply(disable, customer_last, customer_first, customer_middle)
    } else {
        style_show(query)
        style_apply(style_hide, customer_wrapper, transaction_date)
        style_apply(disable, customer_last, customer_first, customer_middle, transaction_date)
    }

    if (value === 'transaction-id') {
        query.placeholder = 'Transaction ID'
    } else if (value === 'account-no') {
        query.placeholder = 'Account Number'
    }

    query.value            = ''
    transaction_date.value = ''
    customer_last.value    = ''
    customer_first.value   = ''
    customer_middle.value  = ''
}

function load_to_table(transactions) {
    clearchildren(table)

    if (!(transactions instanceof Array)) {
        transactions = [transactions]
    }

    size.innerText = `Showing ${transactions.length} result(s)`

    for (const transaction of transactions) {
        const row = create('tr')
        const cell1 = create('td')
        const cell2 = create('td')
        const cell3 = create('td')
        const cell4 = create('td')
        const cell5 = create('td')
        const cell6 = create('td')

        row.classList.add('tr-hover')

        const { 
            transaction_id, transaction_type, total_amount , customer_name, account_no, transaction_date
        } = transaction

        const { last, first, middle } = customer_name

        cell1.innerText = transaction_id
        cell2.innerText = transaction_type
        cell3.innerText = total_amount
        cell4.innerText = `${last}, ${first} ${middle}`
        cell5.innerText = account_no
        cell6.innerText = transaction_date

        row.appendChild(cell1)
        row.appendChild(cell2)
        row.appendChild(cell3)
        row.appendChild(cell4)
        row.appendChild(cell5)
        row.appendChild(cell6)

        table.appendChild(row)

        row.onclick = () => {
            const param = new URLSearchParams({id: transaction_id})

            redirectWithParams('pages/p-transaction.html', param.toString())
        }
    }

}

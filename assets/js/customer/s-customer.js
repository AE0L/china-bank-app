const form  = e('search-form')
const type  = e('type')
const size  = e('result-size')
const table = e('tbody-result')

function listAllRecords() {
    if ('_controller' in window) {
        _controller({
            request: jsontostring({
                controller: 'customer',
                method: {
                    type: 'list-all'
                }
            }),

            onSuccess: (res) => {
                loadData(jsonparse(res))
            }
        })
    }
}

form.onsubmit = (evt) => {
    evt.preventDefault()

    const formdata = new FormData(form)
    const data     = {}

    for (const entry of formdata.entries()) {
        data[entry[0]] = entry[1]
    }

    const request = {
        controller: 'customer',
        method: null
    }

    if (data.type == 'id') {
        request.method = {
            type: 'search-id',
            data: {
                id: data.query
            }
        }
    } else {
        request.method = {
            type: 'search-name',
            data: {
                part: data.type,
                name: data.query
            }
        }
    }

    if ("_controller" in window) {
        _controller({
            request: jsontostring(request),

            onSuccess: (res) => {
                const customers = jsonparse(res)

                loadData(customers)
            },

            onFailure: (code, msg) => {
                style_noscroll(e('main-body'))
                style_show(e('error-modal'))
                e('error-text').innerText = msg
            }
        })
    }
}

function closeErrorModal() {
    style_scroll(e('main-body'))
    style_hide(e('error-modal'))
}

function loadData(customers) {
    clearchildren(table)

    size.innerText = `Showing ${customers.length} result(s)`

    for (const customer of customers) {
        const { id, name, gender, nation }  = customer

        const row       = create('tr')
        const cell1     = create('td')
        const cell2     = create('td')
        const cell3     = create('td')
        const cell4     = create('td')
        const appendrow = (e) => appendchild(row, e)

        row.classList.add('tr-hover')
        cell2.classList.add('td-name')

        cell1.innerText = id
        cell2.innerText = `${name.last}, ${name.first} ${name.middle}`
        cell3.innerText = gender
        cell4.innerText = nation

        appendrow(cell1)
        appendrow(cell2)
        appendrow(cell3)
        appendrow(cell4)

        appendchild(table, row)

        row.onclick = () => {
            const idParam = new URLSearchParams()

            idParam.append('id', id)

            redirectWithParams('pages/p-customer.html', idParam.toString())
        }
    }
}

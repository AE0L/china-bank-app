function otherCurrClicked() {
    const input   = e('other-curr-input')
    const radio   = e('other-curr-radio')
    radio.checked = true

    enable(input)

    input.oninput = () => {
        radio.value = input.value
        e('total-amount-curr').innerText = input.value
    }

    input.focus()
}

function givenCurrClicked(curr) {
    const input = e('other-curr-input')
    const radio = e('other-curr-radio')

    disable(input)
    radio.value = ''
    input.value = ''
    e('total-amount-curr').innerText = curr
}

function depWithEvents(payment=false) {
    const currs = document.getElementsByName('currency')

    currs.forEach(curr => {
        if (curr.checked) {
            const { value } = curr
            const table     = value === 'PHP' ? 'local' : 'foreign'

            changeCashBreakdownTable(table, payment)
        }
    })

    e('php-curr').onclick = () => {
        givenCurrClicked('PHP')
        changeCashBreakdownTable('local', payment)
    }

    e('usd-curr').onclick = () => {
        givenCurrClicked('USD')
        changeCashBreakdownTable('foreign', payment)
    }

    e('other-curr').onclick = () => {
        otherCurrClicked()
        changeCashBreakdownTable('foreign', payment)
    }
}

function changeCashBreakdownTable(curr, payment=false) {
    const table = payment === false ? e('cash-breakdown') : e('payment-table')
    const tbody  = table.tBodies[0]
    const header = create('tr')

    clearChildren(tbody)
    addClass(header, 'col-header')

    e('total-amount').innerText = '0.00'

    if (curr === 'local') {
        const cols   = ['Denomination', 'Pieces', 'Amount']
        const denoms = [
            { name: '1000', value: 1000 },
            { name: '500', value: 500 },
            { name: '200', value: 200 },
            { name: '100', value: 100 },
            { name: '50', value: 50 },
            { name: '20', value: 20 },
            { name: '10', value: 10 },
            { name: '5', value: 5 },
            { name: '1', value: 1 },
            { name: 'Centavos', value: 0.5 }
        ]

        cols.forEach((col, i) => {
            const th = create('th')

            th.innerText = col

            addClass(th, 'center')
            header.appendChild(th)
        })

        tbody.appendChild(header)

        denoms.forEach(denom => {
            const row    = create('tr')
            const dCell  = create('td')
            const pCell  = create('td')
            const aCell  = create('td')
            const pInput = create('input')

            pInput.type  = 'number'
            pInput.name  = `denom-${denom.name}`
            pInput.value = '0'
            pInput.min   = '0'

            addClass(pInput, 'form__input-number')
            addClass(pInput, 'pieces')

            row.onclick    = () => { pInput.select() }
            pInput.onclick = () => { pInput.select() }
            pInput.onfocus = () => addClass(row, 'row-highlight')
            pInput.onblur  = () => remClass(row, 'row-highlight')

            dCell.innerText = denom.name
            aCell.innerText = '0'

            pCell.appendChild(pInput)

            const cells = [dCell, pCell, aCell]

            cells.forEach(cell => {
                addClass(cell, 'center')
                row.appendChild(cell)
            })

            pInput.oninput = () => {
                pInput.value    = pInput.value < 0 ? 0 : pInput.value
                aCell.innerText = pInput.value * denom.value

                const result = array(tbody.children)
                                .map(r => parseFloat(r.lastChild.innerText))
                                .slice(1)
                                .reduce((a, b) => a + b).toFixed(2)

                e('total-amount').innerText = result
                e('input-total-amount').value = result
            }

            tbody.appendChild(row)
        })
    } else if (curr === 'foreign') {
        const cols = ['Serial Number', 'Denomination', ' ']

        cols.forEach(col => {
            const th = create('th')

            th.innerText = col

            header.appendChild(th)
        })

        tbody.appendChild(header)

        const updateTotalAmount = (evt, first=false) => {
            if (!first) {
                evt.target.value = evt.target.value < 1 ? 1 : evt.target.value
            }

            const amounts = array(table.tBodies[0].children)
                                .slice(1)
                                .map(r => float(r.lastChild.previousSibling.firstChild.value))
                                .reduce((a, b) => a + b).toFixed(2)

            e('total-amount').innerText = amounts
            e('input-total-amount').value = amounts
        }

        const addRow = (i) => {
            const row    = create('tr')
            const td1    = create('td')
            const td2    = create('td')
            const td3    = create('td')
            const serial = create('input')
            const denom  = create('input')
            const add    = create('button')
            const icon   = create('i')

            serial.type  = 'text'
            serial.name  = `serial-${i}`
            serial.required = true
            serial.placeholder = 'Enter serial number here...'
            denom.type   = 'number'
            denom.name   = `denom-${i}`
            denom.value  = 1
            add.type     = 'button'

            denom.classList.add('form__input-number')
            serial.classList.add('form__input-text')
            denom.classList.add('form__input-no-bottom')
            serial.classList.add('form__input-no-bottom')
            icon.classList.add('fas')
            icon.classList.add('fa-plus')
            add.classList.add('add-row')
            td1.classList.add('no-bottom')
            td2.classList.add('no-bottom')
            td3.classList.add('no-bottom')
            td3.classList.add('add-row-cell')

            add.appendChild(icon)
            td1.appendChild(serial)
            td2.appendChild(denom)
            td3.appendChild(add)
            row.appendChild(td1)
            row.appendChild(td2)
            row.appendChild(td3)
            tbody.appendChild(row)

            denom.onfocus  = () => { denom.select() }
            denom.oninput  = updateTotalAmount
            serial.onfocus = () => addClass(row, 'row-highlight')
            serial.onblur  = () => remClass(row, 'row-highlight')
            denom.onfocus  = () => addClass(row, 'row-highlight')
            denom.onblur   = () => remClass(row, 'row-highlight')

            updateTotalAmount(null, first=true)

            add.onclick = () => {
                addRow(i + 1)
                repClass(icon, 'fa-plus', 'fa-minus')

                add.onclick = () => {
                    tbody.removeChild(add.parentNode.parentNode)
                    updateTotalAmount(null, first=true)
                }
            }
        }

        addRow(1)
    }
}

function setupcashforms(cashform) {
  const form          = e(cashform)
  const body          = e('main-body')
  const modal         = e('modal')
  const ver_cancel    = e('ver-cancel')
  const ver_submit    = e('ver-submit')
  const ver_tablehead = e('ver-table-header')
  const ver_tablebody = e('ver-table-body')

  form.onsubmit = function(evt) {
    evt.preventDefault()

    clearchildren(ver_tablehead)
    clearchildren(ver_tablebody)
    style_noscroll(body)
    style_show(modal)

    const formdata    = new FormData(this)
    const transaction = new CashTransaction(formdata);

    const { account_no, currency, total_amount } = transaction.data

    if (total_amount <= 0) {
        style_hide(modal)
        style_show(e('error-modal'))
        
        e('error-text').innerText = "Invalid amount, please check transaction details."
    }

    const vers = [
      ['ver-account-no',   account_no],
      ['ver-currency',     currency],
      ['ver-total-amount', `${currency} ${total_amount}`]
    ]

    for (const ver of vers) {
      e(ver[0]).innerText = ver[1]
    }

    const headrow = create('tr')

    headrow.classList.add('col-header')

    if (transaction.islocal()) {
      const cell1 = create('th')
      const cell2 = create('th')
      const cell3 = create('th')

      cell1.innerText = 'Denomination'
      cell2.innerText = 'Pieces'
      cell3.innerText = 'Amount'

      appendchildren(headrow, cell1, cell2, cell3)

      const { cash_breakdown } = transaction.data

      for (const cash of cash_breakdown) {
        const row = create('tr')
        const td1 = create('td')
        const td2 = create('td')
        const td3 = create('td')

        const { denom, piece } = cash

        td1.innerText = denom
        td2.innerText = piece
        td3.innerText = denom * piece

        style_apply(style_center, td1, td2, td3)
        appendchildren(row, td1, td2, td3)
        appendchild(ver_tablebody, row)
      }
    } else {
      const cell1 = create('th')
      const cell2 = create('th')

      cell1.innerText = 'Serial Number'
      cell2.innerText = 'Denomination'

      appendchildren(headrow, cell1, cell2)

      const { cash_breakdown } = transaction.data

      for (const cash of cash_breakdown) {
        const row = create('tr')
        const td1 = create('td')
        const td2 = create('td')

        const { serial, denom } = cash

        td1.innerText = serial
        td2.innerText = denom

        style_apply(style_center, td1, td2)
        appendchildren(row, td1, td2)
        appendchild(ver_tablebody, row)
      }
    }

    appendchild(ver_tablehead, headrow)

    ver_submit.onclick = () => {
        if ('_controller' in window) {
            const req = {
                controller: 'transaction',
                method: {
                    type: 'save',
                    data: {
                        transactionType: 'cash',
                        transaction: transaction.data
                    }
                }
            }

            _controller({
                request: jsontostring(req),

                onSuccess: (id) => {
                    const param = new URLSearchParams({id: id})

                    redirectWithParams('pages/p-transaction.html', param.toString())
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
    style_scroll(body)
    style_hide(modal)
  }
}

function createPaymentForm() {
    return `
      <form id=withdraw-form>
        <div class=form__content-wrapper>
          <h3 class=form__section-title>Transaction Details</h3>
          
          <label class=form__label>Company / Institution Name</label>
          <input class=form__input-text type=text name=company-name required>
          <label class="form__label">Customer Name</label>
          <div class="col-3 col-05-gap">
            <div class=text-underlabel>
              <input type=text class=form__input-text name=customer-name-last required>
              <label class=form__label-under>Last</label>
            </div>
            <div class=text-underlabel>
              <input type=text class=form__input-text name=customer-name-first required>
              <label class=form__label-under>First</label>
            </div>
            <div class=text-underlabel>
              <input type=text class=form__input-text name=customer-name-middle required>
              <label class=form__label-under>Middle</label>
            </div>
          </div>
          <label class=form__label>Reference Number</label>
          <div class=text-underlabel>
            <input class=form__input-text type=text name=reference-no required>
            <label class=form__label-under>Subscriber / Policy / Credit Card / Promissory Note Number</label>
          </div>

          <label class=form__label>Currency</label>
          <div class=form__radio-group>
            <label id=php-curr>
              <input id=curr-php class=form__input-radio checked name=currency type=radio value=PHP>
              <label class=form__label-radio for=curr-php>PHP</label>
            </label>

            <label id=usd-curr>
              <input id=curr-usd class=form__input-radio name=currency type=radio value=USD>
              <label class=form__label-radio for=curr-usd>USD</label>
            </label>

            <label id=other-curr>
              <input class=form__input-radio id=other-curr-radio name=currency type=radio>
              <input class="form__input-text form__label-radio" size=10 id=other-curr-input placeholder="Other" type=text disabled required>
            </label>
          </div>

          <label class=form__label>Mode of Payment</label>
          <div class=form__radio-group>
            <label id=mode-cash>
              <input id=cash-mode class=form__input-radio checked name=payment-mode type=radio value=cash>
              <label class=form__label-radio for=cash-mode>Cash</label>
            </label>

            <label id=mode-check>
              <input id=check-mode class=form__input-radio name=payment-mode type=radio value=check>
              <label class=form__label-radio for=check-mode>Check</label>
            </label>

            <label id=mode-debit>
              <input id=debit-mode class=form__input-radio name=payment-mode type=radio value=debit>
              <label class=form__label-radio for=debit-mode>Debit Account</label>
            </label>
          </div>
        
          <h3 class=form__section-title>Payment Details</h3>

          <label class=form__label id=debit-label>Debit Account Number</label>
          <input class=form__input-text id=debit-input name=debit-no type=text>

          <label class=form__label id=debit-amount-label>Amount</label>
          <input class="form__input-number" id=debit-amount-input type=number value="0">

          <input type=hidden name=total-amount id=input-total-amount value=0>

          <table class=form__table id=payment-table>
            <tbody></tbody>
          </table>
        </div>
        
        <div class="total-amount">
          <label>Total Amount:</label>
          <label id=total-amount-curr>PHP</label> <label id=total-amount>0.00</label>
          <button class="form__button form__button-primary" type=submit>Save Transaction</button>
        </div>
      </form>

      <div id=modal class="modal hidden custom-scrollbar">
        <div class=modal__content-verify>
          <h2 class=form__section-title>Transaction Confirmation</h2>
          <p class=modal__verify-text>Please confirm the following details.</p>

          <label class=form__label>Company / Institution Name</label>
          <label id=ver-company></label>

          <label class=form__label>Customer Name</label>
          <label id=ver-customer></label>

          <label class=form__label>Reference Number</label>
          <label id=ver-reference></label>

          <label class=form__label>Currency</label>
          <label id=ver-currency></label>

          <label class=form__label>Payment Mode</label>
          <label id=ver-mode></label>

          <h3 class=form__section-title>Payment Details</h3>
          <table id=ver-detail-table class=form__table>
            <tbody id=ver-table-head></tbody>
            <tbody id=ver-table-body></tbody>
          </table>

          <label id=ver-debit-label class="form__label hidden">Debit Account Number</label>
          <label id=ver-debit class=hidden></label>

          <div></div>

          <div class=modal__verify-amount>
            <h3>Total Amount</h3>
            <label id=ver-total-amount></label>
          </div>
          
          <div class=modal__button-wrapper>
            <button  id=ver-cancel class=modal__button>Cancel</button>
            <button  id=ver-submit class=modal__button>Submit</button>
          </div>
        </div>
      </div>
  `
}

function paymentEvents() {
  const paymentModes = array(document.getElementsByName('payment-mode'))

  paymentModes.forEach(mode => {
    mode.onchange = () => updatePaymentForm(mode.value)
  })

  depWithEvents(true)
  setuppaymentmodal()
}

function updatePaymentForm(mode) {
  switch (mode) {
    case 'cash':
      hideDebitAccountInput()
      depWithEvents(true)
      break
    case 'check':
      hideDebitAccountInput()
      checkEvents(true)
      paymentCurrEvents()
      break
    case 'debit':
      paymentCurrEvents()
      showDebitAccountInput()
      break
  }
}

function showDebitAccountInput() {
  e('payment-table').style.display      = 'none'
  e('debit-label').style.display        = 'unset'
  e('debit-input').style.display        = 'unset'
  e('debit-amount-label').style.display = 'unset'
  e('debit-amount-input').style.display = 'unset'

  clearchildren(e('payment-table').tBodies[0])

  e('total-amount').innerText = '0.00'

  e('debit-amount-input').onfocus = function() {
    this.select()
  }

  e('debit-amount-input').disabled = false

  e('debit-amount-input').oninput = function() {
    if (this.value && this.value < 0) {
      this.value = 0
      e('total-amount').innerText = '0.00'
    } else if (!this.value) {
      e('total-amount').innerText = '0.00'
    } else {
      e('total-amount').innerText = float(this.value).toFixed(2)
    }

    e('input-total-amount').value = float(e('total-amount').innerText)
  }

  e('debit-amount-input').onchange = function() { this.value = !this.value ? 0 : this.value }
}

function hideDebitAccountInput() {
  e('payment-table').style.display      = 'table'
  e('debit-label').style.display        = 'none'
  e('debit-input').style.display        = 'none'
  e('debit-amount-label').style.display = 'none'
  e('debit-amount-input').style.display = 'none'
  e('debit-amount-input').disabled      = true
}

function paymentCurrEvents() {
  e('php-curr').onclick   = () => givenCurrClicked('PHP')
  e('usd-curr').onclick   = () => givenCurrClicked('USD')
  e('other-curr').onclick = otherCurrClicked
}

function setuppaymentmodal() {
  const form       = e('withdraw-form')
  const modal      = e('modal')
  const body       = e('main-body')
  const ver_cancel = e('ver-cancel')
  const ver_submit = e('ver-submit')
  const table_head = e('ver-table-head')
  const table_body = e('ver-table-body')
  const debitlabel = e('ver-debit-label')
  const debitno    = e('ver-debit')

  form.onsubmit = function(evt) {
    evt.preventDefault()

    style_noscroll(body)
    style_show(modal)
    style_hide(debitlabel)
    style_hide(debitno)
    clearchildren(table_head)
    clearchildren(table_body)

    const formdata = new FormData(this)
    const transaction = new PaymentTransaction(formdata)

    const { business_name, customer_name, reference_no, currency, payment_mode, total_amount } = transaction.data

    if (total_amount <= 0) {
        style_hide(modal)
        style_show(e('error-modal'))
        
        e('error-text').innerText = "Invalid amount, please check transaction details."
    }

    const vers = [
      ['ver-company',      string_capall(business_name)],
      ['ver-customer',     string_capall(`${customer_name.last}, ${customer_name.first} ${customer_name.middle}`)],
      ['ver-reference',    reference_no],
      ['ver-currency',     currency],
      ['ver-mode',         string_cap(payment_mode)],
      ['ver-total-amount', `${currency} ${float(total_amount).toFixed(2)}`]
    ]

    for (const v of vers) {
      e(v[0]).innerText = v[1]
    }

    switch (payment_mode) {
      case 'cash': {
        const headrow = create('tr')

        headrow.classList.add('col-header')

        if (transaction.islocal()) {
          const th1 = create('th')
          const th2 = create('th')
          const th3 = create('th')

          th1.innerText = 'Denomination'
          th2.innerText = 'Pieces'
          th3.innerText = 'Amount'
          
          headrow.classList.add('form__table_header')
          th1.classList.add('form__table_th')
          th2.classList.add('form__table_th')
          th3.classList.add('form__table_th')

          appendchildren(headrow, th1, th2, th3)

          const { cash_breakdown } = transaction.data

          for (const cash of cash_breakdown) {
            const row = create('tr')
            const td1 = create('td')
            const td2 = create('td')
            const td3 = create('td')

            const { denom, piece } = cash

            td1.innerText = denom
            td2.innerText = piece
            td3.innerText = parseFloat(denom) * piece

            style_apply(style_center, td1, td2, td3)
            appendchildren(row, td1, td2, td3)
            appendchild(table_body, row)
          }
        } else {
          const th1 = create('th')
          const th2 = create('th')

          th1.innerText = 'Serial Number'
          th2.innerText = 'Denomination'

          headrow.classList.add('form__table_header')
          th1.classList.add('form__table_th')
          th2.classList.add('form__table_th')

          appendchildren(headrow, th1, th2)

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
            appendchild(table_body, row)
          }
        }

        appendchild(table_head, headrow)
        break
      }

      case 'check': {
        const headrow = create('tr')
        const th1   = create('th')
        const th2   = create('th')
        const th3   = create('th')

        th1.innerText = 'Bank/Branch'
        th2.innerText = 'Check Number'
        th3.innerText = 'Amount'

        headrow.classList.add('form__table_header')
        th1.classList.add('form__table_th')
        th2.classList.add('form__table_th')
        th3.classList.add('form__table_th')

        appendchildren(headrow, th1, th2, th3)
        appendchild(table_head, headrow)

        const { check_details } = transaction.data

        for (const check of check_details) {
          const row   = create('tr')
          const td1 = create('td')
          const td2 = create('td')
          const td3 = create('td')

          const { branch, no, amount } = check

          td1.innerText = branch
          td2.innerText = no
          td3.innerText = amount

          style_apply(style_center, td1, td2, td3)
          appendchildren(row, td1, td2, td3)
          appendchild(table_body, row)
        }

        break
      }

      case 'debit': {
        style_show(debitlabel)
        style_show(debitno)

        debitno.innerText = transaction.data.debit_no
      }
    }

    ver_submit.onclick = () => {
      if ('_controller' in window) {
        const req = {
          controller: 'transaction',
          method: {
            type: 'save',
            data: {
              transactionType: 'payment',
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

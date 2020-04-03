function createCheckForm() {
    return `
      <form class=form id=check-deposit-form>
        <div class=form__content-wrapper>
          <h3 class=form__section-title>Transaction Details</h3>
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
          <label class=form__label>Account Number</label>
          <input class="form__input-text monospace" type=text name=account-no required>
          <label class=form__label>Type of Check Deposit</label>
          <div class=form__radio-group>
            <div>
              <input id=on-us class=form__input-radio checked name=check-type type=radio value="On-Us Check (CBC)">
              <label class=form__label-radio for=on-us>On-Us Check (CBC)</label>
            </div>
            <div>
              <input id=local-check class=form__input-radio name=check-type type=radio value="Local Check">
              <label class=form__label-radio for=local-check>Local Check</label>
            </div>
            <div>
              <input id=regional-check class=form__input-radio name=check-type type=radio value="Regional Check">
              <label class=form__label-radio for=regional-check>Regional Check</label>
            </div>
          </div>
          <h3 class=form__section-title>Check Details</h3>
          <table class=form__table id=check-details><tbody></tbody></table>
          <input type=hidden id=input-total-amount name=total-amount value="0.00">
          <div class="total-amount">
            <label>Total Amount:</label>
            <label id=total-amount-curr>PHP</label> <label id=total-amount>0.00</label>
            <button class="form__button form__button-primary" type=submit>Save Transaction</button>
          </div>
        </div>
      </form>
      <div id=modal class="custom-scrollbar modal hidden">
        <div class=modal__content-verify>
          <h2 class=form__section-title>Transaction Confirmation</h2>
          <p class=modal__verify-text>Please conform the following details.</p>
          <label class=form__label>Transaction Type</label>
          <label>Peso Check Deposit</label>
          <label class=form__label>Account Number</label>
          <label id=ver-account-no></label>
          <label class=form__label>Check Type</label>
          <label id=ver-check-type></label>
          <h3 class=form__section-title>Check Details</h3>
          <table class=form__table>
            <tbody>
              <tr class=col-header>
                <th>Bank / Branch</th>
                <th>Check Number</th>
                <th>Amount</th>
              </tr>
            </tbody>
            <tbody id=ver-table-body></tbody>
          </table>
          <h3 class=form__section-title>Total Amount</h3>
          <div><!-- SPACE --></div>
          <label id=ver-total-amount></label>
          <div class=modal__button-wrapper>
            <button  id=ver-cancel class=modal__button>Cancel</button>
            <button  id=ver-submit class=modal__button>Submit</button>
          </div>
        </div>
      </div>
  `
}

function checkEvents(payment=false) {
  const table  = !payment ? e('check-details') : e('payment-table')
  const tbody  = table.tBodies[0]
  const header = create('tr')
  const cols   = ['Bank / Branch', 'Check Number', 'Amount', ' ']

  clearChildren(tbody)

  header.classList.add('form__table_header')

  cols.forEach(col => {
      const th = create('th')

      th.classList.add('form__table_th')

      th.innerText = col

      header.appendChild(th)
  })

  tbody.appendChild(header)

  const updateTotalAmount = (evt, first=false) => {
      if (!first) {
          evt.target.value = evt.target.value < 1 || evt.target.value === '' ? 1 : evt.target.value
      }

      const totalamount = array(table.tBodies[0].children)
          .slice(1)
          .map(r => float(r.lastChild.previousSibling.firstChild.value))
          .reduce((a, b) => a + b)

      e('total-amount').innerText   = float(totalamount).toFixed(2)
      e('input-total-amount').value = totalamount
  }

  const addRow = (i) => {
      const row    = create('tr')
      const td1    = create('td')
      const td2    = create('td')
      const td3    = create('td')
      const td4    = create('td')
      const branch = create('input')
      const check  = create('input')
      const amount = create('input')
      const add    = create('button')
      const icon   = create('i')

      branch.type  = 'text'
      check.type   = 'text'
      amount.type  = 'number'

      branch.required = true
      check.required  = true

      branch.classList.add('form__input-text', 'form__input-no-bottom')
      check.classList.add('form__input-text', 'form__input-no-bottom')
      amount.classList.add('form__input-number', 'form__input-no-bottom')

      branch.placeholder = 'Enter bank / branch name here...'
      check.placeholder  = 'Enter check number here...'
      branch.name        = `check-branch-${i}`
      check.name         = `check-no-${i}`
      amount.name        = `check-amount-${i}`
      amount.size        = 3
      amount.value       = 1
      add.type           = 'button'

      branch.classList.add('check-branch')
      check.classList.add('check-no')
      amount.classList.add('check-amount')
      icon.classList.add('fas')
      icon.classList.add('fa-plus')
      add.classList.add('add-row')
      td1.classList.add('no-bottom')
      td2.classList.add('no-bottom')
      td3.classList.add('no-bottom')
      td4.classList.add('no-bottom')
      td4.classList.add('add-row-cell')

      add.appendChild(icon)
      td1.appendChild(branch)
      td2.appendChild(check)
      td3.appendChild(amount)
      td4.appendChild(add)
      row.appendChild(td1)
      row.appendChild(td2)
      row.appendChild(td3)
      row.appendChild(td4)
      tbody.appendChild(row)

      amount.oninput = updateTotalAmount
      branch.onfocus = () => addClass(row, 'row-highlight')
      branch.onblur  = () => remClass(row, 'row-highlight')
      check.onfocus  = () => addClass(row, 'row-highlight')
      check.onblur   = () => remClass(row, 'row-highlight')
      amount.onblur  = () => remClass(row, 'row-highlight')
      amount.onfocus = () => {
        amount.select()
        addClass(row, 'row-highlight')
      }

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

function setupcheckdeposit() {
  const form       = e('check-deposit-form')
  const body       = e('main-body')
  const modal      = e('modal')
  const ver_cancel = e('ver-cancel')
  const ver_submit = e('ver-submit')
  const table_body = e('ver-table-body')

  form.onsubmit = function(evt) {
    evt.preventDefault()

    style_noscroll(body)
    style_show(modal)

    const formdata    = new FormData(this)
    const transaction = new CheckDepositTransaction(formdata)

    clearchildren(table_body)

    const { account_no, check_type, total_amount } = transaction.data

    if (total_amount <= 0) {
        style_hide(modal)
        style_show(e('error-modal'))
        
        e('error-text').innerText = "Invalid amount, please check transaction details."
    }

    const vers = [
      ['ver-account-no',   account_no],
      ['ver-check-type',   check_type],
      ['ver-total-amount', `PHP ${float(total_amount).toFixed(2)}`]
    ]

    for (const v of vers) {
      e(v[0]).innerText = v[1]
    }

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

    ver_submit.onclick = () => {
      if ('_controller' in window) {
        const req = {
          controller: 'transaction',
          method: {
            type: 'save',
            data: {
              transactionType: 'check',
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

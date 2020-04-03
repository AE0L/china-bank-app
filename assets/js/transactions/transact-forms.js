const deposit    = e('deposit')
const withdraw   = e('withdraw')
const check_dep  = e('check-dep')
const payment    = e('payment')
const formTitle  = e('form-title')
const formInsert = e('form-insert')
const navs       = [deposit, withdraw, check_dep, payment]

deposit.onclick  = function() { onNavClick('Cash Deposit', this) }
withdraw.onclick = function() { onNavClick('Withdrawal', this) }
check_dep.onclick    = function() { onNavClick('Peso Check Deposit', this) }
payment.onclick  = function() { onNavClick('Collections / Bills/ Load Payment', this) }

function onNavClick(title, nav) {
  formTitle.innerText = title
  changeForm(nav)
  document.documentElement.scrollTo({ top: 0, behavior: 'smooth' })
}

deposit.click()

function changeForm(form) {
  navs.forEach(nav => {
    if (nav === form) {
      if (!nav.classList.contains('selected')) {
        nav.classList.add('selected')
      }
    } else {
      nav.classList.remove('selected')
    }
  })

  clearChildren(formInsert)

  displayForm(form.id)
}

function displayForm(id) {
  switch (id) {
    case 'deposit':
      formInsert.innerHTML = createDepositForm()
      depWithEvents()
      setupcashforms('deposit-form')
      break

    case 'withdraw':
      formInsert.innerHTML = createWithdrawForm()
      depWithEvents()
      setupcashforms('withdraw-form')
      break

    case 'check-dep':
      formInsert.innerHTML = createCheckForm()
      checkEvents()
      setupcheckdeposit()
      break

    case 'payment':
      formInsert.innerHTML = createPaymentForm()
      paymentEvents()
      break
  }
}

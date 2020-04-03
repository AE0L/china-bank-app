window.onload = () => {
    const id = new URLSearchParams(location.search).get('id')

    if ('_controller' in window) {
        _controller({
            request: jsontostring({
                controller: 'transaction',
                method: {
                    type: 'preview',
                    data: {
                        id: id
                    }
                }
            }),

            onSuccess: (res) => {
                console.log(res)
                const transaction = jsonparse(res)

                load_data(transaction)
            }
        })
    }
}

function load_data(t) {
    const t_id       = e('t-id')
    const t_date     = e('t-date')
    const t_account  = e('t-account')
    const t_customer = e('t-customer')
    const t_type     = e('t-type')
    const t_dir = e('t-dir')
    const t_amount = e('t-amount')

    t_id.innerText       = t.id
    t_date.innerText     = t.date
    t_account.innerText  = t.account
    t_customer.innerText = `${t.customer.last}, ${t.customer.first} ${t.customer.middle}`
    t_amount.innerText   = `${t.currency} ${t.amount}`

    switch (t.type) {
        case 'deposit':  t_type.innerText = 'Deposit';       break;
        case 'withdraw': t_type.innerText = 'Withdraw';      break;
        case 'check':    t_type.innerText = 'Check Deposit'; break;
        case 'payment':  t_type.innerText = 'Payment';       break;
    }

    t_dir.classList.add(['deposit', 'check', 'payment'].includes(t.type) ? 'fa-long-arrow-alt-left' : 'fa-long-arrow-alt-right')

    if (t.type === 'deposit' || t.type === 'withdraw') {
        if (t.currency === 'PHP') {
            local_cash_breakdown(t)
        } else {
            foreign_cash_breakdown(t)
        }
    } else if (t.type === 'check') {
        check_details(t)
    } else if (t.type === 'payment') {
        const wrap   = e('payment')
        const b_name = e('business-name')
        const ref    = e('reference-no')
        const mode   = e('p-mode')

        style_show(wrap)

        b_name.innerText = t.payment.business
        ref.innerText = t.payment.reference
        mode.innerText = t.payment.mode

        if (t.payment.mode === 'cash') {
            { (t.currency === 'PHP' ? local_cash_breakdown : foreign_cash_breakdown)(t) }
        } else if (t.payment.mode === 'check') {
            check_details(t)
        } else if (t.payment.mode === 'debit') {
            const wrap = e('debit')
            const a_no = e('account-no')
            const a_product = e('product-type')
            const a_type = e('account-type')

            style_show(wrap)

            a_no.innerText      = t.debit.no
            a_product.innerText = t.debit.product
            a_type.innerText    = t.debit.type

            wrap.onclick = () => {
                const param = new URLSearchParams({no: t.debit.no})

                redirectWithParams('pages/p-account.html', param.toString())
            }
        }
    }
}

function local_cash_breakdown(t) {
    const wrap = e('local-cash')
    const table = e('local-cash-table')

    style_show(wrap)

    for (const cash of t.cashBreakdown) {
        const tr = create('tr')
        const td1 = create('td')
        const td2 = create('td')
        const td3 = create('td')

        appendchildren(tr, td1, td2, td3)

        tr.classList.add('tr-hover')

        td1.innerText = cash.denom
        td2.innerText = cash.pieces
        td3.innerText = cash.denom * cash.pieces

        appendchild(table, tr)
    }
}

function foreign_cash_breakdown(t) {
    const wrap = e('foreign-cash')
    const table = e('foreign-cash-table')

    style_show(wrap)

    for (const cash of t.cashBreakdown) {
        const tr = create('tr')
        const td1 = create('td')
        const td2 = create('td')

        appendchildren(tr, td1, td2)

        tr.classList.add('tr-hover')

        td1.innerText = cash.serial
        td2.innerText = cash.denom

        appendchild(table, tr)
    }
}

function check_details(t) {
    const wrap  = e('check-details')
    const table = e('check-details-table')

    style_show(wrap)

    for (const check of t.checkDetails) {
        const tr  = create('tr')
        const td1 = create('td')
        const td2 = create('td')
        const td3 = create('td')

        appendchildren(tr, td1, td2, td3)

        tr.classList.add('tr-hover')

        td1.innerText = check.bank
        td2.innerText = check.no
        td3.innerText = check.amount

        appendchild(table, tr)
    }
}
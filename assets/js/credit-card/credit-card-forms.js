const form              = e('credit-card-form')
const soa_accept        = e('soa-accept')
const soa_reject        = e('soa-reject')
const deliver_label     = e('deliver-label')
const deliver_value     = e('deliver-value')
const main_body         = e('main-body')
const modal             = e('modal')
const ver_cancel        = e('ver-cancel')
const ver_submit        = e('ver-submit')
const ver_deliver_label = e('ver-card-deliver-label')
const ver_deliver_value = e('ver-card-deliver')

soa_reject.onchange = function() {
    style_show(deliver_label)
    style_show(deliver_value)
}

soa_accept.onchange = function() {
    style_hide(deliver_label)
    style_hide(deliver_value)
}

form.onsubmit = function(evt) {
    evt.preventDefault()

    const formdata   = new FormData(this)
    const creditcard = new CreditCard(formdata)

    style_noscroll(main_body)
    style_show(modal)

    const verids = [
        ['ver-name',      creditcard.accname],
        ['ver-card-type', creditcard.type],
        ['ver-card-name', creditcard.name],
        ['ver-card-soa',  creditcard.soa],
    ]

    if (creditcard.soa === 'Delivery') {
        verids.push(['ver-card-deliver', creditcard.deliveryaddress])
        style_show(ver_deliver_label)
        style_show(ver_deliver_value)
    } else {
        style_hide(ver_deliver_label)
        style_hide(ver_deliver_value)
    }

    for (const id of verids) {
        e(id[0]).innerText = id[1]
    }

    ver_submit.onclick = () => {
        const request = {
            controller: 'credit-card',
            method: {
                type: 'save',
                data: creditcard.data
            }
        }

        if ('_controller' in window) {
            _controller({
                request: jsontostring(request),

                onSuccess: (no) => {
                    const param = new URLSearchParams({no: no})

                    redirectWithParams('pages/p-credit-card.html', param.toString())
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
    style_scroll(main_body)
    style_hide(modal)
}

function closeErrorModal() {
    style_hide(e('error-modal'))
    style_scroll(main_body)
}

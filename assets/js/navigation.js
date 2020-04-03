const SUBMENU_MAX_WIDTH = '8em'

const nav_btns       = array(class_name('nav-btn'))
const submenu_nav    = e('submenu-nav')
const submenu_cont   = e('submenu-cont')
const submenu_space  = e('submenu-space-1')
const logo_cont      = e('logo-cont')
const set_width      = (e, v) => value('width', e, v)
const set_height     = (e, v) => value('height', e, v)
const set_submenu    = (v) => value('innerHTML', submenu_cont, v)
const open_submenu   = () => animation(() => set_width(submenu_nav.style, SUBMENU_MAX_WIDTH))
const close_submenu  = () => animation(() => set_width(submenu_nav.style, '0em'))
const adjust_top_pad = (p) => animation(() => set_height(submenu_space.style, `${p}px`))

const submenus = {
  'nav-cstm': `
    <div class=nav-btn-title>Customer</div>
        <div class=submenu-item-cont>
            <a draggable=false onclick="redirect('pages/customer.html')" class=nav-submenu-item>Create</a>
            <a draggable=false onclick="redirect('pages/s-customer.html')" class=nav-submenu-item>Search</a>
        </div>
    </div>
  `,

  'nav-trns': `
    <div class=nav-btn-title>Transactions</div>
      <div class=submenu-item-cont>
        <a draggable=false class=nav-submenu-item onclick="redirect('pages/transaction.html')">New</a>
        <a draggable=false class=nav-submenu-item onclick="redirect('pages/s-transaction.html')">History</a>
      </div>
    </div>
  `,

  'nav-acct': `
    <div class=nav-btn-title>Accounts</div>
        <div class=submenu-item-cont>
            <a draggable=false class=nav-submenu-item onclick="redirect('pages/accounts.html')">Open</a>
            <a draggable=false class=nav-submenu-item onclick="redirect('pages/s-accounts.html')">Search</a>
        </div>
    </div>
  `,

  'nav-crdt': `
    <div class=nav-btn-title>Credit Cards</div>
        <div class=submenu-item-cont>
            <a draggable=false class=nav-submenu-item onclick="redirect('pages/credit-card.html')">Apply</a>
            <a draggable=false class=nav-submenu-item onclick="redirect('pages/s-credit-card.html')">Search</a>
        </div>
    </div>
  `
}

for (const btn of nav_btns) {
  const id = btn.id

  btn.onmouseenter = () => {
    adjust_top_pad(e(id).offsetTop)
    set_submenu(prop(id, submenus))
    open_submenu()
  }

  btn.onmouseleave = () => close_submenu()
}

submenu_cont.onmouseenter = () => open_submenu()
submenu_cont.onmouseleave = () => close_submenu()


// NAVIGATION METHODS
function redirect(page) {
    const request = {
        method: 'redirect',
        page: page
    }

    if ("_redirect" in window) {
        window._redirect({
            request: jsontostring(request),

            onSuccess: (res) => {
                window.location = res;
            },

            onFailure: (err_code, err_msg) => {
                console.log(`[${err_code}: ${err_msg}`)
            }
        })
    } else {
        console.log('_redirect not found')
    }
}

function redirectWithParams(page, params) {
  const req = {
    method: 'params',
    page: page,
    params: params
  }

  if ('_redirect' in window) {
    _redirect({
      request: jsontostring(req),

      onSuccess: (res) => {
        window.location = res;
      },

      onFailure: (code, msg) => {
        console.log('redirect error')
        console.log(`[${code}]: ${msg}`)
      }
    })
  }
}

function historyBack() {
  window.history.back()
}

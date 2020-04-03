const e              = (e) => document.getElementById(e)
const create         = (t) => document.createElement(t)
const class_name     = (c) => document.getElementsByClassName(c)
const names          = (n, i=null) => i === null ? document.getElementsByName(n) : document.getElementsByName(n)[i]
const array          = (o) => Array.from(o)
const freeze         = (o) => Object.freeze(o)
const prop           = (p, e) => e[p]
const value          = (p, e, v) => { e[p] = v }
const enable         = (e) => value('disabled', e, false)
const disable        = (e) => value('disabled', e, true)
const focus          = (e) => e.focus()
const animation      = (f) => requestAnimationFrame(f)
const clearchildren  = (e) => { while (e.lastChild) { e.removeChild(e.lastChild) } }
const appendchild    = (e, a) => e.appendChild(a)
const appendchildren = (e, ...c) => [...c].forEach(a => e.appendChild(a))
const jsonparse      = (s) => JSON.parse(s)
const jsontostring   = (o) => JSON.stringify(o)

// STYLE
const style_apply    = (s, ...e) => [...e].forEach(s)
const style_scroll   = (e) => e.classList.remove('noscroll')
const style_noscroll = (e) => e.classList.add('noscroll')
const style_hide     = (e) => e.classList.add('hidden')
const style_show     = (e) => e.classList.remove('hidden')
const style_center   = (e) => e.classList.add('center')

// NUMBERS
const float          = parseFloat
const int            = parseInt

// STRING
const string_capall  = (s) => s.split(' ').map(c => c[0].toUpperCase() + c.slice(1)).join(' ')
const string_cap     = (s) => s.split('')[0].toUpperCase() + s.slice(1)
const string_allcap  = (s) => s.split('').map(c => c.toUpperCase()).join('')

// INPUT
const check          = (i) => { i.checked = true }
const clear_value    = (i) => { i.value = '' }

// OBJECTS
const entries        = (o) => Object.entries(o)
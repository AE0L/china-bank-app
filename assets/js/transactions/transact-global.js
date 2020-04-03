const addClass = (e, c) => e.classList.add(c)
const remClass = (e, c) => e.classList.remove(c)
const repClass = (e, o, n) => e.classList.replace(o, n)
const cmpStyle = (e, s) => window.getComputedStyle(e)[s]

// GLOBAL ELEMENTS
const pageTitle = e('page-title')
const sideNav   = e('forms-side-nav')

const clearChildren = (el) => array(el.children).forEach(e => el.removeChild(e))

const cache = {
    PAGE_TITLE_SIZE: pageTitle.offsetHeight + (float(cmpStyle(pageTitle, 'margin-top')) * 2),
}

// FORMS-SIDE-NAV Grow
const observer = new IntersectionObserver(function(entries) {
    if (entries[0].isIntersecting === false) {
        sideNav.style.transform = `translate(0, -${cache.PAGE_TITLE_SIZE}px)`

        if (!cache.SIDE_BAR_GROW) {
            sideNav.style.height = '-webkit-fill-available'
            const height         = `${float(cmpStyle(sideNav, 'height')) - 30}px`
            sideNav.style.height = height
            cache.SIDE_BAR_GROW  = height
        } else {
            sideNav.style.height = cache.SIDE_BAR_GROW
        }
    } else {
        sideNav.style.transform = ''

        if (!cache.SIDE_BAR_SHRINK) {
            sideNav.style.height  = '-webkit-fill-available'
            const height          = `${float(cmpStyle(sideNav, 'height')) - 100}px`
            sideNav.style.height  = height
            cache.SIDE_BAR_SHRINK = height
        } else {
            sideNav.style.height  = cache.SIDE_BAR_SHRINK
        }
    }
})

observer.observe(document.querySelector('#page-title'))

function closeErrorModal() {
    style_scroll(e('main-body'))
    style_hide(e('error-modal'))
}

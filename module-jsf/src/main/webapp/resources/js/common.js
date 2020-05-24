//Usado en caso donde se usa el componente "p:ajax" con su atributo "delay". EJ: /atm/modaldataconfirm.xhtml
var canHideScreenLoading = true;
var loadingSetTimeout = null;
var sessionSetTimeout = null;
var sessionSetInterval = null;
var viewExpiredSetTimeout = null;

(function () {
    extracted();
    setCurrentPageSidebarMenu();
})();

/**
 * @param value boolean
 */
function screenLoading(value) {
    if (canHideScreenLoading) {
        clearTimeout(loadingSetTimeout);
    }

    var divLoading = document.getElementById('loading-screen');

    if (typeof value === 'boolean' && value) {
        /*
         * Con el "setTimeout" se evita mostrar la vista de carga para peticiones
         * que no sobrepasen los 500 milisegundos.
         */
        loadingSetTimeout = setTimeout(function () {
            divLoading.style.display = 'block';
        }, 500);
        document.body.style.cursor = 'wait';
    } else if (canHideScreenLoading) {
        divLoading.style.display = 'none';
        document.body.style.cursor = '';
    }
}

/**
 * @param value boolean
 */
function setCanHideScreenLoading(value) {
    canHideScreenLoading = value;
}

/**
 *
 * @param maxInactiveInterval int Segundos
 * @param sessionTimeoutCount int Valor en segundos. Cuenta regresiva para cerrar sesión y mostrar la pagina de inicio.
 */
function sessionScreenTimeOut(maxInactiveInterval, sessionTimeoutCount) {
    clearTimeout(sessionSetTimeout);
    clearInterval(sessionSetInterval);
    var divSession = document.getElementById('session-timeout-screen');

    if (divSession === null) {
        return false;
    }

    divSession.style.display = 'none';

    sessionSetTimeout = setTimeout(function () {
        var spanCount = document.getElementById('session-timeout-count');
        spanCount.innerHTML = sessionTimeoutCount;
        divSession.style.display = 'block';

        sessionSetInterval = setInterval(function () {
            if (sessionTimeoutCount === 0) {
                clearInterval(sessionSetInterval);
                //Esta función se encuentra en la vista "/template/sessiontimeout.xhtml"
                remoteCommandLogout();
            }

            spanCount.innerHTML = sessionTimeoutCount--;
        }, 1000);
    }, (maxInactiveInterval - sessionTimeoutCount) * 1000);
}

function viewExpiredScreen(maxInactiveInterval) {
    clearTimeout(viewExpiredSetTimeout);
    var divViewExpired = document.getElementById('view-expired-screen');

    if (divViewExpired == null) {
        return false;
    }

    $(divViewExpired).on('click', 'form button', function () {
        divViewExpired.style.display = 'none';
    });

    viewExpiredSetTimeout = setTimeout(function () {
        divViewExpired.style.display = 'block';
    }, maxInactiveInterval * 1000);
}

function locationReplace(replace) {
    location.replace(replace);
}

/**
 * Por ejemplo, Usado en la vista "taxpayerdeceased", en el componente "p:fileupload".
 * Mediante el componente "http://xmlns.jcp.org/jsf/passthrough"
 * se le agrega el atributo "onchange" al componente "p:fileupload".
 */
function valueChangeListenerFileUpload(tagName, elementId) {
    if (tagName == 'INPUT') {
        var button = $('#' + elementId).find('.btn-upload-file');
        if (button.hasClass('hidden')) {
            button.removeClass('hidden');
        }
    }
}

/**
 * @deprecated
 */
function extracted() {
    $(document).on('click', '.modal .ui-selectonemenu', function () {
        var zIndex = $(this).closest('.modal').css('z-index');
        var panel = $(this).closest('.ui-selectonemenu').attr('id') + '_panel';
        $('[id*="' + panel + '"]').css('z-index', zIndex);
    });

    //Ocultar/Mostrar menu lateral
    $('#btn-toggle-side-nav').on('click', function () {
        $('.side-nav').toggleClass('side-nav-hidden');
        $('.wrapper').toggleClass('wrapper-hidden');
    });
}

function setCurrentPageSidebarMenu() {
    let currentElement = $('ul[data-widget=tree]').find('li.treeview ul.treeview-menu li a')
        .toArray()
        .map(value => $(value))
        .filter(value => value.attr('href') === window.location.pathname)
        .shift();

    if (currentElement === undefined) {
        return;
    }

    let treeviewElement = currentElement.closest('li.treeview');
    let parentElement = currentElement.parent('li');

    treeviewElement.addClass('active');
    parentElement.addClass('active');
}

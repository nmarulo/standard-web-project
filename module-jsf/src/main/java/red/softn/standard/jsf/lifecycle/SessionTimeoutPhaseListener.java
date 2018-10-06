package red.softn.standard.jsf.lifecycle;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class SessionTimeoutPhaseListener implements PhaseListener {
    
    @Override
    public void afterPhase(PhaseEvent event) {
    }
    
    @Override
    public void beforePhase(PhaseEvent event) {
        if (event.getPhaseId()
                 .equals(PhaseId.RENDER_RESPONSE)) {
            try {
//                SessionBean sessionBean = SessionBean.getInstance();
//
//                if (sessionBean != null && !FacesUtils.checkURI("/index.xhtml")) {
//                    sessionBean.sessionScreenTimeOut();
//                }
            } catch (Exception ex) {
                //TODO: log
            }
        }
    }
    
    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
